package com.nitsan.strooptest;

import rx.Observable;

interface UI {
    Observable<Object> initialExplanation();

    void test();

    void summary(String summary);
}
