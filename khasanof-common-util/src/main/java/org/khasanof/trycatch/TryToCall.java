package org.khasanof.trycatch;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.khasanof.trycatch.Constant.DO_NOTHING;
import static org.khasanof.trycatch.SneakyThrower.sneakyThrow;
import static org.khasanof.trycatch.Utils.closeResources;
import static org.khasanof.trycatch.Utils.executeCallable;

/**
 * @author Nurislom
 * @see org.khasanof.trycatch
 * @since 9/16/2023 11:32 PM
 */
class TryToCall {

    final private Callable callable;
    final private AutoCloseable[] resources;

    TryToCall(final Callable callable) {
        this.callable = callable;
        this.resources = new AutoCloseable[0];
    }

    TryToCall(final Callable callable, final AutoCloseable[] resources) {
        this.callable = callable;
        this.resources = resources;
    }

    @SuppressWarnings("unchecked")
    public ThenHandler ifRaises(final Class<? extends Throwable>... exceptionsToBeHandle) {
        return new ThenHandler(
                this.callable,
                asList(exceptionsToBeHandle),
                emptyList(),
                this.resources);
    }

    public void done() {
        try {
            callable.call();
        } catch (Throwable raisedException) {
            closeResources(resources);
            throw sneakyThrow(raisedException);
        }
    }

    public void finallyDone(final Callable finallyCallable) {
        try {
            this.callable.call();
        } catch (Throwable raisedException) {
            closeResources(this.resources);
            throw sneakyThrow(raisedException);
        } finally {
            closeResources(this.resources);
            executeCallable(finallyCallable);
        }
    }

    public static class ThenHandler {
        final private Callable callable;
        final private List<Class<? extends Throwable>> exceptionsToBeHandled;
        final private List<IExceptionHandler> exceptionsHandlers;
        final private AutoCloseable[] resources;

        public ThenHandler(final Callable callable,
                           final List<Class<? extends Throwable>> exceptionsToBeHandled,
                           final List<IExceptionHandler> exceptionsHandlers,
                           final AutoCloseable[] resources) {
            this.callable = callable;
            this.exceptionsToBeHandled = exceptionsToBeHandled;
            this.exceptionsHandlers = exceptionsHandlers;
            this.resources = resources;
        }

        public Executor thenCall(final Consumer<Throwable> onExceptionCallable) {
            final ArrayList<IExceptionHandler> alreadyRegisteredExceptionHandlers = new ArrayList<>(this.exceptionsHandlers);
            final List<IExceptionHandler> exceptionHandlers = this.exceptionsToBeHandled.stream()
                    .map(exception -> new ExceptionConsumer(exception, onExceptionCallable))
                    .collect(Collectors.toList());

            alreadyRegisteredExceptionHandlers.addAll(exceptionHandlers);
            return new Executor(
                    this.callable,
                    Collections.unmodifiableList(alreadyRegisteredExceptionHandlers),
                    DO_NOTHING,
                    this.resources);
        }

        public <E extends Throwable> Executor thenThrow(final Function<Throwable, ? extends E> onExceptionFunction) throws E {
            final ArrayList<IExceptionHandler> alreadyRegisteredExceptionHandlers = new ArrayList<>(this.exceptionsHandlers);
            final List<IExceptionHandler> exceptionHandlers = this.exceptionsToBeHandled.stream()
                    .map(exception -> new ExceptionThrower(exception, onExceptionFunction))
                    .collect(Collectors.toList());

            alreadyRegisteredExceptionHandlers.addAll(exceptionHandlers);
            return new Executor(
                    this.callable,
                    Collections.unmodifiableList(alreadyRegisteredExceptionHandlers),
                    DO_NOTHING,
                    this.resources);
        }
    }

    public interface IExceptionHandler {
        void handleException(final Throwable exception);

        Class<? extends Throwable> getExceptionToBeHandled();
    }

    static class ExceptionConsumer implements IExceptionHandler {
        final private Class<? extends Throwable> exceptionToBeHandled;
        final private Consumer<Throwable> onExceptionConsumer;

