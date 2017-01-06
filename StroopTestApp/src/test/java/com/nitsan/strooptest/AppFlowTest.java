package com.nitsan.strooptest;


import org.junit.Test;

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
        AppFlow flow = new AppFlow(ui);
        flow.start();
        verify(ui, times(1)).initialExplanation();
    }

    @Test
    public void afterInitialExplanation_shouldGoToFirstTestUI() {
        UI ui = mock(UI.class);
        PublishSubject<Object> initialExplanationSubject = PublishSubject.create();
        when(ui.initialExplanation()).thenReturn(initialExplanationSubject);
        when(ui.test()).thenReturn(PublishSubject.create());
        AppFlow flow = new AppFlow(ui);
        flow.start();
        initialExplanationSubject.onNext(new Object());
        verify(ui, times(1)).test();
    }

    @Test
    public void afterInitialExplanation_shouldStartFirstTest() {
        UI ui = mock(UI.class);
        PublishSubject<Object> initialExplanationSubject = PublishSubject.create();
        when(ui.initialExplanation()).thenReturn(initialExplanationSubject);
        when(ui.test()).thenReturn(PublishSubject.create());
        AppFlow flow = new AppFlow(ui);
        flow.start();
        initialExplanationSubject.onNext(new Object());
        verify(ui, times(1)).test();
    }

}