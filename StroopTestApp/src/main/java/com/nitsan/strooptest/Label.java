package com.nitsan.strooptest;

import android.support.annotation.ColorInt;

class Label {
    private final Color text;
    private final Color color;

    Label(Color text, Color color) {
        this.text = text;
        this.color = color;
    }

    static Label newStroopTestInstance(RandomColor randomColor) {
        return new Label(randomColor.next(), randomColor.next());
    }

    static Label newColorNameInstance(RandomColor randomColor) {
        return new Label(randomColor.next(), Black.get());
    }

    static Label newColoredRectangleInstance(RandomColor randomColor) {
        return new Label(null, randomColor.next());
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

    boolean hasText(Color color) {
        return this.text.equals(color);
    }
}
