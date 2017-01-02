package com.nitsan.strooptest;


import org.junit.Test;

import rx.subjects.PublishSubject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StroopTestStatsTest {

    @Test
    public void shouldStatSingleChallenge() {
        TimeSource time = mock(TimeSource.class);
        when(time.get()).thenReturn(30L).thenReturn(50L);
        PublishSubject<Boolean> clicks = PublishSubject.create();
        StroopTestStats stats = new StroopTestStats(time, clicks);
        clicks.onNext(true);
        assertThat(stats.congruent().num()).isEqualTo(1);
        assertThat(stats.congruent().avgTime()).isEqualTo(20);
    }

    @Test
    public void shouldStatDifferentSingleCongruentChallenge() {
        TimeSource time = mock(TimeSource.class);
        when(time.get()).thenReturn(35L).thenReturn(50L);
        PublishSubject<Boolean> clicks = PublishSubject.create();
        StroopTestStats stats = new StroopTestStats(time, clicks);
        clicks.onNext(true);
        assertThat(stats.congruent().num()).isEqualTo(1);
        assertThat(stats.congruent().avgTime()).isEqualTo(15);
    }

    @Test
    public void shouldStatTwoCongruents() {
        TimeSource time = mock(TimeSource.class);
        when(time.get()).thenReturn(35L).thenReturn(50L).thenReturn(55L);
        PublishSubject<Boolean> clicks = PublishSubject.create();
        StroopTestStats stats = new StroopTestStats(time, clicks);
        clicks.onNext(true);
        clicks.onNext(true);
        assertThat(stats.congruent().num()).isEqualTo(2);
        assertThat(stats.congruent().avgTime()).isEqualTo(10);
    }

    @Test
    public void shouldStatIncongruents() {
        TimeSource time = mock(TimeSource.class);
        when(time.get()).thenReturn(35L).thenReturn(50L).thenReturn(55L);
        PublishSubject<Boolean> clicks = PublishSubject.create();
        StroopTestStats stats = new StroopTestStats(time, clicks);
        clicks.onNext(true);
        assertThat(stats.incongruent().num()).isEqualTo(0);
        assertThat(stats.incongruent().avgTime()).isEqualTo(0);
        clicks.onNext(false);
        assertThat(stats.incongruent().num()).isEqualTo(1);
        assertThat(stats.incongruent().avgTime()).isEqualTo(5);
    }

    @Test
    public void whenIsEnough() {
        PublishSubject<Boolean> clicks = PublishSubject.create();
        StroopTestStats stats = new StroopTestStats(mock(TimeSource.class), clicks);
        for (int i = 0; i < 10; i++) {
            assertThat(stats.enough()).isFalse();
            clicks.onNext(true);
            clicks.onNext(false);
        }
        assertThat(stats.enough()).isTrue();
    }

}