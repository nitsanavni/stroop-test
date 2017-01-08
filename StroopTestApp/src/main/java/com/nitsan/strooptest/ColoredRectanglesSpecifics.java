package com.nitsan.strooptest;

class ColoredRectanglesSpecifics implements TestSpecifics {
    private final RandomColor randomColor;
    private final String instructions;

    ColoredRectanglesSpecifics(RandomColor randomColor, String instructions) {
        this.randomColor = randomColor;
        this.instructions = instructions;
    }

    @Override
    public boolean correct(Label label, Color clicked) {
        return label.hasColor(clicked);
    }

    @Override
    public Label makeNextLabel() {
        return Label.newColoredRectangleInstance(randomColor);
    }

    @Override
    public String testInstructions() {
        return instructions;
    }

    @Override
    public void showLabel(StroopTestFlowUI ui, Label label) {
        ui.showColoredRectangle(label.color());
    }

    @Override
    public String testName() {
        return "Colors";
    }

}
