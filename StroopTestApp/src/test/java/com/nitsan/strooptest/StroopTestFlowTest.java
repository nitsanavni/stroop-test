package com.nitsan.strooptest;


import org.junit.Test;

import java.util.List;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StroopTestFlowTest {

    @Test
    public void shouldShowLabel() {
        StroopTestFlowUI ui = mock(StroopTestFlowUI.class);
        when(ui.getClicks()).thenReturn(PublishSubject.create());
        StroopTestFlow test = new StroopTestFlow(ui, mock(RandomColor.class), mock(StroopTestStats.class));
        test.start();
        verify(ui, times(1)).showLabel(any(Label.class));
    }

    @Test
    public void shouldListenToClicks() {
        StroopTestFlowUI ui = mock(StroopTestFlowUI.class);
        when(ui.getClicks()).thenReturn(PublishSubject.create());
        StroopTestFlow test = new StroopTestFlow(ui, mock(RandomColor.class), mock(StroopTestStats.class));
        test.start();
        verify(ui, times(1)).getClicks();
    }

    @Test
    public void onColorButtonClick_shouldShowNextLabel() {
        TestScheduler scheduler = new TestScheduler();
        RxJavaPlugins.getInstance().reset();
        RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook() {
            @Override
            public Scheduler getComputationScheduler() {
                return scheduler;
            }
        });
        RxAndroidPlugins.getInstance().reset();
        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return scheduler;
            }
        });
        StroopTestFlowUI ui = mock(StroopTestFlowUI.class);
        PublishSubject<Color> clicks = PublishSubject.create();
        when(ui.getClicks()).thenReturn(clicks);
        RandomColor randomColor = mock(RandomColor.class);
        when(randomColor.next()).thenReturn(Black.get());
        StroopTestStats stats = mock(StroopTestStats.class);
        when(stats.enough()).thenReturn(false);
        StroopTestFlow test = new StroopTestFlow(ui, randomColor, stats);
        test.start();
        verify(ui, times(1)).showLabel(any(Label.class));
        clicks.onNext(Blue.get());
        scheduler.advanceTimeBy(1000, TimeUnit.MILLISECONDS);
        verify(ui, times(2)).showLabel(any(Label.class));
    }

    @Test
    public void whenEnough_shouldEnd() {
        TestScheduler scheduler = new TestScheduler();
        RxJavaPlugins.getInstance().reset();
        RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook() {
            @Override
            public Scheduler getComputationScheduler() {
                return scheduler;
            }
        });
        RxAndroidPlugins.getInstance().reset();
        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return scheduler;
            }
        });
        StroopTestFlowUI ui = mock(StroopTestFlowUI.class);
        PublishSubject<Color> clicks = PublishSubject.create();
        when(ui.getClicks()).thenReturn(clicks);
        RandomColor randomColor = mock(RandomColor.class);
        when(randomColor.next()).thenReturn(Black.get());
        StroopTestStats stats = mock(StroopTestStats.class);
        when(stats.enough()).thenReturn(true);
        StroopTestFlow test = new StroopTestFlow(ui, randomColor, stats);
        TestSubscriber<Object> testSubscriber = new TestSubscriber<>();
        test.start().subscribe(testSubscriber);
        clicks.onNext(Blue.get());
        //testSubscriber.awaitTerminalEvent();
        assertThat(testSubscriber.getOnNextEvents()).hasSize(1);
    }

}