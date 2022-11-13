package tsp.mcnexus.util;

import java.util.Iterator;
import java.util.function.Function;

public class ConvertedIterable<T,R> implements Iterable<R> {

    private final Iterable<T> iterable;
    private final Function<T, R> converter;

    public ConvertedIterable(Iterable<T> iterable, Function<T,R> converter) {
        this.iterable = iterable;
        this.converter = converter;
    }

    @Override
    public Iterator<R> iterator() {
        return new ConvertedIterator<>(iterable.iterator(), converter);
    }

    public Function<T, R> getConverter() {
        return converter;
    }

}