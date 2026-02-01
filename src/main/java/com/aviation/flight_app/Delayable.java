package com.aviation.flight_app;

public interface Delayable {
    public void reportDelay(int minutes);

    public default boolean isCriticalDelay() {
            return false;
        
    }
}
