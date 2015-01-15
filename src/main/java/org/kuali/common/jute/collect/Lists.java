package org.kuali.common.jute.collect;

import static com.google.common.base.Preconditions.checkElementIndex;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Ordering.natural;
import static java.lang.Math.min;
import static org.kuali.common.jute.base.Precondition.checkMin;
import static org.kuali.common.jute.base.Precondition.checkNotNull;

import java.util.AbstractList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Doubles;

public final class Lists {

    /**
     * Alter the list passed in by randomly shuffling it's elements.
     *
     * @return the same list that was passed in.
     */
    public static <T> List<T> shuffle(List<T> list) {
        Collections.shuffle(list);
        return list;
    }

    /**
     * Create a mutable shuffled copy of the list passed in.
     */
    public static <T> List<T> shuffledCopy(List<T> list) {
        return shuffle(newArrayList(list));
    }

    /**
     * Create an immutable shuffled copy of the list passed in.
     */
    public static <T> List<T> immutableShuffledCopy(List<T> list) {
        return ImmutableList.copyOf(shuffledCopy(list));
    }

    /**
     * Returns consecutive {@linkplain List#subList(int, int) sublists} of a list, distributing the elements of the list as evenly as possible into the specified number of
     * partitions. For example, distributing a list containing {@code [a, b, c, d, e]} into 3 partitions yields {@code [[a, b], [c, d], [e]]} -- an outer list containing three
     * inner lists of one and two elements, all in the original order. The sizes of the returned lists will differ by at most one from each other.
     *
     * <p>
     * The outer list is unmodifiable, but reflects the latest state of the source list. The inner lists are sublist views of the original list, produced on demand using
     * {@link List#subList(int, int)}, and are subject to all the usual caveats about modification as explained in that API.
     */
    public static <T> List<List<T>> distribute(List<T> list, int partitions) {
        checkNotNull(list);
        checkMin(partitions, 1, "partitions");
        return (list instanceof RandomAccess) ? new RandomAccessDistribution<T>(list, partitions) : new Distribution<T>(list, partitions);
    }

    /**
     * Creates a new, immutable, partitioned, list containing all of the elements from {@code list}. Elements are pseudo-evenly placed into sublists according to weight. The
     * algorithm used to distribute the elements by weight is greedy and non-perfect, but fast, and good enough for many real-life situations.
     *
     * <p>
     * Algorithm description:
     * <li>Calculate the weight of each element using the supplied function</li>
     * <li>Sort the elements in descending order by weight</li>
     * <li>Create {@code partitions} empty sublists</li>
     * <li>Add each element one at a time to the sublist with the smallest total weight</li>
     *
     * <p>
     * The size of each partition can vary (and likely will vary in real world situations)
     *
     * <p>
     * The ordering from the original {@code list} is not considered when producing the distribution.
     *
     * <p>
     * The distribution is an immutable copy. Changes to the original {@code list} have no affect on the distribution.
     */
    public static <T> List<List<T>> scatter(Iterable<T> iterable, int partitions, Function<T, Double> weigher) {
        checkNotNull(iterable, "iterable");
        checkMin(partitions, 1, "partitions");
        checkNotNull(weigher, "weigher");
        List<Weighed<T>> weighed = natural().reverse().sortedCopy(weighElements(iterable, weigher));
        List<List<T>> container = newContainer(min(weighed.size(), partitions));
        if (!weighed.isEmpty()) {
            fillContainer(container, weighed);
        }
        return immutableCopy(container);
    }

    private static <T> List<List<T>> newContainer(int size) {
        List<List<T>> container = newArrayList();
        for (int i = 0; i < size; i++) {
            List<T> list = newArrayList();
            container.add(list);
        }
        return container;
    }

    private static <T> void fillContainer(List<List<T>> container, List<Weighed<T>> sorted) {
        List<WeightIndex> weightIndexes = newArrayList();
        for (int i = 0; i < container.size(); i++) {
            weightIndexes.add(new WeightIndex(i));
        }
        Ordering<WeightIndex> ordering = Ordering.from(WeightIndexComparator.INSTANCE);
        WeightIndex min = ordering.min(weightIndexes);
        for (Weighed<T> element : sorted) {
            int index = min.getIndex();
            List<T> list = container.get(index);
            T value = element.getElement();
            list.add(value);
            double weight = min.getWeight() + element.getWeight();
            min.setWeight(weight);
            min = ordering.min(weightIndexes);
        }
    }

    private static <T> List<List<T>> immutableCopy(List<List<T>> container) {
        List<List<T>> list = newArrayList();
        for (List<T> element : container) {
            list.add(ImmutableList.copyOf(element));
        }
        return ImmutableList.copyOf(list);
    }

    private static <T> List<Weighed<T>> weighElements(Iterable<T> list, Function<T, Double> weigher) {
        List<Weighed<T>> weighted = newArrayList();
        for (T element : list) {
            double weight = weigher.apply(element);
            Weighed<T> weighed = newWeighed(element, weight);
            weighted.add(weighed);
        }
        return weighted;
    }

    private static <T> Weighed<T> newWeighed(T element, double weight) {
        return new Weighed<T>(element, weight);
    }

    private static class Weighed<T> implements Comparable<Weighed<T>> {

        public Weighed(T element, double weight) {
            this.element = checkNotNull(element);
            this.weight = weight;
        }

        private final T element;
        private final double weight;

        @Override
        public int compareTo(Weighed<T> other) {
            return Doubles.compare(this.weight, other.getWeight());
        }

        public T getElement() {
            return element;
        }

        public double getWeight() {
            return weight;
        }

    }

    private enum WeightIndexComparator implements Comparator<WeightIndex> {
        INSTANCE;

        @Override
        public int compare(WeightIndex one, WeightIndex two) {
            return Double.compare(one.getWeight(), two.getWeight());
        }

    }

    private static class WeightIndex {

        public WeightIndex(int index) {
            this.index = checkElementIndex(index, Integer.MAX_VALUE);
        }

        private final int index;
        private double weight;

        public int getIndex() {
            return index;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

    }

    private static class Distribution<T> extends AbstractList<List<T>> {
        final List<T> list;
        final int partitions;

        Distribution(List<T> list, int partitions) {
            this.list = list;
            this.partitions = partitions;
        }

        @Override
        public List<T> get(int index) {
            checkElementIndex(index, size());
            int listSize = list.size();
            int normalPartitions = listSize % partitions;
            int partialPartitionSize = listSize / partitions;
            int normalPartitionSize = partialPartitionSize + 1;

            // Parts [0, normalPartitions) have size normalPartitionSize, the rest have size partialPartitionSize.
            if (index < normalPartitions) {
                int chunkStart = normalPartitionSize * index;
                return list.subList(chunkStart, chunkStart + normalPartitionSize);
            }

            int normalEnd = normalPartitions * normalPartitionSize;
            int chunkStart = normalEnd + (index - normalPartitions) * partialPartitionSize;
            return list.subList(chunkStart, chunkStart + partialPartitionSize);
        }

        @Override
        public int size() {
            return partitions;
        }
    }

    private static class RandomAccessDistribution<T> extends Distribution<T> implements RandomAccess {
        public RandomAccessDistribution(List<T> list, int parts) {
            super(list, parts);
        }
    }

}
