package org.khasanof.condition;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Nurislom
 * @see org.khasanof.condition
 * @since 9/16/2023 10:16 PM
 */
public abstract class Condition {

    public static <T> NullTernary.IThen<T> isNull(final T value) {
        if (Objects.isNull(value)) {
            return new NullTernary.NullThen<>();
        }

        return new NullTernary.NonNullThen<>(value);
    }

    public static IfExpression.IExpressionThen isTrue(final Boolean expression) {
        if (expression) {
            return new IfExpression.TrueExpressionThen();
        }
        return new IfExpression.FalseExpressionThen();
    }

    public static IfExpression.IExpressionThen isFalse(final Boolean expression) {
        if (!expression) {
            return new IfExpression.TrueExpressionThen();
        }

        return new IfExpression.FalseExpressionThen();
    }

    public static IfOnly.IExecute isTrueThen(final Boolean expression) {
        if (expression) {
            return new IfOnly.Execution();
        }

        return new IfOnly.SkipExecution();
    }

    public static IfOnly.IExecute isFalseThen(final Boolean expression) {
        if (expression) {
            return new IfOnly.SkipExecution();
        }

        return new IfOnly.Execution();
    }


    public static <T> T orElse(final boolean expression,
                               final Supplier<? extends T> ifSupplier,
                               final Supplier<? extends T> elseSupplier) {
        if (expression) {
            return ifSupplier.get();
        }

        return elseSupplier.get();
    }

    public static <T> T orElse(final Supplier<Boolean> booleanSupplier,
                               final Supplier<? extends T> ifSupplier,
                               final Supplier<? extends T> elseSupplier) {

        return orElse(booleanSupplier.get(), ifSupplier, elseSupplier);
    }

    public static <T, R> R nullOrElse(final T value,
                                      final Supplier<? extends R> ifSupplier,
                                      final Supplier<? extends R> elseSupplier) {

        return orElse(Objects.isNull(value), ifSupplier, elseSupplier);
    }

    public static <T, R> R nullOrElse(final T value,
                                      final Supplier<? extends R> ifSupplier,
                                      final Function<T, ? extends R> elseFunction) {

        return orElse(Objects.isNull(value), ifSupplier, () -> elseFunction.apply(value));
    }

    public static void orElse(final Supplier<Boolean> booleanSupplier,
                              final IfExpression.Callable ifCallable,
                              final IfExpression.Callable elseCallable) {

        orElse(booleanSupplier.get(), ifCallable, elseCallable);
    }

    public static void orElse(final boolean expression,
                              final IfExpression.Callable ifCallable,
                              final IfExpression.Callable elseCallable) {
        if (expression) {
            ifCallable.call();
        }

        elseCallable.call();
    }

    public static <T> T orElse(final boolean expression, final T thenValue, final T elseValue) {
        return orElse(expression, () -> thenValue, () -> elseValue);
    }

    public static <T> T orElse(final Supplier<Boolean> booleanSupplier, final T thenValue, final T elseValue) {
        return orElse(booleanSupplier.get(), () -> thenValue, () -> elseValue);
    }

    public static void isTrueThen(final boolean expression, final IfExpression.Callable ifCallable) {
        if (expression) {
            ifCallable.call();
        }
    }

    public static void isTrueThen(final Supplier<Boolean> supplier, final IfExpression.Callable ifCallable) {
        isTrueThen(supplier.get(), ifCallable);
    }

    public static <T> void isNullThen(final T value, final IfExpression.Callable ifCallable) {
        isTrueThen(Objects.isNull(value), ifCallable);
    }

}
