package com.nitsan.strooptest;

final class ColorNamesSpecifics implements TestSpecifics {
    private final RandomColor randomColor;

    ColorNamesSpecifics(RandomColor randomColor) {
        this.randomColor = randomColor;
    }

    @Override
    public boolean correct(Label label, Color clicked) {
        return label.hasText(clicked);
    }

    @Override
    public Label makeNextLabel() {
        return Label.newColorNameInstance(randomColor);
    }
}
