package com.nitsan.strooptest;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

// TODOs
// - add "repeat" button next to summary view
// - add text to summary view
// - add a "Trial" class - has a Label (congruent or not), start/end time, clicked color, is correct/incorrect
// - app flow:
//   - enter child's name (could be done directly in the email app)
//   - phase 1 - color names written in black - explanation & test - click according to written name
//   - phase 2 - colored rectangles - explanation & test - click according to
//   - phase 3 - pseudo-random color names written in pseudo-random colors - explanation & test - click according to color (not name)
//   - celebration & congrats
//   - send summary logs
// - summary logs: 1. child's name 2. per phase (x3) number of incorrect clicks 3. total test time
// - no need for congruency validation, but yes need for correctness validation
// - test ends after constant number of trials
// - open - add settings? (num of trials per test, email address)
// - Labels randomization distribution - uniform?
// - open - how do we know the email address?
// - open - how to pseudo random? answer - Random with same seed
// - add new types of labels: black color names, colored rectangles
// - add new types of tests (or "TestSpecifics"): black color names, colored rectangles
// - TestSpecifics: correctness criterion, instruction text, Label type,
// - maybe a test could ask the UI to show its explanation...
// - measure test time directly (not through individual trials)
// - landscape support?
// - integrate Hebrew texts

class StroopTestFlow {
    private static final int TARGET_NUM_OF_TRIALS = 30;
    private final StroopTestFlowUI ui;
    private final RandomColor randomColor;

    private Label currentLabel;
    private PublishSubject<Object> endSubject;
    private int numOfTrials = 0;
    private int incorrectTrials = 0;

    StroopTestFlow(StroopTestFlowUI ui, RandomColor randomColor) {
        this.randomColor = randomColor;
        this.ui = ui;
        ui.getClicks().subscribe(clickedColor -> {
            numOfTrials++;
            boolean correct = currentLabel.hasColor(clickedColor);
            if (!correct) {
                incorrectTrials++;
            }
            ui.correct(correct);
            if (enough()) {
                endSubject.onNext(new Object());
            } else {
                currentLabel = makeLabel();
                // TODO - move delay functionality to Rx util class
                Observable
                        .just(0)
                        .subscribeOn(Schedulers.immediate())
                        // TODO - subtract this delay from the stats / move timing into "Trial" class having start time and end time
                        .delay(300, TimeUnit.MILLISECONDS, Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(i -> ui.showLabel(currentLabel));
            }
        });
    }

    private boolean enough() {
        return numOfTrials >= TARGET_NUM_OF_TRIALS;
    }

    Observable<Object> end() {
        endSubject = PublishSubject.create();
        return endSubject;
    }

    void start() {
        ui.showTestInstructions(this, instructions());
    }

    private String instructions() {
        return null;
    }

    // TODO - should be "makeTrial"
    private Label makeLabel() {
        return new Label(randomColor);
    }

    void instructionsRead() {
        currentLabel = makeLabel();
        ui.showLabel(currentLabel);
    }

    String stats() {
        return "incorrect: " + incorrectTrials;
    }

}
