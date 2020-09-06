package com.andrew121410.mc.world16firealarms;

public class FireAlarmReason {

    private TroubleReason troubleReason;
    private String reason;

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