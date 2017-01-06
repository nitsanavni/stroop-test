package com.nitsan.strooptest;

final class Yellow implements Color {
    private static final Yellow instance = new Yellow();

    private Yellow() {
    }

    static Yellow get() {
        return instance;
    }

    @Override
    public boolean equals(Object o) {
        return null != o && o instanceof Yellow;
    }

    @Override
    public String text() {
        return "צהוב";
    }

    @Override
    public int color() {
        return android.graphics.Color.YELLOW;
    }
}
