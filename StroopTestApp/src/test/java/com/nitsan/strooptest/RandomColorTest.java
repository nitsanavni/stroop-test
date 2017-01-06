package com.nitsan.strooptest;

import org.junit.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RandomColorTest {

    @Test
    public void shouldGetColor() {
        Random rand = mock(Random.class);
        when(rand.nextInt(anyInt())).thenReturn(0);
        RandomColor randomColor = new RandomColor(rand);
        // too strong assertion?
        assertThat(randomColor.next()).isEqualTo(Black.get());
    }

}