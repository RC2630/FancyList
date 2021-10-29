import java.util.*;

public class FancyList<T> extends ArrayList<T> {

    private String iteratorMode = "none";

    public FancyList() {
        super();
    }

    // THE FOLLOWING CONSTRUCTORS AND METHODS ARE NOT CURRENTLY BEING USED BY ANYTHING:

    /*
    public FancyList(String mode) {
        super();
        iteratorMode = mode;
    }

    public FancyList(Collection<T> c) {
        super(c);
    }

    public FancyList(Collection<T> c, String mode) {
        super(c);
        iteratorMode = mode;
    }

    public String getMode() {
        return iteratorMode;
    }
    */

    public void setMode(String mode) {
        iteratorMode = mode;
    }

    private int indexOfMin(List<Integer> indexesToIgnore) {
        int firstNonIgnoreIndex = -1;
        for (int i = 0; i < super.size(); i++) {
            if (!indexesToIgnore.contains(i)) {
                firstNonIgnoreIndex = i;
                break;
            }
        }
        // at this point, firstNonIgnoreIndex should no longer be -1, because that would mean all indexes are ignored
        if (firstNonIgnoreIndex == -1) {
            throw new IllegalStateException();
        }
        int indexMin = firstNonIgnoreIndex;
        T min = super.get(firstNonIgnoreIndex);
        for (int i = firstNonIgnoreIndex + 1; i < super.size(); i++) {
            if (!indexesToIgnore.contains(i)) {
                if (((Comparable<T>) super.get(i)).compareTo(min) < 0) { // current element < min
                    min = super.get(i);
                    indexMin = i;
                }
            }
        }
        return indexMin;
    }

    private int indexOfMax(List<Integer> indexesToIgnore) {
        int firstNonIgnoreIndex = -1;
        for (int i = 0; i < super.size(); i++) {
            if (!indexesToIgnore.contains(i)) {
                firstNonIgnoreIndex = i;
                break;
            }
        }
        // at this point, firstNonIgnoreIndex should no longer be -1, because that would mean all indexes are ignored
        if (firstNonIgnoreIndex == -1) {
            throw new IllegalStateException();
        }
        int indexMax = firstNonIgnoreIndex;
        T max = super.get(firstNonIgnoreIndex);
        for (int i = firstNonIgnoreIndex + 1; i < super.size(); i++) {
            if (!indexesToIgnore.contains(i)) {
                if (((Comparable<T>) super.get(i)).compareTo(max) > 0) { // current element > max
                    max = super.get(i);
                    indexMax = i;
                }
            }
        }
        return indexMax;
    }

    private boolean hasNoOddsLeft(List<Integer> indexesToIgnore) {
        for (int i = 0; i < super.size(); i++) {
            int currentElement;
            if (super.get(i) instanceof Double) {
                currentElement = (int) ((Double) super.get(i)).doubleValue();
            } else if (super.get(i) instanceof Integer) {
                currentElement = (Integer) super.get(i);
            } else {
                throw new IllegalStateException(); // should never be here
            }
            if (!indexesToIgnore.contains(i) && (currentElement % 2 == 1)) {
                return false;
            }
        }
        return true;
    }

    private int nextOddOrEvenNumberIndex(List<Integer> indexesToIgnore) {
        int firstNonIgnoreIndex = -1;
        for (int i = 0; i < super.size(); i++) {
            if (!indexesToIgnore.contains(i)) {
                firstNonIgnoreIndex = i;
                break;
            }
        }
        // at this point, firstNonIgnoreIndex should no longer be -1, because that would mean all indexes are ignored
        if (firstNonIgnoreIndex == -1) {
            throw new IllegalStateException("all indexes are ignored");
        }
        if (hasNoOddsLeft(indexesToIgnore)) {
            return firstNonIgnoreIndex; // guaranteed to be the index we are looking for, since there are no odds left
        }
        for (int i = firstNonIgnoreIndex; i < super.size(); i++) {
            if (!indexesToIgnore.contains(i)) {
                int currentElement;
                if (super.get(i) instanceof Double) {
                    currentElement = (int) ((Double) super.get(i)).doubleValue();
                } else if (super.get(i) instanceof Integer) {
                    currentElement = (Integer) super.get(i);
                } else {
                    throw new IllegalStateException(); // should never be here
                }
                if (currentElement % 2 == 1) {
                    return i;
                }
            }
        }
        throw new IllegalStateException("no valid index found"); // should never reach here
    }

    private T nthSmallestElement(int n) {
        List<Integer> indexesToIgnore = new ArrayList<>();
        for (int i = 1; i < n; i++) { // loop n - 1 times
            indexesToIgnore.add(indexOfMin(indexesToIgnore));
        }
        return super.get(indexOfMin(indexesToIgnore));
    }

    private T nthBiggestElement(int n) {
        List<Integer> indexesToIgnore = new ArrayList<>();
        for (int i = 1; i < n; i++) { // loop n - 1 times
            indexesToIgnore.add(indexOfMax(indexesToIgnore));
        }
        return super.get(indexOfMax(indexesToIgnore));
    }

    private T nextOddOrEvenNumber(int n) {
        List<Integer> indexesToIgnore = new ArrayList<>();
        for (int i = 1; i < n; i++) { // loop n - 1 times
            indexesToIgnore.add(nextOddOrEvenNumberIndex(indexesToIgnore));
        }
        return super.get(nextOddOrEvenNumberIndex(indexesToIgnore));
    }

    @Override
    public Iterator<T> iterator() {
        if (super.isEmpty()) {
            return super.iterator();
        }
        switch (iteratorMode) {
            case "normal":
                return super.iterator();
            case "reverse":
                return new ReverseIterator();
            case "ascending":
                if (!(super.get(0) instanceof Comparable)) {
                    throw new CustomExceptions.NotComparableException();
                }
                return new AscendingIterator();
            case "descending":
                if (!(super.get(0) instanceof Comparable)) {
                    throw new CustomExceptions.NotComparableException();
                }
                return new DescendingIterator();
            case "odd before even":
                if (!((super.get(0) instanceof Integer) || (super.get(0) instanceof Double))) {
                    throw new CustomExceptions.NotNumberException();
                }
                return new OddBeforeEvenIterator();
            default:
                throw new CustomExceptions.NoSuchIteratorException();
        }
    }

    private class ReverseIterator implements Iterator<T> {

        private int index = FancyList.super.size() - 1;

        @Override
        public boolean hasNext() {
            return index != -1;
        }

        @Override
        public T next() {
            T element = FancyList.super.get(index);
            index--;
            return element;
        }

    }

    private class AscendingIterator implements Iterator<T> {

        private int numUsed = 0;

        @Override
        public boolean hasNext() {
            return numUsed != FancyList.super.size();
        }

        @Override
        public T next() {
            numUsed++;
            return nthSmallestElement(numUsed);
        }

    }

    private class DescendingIterator implements Iterator<T> {

        private int numUsed = 0;

        @Override
        public boolean hasNext() {
            return numUsed != FancyList.super.size();
        }

        @Override
        public T next() {
            numUsed++;
            return nthBiggestElement(numUsed);
        }

    }

    private class OddBeforeEvenIterator implements Iterator<T> {

        private int numUsed = 0;

        @Override
        public boolean hasNext() {
            return numUsed != FancyList.super.size();
        }

        @Override
        public T next() {
            numUsed++;
            return nextOddOrEvenNumber(numUsed);
        }

    }

}