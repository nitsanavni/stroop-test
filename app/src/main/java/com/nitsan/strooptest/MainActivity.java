package com.nitsan.strooptest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import java.util.Random;

import rx.Observable;
import rx.subjects.PublishSubject;

public class MainActivity extends Activity implements UI, StroopTestUI {

    private AppFlow flow;
    private StroopTest test;
    private PublishSubject<Object> initialExplanationSubject;
    private View initialExplanationButton;
    private PublishSubject<Object> testSubject;
    private PublishSubject<Color> colorClicksSubject = PublishSubject.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flow = new AppFlow(this);
        test = new StroopTest(this, new RandomColor(new Random()));
        setContentView(R.layout.activity_main);
        initialExplanationButton = findViewById(R.id.initial_explanation_button);
    }

    @Override
    protected void onResume() {
        flow.start();
    }

    @Override
    public Observable<Object> initialExplanation() {
        initialExplanationSubject = PublishSubject.create();
        initialExplanationButton.setEnabled(true);
        initialExplanationButton.setVisibility(View.VISIBLE);
        initialExplanationButton.setOnClickListener(v -> {
            initialExplanationButton.setEnabled(false);
            initialExplanationSubject.onNext(new Object());
        });
        return initialExplanationSubject;
    }

    @Override
    public Observable<Object> test() {
        testSubject = PublishSubject.create();
        return testSubject;
    }

    @Override
    public void summary() {

    }

    @Override
    public void showLabel(Label label) {

    }

    @Override
    public Observable<Color> getClicks() {
        return colorClicksSubject;
    }
}
