package it.unibz.swipegan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 *   Code from Combinatorics Library 3.
 *   See: https://github.com/dpaukov/combinatoricslib3
 *   Copyright 2009-2016 Dmytro Paukov d.paukov@gmail.com
 */
public class Generator {

    public static <T> CombinationGenerator<T> combination(T... args) {
        return new CombinationGenerator<>(Arrays.asList(args));
    }

    public static <T> CombinationGenerator<T> combination(Collection<T> collection) {
        return new CombinationGenerator<>(collection);
    }

    public static <T> SubSetGenerator<T> subset(T... args) {
        return new SubSetGenerator<>(Arrays.asList(args));
    }
}

/**
 *   Code from Combinatorics Library 3.
 *   See: https://github.com/dpaukov/combinatoricslib3
 *   Copyright 2009-2016 Dmytro Paukov d.paukov@gmail.com
 */
class SubSetGenerator<T> {

    final Collection<T> originalVector;

    SubSetGenerator(Collection<T> originalVector) {
        this.originalVector = originalVector;
    }

    public IGenerator<List<T>> simple() {
        return new SimpleSubSetGenerator<>(originalVector);
    }
}

/**
 *   Code from Combinatorics Library 3.
 *   See: https://github.com/dpaukov/combinatoricslib3
 *   Copyright 2009-2016 Dmytro Paukov d.paukov@gmail.com
 */
class SimpleSubSetGenerator<T> implements IGenerator<List<T>> {

    final List<T> originalVector;

    SimpleSubSetGenerator(Collection<T> originalVector) {
        this.originalVector = new ArrayList<>(originalVector);
    }

    @Override
    public Iterator<List<T>> iterator() {
        return new SimpleSubSetIterator<>(this);
    }
}

/**
 *   Code from Combinatorics Library 3.
 *   See: https://github.com/dpaukov/combinatoricslib3
 *   Copyright 2009-2016 Dmytro Paukov d.paukov@gmail.com
 */
class SimpleSubSetIterator<T> implements Iterator<List<T>> {

    private final SimpleSubSetGenerator<T> generator;
    private final int length;

    private final List<T> currentSubSet;
    private long currentIndex;

    /** Internal bit vector, representing the subset. */
    private final BitSet bitVector;

    SimpleSubSetIterator(final SimpleSubSetGenerator<T> generator) {
        this.generator = generator;
        this.length = generator.originalVector.size();
        this.currentSubSet = new ArrayList<>(length);
        this.bitVector = new BitSet(length + 2);
        this.currentIndex = 0;
    }

    /**
     * Returns true if iteration is done, otherwise false
     *
     * @see Iterator#hasNext()
     */
    @Override
    public boolean hasNext() {
        return !bitVector.get(length + 1);
    }

    /**
     * Returns the next subset if it is available
     *
     * @see Iterator#next()
     */
    @Override
    public List<T> next() {
        this.currentIndex++;
        List<T> originalVector = this.generator.originalVector;
        BitSet bitVector = this.bitVector;
        int subSetSize = currentSubSet.size();
        int j = 0;

        for (int i = bitVector.nextSetBit(1); i >= 0; i = bitVector.nextSetBit(i + 1)) {
            T e = originalVector.get(i - 1);
            if (j < subSetSize) {
                currentSubSet.set(j++, e);
            } else {
                currentSubSet.add(e);
            }
        }

        // Do we have leftovers?
        if (j < subSetSize) {
            currentSubSet.subList(j, subSetSize).clear();
        }

        int i = 1;
        while (bitVector.get(i)) {
            bitVector.clear(i);
            i++;
        }
        bitVector.set(i);

        return new ArrayList<>(currentSubSet);
    }


    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }


    @Override
    public String toString() {
        return "SimpleSubSetIterator=[#" + currentIndex + ", " + currentSubSet + "]";
    }
}

/**
 *   Code from Combinatorics Library 3.
 *   See: https://github.com/dpaukov/combinatoricslib3
 *   Copyright 2009-2016 Dmytro Paukov d.paukov@gmail.com
 */
