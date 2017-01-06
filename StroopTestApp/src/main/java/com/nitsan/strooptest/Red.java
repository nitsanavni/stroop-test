package com.nitsan.strooptest;

final class Red implements Color {
    private static final Red instance = new Red();

    private Red() {
    }

    static Red get() {
        return instance;
    }
    @Override
    public boolean equals(Object o) {
        return null != o && o instanceof Red;
    }

    @Override
    public String text() {
        return "אדום";
    }

    @Override
    public int color() {
        return android.graphics.Color.RED;
    }
}
