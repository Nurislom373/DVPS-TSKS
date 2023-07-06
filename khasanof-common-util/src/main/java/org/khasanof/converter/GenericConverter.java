package org.khasanof.converter;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Nurislom
 * @see org.khasanof.converter
 * @since 06.07.2023 11:40
 */
public class GenericConverter<T, U> {

    private final Function<T, U> fromConvert;
    private final Function<U, T> toConvert;

    public GenericConverter(Function<T, U> fromConvert, Function<U, T> toConvert) {
        this.fromConvert = fromConvert;
        this.toConvert = toConvert;
    }

    public final U fromConvert(final T type) {
        return fromConvert.apply(type);
    }

    public final T toConvert(final U type) {
        return toConvert.apply(type);
    }

    public final List<U> fromConvert(final Collection<T> types) {
        return types.stream().map(this::fromConvert).collect(Collectors.toList());
    }

    public final List<T> toConvert(final Collection<U> types) {
        return types.stream().map(this::toConvert).collect(Collectors.toList());
    }

}
