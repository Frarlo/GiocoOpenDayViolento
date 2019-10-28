package gov.ismonnet.util;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.concurrent.Callable;

/**
 * Classes that abuses Java lambdas to throw checked exceptions
 * without having to catch them and without rethrowing them.
 *
 * @author Ferlo
 *
 * See <a href="http://bytex.solutions/2017/07/java-lambdas/"> where I stole it </a>
 */
public final class SneakyThrow {

    private SneakyThrow() {} // Limit scope

    /**
     * Class generated using {@link LambdaMetafactory} which
     * is able to invoke {@link Callable#call()} without declaring
     * its checked exception.
     *
     * {@link Callable} is used instead of other interfaces because
     * its signature has already an exception
     */
    private static final SilentInvoker SILENT_INVOKER;

    static {
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            SILENT_INVOKER = (SilentInvoker) LambdaMetafactory.metafactory(lookup,
                    SilentInvoker.class.getMethods()[0].getName(),
                    MethodType.methodType(SilentInvoker.class), SilentInvoker.INVOKE_SIGNATURE,
                    lookup.findVirtual(Callable.class, "call", MethodType.methodType(Object.class)),
                    SilentInvoker.INVOKE_SIGNATURE).getTarget().invokeExact();
        } catch (Throwable e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Interface of {@link #SILENT_INVOKER} used to generate it using
     * the {@link LambdaMetafactory}
     */
    @FunctionalInterface
    private interface SilentInvoker {
        /**
         * Signature of the invoke method
         */
        MethodType INVOKE_SIGNATURE = MethodType.methodType(Object.class, Callable.class);

        <T> T invoke(Callable<T> callable);
    }

    /**
     * Invoke {@link Callable#call()} without having to
     * catch its checked exceptions.
     *
     * The exceptions are not suppressed and it does
     * not rethrow it.
     *
     * @param callable object to call
     * @param <T> return type
     * @return the callable result
     */
    public static <T> T callUnchecked(Callable<T> callable) {
        return SILENT_INVOKER.invoke(callable);
    }

    /**
     * Invoke {@link CheckedExceptionRunnable#run()} without having to
     * catch its checked exceptions.
     *
     * The exceptions are not suppressed and it does
     * not rethrow it.
     *
     * @param runnable object to call
     */
    public static void runUnchecked(CheckedExceptionRunnable runnable) {
        Callable<Boolean> callable = () -> {
            runnable.run();
            return true;
        };

        SILENT_INVOKER.invoke(callable);
    }

    /**
     * Throw the given checked exception as an unchecked one.
     *
     * The exceptions are not suppressed and it does
     * not rethrow it.
     *
     * It returns a RuntimeException so that the method can be used as
     * {@code throw throwUnchecked(new Exception())}
     * without having to return from the method.
     *
     * @param exception checked exception to be thrown
     * @return nothing
     */
    public static RuntimeException throwUnchecked(Exception exception) {
        SILENT_INVOKER.invoke(() -> {
            throw exception;
        });
        throw new AssertionError("Should have thrown an exception");
    }

    /**
     * Returns the {@link Callable#call()} result or, if an
     * exception is thrown, the default value
     *
     * @param callable object to call
     * @param defaultValue default value to return in case an exception is thrown
     * @param <T> return type
     * @return the {@link Callable#call()} result or, if an
     *         exception is thrown, the default value
     */
    public static <T> T ignore(Callable<T> callable, T defaultValue) {
        try {
            return callable.call();
        } catch (Throwable ignored) {
            return defaultValue;
        }
    }

    /**
     * Runnable interface that can throw any checked exception
     */
    @FunctionalInterface
    public interface CheckedExceptionRunnable {
        void run() throws Exception;
    }
}
