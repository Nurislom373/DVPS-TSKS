package org.khasanof.condition;

import java.util.function.Supplier;

/**
 * @author Nurislom
 * @see org.khasanof.condition
 * @since 9/16/2023 10:18 PM
 */
final class IfExpression {

    public interface Callable {

        void call();

    }

    public interface IElseIf<T> {

        IElse<T> thenGet(final Supplier<? extends T> supplier);

        IElse<T> thenValue(final T value);

        <X extends Throwable> IElse<T> thenThrow(final Supplier<? extends X> exceptionSupplier) throws X;

    }

    public interface IExceptionThrow {

        <T> T elseGet(final Supplier<? extends T> supplier);

        <T> T elseValue(final T value);

        IExceptionElseIf elseIf(final boolean expression);

        <X extends Throwable> void elseThrow(final Supplier<? extends X> exceptionSupplier) throws X;

        void elseDoNothing();

    }

    public interface IExceptionElseIf {

        <T> IElse<T> thenGet(final Supplier<? extends T> supplier);

        <T> IElse<T> thenValue(final T value);

        <T> IElseCall thenCall(final Callable callable);

        <X extends Throwable> IExceptionThrow thenThrow(final Supplier<? extends X> exceptionSupplier) throws X;

    }

    public interface IElse<T> {

        T elseGet(final Supplier<? extends T> supplier);

        T elseValue(final T value);

        IElseIf<T> elseIf(final boolean expression);

        <X extends Throwable> T elseThrow(final Supplier<? extends X> exceptionSupplier) throws X;

    }

    public interface IElseIfCall {

        IElseCall thenCall(final Callable callable);

        <X extends Throwable> IElseCall thenThrow(final Supplier<? extends X> exceptionSupplier) throws X;

    }

    public interface IElseCall {

        void elseCall(final Callable callable);

        <X extends Throwable> void elseThrow(final Supplier<? extends X> exceptionSupplier) throws X;

        IElseIfCall elseIf(final boolean expression);

        void elseDoNothing();

    }

    public interface IExpressionThen {
        <T> IElse<T> thenGet(final Supplier<? extends T> supplier);

        <T> IElse<T> thenValue(final T value);

        IElseCall thenCall(final Callable callable);

        <X extends Throwable> IException thenThrow(final Supplier<? extends X> exceptionSupplier) throws X;
    }

    public interface IException {
        void elseCall(final Callable callable);

        <T> T elseValue(final T value);

        <X extends Throwable> void elseThrow(final Supplier<? extends X> exceptionSupplier) throws X;

        <T> T elseGet(final Supplier<? extends T> supplier);

        IExceptionElseIf elseIf(final boolean expression);

        void elseDoNothing();
    }

    static class TrueExpressionThen implements IExpressionThen {
        @Override
        public <T> IElse<T> thenGet(final Supplier<? extends T> supplier) {
            return new TrueExpression<>(supplier.get());
        }

        @Override
        public <T> IElse<T> thenValue(final T value) {
            return new TrueExpression<>(value);
        }

        @Override
        public IElseCall thenCall(final Callable callable) {
            callable.call();
            return new DoNothingObject();
        }

        public <X extends Throwable> IException thenThrow(final Supplier<? extends X> exceptionSupplier) throws X {
            throw exceptionSupplier.get();
        }
    }

    static class FalseExpressionThen implements IExpressionThen {
        @Override
        public <T> IElse<T> thenGet(final Supplier<? extends T> supplier) {
            return new FalseExpression<>();
        }

        @Override
        public <T> IElse<T> thenValue(final T value) {
            return new FalseExpression<>();
        }

        @Override
        public IElseCall thenCall(final Callable callable) {
            return new FalseExpressionCall();
        }

        public <X extends Throwable> IException thenThrow(final Supplier<? extends X> exceptionSupplier) throws X {
            return new ElseException();
        }
    }

    private static class ElseException implements IException {
        @Override
        public void elseCall(final Callable callable) {
            callable.call();
        }

        @Override
        public <T> T elseValue(final T value) {
            return value;
        }

        @Override
        public <X extends Throwable> void elseThrow(final Supplier<? extends X> exceptionSupplier) throws X {
            throw exceptionSupplier.get();
        }

        @Override
        public <T> T elseGet(final Supplier<? extends T> supplier) {
            return supplier.get();
        }

        @Override
        public IExceptionElseIf elseIf(final boolean expression) {
            if (expression) {
                return new IExceptionTrueElseIfImpl();
            }

            return new IExceptionFalseElseIfImpl();
        }

        @Override
        public void elseDoNothing() {
            // do nothing
        }

        private static class IExceptionFalseElseIfImpl implements IExceptionElseIf {
            @Override
            public <T> IElse<T> thenGet(final Supplier<? extends T> supplier) {
                return new FalseExpression<>();
            }

            @Override
            public <T> IElse<T> thenValue(final T value) {
                return new FalseExpression<>();
            }

            @Override
            public <T> IElseCall thenCall(final Callable callable) {
                return new FalseExpressionCall();
            }

            @Override
            public <X extends Throwable> IExceptionThrow thenThrow(final Supplier<? extends X> exceptionSupplier) throws X {
                return new ExceptionThrow();
            }

            private static class ExceptionThrow implements IExceptionThrow {
                @Override
                public <T> T elseGet(final Supplier<? extends T> supplier) {
                    return supplier.get();
                }

                @Override
                public <T> T elseValue(final T value) {
                    return value;
                }

                @Override
                public IExceptionElseIf elseIf(final boolean expression) {
                    if (expression) {
                        return new IExceptionTrueElseIfImpl();
                    }

                    return new IExceptionFalseElseIfImpl();
                }

