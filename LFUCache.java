/*
Problem: https://leetcode.com/problems/lfu-cache/
TC: O(1) for get and put
SC: O(n)

Ideally, shouldn't be using LinkedHashSet. Need to replace this with another doubly linked list and another hasmap that points to nodes in this list
*/
class Node {
    int key, value, freq;
    
    public Node(int k, int v) {
        key = k;
        value = v;
        freq = 1;
    }
}

class LFUCache {

    HashMap<Integer, Node> cache = null;
    HashMap<Integer, LinkedHashSet<Node>> freqMap = null;
    int minFreq;
    int capacity = 0;
    
    public LFUCache(int capacity) {
        minFreq = 1;
        freqMap = new HashMap<>();
        cache = new HashMap<>();
        this.capacity = capacity;
    }
    
    public int get(int key) {
        if (capacity == 0 || !cache.containsKey(key)) {
            return -1;
        }
        Node n = updateNodeAndFreq(key);
        // System.out.println("Updating node freq get");
        // System.out.println("cache: " + cache);
        // System.out.println("freqMap: " + freqMap);
        return n.value;
    }
    
    public void put(int key, int value) {
        
        if (capacity == 0)
            return;
        if (cache.containsKey(key)) {
            Node n = updateNodeAndFreq(key);
            n.value = value;
            // System.out.println("Updating node freq put");
            // System.out.println("cache: " + cache);
            // System.out.println("freqMap: " + freqMap);
        } else {
            if (cache.size() >= capacity) {
                LinkedHashSet<Node> nodesWithSameFreq = freqMap.get(minFreq);
                Node delete = nodesWithSameFreq.iterator().next();
                
                cache.remove(delete.key);
                nodesWithSameFreq.remove(delete);
                if (nodesWithSameFreq.size() == 0) {
                    freqMap.remove(delete.freq);
            
                    if (minFreq == delete.freq) {
                        ++minFreq;
                    }
                }
                // System.out.println("evicting from cache: " + delete);
                // System.out.println("cache: " + cache);
                // System.out.println("freqMap: " + freqMap);
            }
            Node n = new Node(key, value);
            cache.put(n.key, n);
            if (!freqMap.containsKey(1)) {
                freqMap.put(1, new LinkedHashSet<>());
            }
            freqMap.get(1).add(n);
            minFreq = 1;
            // System.out.println("Add to cache... ");
            // System.out.println("cache: " + cache);
            // System.out.println("freqMap: " + freqMap);
        }
    }
    
    private Node updateNodeAndFreq(int key) {
        Node n = cache.get(key);
        LinkedHashSet<Node> nodesWithSameFreq = freqMap.get(n.freq);
        nodesWithSameFreq.remove(n);
        
        if (!freqMap.containsKey(n.freq+1)) {
            freqMap.put(n.freq + 1, new LinkedHashSet<>());
        }
        freqMap.get(n.freq+1).add(n);
        
        if (nodesWithSameFreq.size() == 0) {
            freqMap.remove(n.freq);
            
            if (minFreq == n.freq) {
                ++minFreq;
            }
        }
        ++n.freq;
        return n;
    }
}

/**
 * Your LFUCache object will be instantiated and called as such:
 * LFUCache obj = new LFUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */