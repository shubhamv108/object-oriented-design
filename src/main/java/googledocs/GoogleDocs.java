package googledocs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class GoogleDocs {

    public class DocumentService {
        public final Map<String, Document> documents = new HashMap<>();

        public String createDocument(String documentId) {
            documents.put(documentId, new Document(documentId));
            return documentId;
        }

        public Editor subscribe(String editorId, String documentId) {
            Document document = Optional.ofNullable(documents.get(documentId)).orElseThrow();
            Editor editor = new Editor(editorId, this, (Document) document.clone());
            document.subscribe(editor);
            return editor;
        }

        public void operate(String documentId, Operation operation) {
            Document document = Optional.ofNullable(documents.get(documentId))
                    .orElseThrow();

            Operation transformedOperation = document.apply(operation);
            System.out.println("DocumentSvc Transformed Operation=" + transformedOperation);
            System.out.printf("Document %s=%s%n", documentId, document.getContent());
        }
    }

    public abstract class Subject {
        protected final List<Observer> observers = new CopyOnWriteArrayList<>();

        public void subscribe(Editor editor) {
            observers.add(editor);
        }
        public void removeSubscriber(Editor editor) {
            observers.remove(editor);
        }
    }

    public class Document extends Subject {
        private final String id;
        private final StringBuilder content = new StringBuilder();
        private int version = 0;
        private final List<Operation> history = new ArrayList<>();

        public Document(String id) {
            this(id, null);
        }

        public Document(String id, Document document) {
            this.id = id;
            if (document != null) {
                this.content.append(document.getContent());
                this.history.addAll(document.history);
                this.version = document.getVersion();
            }
        }

        public String getId() {
            return id;
        }

        public synchronized int getVersion() {
            return version;
        }

        public synchronized String getContent() {
            return content.toString();
        }

        public List<Operation> getHistoryFrom(int baseVersion) {
            return history.subList(baseVersion, history.size());
        }

        public synchronized Operation apply(Operation operation) {
            int currentVersion = this.getVersion();

            // transform operation
            if (operation.getBaseVersion() < currentVersion) {
                for (Operation missedOperation : getHistoryFrom(operation.getBaseVersion())) {
                    operation = OperationTransform.transform(operation, missedOperation);
                }
            }

            operation.setBaseVersion(version);
            final Operation transformedOperation = operation;

            OperationApplier operationApplier = new OperationApplierFactory().get(transformedOperation.getType());
            operationApplier.validateOperationOrThrowException(content.length(), transformedOperation);
            operationApplier.apply(content, transformedOperation);

            history.add(transformedOperation);
            ++version;

            observers.forEach(editor -> editor.receive(transformedOperation));

            return operation;
        }

        @Override
        protected Object clone() {
            return new Document(this.id, this);
        }

        public class OperationApplierFactory {
            public OperationApplier get(OperationType operationType) {
                return switch (operationType) {
                    case OperationType.INSERT -> new InsertOperationApplier();
                    case OperationType.DELETE -> new DeleteOperationApplier();
                };
            }
        }

        public interface OperationApplier {
            void apply(StringBuilder content, Operation operation);
            void validateOperationOrThrowException(int contentLength, Operation operation);
        }

        public class InsertOperationApplier implements OperationApplier {
            @Override
            public void apply(StringBuilder content, Operation operation) {
                content.insert(operation.getPosition(), operation.getText());
            }

            @Override
            public void validateOperationOrThrowException(int contentLength, Operation operation) {
                if (operation.getPosition() < 0 || operation.getPosition() > content.length()) {
                    throw new IllegalArgumentException("Invalid insert position=" + operation.getPosition());
                }
            }
        }

        public class DeleteOperationApplier implements OperationApplier {
            @Override
            public void apply(StringBuilder content, Operation operation) {
                content.delete(operation.getPosition(), operation.getPosition() + operation.getLength());
            }

            @Override
            public void validateOperationOrThrowException(int contentLength, Operation operation) {
                if (operation.getPosition() < 0 || operation.getPosition() + operation.getLength() > content.length()) {
                    throw new IllegalArgumentException(
                            String.format("Invalid delete range=[%s,%s]", operation.getPosition(),
                                          operation.getPosition() + operation.getLength()));
                }
            }
        }
    }

    public enum OperationType {
        INSERT,
        DELETE
    }

    public static class Operation {
        private final String operationId;
        private final String clientId;
        private final OperationType type;
        private final String text;
        private int baseVersion;
        private int position;
        private int length;

        public Operation(String operationId, String clientId, int baseVersion, OperationType type, String text, int position, int length) {
            this.operationId = operationId;
            this.clientId = clientId;
            this.baseVersion = baseVersion;
            this.type = type;
            this.text = text;
            this.position = position;
            this.length = length;
        }

        public void setBaseVersion(int baseVersion) {
            this.baseVersion = baseVersion;
        }

        public Operation copy() {
            return new Operation(operationId, clientId, baseVersion, type, text, position, length);
        }

        public String getOperationId() {
            return operationId;
        }

        public String getClientId() {
            return clientId;
        }

        public int getBaseVersion() {
            return baseVersion;
        }

        public OperationType getType() {
            return type;
        }

        public int getPosition() {
            return position;
        }

        public String getText() {
            return text;
        }

        public int getLength() {
            return length;
        }

        @Override
        public String toString() {
            return "Operation{" +
                    "type=" + type +
                    ", pos=" + position +
                    ", text='" + text + '\'' +
                    ", len=" + length +
                    '}';
        }
    }

    public class OperationTransform {

        /**
         * Transform opA against opB (i.e., opB already applied)
         */
        public static Operation transform(Operation opA, Operation opB) {
            Operation a = opA.copy();

            record OperationPair(OperationType bType, OperationType aType) {
            }

            return switch (new OperationPair(opB.type, a.type)) {
                case OperationPair(
                        var bType, var aType
                ) when bType == OperationType.INSERT && aType == OperationType.INSERT -> transformInsertInsert(a, opB);

                case OperationPair(
                        var bType, var aType
                ) when bType == OperationType.INSERT && aType == OperationType.DELETE -> transformDeleteInsert(a, opB);

                case OperationPair(
                        var bType, var aType
                ) when bType == OperationType.DELETE && aType == OperationType.INSERT -> transformInsertDelete(a, opB);

                case OperationPair(
                        var bType, var aType
                ) when bType == OperationType.DELETE && aType == OperationType.DELETE -> transformDeleteDelete(a, opB);

                default -> a;
            };
        }

        // ----------------------------
        // INSERT vs INSERT
        // ----------------------------
        private static Operation transformInsertInsert(Operation a, Operation b) {
            if (a.getPosition() > b.getPosition()) {
                a.position += b.getText().length();
            } else if (a.getPosition() == b.getPosition()) {
                // deterministic tie-breaker
                if (compareClient(a, b) > 0) {
                    a.position += b.getText().length();
                }
            }
            return a;
        }

        // ----------------------------
        // INSERT vs DELETE
        // ----------------------------
        private static Operation transformInsertDelete(Operation a, Operation b) {
            if (a.getPosition() > b.getPosition()) {
                int shift = Math.min(b.getLength(), a.getPosition() - b.getPosition());
                a.position -= shift;
            }
            return a;
        }

        // ----------------------------
        // DELETE vs INSERT
        // ----------------------------
        private static Operation transformDeleteInsert(Operation a, Operation b) {
            if (a.getPosition() >= b.getPosition()) {
                a.position += b.getText().length();
            }
            return a;
        }

        // ----------------------------
        // DELETE vs DELETE
        // ----------------------------
        private static Operation transformDeleteDelete(Operation a, Operation b) {
            if (a.getPosition() >= b.getPosition() + b.getLength()) {
                // completely after
                a.position -= b.getLength();
            } else if (a.getPosition() + a.getLength() <= b.getPosition()) {
                // completely before → no change
            } else {
                // overlapping deletes → shrink range
                int overlapStart = Math.max(a.getPosition(), b.getPosition());
                int overlapEnd = Math.min(a.getPosition() + a.getLength(), b.getPosition() + b.getLength());
                int overlap = overlapEnd - overlapStart;

                a.length -= overlap;

                if (a.getPosition() > b.getPosition()) {
                    a.position = b.getPosition();
                }
            }
            return a;
        }

        // ----------------------------
        // Tie-breaker
        // ----------------------------
        private static int compareClient(Operation a, Operation b) {
            return Objects.compare(a.getClientId(), b.getClientId(), String::compareTo);
        }
    }

    public interface Observer {
        void receive(Operation operation);
    }

    public class Editor implements Observer {
        private final String id;
        private final GoogleDocs.DocumentService documentService;
        private final Document document;

        public Editor(String id, DocumentService documentService, Document document) {
            this.id = id;
            this.documentService = documentService;
            this.document = document;
        }

        public void insert(int baseVersion, String text, int position) {
            System.out.printf("operation=INSERT text=\"%s\" position=%s%n", text, position);
            performOperation(new Operation(UUID.randomUUID().toString(), id, baseVersion, OperationType.INSERT, text, position, text.length()));
        }

        public void delete(int baseVersion, int position, int length) {
            System.out.printf("operation=DELETE position=%s length=%s%n", position, length);
            performOperation(new Operation(UUID.randomUUID().toString(), id,  baseVersion, OperationType.DELETE, null, position, length));
        }

        public void performOperation(Operation operation) {
            documentService.operate(document.getId(), operation);
        }

        public void receive(Operation operation) {
            document.apply(operation);
            print();
        }

        public String getId() {
            return id;
        }

        public void print() {
            System.out.println(String.format("Editor %s=%s", id, document.getContent()));
        }
    }

    void main() {
        final GoogleDocs wordProcessor = new GoogleDocs();
        final DocumentService documentService = wordProcessor.new DocumentService();
        final String documentId = "Doc1";
        documentService.createDocument(documentId);
        final Editor editor1 = documentService.subscribe("A", documentId);
        final Editor editor2 = documentService.subscribe("B", documentId);
        editor1.insert(0, "Hello!", 0);
        editor1.insert(1, ", world", 5);
        editor2.delete(1, 5, 1);
    }
}