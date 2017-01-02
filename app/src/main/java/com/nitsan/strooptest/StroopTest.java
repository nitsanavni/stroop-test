package com.nitsan.strooptest;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

// TODOs
// - add "repeat" button next to summary view
// - summary view should be a table
// - yes! we do care about correctness
// - how is the summary calculation going? I think it might take into account the time of incorrect
// responses but not the number
// - shortly show a "correct" / "incorrect" label between challenges
// - add text to summary view

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
            if (stats.enough()) {
                ui.end(stats.toString());
            } else {
                ui.correct(currentLabel.hasColor(clickedColor));
                currentLabel = makeLabel();
                Observable
                        .just(0)
                        .subscribeOn(Schedulers.immediate())
                        .delay(300, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(i -> ui.showLabel(currentLabel));
            }
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
