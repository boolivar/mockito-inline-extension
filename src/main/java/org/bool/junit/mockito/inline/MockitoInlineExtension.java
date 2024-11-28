package org.bool.junit.mockito.inline;

import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This extension provides
 * {@link org.mockito.Mockito#mockConstruction(Class) Mockito.mockConstruction}
 * and {@link org.mockito.Mockito#mockStatic(Class) Mockito.mockStatic}
 * by {@link ConstructionMock} and {@link StaticMock} annotations.
 * 
 * @see ConstructionMock
 * @see StaticMock
 */
public class MockitoInlineExtension implements ParameterResolver, BeforeTestExecutionCallback {

    private final Namespace namespace = Namespace.create(MockitoInlineExtension.class);

    private final Map<Class<?>, AnnotationHandler> handlers;

    public MockitoInlineExtension() {
        this(new ConstructionMockHandler(), new StaticMockHandler());
    }

    public MockitoInlineExtension(AnnotationHandler... handlers) {
        this(Stream.of(handlers)
            .collect(Collectors.toMap(AnnotationHandler::parameterType, Function.identity(), MockitoInlineExtension::throwingMerger, LinkedHashMap::new)));
    }

    public MockitoInlineExtension(Map<Class<?>, AnnotationHandler> handlers) {
        this.handlers = handlers;
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        for (AnnotationHandler handler : handlers.values()) {
            Store store = context.getStore(namespace.append(handler.parameterType()));
            handler.init(store, context.getTestClass(), context.getTestMethod(), context.getTestInstance());
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameterContext.getDeclaringExecutable().equals(extensionContext.getTestMethod().orElse(null))
            && handlers.keySet().contains(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        Parameter parameter = parameterContext.getParameter();
        Store store = extensionContext.getStore(namespace.append(parameter.getType()));
        return handlers.get(parameter.getType()).resolveParameter(store, parameter);
    }

    private static AnnotationHandler throwingMerger(AnnotationHandler a, AnnotationHandler b) {
        throw new IllegalStateException("Duplicate key: " + a.parameterType());
    }
}