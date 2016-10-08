package com.hfad.james;

/**
 * Created by heleneshaikh on 07/10/2016.
 */
public class IABEvent {
    private final boolean b;

    public IABEvent(boolean b) {
        this.b = b;
    }

    public boolean isConsumptionOK() {
        return b;
    }
}
