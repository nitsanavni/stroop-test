package com.nitsan.strooptest;

class StroopTest {
    private final StroopTestUI ui;

    StroopTest(StroopTestUI ui) {
        this.ui = ui;
    }

    void start() {
        ui.showLabel(new Label(0, 0));
    }
}
