package org.bool.junit.mockito.inline;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoInlineExtension.class)
class StaticMockTest {

    @StaticMock(TestClass.class)
    @Test
    void testStaticMock() {
        assertThat(TestClass.staticTest())
            .isNull();
    }

    @StaticMock(TestClass.class)
    @Test
    void testStaticMock(MockedStatic<TestClass> mocked) {
        mocked.when(TestClass::staticTest)
            .thenReturn("mock");
        assertThat(TestClass.staticTest())
            .isEqualTo("mock");
    }

    @StaticMock({TestClass.class, AnotherTestClass.class})
    @Test
    void testMultipleMocks(MockedStatic<TestClass> mocked, MockedStatic<AnotherTestClass> anotherMocked) {
        mocked.when(TestClass::staticTest)
            .thenReturn("a");
        mocked.when(AnotherTestClass::staticTest)
            .thenReturn("b");

        assertThat(TestClass.staticTest())
            .isEqualTo("a");
        assertThat(AnotherTestClass.staticTest())
            .isEqualTo("b");
    }

    @Nested
    class MocksReleasedTest {

        @Test
        void testStaticMocksReleased() {
            assertThat(TestClass.staticTest())
                .isEqualTo("test");
            assertThat(AnotherTestClass.staticTest())
                .isEqualTo("test");
        }
    }
}