        ExceptionConsumer(final Class<? extends Throwable> exceptionToBeHandled,
                          final Consumer<Throwable> onExceptionConsumer) {
            this.exceptionToBeHandled = exceptionToBeHandled;
            this.onExceptionConsumer = onExceptionConsumer;
        }

        public Class<? extends Throwable> getExceptionToBeHandled() {
            return this.exceptionToBeHandled;
        }

        @Override
        public void handleException(Throwable exception) {
            this.onExceptionConsumer.accept(exception);
        }
    }

    static class ExceptionThrower implements IExceptionHandler {
        final private Class<? extends Throwable> exceptionToBeHandled;
        final Function<Throwable, ? extends Throwable> onExceptionNewExceptionProviderFunction;

        ExceptionThrower(final Class<? extends Throwable> exceptionToBeHandled,
                         final Function<Throwable, ? extends Throwable> onExceptionNewExceptionProviderFunction) {
            this.exceptionToBeHandled = exceptionToBeHandled;
            this.onExceptionNewExceptionProviderFunction = onExceptionNewExceptionProviderFunction;
        }

        public Class<? extends Throwable> getExceptionToBeHandled() {
            return this.exceptionToBeHandled;
        }

        @Override
        public void handleException(final Throwable exceptionToHandle) {
            throw sneakyThrow(this.onExceptionNewExceptionProviderFunction.apply(exceptionToHandle));
        }
    }

    public interface IElseCall {
        IExecutor elseCall(final Callable onSuccessCallable);
    }

    public interface IExecutor {
        void done();

        void finallyDone(final Callable finallyCallable);
    }

    public static class Executor implements IElseCall, IExecutor {
        final private Callable callable;
        final private List<IExceptionHandler> registeredExceptionHandlers;
        final private Callable onSuccessCallable;
        final private AutoCloseable[] resourcesToBeClosed;

        public Executor(final Callable callable,
                        final List<IExceptionHandler> registeredExceptionHandlers,
                        final Callable doNothing,
                        final AutoCloseable[] resourcesToBeClosed) {
            this.callable = callable;
            this.registeredExceptionHandlers = registeredExceptionHandlers;
            this.onSuccessCallable = doNothing;
            this.resourcesToBeClosed = resourcesToBeClosed;
        }

        @Override
        public void done() {
            boolean success = false;
            try {
                this.callable.call();
                success = true;
            } catch (Throwable raisedException) {
                handleRegisteredExceptions(raisedException);
            }

            if (success) {
                closeResources(this.resourcesToBeClosed);
                executeCallable(this.onSuccessCallable);
            }
        }

        private void handleRegisteredExceptions(final Throwable raisedException) {
            closeResources(this.resourcesToBeClosed);
            final Optional<IExceptionHandler> matchedException = this.registeredExceptionHandlers.stream()
                    .filter((exceptionHandler -> exceptionHandler.getExceptionToBeHandled().isInstance(raisedException)))
                    .findFirst();

            if (matchedException.isPresent()) {
                matchedException.get().handleException(raisedException);
            } else {
                throw sneakyThrow(raisedException);
            }
        }

        @Override
        public void finallyDone(final Callable finallyCallable) {
            boolean success = false;
            try {
                this.callable.call();
                success = true;
            } catch (Throwable raisedException) {
                handleRegisteredExceptions(raisedException);
            } finally {
                if (success) {
                    closeResources(this.resourcesToBeClosed);
                }
                executeCallable(finallyCallable);
            }

            if (success) {
                executeCallable(this.onSuccessCallable);
            }
        }

        @SuppressWarnings("unchecked")
        public ThenHandler elseIfRaises(final Class<? extends Throwable>... exceptionsToBeHandled) {
            return new ThenHandler(
                    this.callable,
                    asList(exceptionsToBeHandled),
                    this.registeredExceptionHandlers,
                    this.resourcesToBeClosed);
        }

        @Override
        public IExecutor elseCall(final Callable onSuccessCallable) {
            return new Executor(
                    this.callable,
                    this.registeredExceptionHandlers,
                    onSuccessCallable,
                    this.resourcesToBeClosed);
        }
    }

}
