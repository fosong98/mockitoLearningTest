package com.mockitoTest;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

public class tutorial {
    @Test
    public void one() {
        // Let's verify some behaviour!

        List mockedList = mock(List.class);

        mockedList.add("one");
        mockedList.clear();

        /** verify(...) : 메서드 호출이 한번 발생했는지 확인 */
        verify(mockedList).add("one");
        verify(mockedList).clear();

        // mock은 상호작용을 기억한다.
    }

    @Test
    public void two() {
        // How about some stubbing?

        // mock은 인터페이스 뿐 아니라 클래스로도 만들 수 있다.
        LinkedList mockedList = mock(LinkedList.class);

        // stubbing
        when(mockedList.get(0)).thenReturn("first");
        when(mockedList.get(1)).thenThrow(new RuntimeException());

        assertEquals("first", mockedList.get(0));
        // mock은 저장되지 않은 메소드에 대해서는 null을 리턴한다.
        // Primitive에 대해서는 0이나 false를 리턴한다.
        assertNull(mockedList.get(1000));
        assertThrows(RuntimeException.class, () -> {
            mockedList.get(1);
        });
    }

    @Test
    public void three() {
        // Argument matchers

        LinkedList mockedList = mock(LinkedList.class);

        // matcher를 활용하여 mock 메소드를 구성할 수 있다.
        when(mockedList.get(anyInt())).thenReturn("element");

        assertEquals("element", mockedList.get(100));
        verify(mockedList).get(anyInt());
    }

    @Test
    public void four() {
        // Verifying exact number of invocations
        LinkedList mockedList = mock(LinkedList.class);

        mockedList.add("once");

        mockedList.add("twice");
        mockedList.add("twice");

        mockedList.add("three times");
        mockedList.add("three times");
        mockedList.add("three times");

        // verification은 times(1)이 디폴트다.
        verify(mockedList).add("once");
        verify(mockedList, times(1)).add("once");
        verify(mockedList, times(2)).add("twice");
        verify(mockedList, times(3)).add("three times");

        // never()를 사용하면 한번도 안일어난 것이어야 한다.
        verify(mockedList, never()).add("never happened");

        // 심지어 atLeast()와 atMost()도 있다.
        verify(mockedList, atMostOnce()).add("once");
        verify(mockedList, atLeastOnce()).add("three times");
        verify(mockedList, atLeast(2)).add("three times");
        verify(mockedList, atMost(5)).add("three times");
    }

    @Test
    public void five() {
        // Stubbing void methds with exceptions
        List mockedList = mock(List.class);

        // 에러를 던질 수 있다.
        doThrow(new RuntimeException()).when(mockedList).clear();

        assertThrows(RuntimeException.class, () -> {
            mockedList.clear();
        });
    }

    @Test
    public void six() {
        // Verification in order

        List singleMock = mock(List.class);

        singleMock.add("was added first");
        singleMock.add("was added second");

        // 하나의 mock에 대해 메소드 호출 순서를 검증한다.
        InOrder inOrder = inOrder(singleMock);

        // 메소드가 순서대로 호출되어야 한다.
        inOrder.verify(singleMock).add("was added first");
        inOrder.verify(singleMock).add("was added second");

        //  InOrder를 여러 개의 mock에 대해서도 적용할 수 있다.
        List firstMock = mock(List.class);
        List secondMock = mock(List.class);

        //using mocks
        firstMock.add("was called first");
        secondMock.add("was called second");

        // 대입 순서는 상관없다.
        InOrder inOrder2 = inOrder(secondMock, firstMock);

        inOrder2.verify(firstMock).add("was called first");
        inOrder2.verify(secondMock).add("was called second");

        // 하나에 대한 InOrder와 여러 mock에 대한 InOrder를 혼합할 수 있다.
    }
}
