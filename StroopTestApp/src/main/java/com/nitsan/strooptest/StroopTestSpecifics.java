package com.nitsan.strooptest;

final class StroopTestSpecifics implements TestSpecifics {
    private final RandomColor randomColor;
    private final String instructions;

    StroopTestSpecifics(RandomColor randomColor, String instructions) {
        this.randomColor = randomColor;
        this.instructions = instructions;
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
        return instructions;
    }

    @Override
    public void showLabel(StroopTestFlowUI ui, Label label) {
        ui.showLabel(label, true);
    }

    @Override
    public String testName() {
        return "Stroop";
    }

}
