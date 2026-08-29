package sortedset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SortedSetSkipList {
    public class Node {
        private String member;
        private double score;
        private Node[] next;
        public Node(String member, double score, int level) {
            this.member = member;
            this.score = score;
            this.next = new Node[level];
        }
    }

    public class SkipList {
        private final Node head = new Node(null, Double.NEGATIVE_INFINITY, MAX_LEVEL);
        private int currentLevel = 1;
        private final Random random = new Random();

        private static final int MAX_LEVEL = 16;

        public int randomLevel() {
            int level = 1;
            while (random.nextBoolean() && level < MAX_LEVEL)
                ++level;
            return level;
        }

        private int compare(Node node, double score, String member) {
            int cmp = Double.compare(node.score, score);
            if (cmp != 0)
                return cmp;
            return node.member.compareTo(member);
        }

        public Node insert(String member, double score) {
            Node[] update = new Node[MAX_LEVEL];
            Node curr = head;
            for (int level = currentLevel - 1; level >= 0; --level) {
                while (curr.next[level] != null && compare(curr.next[level], score, member) < 0)
                    curr = curr.next[level];
                update[level] = curr;
            }

            int nodeLevel = randomLevel();
            if (nodeLevel > currentLevel) {
                for (int i = currentLevel; i < nodeLevel; ++i)
                    update[i] = head;
                currentLevel = nodeLevel;
            }

            Node node = new Node(member, score, nodeLevel);
            for (int i = 0; i < nodeLevel; ++i) {
                node.next[i] = update[i].next[i];
                update[i].next[i] = node;
            }

            return node;
        }

        public void delete(Node node) {
            Node[] update = new Node[MAX_LEVEL];
            Node curr = head;

            for (int level = currentLevel - 1; level >= 0; --level) {
                while (curr.next[level] != null && compare(curr.next[level], node.score, node.member) < 0) {
                    curr = curr.next[level];
                }
                update[level] = curr;
            }

            Node target = curr.next[0];
            if (target == null || target.score != node.score || !target.member.equals(node.member))
                return;

            for (int i = 0; i < currentLevel; ++i) {
                if (update[i].next[i] != target)
                    break;

                update[i].next[i] = target.next[i];
            }

            while (currentLevel > 1 && head.next[currentLevel - 1] == null)
                --currentLevel;
        }

        public int rank(String member, double score) {
            int rank = 0;
            Node curr = head.next[0];

            while (curr != null) {
                if (curr.member.equals(member) && curr.score == score)
                    return rank;

                ++rank;
                curr = curr.next[0];
            }

            return -1;
        }

        public List<String> range(int start, int end) {
            List<String> result = new ArrayList<>();

            if (start > end)
                return result;

            int index = 0;
            Node curr = head.next[0];
            while (curr != null && index <= end) {
                if (index >= start)
                    result.add(curr.member);

                curr = curr.next[0];
                ++index;
            }

            return result;
        }

        public List<String> rangeByScore(double min, double max) {
            List<String> result = new ArrayList<>();

            Node curr = head;

            for (int level = currentLevel - 1; level >= 0; --level) {
                while (curr.next[level] != null &&
                        curr.next[level].score < min) {
                    curr = curr.next[level];
                }
            }

            curr = curr.next[0];
            while (curr != null && curr.score <= max) {
                result.add(curr.member);
                curr = curr.next[0];
            }

            return result;
        }
    }

    public class RedisSortedSet {
        private final Map<String, Node> memberMap = new HashMap<>();
        private final SkipList skipList = new SkipList();

        public void add(String member, double score) {
            Node old = memberMap.get(member);
            if (old != null) {
                if (old.score == score)
                    return;
                skipList.delete(old);
            }
            Node node = skipList.insert(member, score);
            memberMap.put(member, node);
        }

        public void remove(String member) {
            Node node = memberMap.remove(member);
            if (node == null)
                return;
            skipList.delete(node);
        }

        public Double score(String member) {
            Node node = memberMap.get(member);
            return node == null ? null : node.score;
        }

        public Integer rank(String member) {
            Node node = memberMap.get(member);
            if (node == null)
                return null;
            return skipList.rank(member, node.score);
        }

        public List<String> range(int start, int end) {
            return skipList.range(start, end);
        }

        public List<String> rangeByScore(double min, double max) {
            return skipList.rangeByScore(min, max);
        }
    }
}
