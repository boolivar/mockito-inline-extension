package org.bool.junit.mockito.inline;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * {@link MockitoInlineExtension} annotation to setup
 * {@link org.mockito.Mockito#mockStatic(Class) Mockito.mockStatic}
 * for test methods.
 * 
 * <p>Test methods annotated with {@link StaticMock}
 * setup static mock for classes listed in {@link value} attribute.
 * 
 * <p>Test classes annotated with {@link StaticMock}
 * setup static mock on every test method.
 * 
 * <pre><code>
 * &#64;StaticMock(ToMock.class)
 * &#64;Test
 * void testMethod() {
 *     ToMock.run // this call is mocked
 * }
 * </code></pre>
 * 
 * <p>To configure static mocks behavior test method can
 * accept {@link org.mockito.MockedStatic MockedStatic} arguments
 * for each mocked type:
 * <pre><code>
 * &#64;StaticMock(ToMock.class)
 * &#64;Test
 * void testMethod(MockedStatic&#60;ToMock> mocked) {
 *     mocked.when(ToMock::run).thenReturn("success");
 * }
 * </code></pre>
 * 
 * @see MockitoInlineExtension
 * @see ConstructionMock
 */
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface StaticMock {

    Class<?>[] value();
}
