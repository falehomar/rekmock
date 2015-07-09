package com.rekmock.profile;
public aspect Profiler {
    pointcut profile() :execution(public * com.telus.ucss.wirelesssales..*(..));


    private static ThreadLocal<Timer> timers = new ThreadLocal<Timer>(){
        @Override
        protected Timer initialValue() {
            return new Timer();
        }
    };
    Object around ():profile(){
        final Timer timer = timers.get();
        timer.start();
        try {
            return proceed();
        } finally {
            timer.stop();
        }
    }
}