package com.nitsan.strooptest;

class SystemTime {
    static TimeSource get() {
        return System::currentTimeMillis;
    }
}
