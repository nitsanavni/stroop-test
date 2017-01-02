package com.nitsan.strooptest;

import java.util.Random;

class RandomColor {
    private static final Color[] colors = new Color[]{Black.get(),
            Blue.get(),
            Red.get(),
            Green.get(),
            Yellow.get()};
    private final Random random;

    RandomColor(Random random) {
        this.random = random;
    }

    Color next() {
        return colors[random.nextInt(Color.NUM_OF_COLORS)];
    }

}
