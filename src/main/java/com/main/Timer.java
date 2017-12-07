package com.main;

public abstract class Timer {
    private static Long startTime = 0L;
    private static Long endTime = 0L;

    public static void startTimer() {
        startTime = System.currentTimeMillis();
    }

    public static void stopTimer() {
        endTime = System.currentTimeMillis();
        Long timeTaken = endTime - startTime;
        System.out.println("Total Time Taken: " + timeTaken/1000 + " secs.");
    }
}
