package com.nitsan.strooptest;


import org.junit.Test;

import rx.subjects.PublishSubject;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StroopTestTest {

    @Test
    public void shouldShowLabel() {
        StroopTestUI ui = mock(StroopTestUI.class);
        when(ui.getClicks()).thenReturn(PublishSubject.create());
        StroopTest test = new StroopTest(ui, mock(RandomColor.class));
        test.start();
        verify(ui, times(1)).showLabel(any(Label.class));
    }

    @Test
    public void shouldListenToClicks() {
        StroopTestUI ui = mock(StroopTestUI.class);
        when(ui.getClicks()).thenReturn(PublishSubject.create());
        StroopTest test = new StroopTest(ui, mock(RandomColor.class));
        test.start();
        verify(ui, times(1)).getClicks();
    }

    @Test
    public void onColorButtonClick_shouldShowNextLabel() {
        StroopTestUI ui = mock(StroopTestUI.class);
        PublishSubject<Color> clicks = PublishSubject.create();
        when(ui.getClicks()).thenReturn(clicks);
        RandomColor randomColor = mock(RandomColor.class);
        when(randomColor.next()).thenReturn(Black.get());
        StroopTest test = new StroopTest(ui, randomColor);
        test.start();
        verify(ui, times(1)).showLabel(any(Label.class));
        clicks.onNext(Blue.get());
        verify(ui, times(2)).showLabel(any(Label.class));
    }

}