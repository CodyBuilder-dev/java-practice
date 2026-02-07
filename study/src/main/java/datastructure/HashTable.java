package datastructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class HashTable<K,V> {
  static class Node<K,V> {
    K key;
    V value;
    Node<K,V> next;

    Node(K key, V value) {
      this.key = key;
      this.value = value;
      this.next = null;
    }
    
  }

  private Node<K,V>[] table;

  public HashTable(int maxSize) {
    table = new Node[maxSize];
  }

  private int hash(K key) {
    return Math.abs(key.hashCode()) % table.length;
  }

  public int size() {
    int count = 0;
    for(Node<K,V> i: table) {
      if(i != null) {
        count++;
      }
    }
    return count;
  }
  
  public boolean isEmpty() {
    return size() == 0;
  }
  
  public boolean containsKey(K key) {
    return table[hash(key)] != null;
  }
  
  public boolean containsValue(Object value) {
    for (Node<K,V> i: table) {
      Node<K,V> current = i;
      while (current != null) {
        if (current.value.equals(value)) {
          return true;
        }
        current = current.next;
      }
    }

    return false;
  }

  public V get(K key) {
    int index = hash(key);
    Node<K,V> current = table[index];

    while (current != null) {
      if (current.key.equals(key)) {
        return current.value;
      }
      current = current.next;
    }
    return null; // Key not found
  }

  public void put(K key, V value) {
    int index = hash(key);
    Node<K,V> newNode = new Node<>(key, value);

    if (table[index] == null) {
      table[index] = newNode;
    } else {
      Node<K,V> current = table[index];
      while (true) {
        if (current.key.equals(key)) {
          current.value = value; // Update existing key
          return;
        }
        if (current.next == null) {
          break;
        }
        current = current.next;
      }
      current.next = newNode; // Add to the end of the chain
    }
  }
  public void putAll(Map<K,V> m) {
    for(Entry<K,V> entry: m.entrySet()) {
      put(entry.getKey(), entry.getValue());
    }
  }
  
  public V remove(K key) {
    Node<K,V> current = table[hash(key)];
    Node<K,V> prev = null;
    while (current != null) {
      if (current.key.equals(key)) {
        if (prev == null) {
          table[hash(key)] = current.next; // Remove head
        } else {
          prev.next = current.next; // Bypass the current node
        }
        return current.value;
      }
      prev = current;
      current = current.next;
    }
    return null; // Key not found
  }
  
  public void clear() {
    this.table = new Node[table.length];
  }
  
  public Set<K> keySet() {
    Set<K> set = new HashSet<>();
    for(Node<K,V> i: table) {
      if(i != null) {
        set.add(i.key);
      }
    }
    return set;
  }
  
  public Collection<V> values() {
    List<V> values = new ArrayList<>();
    for(Node<K,V> i: table) {
      Node<K,V> current = i;
      while (current != null) {
        values.add(current.value);
        current = current.next;
      }
    }
    return values;
  }

  public static void main(String[] args) {
    HashTable<String,String> hashTable = new HashTable<>(100);
    hashTable.put("name", "Alice");
    hashTable.put("age", "30");
    hashTable.put("city", "New York");

    System.out.println("Name: " + hashTable.get("name"));
    System.out.println("Age: " + hashTable.get("age"));
    System.out.println("City: " + hashTable.get("city"));
    System.out.println("Country: " + hashTable.get("country")); // Key not found

    System.out.println("Updating age...");
    hashTable.put("age", "31");
    System.out.println("Age: " + hashTable.get("age"));

    System.out.println("Removing city...");
    System.out.println("Removed City: " + hashTable.remove("city"));
    System.out.println("City: " + hashTable.get("city")); // Key not found

    System.out.println("Keys: " + hashTable.keySet()); // Should not include city. Order not guaranteed
    System.out.println("Values: " + hashTable.values()); // Should not include New York, only Alice and 31. Order not guaranteed

    System.out.println("Size: " + hashTable.size());
    System.out.println("Is Empty: " + hashTable.isEmpty());
    System.out.println("Contains key 'name': " + hashTable.containsKey("name"));
    System.out.println("Contains value 'Alice': " + hashTable.containsValue("Alice"));
    System.out.println("Contains key 'age': " + hashTable.containsKey("age"));
    System.out.println("Contains value '30': " + hashTable.containsValue("30"));
    System.out.println("Contains key 'city': " + hashTable.containsKey("city"));
    System.out.println("Contains value 'New York': " + hashTable.containsValue("New York"));

    hashTable.clear();
    System.out.println("After clearing, size: " + hashTable.size());

    Map<String, String> newEntries = Map.of(
      "country", "USA",
      "language", "English"
    );
    hashTable.putAll(newEntries);
    System.out.println("After putAll, keys: " + hashTable.keySet());
    System.out.println("After putAll, values: " + hashTable.values());
  }
}