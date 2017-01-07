package com.nitsan.strooptest;

interface TestSpecifics {
    boolean correct(Label label, Color clicked);

    Label makeNextLabel();

    String testInstructions();

    void showLabel(StroopTestFlowUI ui, Label label);

    String testName();
}
