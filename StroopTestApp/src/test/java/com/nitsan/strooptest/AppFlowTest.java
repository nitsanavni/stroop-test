package com.nitsan.strooptest;


import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.subjects.PublishSubject;

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
        AppFlow flow = new AppFlow(ui, Collections.EMPTY_LIST);
        flow.start();
        initialExplanationSubject.onNext(new Object());
        verify(ui, times(1)).test();
    }

    @Test
    public void afterInitialExplanation_shouldStartFirstTest() {
        UI ui = mock(UI.class);
        PublishSubject<Object> initialExplanationSubject = PublishSubject.create();
        when(ui.initialExplanation()).thenReturn(initialExplanationSubject);
        List<StroopTestFlow> flows = new ArrayList<>(3);
        StroopTestFlow stroopTestFlow = mock(StroopTestFlow.class);
        when(stroopTestFlow.end()).thenReturn(PublishSubject.create());
        flows.add(stroopTestFlow);
        AppFlow flow = new AppFlow(ui, flows);
        flow.start();
        initialExplanationSubject.onNext(new Object());
        verify(stroopTestFlow, times(1)).start();
    }

    @Test
    public void afterTestEnds_shouldShowSummary() {
        UI ui = mock(UI.class);
        PublishSubject<Object> initialExplanationSubject = PublishSubject.create();
        when(ui.initialExplanation()).thenReturn(initialExplanationSubject);
        List<StroopTestFlow> flows = new ArrayList<>(3);
        StroopTestFlow stroopTestFlow = mock(StroopTestFlow.class);
        PublishSubject<Object> testEndSubject = PublishSubject.create();
        when(stroopTestFlow.end()).thenReturn(testEndSubject);
        flows.add(stroopTestFlow);
        AppFlow flow = new AppFlow(ui, flows);
        flow.start();
        initialExplanationSubject.onNext(new Object());
        testEndSubject.onNext(new Object());
        verify(ui, times(1)).summary();
    }

    @Test
    public void shouldRunTestsOneAtATime() {
        UI ui = mock(UI.class);
        PublishSubject<Object> initialExplanationSubject = PublishSubject.create();
        when(ui.initialExplanation()).thenReturn(initialExplanationSubject);
        List<StroopTestFlow> flows = new ArrayList<>(3);
        StroopTestFlow stroopTestFlow = mock(StroopTestFlow.class);
        PublishSubject<Object> testEndSubject = PublishSubject.create();
        when(stroopTestFlow.end()).thenReturn(testEndSubject);
        flows.add(stroopTestFlow);
        StroopTestFlow stroopTestFlow2 = mock(StroopTestFlow.class);
        PublishSubject<Object> testEndSubject2 = PublishSubject.create();
        when(stroopTestFlow2.end()).thenReturn(testEndSubject2);
        flows.add(stroopTestFlow2);
        AppFlow flow = new AppFlow(ui, flows);
        flow.start();
        initialExplanationSubject.onNext(new Object());
        testEndSubject.onNext(new Object());
        verify(ui, times(0)).summary();
        verify(stroopTestFlow2, times(1)).start();
    }

    @Test
    public void shouldRunTestsOneAtATime_threeTests() {
        UI ui = mock(UI.class);
        PublishSubject<Object> initialExplanationSubject = PublishSubject.create();
        when(ui.initialExplanation()).thenReturn(initialExplanationSubject);
        List<StroopTestFlow> flows = new ArrayList<>(3);
        StroopTestFlow stroopTestFlow = mock(StroopTestFlow.class);
        PublishSubject<Object> testEndSubject = PublishSubject.create();
        when(stroopTestFlow.end()).thenReturn(testEndSubject);
        flows.add(stroopTestFlow);
        StroopTestFlow stroopTestFlow2 = mock(StroopTestFlow.class);
        PublishSubject<Object> testEndSubject2 = PublishSubject.create();
        when(stroopTestFlow2.end()).thenReturn(testEndSubject2);
        flows.add(stroopTestFlow2);
        StroopTestFlow stroopTestFlow3 = mock(StroopTestFlow.class);
        PublishSubject<Object> testEndSubject3 = PublishSubject.create();
        when(stroopTestFlow3.end()).thenReturn(testEndSubject3);
        flows.add(stroopTestFlow3);
        AppFlow flow = new AppFlow(ui, flows);
        flow.start();
        initialExplanationSubject.onNext(new Object());
        testEndSubject.onNext(new Object());
        verify(stroopTestFlow2, times(1)).start();
        verify(stroopTestFlow3, times(0)).start();
        testEndSubject2.onNext(new Object());
        verify(stroopTestFlow3, times(1)).start();
        verify(ui, times(0)).summary();
        testEndSubject3.onNext(new Object());
        verify(ui, times(1)).summary();
    }

}