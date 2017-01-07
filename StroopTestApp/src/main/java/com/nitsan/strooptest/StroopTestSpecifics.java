package com.nitsan.strooptest;

final class StroopTestSpecifics implements TestSpecifics {
    private final RandomColor randomColor;

    StroopTestSpecifics(RandomColor randomColor) {
        this.randomColor = randomColor;
    }

    @Override
    public boolean correct(Label label, Color clicked) {
        return label.hasColor(clicked);
    }

    @Override
    public Label makeNextLabel() {
        return Label.newStroopTestInstance(randomColor);
    }

    @Override
    public String testInstructions() {
        return null;
    }
}
