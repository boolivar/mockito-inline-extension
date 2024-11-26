package org.bool.junit.mockito.inline;

import org.junit.jupiter.api.extension.ExtensionContext.Store;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

public interface AnnotationHandler {

    Class<? extends Annotation> annotationType();

    Class<?> parameterType();

    void init(Store store, Optional<Class<?>> testClass, Optional<Method> testMethod, Optional<Object> testInstance);

    default Object resolveParameter(Store store, Parameter parameter) {
        Type typeArg = ((ParameterizedType) parameter.getParameterizedType()).getActualTypeArguments()[0];
        return Optional.ofNullable(store.get(typeArg, ResourceHolder.class)).map(ResourceHolder::getResource)
            .orElseThrow(() -> new IllegalArgumentException("No " + parameter.getType() + " for " + typeArg + ". Add " + typeArg + " to " + annotationType()));
    }
}
