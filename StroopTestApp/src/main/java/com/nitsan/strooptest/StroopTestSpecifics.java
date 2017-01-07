package com.nitsan.strooptest;

final class StroopTestSpecifics implements TestSpecifics {
    @Override
    public boolean correct(Label label, Color clicked) {
        return label.hasColor(clicked);
    }
}
