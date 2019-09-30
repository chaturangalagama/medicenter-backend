package com.ilt.cms.core.entity.item;

import java.util.List;

public class VaccinationItem extends DrugItem {

    private List<Dosage> dosages;


    public VaccinationItem() {
        setInventoried(true);
        setItemType(ItemType.VACCINATION);
    }

    public static class Dosage {
        private String name;
        private String description;
        private String recommendedAge;
        private int intervalToNextDoseInDays;

        public Dosage() {
        }

        public Dosage(String name, String description, String recommendedAge, int intervalToNextDoseInDays) {
            this.name = name;
            this.description = description;
            this.recommendedAge = recommendedAge;
            this.intervalToNextDoseInDays = intervalToNextDoseInDays;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getRecommendedAge() {
            return recommendedAge;
        }

        public int getIntervalToNextDoseInDays() {
            return intervalToNextDoseInDays;
        }

        @Override
        public String toString() {
            return "Dosage{" +
                    "name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", recommendedAge='" + recommendedAge + '\'' +
                    ", intervalToNextDoseInDays=" + intervalToNextDoseInDays +
                    '}';
        }
    }

    public List<Dosage> getDosages() {
        return dosages;
    }

    public void setDosages(List<Dosage> dosages) {
        this.dosages = dosages;
    }
}
