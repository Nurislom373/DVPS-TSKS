package org.khasanof.condition;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Nurislom
 * @see org.khasanof.condition
 * @since 9/16/2023 10:21 PM
 */
final class NullTernary {

    interface IElse<T, R> {
        R elseValue(final R value);

        R elseMap(final Function<T, R> function);

        <X extends Throwable> R elseThrow(final Supplier<? extends X> exceptionSupplier) throws X;
    }

    interface IExceptionHandler<T> {
        <U> U elseValue(final U returnValue);

        <U> U elseMap(final Function<T, U> function);

        <X extends Throwable> void elseThrow(final Supplier<? extends X> exceptionSupplier) throws X;
    }

    interface IThen<T> {
        <R> IElse<T, R> thenValue(final R returnValue);

        <R> IElse<T, R> thenGet(final Supplier<R> supplier);

        <X extends Throwable> IExceptionHandler<T> thenThrow(final Supplier<? extends X> exceptionSupplier) throws X;
    }

    public static final class NullThen<T> implements IThen<T> {
        public <R> IElse<T, R> thenValue(final R returnValue) {
            return new NullElse<>(returnValue);
        }

        public <R> IElse<T, R> thenGet(final Supplier<R> supplier) {
            final R returnValue = supplier.get();
            return new NullElse<>(returnValue);
        }

        public <X extends Throwable> IExceptionHandler<T> thenThrow(final Supplier<? extends X> exceptionSupplier) throws X {
            throw exceptionSupplier.get();
        }
    }

    public static final class NonNullThen<T> implements IThen<T> {
        private final T object;

        public NonNullThen(final T object) {
            this.object = object;
        }

        public <R> IElse<T, R> thenValue(final R returnValue) {
            return new NonNullElse<>(this.object);
        }

        public <R> IElse<T, R> thenGet(final Supplier<R> supplier) {
            return new NonNullElse<>(this.object);
        }

        public <X extends Throwable> IExceptionHandler<T> thenThrow(final Supplier<? extends X> exceptionSupplier) throws X {
            return new NonNullExceptionHandler<>(this.object);
        }
    }

    private static final class NonNullExceptionHandler<T> implements IExceptionHandler<T> {
        private final T object;

        NonNullExceptionHandler(final T object) {
            this.object = object;
        }

        @Override
        public <U> U elseValue(final U returnValue) {
            return returnValue;
        }

        @Override
        public <U> U elseMap(final Function<T, U> function) {
            return function.apply(this.object);
        }

        public <X extends Throwable> void elseThrow(final Supplier<? extends X> exceptionSupplier) throws X {
            throw exceptionSupplier.get();
        }
    }

    private static final class NullElse<T, R> implements IElse<T, R> {
        private final R returnValue;

        NullElse(final R returnValue) {
            this.returnValue = returnValue;
        }

        @Override
        public R elseValue(final R value) {
            return this.returnValue;
        }

        @Override
        public R elseMap(final Function<T, R> function) {
            return this.returnValue;
        }

        public <X extends Throwable> R elseThrow(final Supplier<? extends X> exceptionSupplier) throws X {
            return this.returnValue;
        }
    }

    private static final class NonNullElse<T, R> implements IElse<T, R> {
        private final T object;

        NonNullElse(final T object) {
            this.object = object;
        }

        @Override
        public R elseValue(final R returnValue) {
            return returnValue;
        }

        @Override
        public R elseMap(final Function<T, R> function) {
            return function.apply(this.object);
        }

        public <X extends Throwable> R elseThrow(final Supplier<? extends X> exceptionSupplier) throws X {
            throw exceptionSupplier.get();
        }
    }

}
