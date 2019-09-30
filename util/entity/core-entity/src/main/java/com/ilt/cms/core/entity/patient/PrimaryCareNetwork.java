package com.ilt.cms.core.entity.patient;

import java.time.LocalDate;

public class PrimaryCareNetwork {
    private boolean optIn;
    private boolean optOut;
    private LocalDate optInDate;

    public PrimaryCareNetwork() {
    }

    public PrimaryCareNetwork(boolean optIn, boolean optOut, LocalDate optInDate) {
        this.optIn = optIn;
        this.optOut = optOut;
        this.optInDate = optInDate;
    }

    public boolean areParametersValid() {
        if (optIn) {
            return !optOut;
        }
        return true;
    }

    public boolean isOptIn() {
        return optIn;
    }

    public boolean isOptOut() {
        return optOut;
    }

    public LocalDate getOptInDate() {
        return optInDate;
    }

    @Override
    public String toString() {
        return "PrimaryCareNetwork{" +
                "optIn=" + optIn +
                ", optOut=" + optOut +
                ", optInDate=" + optInDate +
                '}';
    }
}
