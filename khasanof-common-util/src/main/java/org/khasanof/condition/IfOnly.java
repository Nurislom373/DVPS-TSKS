package org.khasanof.condition;

import java.util.function.Supplier;

/**
 * @author Nurislom
 * @see org.khasanof.condition
 * @since 9/16/2023 10:19 PM
 */
final class IfOnly {

    public interface IExecute {

        void thenCall(final IfExpression.Callable callable);

        <X extends Throwable> void thenThrow(final Supplier<? extends X> exceptionSupplier) throws X;

    }

    public static class Execution implements IExecute {

        public void thenCall(final IfExpression.Callable callable) {
            callable.call();
        }

        public <X extends Throwable> void thenThrow(final Supplier<? extends X> exceptionSupplier) throws X {
            throw exceptionSupplier.get();
        }

    }

    public static class SkipExecution implements IExecute {

        public void thenCall(final IfExpression.Callable callable) {
            // do not call
        }

        public <X extends Throwable> void thenThrow(final Supplier<? extends X> exceptionSupplier) throws X {
            // do not throw exception
        }

    }

}
