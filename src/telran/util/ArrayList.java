package telran.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class ArrayList<T> extends AbstractList<T> {
	private static final int DEFAULT_CAPACITY = 16;
	private T[] array;
	
	private class ArrayListIterator implements Iterator<T> {
		int current = 0;
		
		@Override
		public boolean hasNext() {
			
			return current < size;
		}

		@Override
		public T next() {
			if(current >= size) {
				throw new NoSuchElementException();
			}
			return array[current++];
		}
		@Override
		public void remove() {
			if(next() == null) {
				throw new IllegalStateException();
			}
			ArrayList.this.remove(--current);
		}
	}
	@SuppressWarnings("unchecked")
	public ArrayList(int capacity) {
		array = (T[]) new Object[capacity];
	}
	public ArrayList() {
		this(DEFAULT_CAPACITY);
	}
	
	@Override
	public void add(T element) {
		//O[1]
		if (size == array.length) {
			//size is capacity
			allocate();
		}
		array[size++] = element;
	}
	
	private void allocate() {
		array = Arrays.copyOf(array, array.length * 2);
		
	}
	@Override
	public boolean add(int index, T element) {
		//O[N]
		boolean res = false;
		if (index == size) {
			add(element);
			res = true;
			
		} else if(isValidIndex(index)) {
			res = true;
			if (size == array.length) {
				allocate();
			}
			System.arraycopy(array, index, array, index + 1, size - index);
			array[index] = element;
			size++;
		}
		return res;
	}

	@Override
	public T get(int index) {
		//O[1]
		return isValidIndex(index) ? array[index] : null;
	}

	@Override
	public T remove(int index) {
		//O[N]
		T res = null;
		if (isValidIndex(index)) {
			res = array[index];
			size--;
			System.arraycopy(array, index + 1, array, index, size - index);
			//FIXME regarding setting null
		}
		return res;
	}
	

	@Override
	public int indexOf(Predicate<T> predicate) {
		//O[N]
				int res = -1;
				for (int i = 0; i < size; i++) {
					if (predicate.test(array[i])) {
						res = i;
						break;
					}
				}
				return res;
	}
	@Override
	public int lastIndexOf(Predicate<T> predicate) {
		//O[N]
				int res = -1;
				for (int i = size - 1; i >=0 ; i--) {
					if (predicate.test(array[i])) {
						res = i;
						break;
					}
				}
				return res;
	}
	@Override
	public boolean removeIf(Predicate<T> predicate) {
		//O[N]
		int oldSize = size;
		int indCopy = 0;
		for (int i = 0; i < oldSize; i++) {
			if (!predicate.test(array[i])) {
				array[indCopy++] = array[i];
			}
		}
		size = indCopy;
		return oldSize > size;
	}
	//FIXME
	@Override
	public void sort(Comparator<T> comp) {
		//O[N * LogN]
		Arrays.sort(array, 0, size, comp);
		
	}
	@Override
	public int sortedSearch(T pattern, Comparator<T> comp) {
		int left = 0;
		int right = size - 1;
		int middle = 0;
		int res = -1;
		while (left <= right) {
			middle = (left + right) / 2;
			int resComp = comp.compare(pattern, array[middle]);
			if (resComp == 0) {
				res = middle;
				break;
			}
			if (resComp > 0) {
				left = middle + 1;
			} else {
				right = middle - 1;
			}
		}
		return left > right ? -(left + 1) : res;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void clear() {
		array = (T[]) new Object[DEFAULT_CAPACITY];
		size = 0;
		//FIXME
	}
	@Override
	public Iterator<T> iterator() {
		
		return new ArrayListIterator();
	}
	

}