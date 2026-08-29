package sortedset;

import java.util.*;

public class RedisSortedSet {
    private final Map<String, Double> memberMap = new HashMap<>();
    private final TreeMap<Double, TreeSet<String>> scoreMap = new TreeMap<>();

    public void add(String member, double score) {
        Double oldScore = memberMap.get(member);
        if (oldScore != null) {
            if (oldScore == score)
                return;
            remove(member);
        }

        memberMap.put(member, score);
        scoreMap.computeIfAbsent(score, k -> new TreeSet<>()).add(member);
    }

    public boolean remove(String member) {
        Double score = memberMap.remove(member);
        if (score == null)
            return false;

        TreeSet<String> members = scoreMap.get(score);
        members.remove(member);
        if (members.isEmpty())
            scoreMap.remove(score);
        return true;
    }

    public Double score(String member) {
        return memberMap.get(member);
    }

    public int size() {
        return memberMap.size();
    }

    public List<String> range(int start, int end) {
        List<String> result = new ArrayList<>();
        if (start < 0)
            start = 0;

        int index = 0;
        outer:
        for (Map.Entry<Double, TreeSet<String>> entry : scoreMap.entrySet()) {
            for (String member : entry.getValue()) {
                if (index >= start && index <= end)
                    result.add(member);
                if (index > end)
                    break outer;
                ++index;
            }
        }
        return result;
    }

    public List<String> revRange(int start, int end) {
        List<String> result = new ArrayList<>();
        if (start < 0)
            start = 0;

        int index = 0;
        outer:
        for (Map.Entry<Double, TreeSet<String>> entry : scoreMap.descendingMap().entrySet()) {
            Iterator<String> it = entry.getValue().descendingIterator();
            while (it.hasNext()) {
                String member = it.next();
                if (index >= start && index <= end)
                    result.add(member);
                if (index > end)
                    break outer;
                ++index;
            }
        }
        return result;
    }

    public List<String> rangeByScore(double min, double max) {
        List<String> result = new ArrayList<>();
        NavigableMap<Double, TreeSet<String>> sub = scoreMap.subMap(min, true, max, true);
        for (TreeSet<String> members : sub.values())
            result.addAll(members);
        return result;
    }

    public List<String> revRangeByScore(double max, double min) {
        List<String> result = new ArrayList<>();
        NavigableMap<Double, TreeSet<String>> sub = scoreMap.subMap(min, true, max, true).descendingMap();
        for (Map.Entry<Double, TreeSet<String>> entry : sub.entrySet()) {
            Iterator<String> it = entry.getValue().descendingIterator();
            while (it.hasNext())
                result.add(it.next());
        }
        return result;
    }

    public Integer rank(String member) {
        Double score = memberMap.get(member);
        if (score == null)
            return null;

        int rank = 0;
        for (Map.Entry<Double, TreeSet<String>> entry : scoreMap.entrySet()) {
            if (entry.getKey().equals(score)) {
                for (String m : entry.getValue()) {
                    if (m.equals(member))
                        return rank;
                    ++rank;
                }
            } else {
                rank += entry.getValue().size();
            }
        }
        return null;
    }

    public List<String> neighbours(String member, int k) {
        Double score = memberMap.get(member);
        if (score == null)
            return Collections.emptyList();

        List<String> below = new ArrayList<>();
        List<String> above = new ArrayList<>();
        TreeSet<String> sameScore = scoreMap.get(score);

        String curr = member;
        while (below.size() < k) {
            String lower = sameScore.lower(curr);
            if (lower != null) {
                below.add(lower);
                curr = lower;
                continue;
            }
            Map.Entry<Double, TreeSet<String>> lowerScore = scoreMap.lowerEntry(score);
            while (lowerScore != null && below.size() < k) {
                Iterator<String> it = lowerScore.getValue().descendingIterator();
                while (it.hasNext() && below.size() < k)
                    below.add(it.next());
                lowerScore = scoreMap.lowerEntry(lowerScore.getKey());
            }

            break;
        }

        curr = member;
        while (above.size() < k) {
            String higher = sameScore.higher(curr);
            if (higher != null) {
                above.add(higher);
                curr = higher;
                continue;
            }

            Map.Entry<Double, TreeSet<String>> higherScore = scoreMap.higherEntry(score);
            while (higherScore != null && above.size() < k) {
                for (String s : higherScore.getValue()) {
                    if (above.size() == k)
                        break;
                    above.add(s);
                }
                higherScore = scoreMap.higherEntry(higherScore.getKey());
            }
            break;
        }

        Collections.reverse(below);
        List<String> result = new ArrayList<>();
        result.addAll(below);
        result.add(member);
        result.addAll(above);
        return result;
    }
}