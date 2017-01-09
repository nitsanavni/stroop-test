package com.nitsan.strooptest;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

// TODOs
// - add text to summary view
// - app flow: celebration & congrats
// - open - add settings? (num of trials per test, email address)
// - Labels randomization distribution - uniform?
// - open - how do we know the email address?
// - open - how to pseudo random? answer - Random with same seed
// - landscape support?
// - integrate Hebrew texts

class TestFlow {
    static final int TARGET_NUM_OF_TRIALS = 20;
    private final StroopTestFlowUI ui;
    private final TestSpecifics specifics;
    private final TimeSource time;

    private Label currentLabel;
    private PublishSubject<Object> endSubject;
    private int numOfTrials = 0;
    private int incorrectTrials = 0;
    private Subscription clickSubscription;
    private long startTime;
    private long endTime;

    TestFlow(StroopTestFlowUI ui, TestSpecifics specifics, TimeSource time) {
        this.specifics = specifics;
        this.ui = ui;
        this.time = time;
    }

    private void subscribeToClicks() {
        clickSubscription = ui.getClicks().subscribe(clickedColor -> {
            numOfTrials++;
            boolean correct = specifics.correct(currentLabel, clickedColor);
            if (!correct) {
                incorrectTrials++;
            }
            ui.correct(correct);
            if (enough()) {
                endTime = time.get();
                clickSubscription.unsubscribe();
            } else {
                currentLabel = specifics.makeNextLabel();
            }
            // TODO - move delay functionality to Rx util class
            Observable
                    .just(0)
                    .subscribeOn(Schedulers.immediate())
                    .delay(100, TimeUnit.MILLISECONDS, Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(i -> {
                        if (enough()) {
                            endSubject.onNext(new Object());
                        } else {
                            specifics.showLabel(ui, currentLabel);
                        }
                    });
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
        subscribeToClicks();
    }

    private String instructions() {
        return specifics.testInstructions();
    }

    void instructionsRead() {
        startTime = time.get();
        ui.startTest();
        currentLabel = specifics.makeNextLabel();
        specifics.showLabel(ui, currentLabel);
    }

    String stats() {
        return "incorrect: " + incorrectTrials + ", time: " + ((float) (endTime - startTime) / 1000);
    }

    public String name() {
        return specifics.testName();
    }
}
