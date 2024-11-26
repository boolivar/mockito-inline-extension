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

    @Nested
    class MocksReleasedTest {

        @Test
        void testConstructionMockReleased() {
            assertThat(mockingDetails(new TestClass()))
                .returns(false, MockingDetails::isMock);
        }
    }
}
