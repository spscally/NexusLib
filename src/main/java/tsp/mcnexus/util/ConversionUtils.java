package tsp.mcnexus.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

public class ConversionUtils {

    public static <T,R> Iterable<R> convert(Iterable<T> iterable, Function<T,R> converter) {
        return new ConvertedIterable<>(iterable, converter);
    }

    public static <T,R> Iterator<R> convert(Iterator<T> iterator, Function<T,R> converter) {
        return new ConvertedIterator<>(iterator, converter);
    }

    // Collections

    public static <T> String toString(Collection<T> collection, String delimiter, Function<T, String> converter) {
        StringBuilder builder = new StringBuilder();
        for (T entry : collection) {
            builder.append(converter.apply(entry)).append(delimiter);
        }

        return builder.toString();
    }

    public static <T> String toString(Collection<T> collection, String delimiter) {
        return toString(collection, delimiter, Object::toString);
    }

    public static <T> String toString(Collection<T> collection, Function<T, String> converter) {
        return toString(collection, ",", converter);
    }

    public static <T> String toString(Collection<T> collection) {
        return toString(collection, ",");
    }

    // Arrays

    public static <T> String toString(T[] array, String delimiter, Function<T, String> converter) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            builder.append(converter.apply(array[i]));
            if (i < array.length - 1) {
                builder.append(delimiter);
            }
        }

        return builder.toString();
    }

    public static <T> String toString(T[] array, String delimiter) {
        return toString(array, delimiter, Object::toString);
    }

    public static <T> String toString(T[] array, Function<T, String> converter) {
        return toString(array, ",", converter);
    }

    public static <T> String toString(T[] array) {
        return toString(array, ",");
    }

}