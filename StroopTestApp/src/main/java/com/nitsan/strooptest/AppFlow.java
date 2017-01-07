package com.nitsan.strooptest;

import java.util.List;

import rx.Observable;

class AppFlow {
    private final UI ui;
    private final List<TestFlow> testFlows;

    AppFlow(UI ui, List<TestFlow> testFlows) {
        this.ui = ui;
        this.testFlows = testFlows;
        connectTheFlow();
    }

    void start() {
        ui.initialExplanation()
                .subscribe(o -> {
                    if (!testFlows.isEmpty()) {
                        testFlows.get(0).start();
                    }
                });
    }

    private void connectTheFlow() {
        Observable
                .from(testFlows)
                .scan((p, f) -> {
                    p.end().subscribe(o -> f.start());
                    return f;
                })
                .subscribe();
        if (testFlows.size() >= 1) {
            testFlows.get(testFlows.size() - 1).end().subscribe(o -> ui.summary(summary()));
        }
    }

    private String summary() {
        StringBuilder sb = new StringBuilder();
        for (TestFlow test : testFlows) {
            sb.append(test.name()).append(": ").append(test.stats()).append("\n");
        }
        return sb.toString();
    }
}