class CombinationGenerator<T> {
    final Collection<T> originalVector;

    CombinationGenerator(Collection<T> originalVector) {
        this.originalVector = originalVector;
    }

    public IGenerator<List<T>> simple(int length) {
        return new SimpleCombinationGenerator<>(originalVector, length);
    }
}

/**
 *   Code from Combinatorics Library 3.
 *   See: https://github.com/dpaukov/combinatoricslib3
 *   Copyright 2009-2016 Dmytro Paukov d.paukov@gmail.com
 */
class SimpleCombinationGenerator<T> implements IGenerator<List<T>> {

    final List<T> originalVector;
    final int combinationLength;

    /**
     * Constructor
     *
     * @param originalVector Original vector which is used for generating the combination
     * @param combinationsLength Length of the combinations
     */
    SimpleCombinationGenerator(Collection<T> originalVector,
                               int combinationsLength) {
        this.originalVector = new ArrayList<>(originalVector);
        this.combinationLength = combinationsLength;
    }

    /**
     * Creates an iterator of the simple combinations (without repetitions)
     */
    @Override
    public Iterator<List<T>> iterator() {
        return new SimpleCombinationIterator<>(this);
    }
}

/**
 *   Code from Combinatorics Library 3.
 *   See: https://github.com/dpaukov/combinatoricslib3
 *   Copyright 2009-2016 Dmytro Paukov d.paukov@gmail.com
 */
class SimpleCombinationIterator<T> implements Iterator<List<T>> {

    private final SimpleCombinationGenerator<T> generator;
    private final List<T> currentCombination = new ArrayList<>();
    // Internal array
    private final int[] bitVector;
    private long currentIndex;
    //Criteria to stop iterating the combinations.
    private int endIndex = 0;

    SimpleCombinationIterator(SimpleCombinationGenerator<T> generator) {
        this.generator = generator;
        this.bitVector = new int[generator.combinationLength + 1];
        for (int i = 0; i <= generator.combinationLength; i++) {
            this.bitVector[i] = i;
        }
        if (generator.originalVector.size() > 0) {
            this.endIndex = 1;
        }
        this.currentIndex = 0;
    }

    private void setValue(int index, T value) {
        if (index < this.currentCombination.size()) {
            this.currentCombination.set(index, value);
        } else {
            this.currentCombination.add(index, value);
        }
    }

    /**
     * Returns true if all combinations were iterated, otherwise false
     */
    @Override
    public boolean hasNext() {
        return !((this.endIndex == 0) || (this.generator.combinationLength
                > this.generator.originalVector.size()));
    }

    /**
     * Moves to the next combination
     */
    @Override
    public List<T> next() {
        this.currentIndex++;

        for (int i = 1; i <= this.generator.combinationLength; i++) {
            int index = this.bitVector[i] - 1;
            if (this.generator.originalVector.size() > 0) {
                this.setValue(i - 1, this.generator.originalVector.get(index));
            }
        }

        this.endIndex = this.generator.combinationLength;
        while (this.bitVector[this.endIndex]
                == this.generator.originalVector.size() - this.generator.combinationLength + endIndex) {
            this.endIndex--;
            if (endIndex == 0) {
                break;
            }
        }
        this.bitVector[this.endIndex]++;
        for (int i = this.endIndex + 1; i <= this.generator.combinationLength; i++) {
            this.bitVector[i] = this.bitVector[i - 1] + 1;
        }

        // Return a copy of the current combination.
        return new ArrayList<>(this.currentCombination);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "SimpleCombinationIterator=[#" + this.currentIndex + ", " + this.currentCombination
                + "]";
    }
}

/**
 *   Code from Combinatorics Library 3.
 *   See: https://github.com/dpaukov/combinatoricslib3
 *   Copyright 2009-2016 Dmytro Paukov d.paukov@gmail.com
 */
interface IGenerator<T> extends Iterable<T> {
    default Stream<T> stream() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(), 0), false);
    }
}