package com.ilt.cms.core.entity.coverage;

import com.ilt.cms.core.entity.PersistedObject;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class PolicyHolderLimit extends PersistedObject {

    @Indexed(unique = true)
    private String patientId;

    private Map<String, LimitRecorder> usage;

    public PolicyHolderLimit() {
        usage = new HashMap<>();
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Map<String, LimitRecorder> getUsage() {
        return usage;
    }

    public void setUsage(Map<String, LimitRecorder> usage) {
        this.usage = usage;
    }


    @Override
    public String toString() {
        return "PolicyHolderLimit{" +
                "patientId='" + patientId + '\'' +
                ", usage=" + usage +
                '}';
    }

    public static class CoverageUsage extends CapLimiter {
        private LocalDateTime cycleFirstTimeUsed;
        private LocalDateTime lastUsedTime;

        public CoverageUsage() {
        }

        public CoverageUsage(LocalDateTime lastUsedTime) {
            this.lastUsedTime = lastUsedTime;
            this.cycleFirstTimeUsed = lastUsedTime;
        }

        public LocalDateTime getLastUsedTime() {
            return lastUsedTime;
        }

        public void setLastUsedTime(LocalDateTime lastUsedTime) {
            this.lastUsedTime = lastUsedTime;
        }

        public LocalDateTime getCycleFirstTimeUsed() {
            return cycleFirstTimeUsed;
        }

        public void setCycleFirstTimeUsed(LocalDateTime cycleFirstTimeUsed) {
            this.cycleFirstTimeUsed = cycleFirstTimeUsed;
        }

        @Override
        public String toString() {
            return "CoverageUsage{" +
                    "cycleFirstTimeUsed=" + cycleFirstTimeUsed +
                    ", lastUsedTime=" + lastUsedTime +
                    '}';
        }
    }

    public static class LimitRecorder {
        private CoverageUsage visitUsage;
        private CoverageUsage dayUsage;
        private CoverageUsage weekUsage;
        private CoverageUsage monthUsage;
        private CoverageUsage yearUsage;
        private CoverageUsage lifeTimeUsage;

        public LimitRecorder() {
            visitUsage = new CoverageUsage(LocalDateTime.now());
            dayUsage = new CoverageUsage(LocalDateTime.now());
            dayUsage = new CoverageUsage(LocalDateTime.now());
            weekUsage = new CoverageUsage(LocalDateTime.now());
            monthUsage = new CoverageUsage(LocalDateTime.now());
            yearUsage = new CoverageUsage(LocalDateTime.now());
            lifeTimeUsage = new CoverageUsage(LocalDateTime.now());
        }

        public CoverageUsage getVisitUsage() {
            return visitUsage;
        }

        public CoverageUsage getDayUsage() {
            return dayUsage;
        }

        public CoverageUsage getWeekUsage() {
            return weekUsage;
        }

        public CoverageUsage getMonthUsage() {
            return monthUsage;
        }

        public CoverageUsage getYearUsage() {
            return yearUsage;
        }

        public CoverageUsage getLifeTimeUsage() {
            return lifeTimeUsage;
        }

        @Override
        public String toString() {
            return "LimitRecorder{" +
                    "visitUsage=" + visitUsage +
                    ", dayUsage=" + dayUsage +
                    ", weekUsage=" + weekUsage +
                    ", monthUsage=" + monthUsage +
                    ", yearUsage=" + yearUsage +
                    ", lifeTimeUsage=" + lifeTimeUsage +
                    '}';
        }
    }
}
