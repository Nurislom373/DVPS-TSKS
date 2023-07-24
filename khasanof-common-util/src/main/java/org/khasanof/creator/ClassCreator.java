package org.khasanof.creator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * @author Nurislom
 * @see org.khasanof.creator
 * @since 23.07.2023 16:24
 */
public class ClassCreator {


    public static <T> T argsToClass(Class<T> clazz, Object... args) {
        List<Object> list = Arrays.stream(args)
                .filter(Objects::nonNull)
                .toList();

        Optional<Constructor<?>> optionalConstructor = findByAllArgsMatchConstructor(list,
                clazz.getDeclaredConstructors());

        if (optionalConstructor.isPresent()) {

            Constructor<?> constructor = optionalConstructor.get();
            constructor.setAccessible(true);

            try {
                return (T) constructor.newInstance(list.toArray());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }

        } else {

            Optional<Constructor<?>> optional = findAllByConParamAllMatch(list, clazz.getDeclaredConstructors());

            if (optional.isPresent()) {

                Constructor<?> constructor = optional.get();
                constructor.setAccessible(true);

                List<Object> objects = cleanerV2(constructor.getParameterTypes(), list);

                try {
                    return (T) constructor.newInstance(objects.toArray());
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }

            }

        }

        return null;
    }

    public static Object[] cleaner(Object[] objects, Constructor<?> constructor) {
        Object[] newObjects = new Object[objects.length];
        int count = 0;
        for (Object object : objects) {
            for (Class<?> parameterType : constructor.getParameterTypes()) {
                if (object.getClass().equals(parameterType) || (object.getClass().equals(Throwable.class) &&
                        parameterType.isAssignableFrom(object.getClass()))) {
                    newObjects[count] = object;
                    count++;
                }
            }
        }
        return newObjects;
    }

    public static Object[] sorterV2(Object[] objects, Class<?>[] parameterTypes) {
        List<Object> list = new ArrayList<>();
        Arrays.stream(parameterTypes)
                .forEach(parameterType -> {
                    for (Object arg : objects) {
                        if (parameterType.equals(arg.getClass()) ||
                                parameterType.isAssignableFrom(arg.getClass())) {
                            list.add(arg);
                        }
                    }
                });
        return list.toArray();
    }

    private static List<Object> cleanerV2(Class<?>[] conTypes, List<Object> args) {
        List<Object> list = new ArrayList<>();
        for (Class<?> conType : conTypes) {
            for (Object arg : args) {
                if (conMatchPredicate.test(conType, arg)) {
                    list.add(arg);
                }
            }
        }
        return list;
    }

    private static Optional<Constructor<?>> findByAllArgsMatchConstructor(List<Object> args, Constructor<?>[] constructors) {
        return Arrays.stream(constructors)
                .filter(constructor -> constructor.getParameterCount() == args.size())
                .filter(constructor -> allMatchV2(constructor.getParameterTypes(), args))
                .findFirst();
    }

    private static Optional<Constructor<?>> findAllByConParamAllMatch(List<Object> args, Constructor<?>[] constructors) {
        return Arrays.stream(constructors)
                .filter(constructor -> allMatch(constructor.getParameterTypes(), args))
                .findFirst();
    }

    private static boolean allMatchV2(Class<?>[] conTypes, List<Object> args) {
        return args.stream().allMatch(all -> Arrays.stream(conTypes)
                .anyMatch(any -> all.getClass().equals(any) || all.getClass().isAssignableFrom(any)));
    }

    private static boolean allMatch(Class<?>[] conTypes, List<Object> args) {
        return Arrays.stream(conTypes)
                .allMatch(all -> args.stream().anyMatch(any -> all.equals(any.getClass()) ||
                        all.isAssignableFrom(any.getClass())));
    }

    private static final BiPredicate<Class<?>, Object> conMatchPredicate =
            ((conType, obj) -> conType.equals(obj.getClass()) || conType.isAssignableFrom(obj.getClass()));

}
