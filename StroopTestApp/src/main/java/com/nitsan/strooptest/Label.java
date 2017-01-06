package com.nitsan.strooptest;

import android.support.annotation.ColorInt;

final class Label {
    private final Color text;
    private final Color color;

    Label(RandomColor randomColor) {
        this.text = randomColor.next();
        this.color = randomColor.next();
    }

    String text() {
        return text.text();
    }

    @ColorInt
    int color() {
        return color.color();
    }

    boolean isCongruent() {
        return text.equals(color);
    }

    boolean hasColor(Color color) {
        return this.color.equals(color);
    }
}
