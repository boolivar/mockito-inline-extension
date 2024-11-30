mockito-inline-extension
---

[![CI](https://github.com/boolivar/mockito-inline-extension/workflows/CI/badge.svg)](https://github.com/boolivar/mockito-inline-extension/actions/workflows/ci.yml)
[![license](https://img.shields.io/badge/license-MIT-green)](https://github.com/boolivar/jdoc-test/blob/master/LICENSE)

[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=boolivar_mockito-inline-extension&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=boolivar_mockito-inline-extension)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=boolivar_mockito-inline-extension&metric=coverage)](https://sonarcloud.io/summary/new_code?id=boolivar_mockito-inline-extension)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=boolivar_mockito-inline-extension&metric=bugs)](https://sonarcloud.io/summary/new_code?id=boolivar_mockito-inline-extension)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=boolivar_mockito-inline-extension&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=boolivar_mockito-inline-extension)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=boolivar_mockito-inline-extension&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=boolivar_mockito-inline-extension)

JUnit jupiter extension for Mockito construction and static mocking.

<!-- x-release-please-start-version -->
```xml
<dependency>
    <groupId>io.github.boolivar</groupId>
    <artifactId>mockito-inline-extension</artifactId>
    <version>0.0.0</version>
    <scope>test</scope>
</dependency>
```
<!-- x-release-please-end -->
<!-- x-release-please-start-version -->
```gradle
testImplementation "io.github.boolivar:mockito-inline-extension:0.0.0"
```
<!-- x-release-please-end -->

Construction mock
---

Mockito:
```java
class FooTest {
    @Test
    void testFoo() {
        try (MockedConstruction mocked = mockConstruction(Foo.class)) {
            Foo foo = new Foo();
            when(foo.method()).thenReturn("bar");
            assertEquals("bar", foo.method());
            verify(foo).method();
        }
    }
}
```
MockitoInlineExtension:
```java
@ExtendWith(MockitoInlineExtension.class)
class FooTest {
    @ConstructionMock(Foo.class)
    @Test
    void testFoo() {
        Foo foo = new Foo();
        when(foo.method()).thenReturn("bar");
        assertEquals("bar", foo.method());
        verify(foo).method();
    }
}
```

`@ConstructionMock` supports list of classes to mock for single test. Test method can accept arguments of type `MockedConstruction` parameterized by mocked type:
```java
@ConstructionMock({Foo.class, Bar.class})
@Test
void testFooBar(MockedConstruction<Foo> mockedFoo, MockedConstruction<Bar> mockedBar) {
}
```

Optional mock initialization method can be provided with the same name as test, accepting actual mock and `MockedConstruction.Context` arguments:
```java
void testFooBar(Foo mock, MockedConstruction.Context context) {
}
void testFooBar(Bar mock, MockedConstruction.Context context) {
} 
```
Static mock
---

Mockito:
```java
class FooTest {
    @Test
    void testFoo() {
        try (MockedStatic mocked = mockStatic(Foo.class)) {
            mocked.when(Foo::method).thenReturn("bar");
            assertEquals("bar", Foo.method());
            mocked.verify(Foo::method);
        }
    }
}
```
MockitoInlineExtension:
```java
@ExtendWith(MockitoInlineExtension.class)
class FooTest {
    @StaticMock(Foo.class)
    @Test
    void testFoo(MockedStatic<Foo> mocked) {
        mocked.when(Foo::method).thenReturn("bar");
        assertEquals("bar", Foo.method());
        mocked.verify(Foo::method);
    }
}
```