                @Override
                public <Y extends Throwable> void elseThrow(final Supplier<? extends Y> exceptionSupplier) throws Y {
                    throw exceptionSupplier.get();
                }

                @Override
                public void elseDoNothing() {
                    // do nothing
                }
            }
        }

        private static class IExceptionTrueElseIfImpl implements IExceptionElseIf {
            @Override
            public <T> IElse<T> thenGet(final Supplier<? extends T> supplier) {
                return new TrueExpression<>(supplier.get());
            }

            @Override
            public <T> IElse<T> thenValue(final T value) {
                return new TrueExpression<>(value);
            }

            @Override
            public <T> IElseCall thenCall(final Callable callable) {
                callable.call();
                return new DoNothingObject();
            }

            @Override
            public <X extends Throwable> IExceptionThrow thenThrow(final Supplier<? extends X> exceptionSupplier) throws X {
                throw exceptionSupplier.get();
            }
        }
    }

    private static class TrueExpression<T> implements IElse<T> {
        final private T value;

        TrueExpression(final T value) {
            this.value = value;
        }

        @Override
        public T elseGet(final Supplier<? extends T> supplier) {
            return this.value;
        }

        @Override
        public T elseValue(final T elseValue) {
            return this.value;
        }

        @Override
        public IElseIf<T> elseIf(final boolean expression) {
            return new ElseIf<>(this.value);
        }

        @Override
        public <X extends Throwable> T elseThrow(final Supplier<? extends X> exceptionSupplier) throws X {
            return this.value;
        }

        private static class ElseIf<T> implements IElseIf<T> {
            final private T value;

            public ElseIf(final T value) {
                this.value = value;
            }

            @Override
            public IElse<T> thenGet(final Supplier<? extends T> supplier) {
                return new TrueExpression<>(this.value);
            }

            @Override
            public IElse<T> thenValue(final T value) {
                return new TrueExpression<>(this.value);
            }

            @Override
            public <X extends Throwable> IElse<T> thenThrow(final Supplier<? extends X> exceptionSupplier) throws X {
                return new TrueExpression<>(this.value);
            }
        }
    }

    private static class FalseExpression<T> implements IElse<T> {
        @Override
        public T elseGet(final Supplier<? extends T> supplier) {
            return supplier.get();
        }

        @Override
        public T elseValue(final T value) {
            return value;
        }

        @Override
        public IElseIf<T> elseIf(final boolean expression) {
            if (expression) {
                return new TrueElseIf<>();
            }

            return new FalseElseIf<>();
        }

        @Override
        public <X extends Throwable> T elseThrow(final Supplier<? extends X> exceptionSupplier) throws X {
            throw exceptionSupplier.get();
        }

        private static class TrueElseIf<T> implements IElseIf<T> {
            @Override
            public IElse<T> thenGet(final Supplier<? extends T> supplier) {
                return new TrueExpression<>(supplier.get());
            }

            @Override
            public IElse<T> thenValue(final T value) {
                return new TrueExpression<>(value);
            }

            @Override
            public <X extends Throwable> IElse<T> thenThrow(final Supplier<? extends X> exceptionSupplier) throws X {
                throw exceptionSupplier.get();
            }
        }

        private static class FalseElseIf<T> implements IElseIf<T> {
            @Override
            public IElse<T> thenGet(final Supplier<? extends T> supplier) {
                return new FalseExpression<>();
            }

            @Override
            public IElse<T> thenValue(final T value) {
                return new FalseExpression<>();
            }

            @Override
            public <X extends Throwable> IElse<T> thenThrow(final Supplier<? extends X> exceptionSupplier) throws X {
                return new FalseExpression<>();
            }
        }
    }

    private static class DoNothingObject implements IElseCall {
        @Override
        public void elseCall(final Callable callable) {
            // does not anything
        }

        @Override
        public <X extends Throwable> void elseThrow(final Supplier<? extends X> exceptionSupplier) throws X {
            // does not anything
        }

        @Override
        public IElseIfCall elseIf(final boolean expression) {
            return new IElseIfCall() {

                @Override
                public IElseCall thenCall(final Callable callable) {
                    return new DoNothingObject();
                }

                @Override
                public <X extends Throwable> IElseCall thenThrow(final Supplier<? extends X> exceptionSupplier) throws X {
                    return new DoNothingObject();
                }
            };
        }

        @Override
        public void elseDoNothing() {
            //
        }
    }

    private static class FalseExpressionCall implements IElseCall {
        @Override
        public void elseCall(final Callable callable) {
            callable.call();
        }

        @Override
        public <X extends Throwable> void elseThrow(final Supplier<? extends X> exceptionSupplier) throws X {
            throw exceptionSupplier.get();
        }

        @Override
        public IElseIfCall elseIf(final boolean expression) {
            if (expression) {
                return new TrueElseIfCall();
            }

            return new FalseElseIfCall();
        }

        @Override
        public void elseDoNothing() {
            // do nothing
        }
    }

    private static class TrueElseIfCall implements IElseIfCall {
        @Override
        public IElseCall thenCall(final Callable callable) {
            callable.call();
            return new DoNothingObject();
        }

        @Override
        public <X extends Throwable> IElseCall thenThrow(final Supplier<? extends X> exceptionSupplier) throws X {
            throw exceptionSupplier.get();
        }
    }

    private static class FalseElseIfCall implements IElseIfCall {
        @Override
        public IElseCall thenCall(final Callable callable) {
            return new FalseExpressionCall();
        }

        @Override
        public <X extends Throwable> IElseCall thenThrow(final Supplier<? extends X> exceptionSupplier) throws X {
            return new FalseExpressionCall();
        }
    }

}
