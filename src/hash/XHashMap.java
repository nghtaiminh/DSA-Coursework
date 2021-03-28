/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hash;

import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author LTSACH
 */
public class XHashMap<K, V> implements IMap<K, V> {
	private static final int MAX_CAPACITY = Integer.MAX_VALUE - 8;
	private Entry<K, V>[] table;
	private int size;
	private float loadFactor;

	public XHashMap() {
		this(10, 0.75f);
	}

	public XHashMap(int initialCapacity, float loadFactor) {
		this.table = new Entry[initialCapacity];
		this.size = 0;
		this.loadFactor = loadFactor;
	}

	//////////////////////////////////////////////////////////////////////////
	/////////////////// Utility methods (private) ////////////////////
	//////////////////////////////////////////////////////////////////////////
	private void ensureLoadFactor(int minCapacity) {
		int maxSize = (int) (this.loadFactor * this.table.length);
		if ((minCapacity < 0) || (minCapacity > MAX_CAPACITY))
			throw new OutOfMemoryError("Not enough memory to store the array");
		if (minCapacity >= maxSize) {
			// grow: oldCapacity >> 1 (= oldCapacity/2)
			int oldCapacity = this.table.length;
			int newCapacity = 2 * oldCapacity;
			if (newCapacity < 0)
				newCapacity = MAX_CAPACITY;
			rehash(newCapacity);
		}
	}

	private void rehash(int newCapacity) {
		Entry<K, V>[] oldTable = this.table;
		this.table = new Entry[newCapacity];
		this.size = 0;

		for (Entry<K, V> entry : oldTable) {
			while (entry != null) {
				this.put(entry.key, entry.value);
				entry = entry.next;
			}
		}
	}

	protected int key2index(K key) {
		return key.hashCode() % this.table.length;
	}

	//////////////////////////////////////////////////////////////////////////
	/////////////////// API of XHashMap ///////////////////
	//////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("unused")
	@Override
	public V put(K key, V value) {
		int index = this.key2index(key);

		/* YOUR CODE HERE */
		ensureLoadFactor(size++);

		Entry<K, V> entry = table[index];

		while (entry != null) {
			if (entry.key.equals(key)) {
				V oldValue = entry.value;
				entry.value = value;
				return oldValue;

			} else if (entry.next == null) {
				Entry<K, V> e = new Entry(key, value, null, null);
				e.prev = entry;
				entry.next = e;
				return null;
			}

			entry = entry.next;
		}

		entry = new Entry(key, value, null, null);
		table[index] = entry;

		return null;
	}

	@Override
	public V get(K key) {
		int index = this.key2index(key);
		/* YOUR CODE HERE */

		Entry<K, V> entry = table[index];

		while (entry != null) {
			if (entry.key.equals(key))
				return entry.value;
			entry = entry.next;
		}

		return null; // should remove this line
	}

	@Override
	public V remove(K key) {
		int index = this.key2index(key);
		Entry<K, V> entry = table[index];
		while (entry != null) {
			/* YOUR CODE HERE */

			if (entry.key.equals(key)) {
				size--;
				this.table[index] = entry.next;
				return entry.value;
			}
			entry = entry.next;
		}
		return null;
	}

	@Override
	public boolean remove(K key, V value) {
		int index = this.key2index(key);
		Entry<K, V> entry = this.table[index];
		while (entry != null) {
			if (entry.key.equals(key) && entry.value.equals(value)) {
				size -= 1;
				/* YOUR CODE HERE */

				if (entry.prev == null) {
					this.table[index] = entry.next;

				} else if (entry.next == null) {
					entry.prev.next = null;

				} else {
					entry.prev.next = entry.next;
					entry.next.prev = entry.prev;
				}

				return true;

			}
			entry = entry.next;
		}
		return false;
	}

	@Override
	public boolean containsKey(K key) {
		int index = this.key2index(key);
		Entry<K, V> entry = this.table[index];
		while (entry != null) {
			/* YOUR CODE HERE */
			if (entry.key.equals(key))
				return true;
			entry = entry.next;
		}
		return false;
	}

	@Override
	public boolean containsValue(V value) {
		for (Entry<K, V> entry : this.table) {
			while (entry != null) {
				/* YOUR CODE HERE */
				if (entry.value.equals(value))
					return true;
				entry = entry.next;
			}
		}
		return false;
	}

	@Override
	public boolean empty() {
		return this.size == 0;
	}

	@Override
	public int size() {
		return this.size;
	}

	public void println() {
		System.out.println(this.toString());
	}

	public String toString() {
		String desc = String.format("%s\n", new String(new char[50]).replace('\0', '-'));
		desc += String.format("Capacity: %d\n", this.capacity());
		desc += String.format("Size: %d\n", this.size());
		for (int idx = 0; idx < this.table.length; idx++) {
			Entry<K, V> entry = this.table[idx];
			String line = String.format("[%4d]:", idx);
			while (entry != null) {
				line += String.format("(%s, %s),", entry.key, entry.value);
				entry = entry.next;
			}
			if (line.endsWith(","))
				line = line.substring(0, line.length() - 1);
			desc += line + "\n";
		}
		desc += String.format("%s\n", new String(new char[50]).replace('\0', '-'));
		return desc;
	}

	/////////////////////////////////////////////////////////
	///// The following methods are used for testing only ///
	/////////////////////////////////////////////////////////
	public int capacity() {
		return this.table.length;
	}

	public int numEntryAt(int index) {
		Entry<K, V> entry = this.table[index];
		int count = 0;
		while (entry != null) {
			count += 1;
			entry = entry.next;
		}
		return count;
	}
}

class Entry<K, V> {
	K key;
	V value;
	Entry<K, V> prev;
	Entry<K, V> next;

	Entry() {
		this(null, null, null, null);
	}

	Entry(K key, V value, Entry<K, V> prev, Entry<K, V> next) {
		this.key = key;
		this.value = value;
		this.prev = prev;
		this.next = next;
	}
}