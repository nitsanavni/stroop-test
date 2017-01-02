package com.nitsan.strooptest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
        super.onResume();
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
        test.start();
        testSubject = PublishSubject.create();
        return testSubject;
    }

    @Override
    public void summary() {

    }

    @Override
    public void showLabel(Label label) {
        findViewById(R.id.colorButtons).setVisibility(View.VISIBLE);
        TextView labelTV = (TextView) findViewById(R.id.label);
        labelTV.setText(label.text());
        labelTV.setTextColor(label.color());
    }

    @Override
    public Observable<Color> getClicks() {
        return colorClicksSubject;
    }

    @Override
    public void end(String stats) {
        findViewById(R.id.colorButtons).setVisibility(View.GONE);
        TextView summary = (TextView) findViewById(R.id.summary);
        summary.setVisibility(View.VISIBLE);
        summary.setText(stats);
    }

    public void colorButtonClick(View view) {
        // TODO - if correctness is required then we need to know the Color clicked
        colorClicksSubject.onNext(Black.get());
    }

}
