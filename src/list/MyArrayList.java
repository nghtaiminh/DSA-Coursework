/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package list;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * See:
 * http://hg.openjdk.java.net/jdk7/jdk7/jdk/file/tip/src/share/classes/java/util/ArrayList.java
 * 
 * @author LTSACH
 */
public class MyArrayList<E> implements java.util.List<E> {

	private static enum MoveType {
		NEXT, PREV
	};

	private static final int MAX_CAPACITY = Integer.MAX_VALUE - 8;
	private E[] elements;
	private int size;

	// Constructor:
	public MyArrayList(int capacity) throws IllegalArgumentException {
		if ((capacity < 0) || (capacity > MAX_CAPACITY)) {
			String message = String.format("Invalid capacity (=%d)", capacity);
			throw new IllegalArgumentException(message);
		}

		this.elements = (E[]) new Object[capacity];
		this.size = 0;
	}

	public MyArrayList() throws IllegalArgumentException {
		this(10);
	}

	// Utitilies
	private void checkValidIndex(int index, int min, int max) {
		if ((index < min) || (index > max)) {
			String message = String.format("Invalid index (=%d)", index);
			throw new IndexOutOfBoundsException(message);
		}
	}

	///
	@Override
	public int size() {
		return this.size;
	}

	@Override
	public boolean isEmpty() {
		return this.size == 0;
	}

	@Override
	public boolean contains(Object o) {
		/* YOUR CODE HERE */
		return indexOf(o) != -1; // should remove this line
	}

	@Override
	public Iterator<E> iterator() {
		/* YOUR CODE HERE */
		return new MyIterator(); // should remove this line
	}

	@Override
	public Object[] toArray() {
		return Arrays.copyOf(elements, size);
	}

