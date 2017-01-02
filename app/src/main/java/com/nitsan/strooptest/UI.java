package com.nitsan.strooptest;

import rx.Observable;

interface UI {
    Observable<Object> initialExplanation();

    Observable<Object> test();

    void summary();
}
