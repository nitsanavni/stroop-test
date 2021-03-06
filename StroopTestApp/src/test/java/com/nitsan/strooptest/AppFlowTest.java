package com.nitsan.strooptest;


import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.subjects.PublishSubject;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AppFlowTest {

    @Test
    public void shouldStartWithInitialExplanation() {
        UI ui = mock(UI.class);
        when(ui.initialExplanation()).thenReturn(PublishSubject.create());
        AppFlow flow = new AppFlow(ui, Collections.EMPTY_LIST);
        flow.start();
        verify(ui, times(1)).initialExplanation();
    }

    @Test
    public void afterInitialExplanation_shouldGoToFirstTestUI() {
        UI ui = mock(UI.class);
        PublishSubject<Object> initialExplanationSubject = PublishSubject.create();
        when(ui.initialExplanation()).thenReturn(initialExplanationSubject);
        List<TestFlow> flows = new ArrayList<>();
        TestFlow testFlow = mock(TestFlow.class);
        when(testFlow.end()).thenReturn(PublishSubject.create());
        flows.add(testFlow);
        AppFlow flow = new AppFlow(ui, flows);
        flow.start();
        initialExplanationSubject.onNext(new Object());
        verify(testFlow, times(1)).start();
    }

    @Test
    public void afterInitialExplanation_shouldStartFirstTest() {
        UI ui = mock(UI.class);
        PublishSubject<Object> initialExplanationSubject = PublishSubject.create();
        when(ui.initialExplanation()).thenReturn(initialExplanationSubject);
        List<TestFlow> flows = new ArrayList<>(3);
        TestFlow testFlow = mock(TestFlow.class);
        when(testFlow.end()).thenReturn(PublishSubject.create());
        flows.add(testFlow);
        AppFlow flow = new AppFlow(ui, flows);
        flow.start();
        initialExplanationSubject.onNext(new Object());
        verify(testFlow, times(1)).start();
    }

    @Test
    public void afterTestEnds_shouldShowSummary() {
        UI ui = mock(UI.class);
        PublishSubject<Object> initialExplanationSubject = PublishSubject.create();
        when(ui.initialExplanation()).thenReturn(initialExplanationSubject);
        List<TestFlow> flows = new ArrayList<>(3);
        TestFlow testFlow = mock(TestFlow.class);
        PublishSubject<Object> testEndSubject = PublishSubject.create();
        when(testFlow.end()).thenReturn(testEndSubject);
        flows.add(testFlow);
        AppFlow flow = new AppFlow(ui, flows);
        flow.start();
        initialExplanationSubject.onNext(new Object());
        testEndSubject.onNext(new Object());
        verify(ui, times(1)).summary(any());
    }

    @Test
    public void shouldRunTestsOneAtATime() {
        UI ui = mock(UI.class);
        PublishSubject<Object> initialExplanationSubject = PublishSubject.create();
        when(ui.initialExplanation()).thenReturn(initialExplanationSubject);
        List<TestFlow> flows = new ArrayList<>(3);
        TestFlow testFlow = mock(TestFlow.class);
        PublishSubject<Object> testEndSubject = PublishSubject.create();
        when(testFlow.end()).thenReturn(testEndSubject);
        flows.add(testFlow);
        TestFlow testFlow2 = mock(TestFlow.class);
        PublishSubject<Object> testEndSubject2 = PublishSubject.create();
        when(testFlow2.end()).thenReturn(testEndSubject2);
        flows.add(testFlow2);
        AppFlow flow = new AppFlow(ui, flows);
        flow.start();
        initialExplanationSubject.onNext(new Object());
        testEndSubject.onNext(new Object());
        verify(ui, times(0)).summary(any());
        verify(testFlow2, times(1)).start();
    }

    @Test
    public void shouldRunTestsOneAtATime_threeTests() {
        UI ui = mock(UI.class);
        PublishSubject<Object> initialExplanationSubject = PublishSubject.create();
        when(ui.initialExplanation()).thenReturn(initialExplanationSubject);
        List<TestFlow> flows = new ArrayList<>(3);
        TestFlow testFlow = mock(TestFlow.class);
        PublishSubject<Object> testEndSubject = PublishSubject.create();
        when(testFlow.end()).thenReturn(testEndSubject);
        flows.add(testFlow);
        TestFlow testFlow2 = mock(TestFlow.class);
        PublishSubject<Object> testEndSubject2 = PublishSubject.create();
        when(testFlow2.end()).thenReturn(testEndSubject2);
        flows.add(testFlow2);
        TestFlow testFlow3 = mock(TestFlow.class);
        PublishSubject<Object> testEndSubject3 = PublishSubject.create();
        when(testFlow3.end()).thenReturn(testEndSubject3);
        flows.add(testFlow3);
        AppFlow flow = new AppFlow(ui, flows);
        flow.start();
        initialExplanationSubject.onNext(new Object());
        testEndSubject.onNext(new Object());
        verify(testFlow2, times(1)).start();
        verify(testFlow3, times(0)).start();
        testEndSubject2.onNext(new Object());
        verify(testFlow3, times(1)).start();
        verify(ui, times(0)).summary(any());
        testEndSubject3.onNext(new Object());
        verify(ui, times(1)).summary(any());
    }

}