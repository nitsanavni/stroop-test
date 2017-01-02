package com.nitsan.strooptest;

import rx.subjects.PublishSubject;

class StroopTest {
    private final StroopTestUI ui;
    private final StroopTestStats stats;
    private final RandomColor randomColor;
    private final PublishSubject<Boolean> congruentSubject = PublishSubject.create();
    private Label currentLabel;

    StroopTest(StroopTestUI ui, RandomColor randomColor) {
        this.randomColor = randomColor;
        this.ui = ui;
        this.stats = new StroopTestStats(SystemTime.get(), congruentSubject);
        ui.getClicks().subscribe(clickedColor -> {
            congruentSubject.onNext(currentLabel.isCongruent());
            if (stats.enough())
            currentLabel = makeLabel();
            ui.showLabel(currentLabel);
        });
    }

    void start() {
        currentLabel = makeLabel();
        ui.showLabel(currentLabel);
    }

    private Label makeLabel() {
        return new Label(randomColor);
    }
}
