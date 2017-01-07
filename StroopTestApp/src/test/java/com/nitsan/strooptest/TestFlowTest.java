package com.nitsan.strooptest;


import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.concurrent.TimeUnit;

import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.observers.TestSubscriber;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.TestScheduler;
import rx.subjects.PublishSubject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@FixMethodOrder(MethodSorters.DEFAULT)
public class TestFlowTest {
    private static TestScheduler scheduler = new TestScheduler();

    @Before
    public void setUp() {
        RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook() {
            @Override
            public Scheduler getComputationScheduler() {
                return scheduler;
            }
        });
        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return scheduler;
            }
        });
    }

    @After
    public void resetHooks() {
        RxJavaPlugins.getInstance().reset();
        RxAndroidPlugins.getInstance().reset();
    }

    @Test
    public void shouldShowLabel() {
        StroopTestFlowUI ui = mock(StroopTestFlowUI.class);
        when(ui.getClicks()).thenReturn(PublishSubject.create());
        TestFlow test = new TestFlow(ui, mock(RandomColor.class), mock(TestSpecifics.class));
        test.start();
        test.instructionsRead();
        verify(ui, times(1)).showLabel(any(Label.class));
    }

    @Test
    public void shouldListenToClicks() {
        StroopTestFlowUI ui = mock(StroopTestFlowUI.class);
        when(ui.getClicks()).thenReturn(PublishSubject.create());
        new TestFlow(ui, mock(RandomColor.class), mock(TestSpecifics.class));
        verify(ui, times(1)).getClicks();
    }

    @Test
    public void onColorButtonClick_shouldShowNextLabel() {
        StroopTestFlowUI ui = mock(StroopTestFlowUI.class);
        PublishSubject<Color> clicks = PublishSubject.create();
        when(ui.getClicks()).thenReturn(clicks);
        RandomColor randomColor = mock(RandomColor.class);
        when(randomColor.next()).thenReturn(Black.get());
        TestFlow test = new TestFlow(ui, randomColor, mock(TestSpecifics.class));
        test.end().subscribe();
        test.start();
        test.instructionsRead();
        verify(ui, times(1)).showLabel(any(Label.class));
        clicks.onNext(Blue.get());
        scheduler.advanceTimeBy(50000, TimeUnit.MILLISECONDS);
        verify(ui, times(2)).showLabel(any(Label.class));
    }

    @Test
    public void shouldEndAfterXTrials() {
        StroopTestFlowUI ui = mock(StroopTestFlowUI.class);
        PublishSubject<Color> clicks = PublishSubject.create();
        when(ui.getClicks()).thenReturn(clicks);
        RandomColor randomColor = mock(RandomColor.class);
        when(randomColor.next()).thenReturn(Black.get());
        TestFlow test = new TestFlow(ui, randomColor, mock(TestSpecifics.class));
        TestSubscriber<Object> testSubscriber = new TestSubscriber<>();
        test.end().subscribe(testSubscriber);
        test.start();
        test.instructionsRead();
        for (int i = 0; i < 30; i++) {
            clicks.onNext(Blue.get());
        }
        assertThat(testSubscriber.getOnNextEvents()).hasSize(1);
    }

    @Test
    public void shouldShowTextInstructionsBeforeTestStarts() {
        StroopTestFlowUI ui = mock(StroopTestFlowUI.class);
        PublishSubject<Color> clicks = PublishSubject.create();
        when(ui.getClicks()).thenReturn(clicks);
        RandomColor randomColor = mock(RandomColor.class);
        when(randomColor.next()).thenReturn(Black.get());
        TestFlow test = new TestFlow(ui, randomColor, mock(TestSpecifics.class));
        TestSubscriber<Object> testSubscriber = new TestSubscriber<>();
        test.end().subscribe(testSubscriber);
        test.start();
        verify(ui, times(0)).showLabel(any(Label.class));
        verify(ui, times(1)).showTestInstructions(any(TestFlow.class), anyString());
    }

    @Test
    public void shouldSpecifyCorrectnessCriterion() {
        StroopTestFlowUI ui = mock(StroopTestFlowUI.class);
        PublishSubject<Color> clicks = PublishSubject.create();
        when(ui.getClicks()).thenReturn(clicks);
        RandomColor randomColor = mock(RandomColor.class);
        when(randomColor.next()).thenReturn(Black.get());
        TestSpecifics specifics = mock(TestSpecifics.class);
        TestFlow test = new TestFlow(ui, randomColor, specifics);
        TestSubscriber<Object> testSubscriber = new TestSubscriber<>();
        test.end().subscribe(testSubscriber);
        test.start();
        test.instructionsRead();
        when(specifics.correct(any(), any())).thenReturn(false);
        clicks.onNext(Black.get());
        assertThat(test.stats()).isEqualTo("incorrect: 1");
        when(specifics.correct(any(), any())).thenReturn(true);
        clicks.onNext(Black.get());
        assertThat(test.stats()).isEqualTo("incorrect: 1");
    }

}