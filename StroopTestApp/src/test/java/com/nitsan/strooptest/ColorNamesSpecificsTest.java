package com.nitsan.strooptest;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ColorNamesSpecificsTest {
    @Test
    public void shouldCorrectAccordingToText() {
        TestSpecifics specifics = new ColorNamesSpecifics(mock(RandomColor.class));
        Label label = mock(Label.class);
        when(label.hasText(any())).thenReturn(true);
        assertThat(specifics.correct(label, Black.get())).isTrue();
        when(label.hasText(any())).thenReturn(false);
        assertThat(specifics.correct(label, Black.get())).isFalse();
    }

    @Test
    public void shouldMakeColorNamesLabels() {
        RandomColor randomColor = mock(RandomColor.class);
        TestSpecifics specifics = new ColorNamesSpecifics(randomColor);
        specifics.makeNextLabel();
        verify(randomColor, times(1)).next();
    }
}