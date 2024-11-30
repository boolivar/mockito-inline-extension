package org.bool.junit.mockito.inline;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.MockedConstruction.Context;
import org.mockito.MockingDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoInlineExtension.class)
class ConstructionMockTest {

    @ConstructionMock(TestClass.class)
    @Test
    void testConstructionMock() {
        assertThat(mockingDetails(new TestClass()))
            .returns(true, MockingDetails::isMock);
    }

    @ConstructionMock(TestClass.class)
    @Test
    void testConstructionMock(MockedConstruction<TestClass> mocked) {
        var mock = new TestClass();
        assertThat(mocked.constructed())
            .singleElement().isSameAs(mock);
    }

    @ConstructionMock(TestClass.class)
    @Test
    void testMockInitializer() {
        assertThat(new TestClass().test())
            .isEqualTo("mock");
    }

    void testMockInitializer(TestClass mock, Context context) {
        when(mock.test())
            .thenReturn("mock");
    }

    @ConstructionMock({TestClass.class, AnotherTestClass.class})
    @Test
    void testMultipleMocks(MockedConstruction<TestClass> mocked, MockedConstruction<AnotherTestClass> anotherMocked) {
        var mock = new TestClass();
        var anotherMock = new AnotherTestClass();
        assertThat(mock.test())
            .isEqualTo("mock");
        assertThat(anotherMock.test())
            .isEqualTo("anotherMock");
        assertThat(mocked.constructed())
            .singleElement().isSameAs(mock);
        assertThat(anotherMocked.constructed())
            .singleElement().isSameAs(anotherMock);
    }

    void testMultipleMocks(TestClass mock, Context context) {
        when(mock.test())
            .thenReturn("mock");
    }

    void testMultipleMocks(AnotherTestClass mock, Context context) {
        when(mock.test())
            .thenReturn("anotherMock");
    }

    @Nested
    class MocksReleasedTest {

        @Test
        void testConstructionMockReleased() {
            assertThat(new TestClass())
                .returns("test", TestClass::test)
                .returns(false, o -> mockingDetails(o).isMock());
            assertThat(new AnotherTestClass())
                .returns("test", AnotherTestClass::test)
                .returns(false, o -> mockingDetails(o).isMock());
        }
    }
}
