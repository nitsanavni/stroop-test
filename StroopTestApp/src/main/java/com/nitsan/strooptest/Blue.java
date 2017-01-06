package com.nitsan.strooptest;

final class Blue implements Color {
    private static final Blue instance = new Blue();

    private Blue() {
    }

    static Blue get() {
        return instance;
    }

    @Override
    public boolean equals(Object o) {
        return null != o && o instanceof Blue;
    }

    @Override
    public String text() {
        return "כחול";
    }

    @Override
    public int color() {
        return android.graphics.Color.BLUE;
    }
}
