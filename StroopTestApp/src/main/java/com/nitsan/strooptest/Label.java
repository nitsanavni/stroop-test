package com.nitsan.strooptest;

import android.support.annotation.ColorInt;

class Label {
    private final Color text;
    private final Color color;

    Label(RandomColor randomColor) {
        this(randomColor.next(), randomColor.next());
    }

    Label(Color text, Color color) {
        this.text = text;
        this.color = color;
    }

    String text() {
        return text.text();
    }

    @ColorInt
    int color() {
        return color.color();
    }

    boolean hasColor(Color color) {
        return this.color.equals(color);
    }
}
