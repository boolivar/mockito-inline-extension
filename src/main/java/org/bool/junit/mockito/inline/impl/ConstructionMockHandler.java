package org.bool.junit.mockito.inline.impl;

import org.bool.junit.mockito.inline.ConstructionMock;

import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.support.ReflectionSupport;
import org.mockito.MockedConstruction;
import org.mockito.MockedConstruction.Context;
import org.mockito.Mockito;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.stream.Stream;

public class ConstructionMockHandler implements AnnotationHandler {

    @Override
    public Class<? extends Annotation> annotationType() {
        return ConstructionMock.class;
    }

    @Override
    public Class<?> parameterType() {
        return MockedConstruction.class;
    }

    @Override
    public void init(Store store, Optional<Class<?>> testClass, Optional<Method> testMethod, Optional<Object> testInstance) {
        Stream.of(testClass, testMethod)
            .map(element -> AnnotationSupport.findAnnotation(element, ConstructionMock.class))
            .flatMap(annotation -> annotation.map(ConstructionMock::value).map(Stream::of).orElse(Stream.empty()))
            .forEach(mockType -> store.put(mockType, new ResourceHolder(mockConstruction(mockType, testClass, testMethod, testInstance))));
    }

    private Object mockConstruction(Class<?> mockType, Optional<Class<?>> maybeTestClass, Optional<Method> maybeTestMethod, Optional<Object> testInstance) {
        return maybeTestClass.flatMap(testClass -> maybeTestMethod.flatMap(testMethod -> ReflectionSupport.findMethod(testClass, testMethod.getName(), mockType, Context.class)))
            .<Object>map(method -> Mockito.mockConstruction(mockType, (mock, context) -> ReflectionSupport.invokeMethod(method, testInstance.get(), mock, context)))
            .orElseGet(() -> Mockito.mockConstruction(mockType));
    }
}
