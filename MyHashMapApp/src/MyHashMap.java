import java.util.Arrays;
import java.util.Objects;

public class MyHashMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node[] table;
    private int size;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        size = 0;
    }

    private static class Node<K, V>{
        final K key;
        V value;
        Node<K, V> next;
        final int hash;

        public Node(int hash, Node<K, V> next, V value, K key) {
            this.hash = hash;
            this.next = next;
            this.value = value;
            this.key = key;
        }
    }

    private int hash(K key){
        if (key == null){
            return 0;
        }
        int h = key.hashCode();
        return h ^ (h >>> 16);
    }

    private int indexFor(int hash, int length){
        return hash & (length - 1);
    }

    public V put(K key, V value){
        if (size >= LOAD_FACTOR * table.length){
            resize();
        }
        int hash = hash(key);
        int index = indexFor(hash, table.length);

        Node<K, V> node = table[index];
        while (node != null){
            if (node.hash == hash &&
                (Objects.equals(key, node.key))) {
                V oldValue = node.value;
                node.value = value;
                return oldValue;
            }
            node = node.next;
        }

        Node<K, V> newNode = new Node<>(hash, table[index], value, key);
        table[index] = newNode;
        size++;
        return null;
    }

    public V get(K key){
        int hash = hash(key);
        int index = indexFor(hash, table.length);

        Node<K, V> node = table[index];
        while (node != null){
            if (node.hash == hash && (Objects.equals(key, node.key))){
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    public V remove(K key){
        int hash = hash(key);
        int index = indexFor(hash, table.length);

        Node<K, V> prev = null;
        Node<K, V> node = table[index];

        while (node != null){
            if (node.hash == hash && (Objects.equals(key, node.key))){
               if (prev == null){
                   table[index] = node.next;
               } else {
                   prev.next = node.next;
               }
               size --;
               return node.value;
            }
            prev = node;
            node = node.next;
        }
        return null;
    }

    private void resize(){
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * 2];

        for (Node<K, V> head : oldTable){
            while (head != null){
                Node<K, V> next = head.next;
                int newIndex = indexFor(head.hash, table.length);
                table[newIndex] = head;
                head = next;
            }
        }
    }

    public int size() {
        return size;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public void clear() {
        Arrays.fill(table, null);
        size = 0;
    }

}
