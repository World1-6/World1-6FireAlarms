package com.andrew121410.mc.world16firealarms;

import java.util.Optional;

public class FireAlarmReason {

    private TroubleReason troubleReason;

    private Optional<String> optionalPullStationName = Optional.empty();

    public FireAlarmReason(TroubleReason troubleReason) {
        this.troubleReason = troubleReason;
    }

    //Getter's and Setters
    public TroubleReason getTroubleReason() {
        return troubleReason;
    }

    public void setTroubleReason(TroubleReason troubleReason) {
        this.troubleReason = troubleReason;
    }

    public Optional<String> getOptionalPullStationName() {
        return optionalPullStationName;
    }

    public void setOptionalPullStationName(Optional<String> optionalPullStationName) {
        this.optionalPullStationName = optionalPullStationName;
    }
}