package com.nitsan.strooptest;

import android.util.Log;

class AppFlow {
    private static final String TAG = "app-flow";
    private final UI ui;

    AppFlow(UI ui) {
        this.ui = ui;
    }

    void start() {
        ui.initialExplanation()
                .flatMap(o -> ui.test())
                .subscribe(o -> ui.summary(), t -> Log.e(TAG, "", t));
    }
}
