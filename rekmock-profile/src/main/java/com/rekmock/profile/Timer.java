package com.rekmock.profile;

/**
 * Created by x153520 on 03/05/2015.
 */
public class Timer {

    private long start;
    private long duration;


    public void start() {
        this.start = System.nanoTime();
    }

    public void stop() {
        duration = System.nanoTime() - start;

    }

}
