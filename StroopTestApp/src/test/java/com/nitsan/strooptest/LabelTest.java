package com.nitsan.strooptest;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class LabelTest {

    @Test
    public void shouldHasColorCorrectly() {
        Label label = new Label(Black.get(), Blue.get());
        assertThat(label.hasColor(Blue.get())).isTrue();
        assertThat(label.hasColor(Black.get())).isFalse();
        assertThat(label.hasColor(Red.get())).isFalse();
    }

}