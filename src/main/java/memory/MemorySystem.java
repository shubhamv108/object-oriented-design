package memory;

public class MemorySystem {

    class Store {

    }

    class PhysicalMemory {
        PageTable pageTable;
    }

    class PageTable {

    }

    class VirtualMemory {

    }

    class MMU {
        TLB tlb = new TLB();
        PageTable pageTable;

        MMU (PageTable pageTable) {
            this.pageTable = pageTable;
        }

        void getPhysicalAddress() {
            try {
                tlb.getPhysicalAddress();
            } catch (TLBMissException tlbMissException) {

            }
        }
    }

    class TLB {
        void getPhysicalAddress() throws TLBMissException {

        }
    }

    enum Locality {
        SPATIAL,
        TEMPORAL
    }

    class TLBMissException extends RuntimeException {}

}
