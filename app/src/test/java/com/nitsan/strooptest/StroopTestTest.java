package com.nitsan.strooptest;


import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class StroopTestTest {

    @Test
    public void shouldShowLabel() {
        StroopTestUI ui = mock(StroopTestUI.class);
        StroopTest test = new StroopTest(ui);
        test.start();
        verify(ui, times(1)).showLabel(any(Label.class));
    }

}