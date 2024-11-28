package org.bool.junit.mockito.inline;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * {@link MockitoInlineExtension} annotation to setup
 * {@link org.mockito.Mockito#mockConstruction(Class) Mockito.mockConstruction}
 * for test methods.
 * 
 * <p>Test methods annotated with {@link ConstructionMock}
 * setup construction mock for classes listed in {@link value} attribute.
 * 
 * <p>Test classes annotated with {@link ConstructionMock}
 * setup construction mock on every test method.
 * 
 * <pre><code>
 * &#64;ConstructionMock(ToMock.class)
 * &#64;Test
 * void testMethod() {
 *     ToMock mock = new ToMock(); // this is mock
 * }
 * </code></pre>
 * 
 * <p>Test method can accept {@link org.mockito.MockedConstruction MockedConstruction}
 * arguments for each mocked type:
 * <pre><code>
 * &#64;ConstructionMock(ToMock.class)
 * &#64;Test
 * void testMethod(MockedConstruction&#60;ToMock> mocked) {
 *     ToMock mock = new ToMock();
 *     assertSame(mock, mocked.constructed().get(0));
 * }
 * </code></pre>
 * 
 * <p>Mock initialization can be performed in instance method with test method name and accepting
 * 2 arguments: actual mock and {@link org.mockito.MockedConstruction.Context Context}:
 * <pre><code>
 * void testMethod(ToMock mock, MockedConstruction.Context context) {
 *     when(mock.run()).thenReturn("success");
 * }
 * </code></pre>
 * 
 * @see MockitoInlineExtension
 * @see StaticMock
 */
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface ConstructionMock {

    /**
     * List of classes for construction mock.
     */
    Class<?>[] value();
}
