package dns;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DNS {

    public class DomainName {
        private final List<String> labels;

        public DomainName(String fqdn) {
            if (fqdn == null || fqdn.isEmpty()) {
                this.labels = new ArrayList<>();
                return;
            }

            // Remove trailing dot if present
            if (fqdn.endsWith(".")) {
                fqdn = fqdn.substring(0, fqdn.length() - 1);
            }

            this.labels = new ArrayList<>(Arrays.asList(fqdn.toLowerCase().split("\\.")));
            Collections.reverse(this.labels); // Store in reverse order for easier hierarchy
        }

        private DomainName(List<String> labels) {
            this.labels = new ArrayList<>(labels);
        }

        public String getFQDN() {
            List<String> reversed = new ArrayList<>(labels);
            Collections.reverse(reversed);
            return String.join(".", reversed);
        }

        public DomainName getParent() {
            if (labels.size() <= 1) {
                return null;
            }
            return new DomainName(labels.subList(0, labels.size() - 1));
        }

        public int getLabelCount() {
            return labels.size();
        }

        public boolean isSubdomainOf(DomainName other) {
            if (other.labels.size() >= this.labels.size()) {
                return false;
            }

            for (int i = 0; i < other.labels.size(); i++) {
                if (!this.labels.get(i).equals(other.labels.get(i))) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DomainName that = (DomainName) o;
            return labels.equals(that.labels);
        }

        @Override
        public int hashCode() {
            return Objects.hash(labels);
        }

        @Override
        public String toString() {
            return getFQDN();
        }   
    }

    public enum RecordType {
        A(1),
        NS(2),
        CNAME(5),
        SOA(6),
        PTR(12),
        MX(15),
        TXT(16),
        AAAA(28),
        SRV(33);

        private final int value;

        RecordType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static RecordType fromValue(int value) {
            for (RecordType type : values()) {
                if (type.value == value) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown record type: " + value);
        }
    }

    public enum RecordClass {
        IN(1),      // Internet
        CS(2),      // CSNET
        CH(3),      // CHAOS
        HS(4);      // Hesiod

        private final int value;

        RecordClass(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static RecordClass fromValue(int value) {
            for (RecordClass rc : values()) {
                if (rc.value == value) {
                    return rc;
                }
            }
            throw new IllegalArgumentException("Unknown record class: " + value);
        }
    }

    public abstract class ResourceRecord {
        protected DomainName name;
        protected RecordType type;
        protected RecordClass recordClass;
        protected int ttl;

        public ResourceRecord(DomainName name, RecordType type, RecordClass recordClass, int ttl) {
            this.name = name;
            this.type = type;
            this.recordClass = recordClass;
            this.ttl = ttl;
        }

        public DomainName getName() {
            return name;
        }

        public RecordType getType() {
            return type;
        }

        public RecordClass getRecordClass() {
            return recordClass;
        }

        public int getTTL() {
            return ttl;
        }

        public abstract byte[] getRData();

        public byte[] toWireFormat() throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);

            // Write name
            writeDomainName(dos, name);

            // Write type, class, ttl, rdlength
            dos.writeShort(type.getValue());
            dos.writeShort(recordClass.getValue());
            dos.writeInt(ttl);

            byte[] rdata = getRData();
            dos.writeShort(rdata.length);
            dos.write(rdata);

            return baos.toByteArray();
        }

        protected void writeDomainName(DataOutputStream dos, DomainName domain) throws IOException {
            String fqdn = domain.getFQDN();
            if (fqdn.isEmpty()) {
                dos.writeByte(0);
                return;
            }

            String[] labels = fqdn.split("\\.");
            for (String label : labels) {
                byte[] labelBytes = label.getBytes("UTF-8");
                dos.writeByte(labelBytes.length);
                dos.write(labelBytes);
            }
            dos.writeByte(0); // Terminating zero
        }

        protected DomainName readDomainName(DataInputStream dis) throws IOException {
            StringBuilder sb = new StringBuilder();
            int length;

            while ((length = dis.readUnsignedByte()) != 0) {
                byte[] labelBytes = new byte[length];
                dis.readFully(labelBytes);

                if (sb.length() > 0) {
                    sb.append(".");
                }
                sb.append(new String(labelBytes, "UTF-8"));
            }

            return new DomainName(sb.toString());
        }

        @Override
        public String toString() {
            return String.format("%s %d %s %s",
                                 name.getFQDN(), ttl, recordClass, type);
        }
    }

}