	@Override
	public <T> T[] toArray(T[] a) {
		/* IMPLEMENTATION: NOT REQUIRED */
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

	@Override
	public boolean add(E e) {

		/* YOUR CODE HERE */
		add(size, e);
		return true;
	}

	/*
	 * int newCapacity = oldCapacity + (oldCapacity >> 1); means: newCapacity =
	 * oldCapacity + oldCapacity/2 => newCapacity maybe a negative becuase of the
	 * overflow
	 */
	private void checkCapacity(int minCapacity) {
		if ((minCapacity < 0) || (minCapacity > MAX_CAPACITY))
			throw new OutOfMemoryError("Not enough memory to store the array");
		if (minCapacity >= this.elements.length) {
			// grow: oldCapacity >> 1 (= oldCapacity/2)
			int oldCapacity = this.elements.length;
			int newCapacity = oldCapacity + (oldCapacity / 2);
			if (newCapacity < 0)
				newCapacity = MAX_CAPACITY;
			this.elements = Arrays.copyOf(this.elements, newCapacity);
		}
	}

	@Override
	public boolean remove(Object o) {

		/* YOUR CODE HERE */
		if (o == null) {
			for (int index = 0; index < size; index++)
				if (elements[index] == null) {
					remove(index);
					return true;
				}
		} else {
			for (int index = 0; index < size; index++)
				if (o.equals(elements[index])) {
					remove(index);
					return true;
				}
		}

		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		/* IMPLEMENTATION: NOT REQUIRED */
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		/* IMPLEMENTATION: NOT REQUIRED */
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		/* IMPLEMENTATION: NOT REQUIRED */
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		/* IMPLEMENTATION: NOT REQUIRED */
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		/* IMPLEMENTATION: NOT REQUIRED */
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

	@Override
	public void clear() {

		/* YOUR CODE HERE */
		for (int i = 0; i < size; i++)
			elements[i] = null;

		size = 0;
	}

	@Override
	public E get(int index) {
		checkValidIndex(index, 0, size - 1);

		/* YOUR CODE HERE */
		return elements[index];
	}

	@Override
	public E set(int index, E element) {
		checkValidIndex(index, 0, size - 1);

		/* YOUR CODE HERE */
		E oldValue = elements[index];
		elements[index] = element;
		return oldValue;
	}

	@Override
	public void add(int index, E element) {
		checkValidIndex(index, 0, size);
		if (element == null)
			throw new NullPointerException("Can not add null pointer");
		checkCapacity(this.size + 1);

		int copyCount = (this.size - 1) - index + 1;
		System.arraycopy(this.elements, index, this.elements, index + 1, copyCount);

		/* YOUR CODE HERE */
		elements[index] = element;
		size++;
	}

	@Override
	public E remove(int index) {
		checkValidIndex(index, 0, size - 1);
		E oldElement = this.elements[index];
		int copyCount = (this.size - 1) - (index + 1) + 1;
		System.arraycopy(this.elements, index + 1, this.elements, index, copyCount);

		/* YOUR CODE HERE */
		elements[--size] = null;

		return oldElement;
	}

	@Override
	public int indexOf(Object o) {
		/* YOUR CODE HERE */
		for (int i = 0; i < size; i++)
			if (o.equals(elements[i]))
				return i;

		return -1;
	}

	@Override
	public int lastIndexOf(Object o) {
		/* YOUR CODE HERE */
		for (int i = size - 1; i >= 0; i--)
			if (o.equals(elements[i]))
				return i;

		return -1;
	}

	@Override
	public ListIterator<E> listIterator() {
		/* YOUR CODE HERE */
		return new MyListIterator(0);
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		/* YOUR CODE HERE */
		checkValidIndex(index, 0, size);
		return new MyListIterator(index);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		/* IMPLEMENTATION: NOT REQUIRED */
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

	@Override
	public String toString() {
		String desc = "[";
		Iterator<E> it = this.iterator();
		while (it.hasNext()) {
			E e = it.next();
			desc += String.format("%s,", e);
		}
		if (desc.endsWith(","))
			desc = desc.substring(0, desc.length() - 1);
		desc += "]";
		return desc;
	}

	// Definition of Inner Class
	///////////////////////////////////////////////////////////////////////////
	public class MyIterator implements Iterator<E> {
		int cursor = 0;
		MoveType moveType = MoveType.NEXT;
		boolean afterMove = false;

		@Override
		public boolean hasNext() {
			return this.cursor != MyArrayList.this.size;
		}

		@Override
		/*
		 * Move cursor to next + return preivous element
		 */
		public E next() {
			cursor += 1;
			moveType = MoveType.NEXT;
			afterMove = true;
			return MyArrayList.this.elements[cursor - 1];
		}

		@Override
		public void remove() {
			if (!afterMove)
				return;
			MyArrayList.this.remove(cursor - 1);
			cursor -= 1;
			afterMove = false;
		}
	}// End of MyIterator

	public class MyListIterator extends MyIterator implements ListIterator<E> {
		public MyListIterator(int index) {
			cursor = index;
		}

		@Override
		public boolean hasPrevious() {
			return this.cursor != 0;
		}

		@Override
		public void remove() {
			if (!afterMove)
				return;
			if (moveType == MoveType.NEXT)
				super.remove();
			else {
				MyArrayList.this.remove(cursor);
				afterMove = false;
			}
		}

		@Override
		public E previous() {
			cursor -= 1;
			moveType = MoveType.PREV;
			afterMove = true;
			return MyArrayList.this.elements[cursor];
		}

		@Override
		public int nextIndex() {
			return cursor;
		}

		@Override
		public int previousIndex() {
			return cursor - 1;
		}

		@Override
		public void set(E e) {
			if (!afterMove)
				return;
			if (moveType == MoveType.NEXT)
				MyArrayList.this.set(cursor - 1, e);
			else
				MyArrayList.this.set(cursor, e);
		}

		@Override
		public void add(E e) {
			if (!afterMove)
				return;
			if (moveType == MoveType.NEXT) {
				MyArrayList.this.add(cursor - 1, e);
			} else {
				MyArrayList.this.add(cursor, e);
			}
			cursor += 1;
			afterMove = false;
		}

	}// End of MyListIterator
}// End of MyArrayList
