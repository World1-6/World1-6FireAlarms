package com.andrew121410.mc.world16firealarms;

public class FireAlarmReason {

    private final TroubleReason troubleReason;
    private final String reason;

    public FireAlarmReason(TroubleReason troubleReason, String reason) {
        this.troubleReason = troubleReason;
        this.reason = reason;
    }

    public TroubleReason getTroubleReason() {
        return troubleReason;
    }

    public String getReason() {
        return reason;
    }
}