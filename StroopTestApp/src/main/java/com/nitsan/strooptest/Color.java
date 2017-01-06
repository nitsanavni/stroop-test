package com.nitsan.strooptest;

import android.support.annotation.ColorInt;

interface Color {
    int NUM_OF_COLORS = 5;

    String text();

    @ColorInt
    int color();
}
