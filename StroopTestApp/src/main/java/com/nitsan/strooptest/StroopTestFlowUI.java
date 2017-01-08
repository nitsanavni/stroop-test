package com.nitsan.strooptest;

import android.support.annotation.ColorInt;

import rx.Observable;

interface StroopTestFlowUI {
    void showLabel(Label label, boolean showTrialInstructions);

    Observable<Color> getClicks();

    void end(String stats);

    void correct(boolean correct);

    void showTestInstructions(TestFlow flow, String instructions);

    void showColoredRectangle(@ColorInt int color);

    void startTest();
}
