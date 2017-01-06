package com.nitsan.strooptest;

import android.util.Log;

import java.util.List;

class AppFlow {
    private static final String TAG = "app-flow";
    private final UI ui;
    private final List<StroopTestFlow> stroopTests;

    AppFlow(UI ui, List<StroopTestFlow> stroopTests) {
        this.ui = ui;
        this.stroopTests = stroopTests;
    }

    void start() {
        ui.initialExplanation()
                .flatMap(o -> {
                    ui.test();
                    // Observable.from(tests)
                    return stroopTests.get(0).start();
                })
                .subscribe(o -> ui.summary(), t -> Log.e(TAG, "", t));
    }
}
