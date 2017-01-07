package com.nitsan.strooptest;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class LabelTest {

    @Test
    public void shouldHasColorCorrectly() {
        Label label = new Label(Black.get(), Blue.get());
        assertThat(label.hasColor(Blue.get())).isTrue();
        assertThat(label.hasColor(Black.get())).isFalse();
        assertThat(label.hasColor(Red.get())).isFalse();
    }

    @Test
    public void stroopTestInstances_shouldBeAccordingToRandom() {
        RandomColor randomColor = mock(RandomColor.class);
        when(randomColor.next()).thenReturn(Yellow.get());
        assertThat(Label.newStroopTestInstance(randomColor).hasColor(Yellow.get())).isTrue();
        assertThat(Label.newStroopTestInstance(randomColor).hasText(Yellow.get())).isTrue();
    }

    @Test
    public void colorNamesInstances_text_shouldBeAccordingToRandom_color_shouldBeBlack() {
        RandomColor randomColor = mock(RandomColor.class);
        when(randomColor.next()).thenReturn(Yellow.get());
        assertThat(Label.newColorNameInstance(randomColor).hasColor(Black.get())).isTrue();
        assertThat(Label.newColorNameInstance(randomColor).hasText(Yellow.get())).isTrue();
    }

}