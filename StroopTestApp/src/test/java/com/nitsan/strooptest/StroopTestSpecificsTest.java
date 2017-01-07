package com.nitsan.strooptest;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StroopTestSpecificsTest {
    @Test
    public void shouldCorrectAccordingToColor() {
        StroopTestSpecifics specifics = new StroopTestSpecifics();
        Label label = mock(Label.class);
        when(label.hasColor(any())).thenReturn(true);
        assertThat(specifics.correct(label, Black.get())).isTrue();
        when(label.hasColor(any())).thenReturn(false);
        assertThat(specifics.correct(label, Black.get())).isFalse();
    }
}