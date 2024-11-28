package org.bool.junit.mockito.inline.impl;

import org.bool.junit.mockito.inline.StaticMock;

import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.platform.commons.support.AnnotationSupport;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.stream.Stream;

public class StaticMockHandler implements AnnotationHandler {

    @Override
    public Class<? extends Annotation> annotationType() {
        return StaticMock.class;
    }

    @Override
    public Class<?> parameterType() {
        return MockedStatic.class;
    }

    @Override
    public void init(Store store, Optional<Class<?>> testClass, Optional<Method> testMethod, Optional<Object> testInstance) {
        Stream.of(testClass, testMethod)
            .map(element -> AnnotationSupport.findAnnotation(element, StaticMock.class))
            .flatMap(annotation -> annotation.map(StaticMock::value).map(Stream::of).orElse(Stream.empty()))
            .forEach(mockType -> store.put(mockType, new ResourceHolder(Mockito.mockStatic(mockType))));
    }
}
