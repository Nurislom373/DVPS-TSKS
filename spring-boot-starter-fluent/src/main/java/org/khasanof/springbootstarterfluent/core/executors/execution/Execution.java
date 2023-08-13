package org.khasanof.springbootstarterfluent.core.executors.execution;

import org.khasanof.springbootstarterfluent.core.event.methodInvoke.MethodV1Event;
import org.khasanof.springbootstarterfluent.core.model.AdditionalType;
import org.khasanof.springbootstarterfluent.main.annotation.extra.ExecutionException;

import java.lang.reflect.InvocationTargetException;

/**
 * The {@link Execution} interface serves as a specification for expressing various methods to be executed.
 * It has a single run method that expects a {@link MethodV1Event} class parameter.
 *
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.executors.execution
 * @since 8/13/2023 9:19 PM
 */
public interface Execution extends AdditionalType {

    /**
     * run method serves to run the method of the given class.
     *
     * @param methodV1Event must not be null!
     * @throws InvocationTargetException The exception that is thrown when the method is executed
     * @throws IllegalAccessException Exception thrown without access to method input
     */
    @ExecutionException
    void run(MethodV1Event methodV1Event) throws InvocationTargetException, IllegalAccessException;

}
