package com.nitsan.strooptest;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

// TODO - do we care about correctness?
class StroopTestStats {

    private static final int ENOUGH = 10;

    private long prevTime = 0L;
    private List<Integer> congruents;
    private List<Integer> incongruents;

    StroopTestStats(TimeSource time, Observable<Boolean> clicks) {
        congruents = new ArrayList<>();
        incongruents = new ArrayList<>();
        prevTime = time.get();
        clicks.subscribe(congruent -> {
            long current = time.get();
            if (congruent) {
                congruents.add((int) (current - prevTime));
            } else {
                incongruents.add((int) (current - prevTime));
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
        return "stats:\ncongruent: " + congruents.toString() + "\nincongruent: " + incongruents.toString();
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
