//package filesystem;
//
//import java.nio.file.InvalidPathException;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * "Design an in-memory file system that supports creating files and directories, navigating paths, and basic file operations."
// *
// * Requirements:
// * 1. Hierarchical file system with single root directory
// * 2. Files store string content
// * 3. Folders contain files and other folders
// * 4. Create and delete files and folders
// * 5. List contents of a folder
// * 6. Navigate/resolve absolute paths (e.g., /home/user/docs)
// * 7. Rename and move files and folders
// * 8. Retrieve full path from any file/folder reference
// * 9. Scale to tens of thousands of entries in memory
// *
// * Out of Scope:
// * - Search functionality
// * - Relative path resolution (../ or ./)
// * - Permissions, ownership, timestamps
// * - File type-specific behavior
// * - Persistence / disk storage
// * - Symbolic links
// * - UI layer
// */
//public class DesignFileSystem {
//
//    private abstract class Node {
//        private String name;
//        private Folder parent;
//
//        public Node (String name) {
//            this.name = name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setParent(Folder parent) {
//            this.parent = parent;
//        }
//
//        public Folder getParent() {
//            return parent;
//        }
//
//        public abstract boolean isDirectory();
//        public abstract String getPath();
//    }
//
//    public class File extends Node {
//        private String content;
//
//        public File(String name) {
//            super(name);
//        }
//
//        @Override
//        public boolean isDirectory() {
//            return false;
//        }
//
//        @Override
//        public String getPath() {
//            return "";
//        }
//    }
//    public class Folder extends Node {
//        private final Map<String, Node> children = new HashMap<>();
//
//        public Folder(String name) {
//            super(name);
//        }
//
//        public void add(Node child) {
//            children.put(child.getName(), child);
//        }
//
//        public Node remove(String name) {
//            return children.remove(name);
//        }
//
//        @Override
//        public boolean isDirectory() {
//            return true;
//        }
//
//        @Override
//        public String getPath() {
//            return "";
//        }
//
//        public Node getChild(String name) {
//            return children.get(name);
//        }
//
//        public boolean hasChild(String name) {
//            return children.containsKey(name);
//        }
//
//        public Map<String, Node> getChildren() {
//            return children;
//        }
//    }
//    public class FileSystem {
//        private final Folder root;
//
//        public FileSystem(Folder root) {
//            this.root = root;
//        }
//
//        public Node createFile(String path, String content) throws InvalidPathException, AlreadyExistsException, NotADirectoryException, NotFoundException {
//            Folder parent = resolveParent(path);
//            if (parent == null)
//                throw new InvalidPathException(path);
//            String fileName = extractName(path);
//            if (parent.hasChild(fileName))
//                throw new AlreadyExistsException(fileName);
//
//            Node file = new File(fileName);
//            parent.add(file);
//            return file;
//        }
//
//        public Node createFolder(String path) throws InvalidPathException, AlreadyExistsException, NotADirectoryException, NotFoundException {
//            Folder parent = resolveParent(path);
//            if (parent == null)
//                throw new InvalidPathException(path);
//            String name = extractName(path);
//            if (parent.hasChild(name))
//                throw new AlreadyExistsException(name);
//
//            Node folder = new Folder(name);
//            parent.add(folder);
//            return folder;
//
//        }
//
//        public void delete(String path) throws InvalidPathException, NotFoundException, NotADirectoryException {
//            Folder parent = resolveParent(path);
//            if (parent == null)
//                throw new InvalidPathException(path);
//            String name = extractName(path);
//            if (parent.remove(name) == null)
//                throw new NotFoundException(path);
//        }
//
//        public Collection<Node> list(String path) throws InvalidPathException, NotADirectoryException, NotFoundException {
//            Node node = resolvePath(path);
//            if (!node.isDirectory())
//                throw new NotADirectoryException(path);
//            return ((Folder) node).getChildren().values();
//        }
//
//        public Node get(String path) throws InvalidPathException {}
//
//        public void rename(String path, String name) throws InvalidPathException, NotFoundException, AlreadyExistsException, NotADirectoryException {
//            Folder parent = resolveParent(path);
//            if (parent == null)
//                throw new InvalidPathException(path);
//            String existingName = extractName(path);
//            if (!parent.hasChild(existingName))
//                throw new NotFoundException(path);
//            if (parent.hasChild(name))
//                throw new AlreadyExistsException(path);
//            Node node = parent.remove(existingName);
//            node.setName(name);
//            parent.add(node);
//        }
//
//        public void move(String srcPath, String destPath) throws InvalidPathException, AlreadyExistsException, NotFoundException, NotADirectoryException {
//            Folder srcParent = resolveParent(srcPath);
//            if (srcParent == null)
//                throw new InvalidPathException(srcPath);
//            String srcName = extractName(srcPath);
//            Node node = srcParent.getChild(srcName);
//            if (node == null)
//                throw new NotFoundException(srcPath);
//            Folder destParent = resolveParent(destPath);
//            if (destParent == null)
//                throw new InvalidPathException(destPath);
//            String destName = extractName(destPath);
//
//            // Check for cycles: can't move a folder into itself or a descendant
//            if (node.isDirectory()) {
//                Node current = destParent;
//                while (current != null) {
//                    if (current == node)
//                        throw new InvalidPathException(destPath);
//                    current = current.getParent();
//                }
//            }
//
//            if (destParent.hasChild(destName))
//                throw new AlreadyExistsException(destPath);
//
//            srcParent.remove(srcName);
//            node.setName(destName);
//            destParent.add(node);
//        }
//
//        private String extractName(String path) {
//            int lastSlash = path.lastIndexOf("/");
//            return path.substring(lastSlash + 1);
//        }
//
//        private Node resolvePath(String path) throws InvalidPathException, NotADirectoryException, NotFoundException {
//            if (path == "/")
//                return root;
//
//            String[] parts = path.substring(1).split("/");
//            Node cur = root;
//            for (String part : parts) {
//                if (part.equals(""))
//                    throw new InvalidPathException(path);
//
//                if (!cur.isDirectory())
//                    throw new NotADirectoryException(path);
//
//                Node child = ((Folder) cur).getChild(part);
//                if (child == null)
//                    throw new NotFoundException(path);
//
//                cur = child;
//            }
//            return cur;
//        }
//
//        public Folder resolveParent(String path) throws NotADirectoryException, NotFoundException, InvalidPathException {
//            int lastSlash = path.lastIndexOf("/");
//            String parentPath = lastSlash == 0 ? "/" : path.substring(0, lastSlash);
//
//            Node parent = resolvePath(parentPath);
//
//            if (!parent.isDirectory())
//                throw new NotADirectoryException(path);
//
//            return (Folder) parent;
//        }
//    }
//
//    public class InvalidPathException extends Exception {
//        private final String path;
//
//        public InvalidPathException(String path) {
//            this.path = path;
//        }
//    }
//
//    public class AlreadyExistsException extends Exception {
//        private final String path;
//
//        public AlreadyExistsException(String path) {
//            this.path = path;
//        }
//    }
//
//    public class NotFoundException extends Exception {
//        private final String path;
//
//        public NotFoundException(String path) {
//            this.path = path;
//        }
//    }
//
//    public class NotADirectoryException extends Exception {
//        private final String path;
//
//        public NotADirectoryException(String path) {
//            this.path = path;
//        }
//    }
//
//}
