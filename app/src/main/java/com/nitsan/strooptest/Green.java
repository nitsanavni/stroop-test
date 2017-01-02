package com.nitsan.strooptest;

final class Green implements Color {
    private static final Green instance = new Green();

    private Green() {
    }

    static Green get() {
        return instance;
    }

    @Override
    public boolean equals(Object o) {
        return null != o && o instanceof Green;
    }

    @Override
    public String text() {
        return "ירוק";
    }

    @Override
    public int color() {
        return android.graphics.Color.GREEN;
    }
}
