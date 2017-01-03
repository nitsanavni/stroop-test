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
// - add text to summary view
// - add a "Trial" class - has a Label (congruent or not), start/end time, clicked color, is correct/incorrect
// - Labels randomization distribution - uniform?
// - use "view source" on online example (Adobe flash player)

class StroopTest {
    private final StroopTestUI ui;
    private final StroopTestStats stats;
    private final RandomColor randomColor;
    private final PublishSubject<Boolean> trialResultSubject = PublishSubject.create();

    private Label currentLabel;

    StroopTest(StroopTestUI ui, RandomColor randomColor) {
        this.randomColor = randomColor;
        this.ui = ui;
        this.stats = new StroopTestStats(SystemTime.get(), trialResultSubject);
        ui.getClicks().subscribe(clickedColor -> {
            boolean congruent = currentLabel.isCongruent();
            trialResultSubject.onNext(congruent);
            boolean correct = currentLabel.hasColor(clickedColor);
            ui.correct(correct);
            if (stats.enough()) {
                ui.end(stats.toString());
            } else {
                currentLabel = makeLabel();
                // TODO - move delay functionality to Rx util class
                Observable
                        .just(0)
                        .subscribeOn(Schedulers.immediate())
                        // TODO - subtract this delay from the stats / move timing into "Trial" class having start time and end time
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

    // TODO - should be "makeTrial"
    private Label makeLabel() {
        return new Label(randomColor);
    }
}
