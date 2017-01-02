package com.nitsan.strooptest;

import rx.Observable;

interface StroopTestUI {
    void showLabel(Label label);

    Observable<Color> getClicks();

    void end(String stats);

    void correct(boolean correct);
}
