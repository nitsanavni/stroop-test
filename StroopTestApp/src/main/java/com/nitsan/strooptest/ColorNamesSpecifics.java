package com.nitsan.strooptest;

final class ColorNamesSpecifics implements TestSpecifics {
    private final RandomColor randomColor;
    private final String instructions;

    ColorNamesSpecifics(RandomColor randomColor, String instructions) {
        this.randomColor = randomColor;
        this.instructions = instructions;
    }

    @Override
    public boolean correct(Label label, Color clicked) {
        return label.hasText(clicked);
    }

    @Override
    public Label makeNextLabel() {
        return Label.newColorNameInstance(randomColor);
    }

    @Override
    public String testInstructions() {
        return instructions;
    }

    @Override
    public void showLabel(StroopTestFlowUI ui, Label label) {
        ui.showLabel(label, false);
    }

    @Override
    public String testName() {
        return "Color Names";
    }

}
