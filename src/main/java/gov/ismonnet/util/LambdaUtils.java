package gov.ismonnet.util;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Interface that allows lambdas implementing it
 * to get a {@link Method} instance of the lambda itself.
 *
 * @author Ferlo
 *
 * See <a href="https://benjiweber.co.uk/blog/2015/08/17/lambda-parameter-names-with-reflection/"> where I stole the idea</a>
 */
public class LambdaUtils {

    private LambdaUtils() {} // Limit the scope

    /**
     * Returns the serialized form of the given lambda expression
     * @param lambda lambda expression
     * @return serialized form of the given lambda
     */
    public static SerializedLambda getSerializedLambda(Serializable lambda) {
        return AccessController.doPrivileged((PrivilegedAction<SerializedLambda>) () -> {
            for (Class<?> clazz = lambda.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
                try {
                    final Method replaceMethod = clazz.getDeclaredMethod("writeReplace");
                    replaceMethod.setAccessible(true);
                    return (SerializedLambda) replaceMethod.invoke(lambda);
                } catch (NoSuchMethodException e) {
                /* fall through the loop and try the next class */
                } catch (Throwable t) {
                    throw new RuntimeException("Error while extracting serialized lambda", t);
                }
            }
            throw new RuntimeException("writeReplace method not found");
        });
    }

    /**
     * Returns an instance of the class containing the
     * lambda implementation method
     *
     * @return class containing the implementation method
     */
    public static Class<?> getLambdaImplClass(Serializable lambda) {
        try {
            return Class.forName(getSerializedLambda(lambda).getImplClass().replace('/', '.'));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to instantiate lambda class");
        }
    }

    /**
     * Returns an instance of the class containing the
     * lambda implementation method
     *
     * @return class containing the implementation method
     */
    public static Method getLambdaMethod(Serializable lambda) {
        return AccessController.doPrivileged((PrivilegedAction<Method>) () -> {
            final String lambdaName = getSerializedLambda(lambda).getImplMethodName();
            for (Method m : getLambdaImplClass(lambda).getDeclaredMethods())
                if (m.getName().equals(lambdaName))
                    return m;
            throw new RuntimeException("Lambda Method not found");
        });
    }
}
