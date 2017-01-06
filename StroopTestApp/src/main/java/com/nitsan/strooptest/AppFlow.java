package com.nitsan.strooptest;

import java.util.List;

import rx.Observable;

class AppFlow {
    private final UI ui;
    private final List<StroopTestFlow> stroopTests;

    AppFlow(UI ui, List<StroopTestFlow> stroopTests) {
        this.ui = ui;
        this.stroopTests = stroopTests;
        connectTheFlow();
    }

    void start() {
        ui.initialExplanation()
                .subscribe(o -> {
                    ui.test();
                    if (!stroopTests.isEmpty()) {
                        stroopTests.get(0).start();
                    }
                });
    }

    private void connectTheFlow() {
        Observable
                .from(stroopTests)
                .scan((p, f) -> {
                    p.end().subscribe(o -> f.start());
                    return f;
                })
                .subscribe();
        if (!stroopTests.isEmpty()) {
            stroopTests.get(stroopTests.size() - 1).end().subscribe(o -> ui.summary());
        }
    }
}
