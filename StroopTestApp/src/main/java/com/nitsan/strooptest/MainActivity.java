package com.nitsan.strooptest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rx.Observable;
import rx.subjects.PublishSubject;

public class MainActivity extends Activity implements UI, StroopTestFlowUI {

    private AppFlow flow;
    private PublishSubject<Object> initialExplanationSubject;
    private View initialExplanationButton;
    private PublishSubject<Color> colorClicksSubject = PublishSubject.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialExplanationButton = findViewById(R.id.initial_explanation_button);
    }

    @Override
    protected void onStart() {
        super.onStart();
        restart();
    }

    @Override
    public Observable<Object> initialExplanation() {
        initialExplanationSubject = PublishSubject.create();
        initialExplanationButton.setEnabled(true);
        initialExplanationButton.setVisibility(View.VISIBLE);
        findViewById(R.id.intro_explanation).setVisibility(View.VISIBLE);
        initialExplanationButton.setOnClickListener(v -> {
            initialExplanationButton.setEnabled(false);
            initialExplanationSubject.onNext(new Object());
        });
        return initialExplanationSubject;
    }

    @Override
    public void test() {
        findViewById(R.id.initial_explanation_button).setVisibility(View.GONE);
        findViewById(R.id.intro_explanation).setVisibility(View.GONE);
    }

    @Override
    public void summary(String summary) {
        findViewById(R.id.colorButtons).setVisibility(View.GONE);
        findViewById(R.id.label).setVisibility(View.GONE);
        findViewById(R.id.trial_instructions).setVisibility(View.GONE);
        findViewById(R.id.mini_celebration).setVisibility(View.GONE);
        View email = findViewById(R.id.email_stats_button);
        email.setVisibility(View.VISIBLE);
        email.setOnClickListener(v -> share(summary));
        View restart = findViewById(R.id.restart);
        restart.setVisibility(View.VISIBLE);
        restart.setOnClickListener(v -> restart());
        TextView tv = (TextView) findViewById(R.id.summary);
        tv.setVisibility(View.VISIBLE);
        tv.setText(summary);
    }

    private void restart() {
        makeFlow();
        findViewById(R.id.restart).setVisibility(View.GONE);
        findViewById(R.id.email_stats_button).setVisibility(View.GONE);
        findViewById(R.id.summary).setVisibility(View.GONE);
        flow.start();
    }

    private void makeFlow() {
        List<TestFlow> flows = new ArrayList<>(3);
        RandomColor randomColor = new RandomColor(new Random());
        TimeSource time = SystemTime.get();
        flows.add(new TestFlow(this, new ColorNamesSpecifics(randomColor, getString(R.string.color_names_instructions)), time));
        flows.add(new TestFlow(this, new ColoredRectanglesSpecifics(randomColor, getString(R.string.colored_rectangles_instructions)), time));
        flows.add(new TestFlow(this, new StroopTestSpecifics(randomColor, getString(R.string.stroop_instructions)), time));
        flow = new AppFlow(this, flows);
    }

    private void share(String text) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Stroop test stats");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
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
        labelTV.setBackgroundColor(android.graphics.Color.TRANSPARENT);
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

    @Override
    public void correct(boolean correct) {
        findViewById(R.id.label).setVisibility(View.INVISIBLE);
        findViewById(R.id.trial_instructions).setVisibility(View.INVISIBLE);
        findViewById(R.id.colorButtons).setVisibility(View.INVISIBLE);
        TextView celebration = (TextView) findViewById(R.id.mini_celebration);
        celebration.setVisibility(View.VISIBLE);
        celebration.setText(correct ? R.string.correct : R.string.incorrect);
    }

    @Override
    public void showTestInstructions(TestFlow flow, String instructions) {
        findViewById(R.id.mini_celebration).setVisibility(View.GONE);
        TextView tv = (TextView) findViewById(R.id.intro_explanation);
        tv.setVisibility(View.VISIBLE);
        tv.setText(instructions);
        initialExplanationButton.setVisibility(View.VISIBLE);
        initialExplanationButton.setEnabled(true);
        initialExplanationButton.setOnClickListener(v -> flow.instructionsRead());
    }

    @Override
    public void showColoredRectangle(@ColorInt int color) {
        findViewById(R.id.trial_instructions).setVisibility(View.VISIBLE);
        findViewById(R.id.label).setVisibility(View.VISIBLE);
        findViewById(R.id.mini_celebration).setVisibility(View.GONE);
        findViewById(R.id.colorButtons).setVisibility(View.VISIBLE);
        TextView labelTV = (TextView) findViewById(R.id.label);
        labelTV.setText("    ");
        labelTV.setBackgroundColor(color);
    }

    @Override
    public void startTest() {
        findViewById(R.id.initial_explanation_button).setVisibility(View.GONE);
        findViewById(R.id.intro_explanation).setVisibility(View.GONE);
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
