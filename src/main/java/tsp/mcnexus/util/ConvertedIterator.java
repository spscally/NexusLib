package tsp.mcnexus.util;

import java.util.Iterator;
import java.util.function.Function;

public class ConvertedIterator<T,R> implements Iterator<R> {

    private final Iterator<T> iterator;
    private final Function<T, R> converter;

    public ConvertedIterator(Iterator<T> iterator, Function<T,R> converter) {
        this.iterator = iterator;
        this.converter = converter;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public R next() {
        return converter.apply(iterator.next());
    }

    public Function<T, R> getConverter() {
        return converter;
    }

}
