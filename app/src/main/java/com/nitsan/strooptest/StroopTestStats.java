package com.nitsan.strooptest;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

// TODO - do we care about correctness?
class StroopTestStats {

    private static final int ENOUGH = 6;

    private long prevTime = 0L;
    private List<Integer> congruents;
    private List<Integer> incongruents;

    // TODO - instead of Boolean - change to TrialResult (isCongruent?, isCorrect?)
    StroopTestStats(TimeSource time, Observable<Boolean> clicks) {
        congruents = new ArrayList<>();
        incongruents = new ArrayList<>();
        // TODO - first timestamp should not be here - should only be after starting the test
        prevTime = time.get();
        clicks.subscribe(congruent -> {
            long current = time.get();
            int trialTime = (int) (current - prevTime);
            if (congruent) {
                congruents.add(trialTime);
            } else {
                incongruents.add(trialTime);
            }
            prevTime = current;
        });
    }

    Result congruent() {
        return new Result(congruents);
    }

    Result incongruent() {
        return new Result(incongruents);
    }

    boolean enough() {
        return congruents.size() >= ENOUGH &&
                incongruents.size() >= ENOUGH;
    }

    @Override
    public String toString() {
        return "stats:\ncongruent: " + congruent().toString() + "\nincongruent: " + incongruent().toString();
    }

    static class Result {
        private final int num;
        private final int avgTime;

        private Result(List<Integer> times) {
            num = times.size();
            avgTime = getAvg(times);
        }

        private int getAvg(List<Integer> times) {
            if (num == 0) {
                return 0;
            }
            int sum = 0;
            for (int i = 0; i < num; i++) {
                sum += times.get(i);
            }
            return sum / num;
        }

        int num() {
            return num;
        }

        int avgTime() {
            return avgTime;
        }

        @Override
        public String toString() {
            return "" + num + ", " + ((float) avgTime) / 1000;
        }
    }

}
