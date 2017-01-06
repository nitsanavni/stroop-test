package com.nitsan.strooptest;

final class Black implements Color {
    private static final Black instance = new Black();

    private Black() {
    }

    static Black get() {
        return instance;
    }

    @Override
    public boolean equals(Object o) {
        return null != o && o instanceof Black;
    }

    @Override
    public String text() {
        return "שחור";
    }

    @Override
    public int color() {
        return android.graphics.Color.BLACK;
    }
}
