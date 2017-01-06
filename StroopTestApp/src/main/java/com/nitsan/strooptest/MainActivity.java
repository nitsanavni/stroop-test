package com.nitsan.strooptest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

import rx.Observable;
import rx.subjects.PublishSubject;

public class MainActivity extends Activity implements UI, StroopTestFlowUI {

    private AppFlow flow;
    private StroopTestFlow test;
    private PublishSubject<Object> initialExplanationSubject;
    private View initialExplanationButton;
    private PublishSubject<Object> testSubject;
    private PublishSubject<Color> colorClicksSubject = PublishSubject.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flow = new AppFlow(this);
        test = new StroopTestFlow(this, new RandomColor(new Random()), new StroopTestStats(SystemTime.get()));
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
        findViewById(R.id.initial_explanation_button).setVisibility(View.GONE);
        findViewById(R.id.intro_explanation).setVisibility(View.GONE);
        test.start();
        testSubject = PublishSubject.create();
        return testSubject;
    }

    @Override
    public void summary() {
        findViewById(R.id.colorButtons).setVisibility(View.GONE);
        findViewById(R.id.label).setVisibility(View.GONE);
        findViewById(R.id.trial_instructions).setVisibility(View.GONE);
    }

    @Override
    public void showLabel(Label label) {
        findViewById(R.id.trial_instructions).setVisibility(View.VISIBLE);
        findViewById(R.id.label).setVisibility(View.VISIBLE);
        findViewById(R.id.mini_celebration).setVisibility(View.GONE);
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
        testSubject.onNext(new Object());
        findViewById(R.id.colorButtons).setVisibility(View.GONE);
        TextView summary = (TextView) findViewById(R.id.summary);
        summary.setVisibility(View.VISIBLE);
        summary.setText(stats);
    }

    @Override
    public void correct(boolean correct) {
        findViewById(R.id.label).setVisibility(View.INVISIBLE);
        findViewById(R.id.trial_instructions).setVisibility(View.INVISIBLE);
        findViewById(R.id.colorButtons).setVisibility(View.INVISIBLE);
        TextView celebration = (TextView) findViewById(R.id.mini_celebration);
        celebration.setVisibility(View.VISIBLE);
        celebration.setText(correct ? R.string.correct : R.string.incorrect);
    }

    public void colorButtonClick(View view) {
        switch (view.getId()) {
            case R.id.blackButton:
                colorClicksSubject.onNext(Black.get());
                break;
            case R.id.blueButton:
                colorClicksSubject.onNext(Blue.get());
                break;
            case R.id.yellowButton:
                colorClicksSubject.onNext(Yellow.get());
                break;
            case R.id.greenButton:
                colorClicksSubject.onNext(Green.get());
                break;
            case R.id.redButton:
                colorClicksSubject.onNext(Red.get());
                break;
        }
    }

}
