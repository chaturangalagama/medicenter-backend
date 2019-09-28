package com.ilt.cms.core.entity.consultation;

import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.UserPaymentOption;
import com.ilt.cms.core.entity.charge.Charge;
import com.ilt.cms.core.entity.vaccination.Dose;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ImmunisationGiven extends PersistedObject {

    private List<Immunisation> immunisation = new ArrayList<>();

    public List<Immunisation> getImmunisation() {
        return immunisation;
    }

    public void setImmunisation(List<Immunisation> immunisation) {
        this.immunisation = immunisation;
    }

    @Override
    public String toString() {
        return "ImmunisationGiven{" +
                "immunisation=" + immunisation +
                '}';
    }

    public static class Immunisation {

        private LocalDate immunisationDate;
        private String batchNumber;
        private String branch;
        private LocalDate nextDose;


        private UserPaymentOption priceAdjustment;
        private String itemId;
        private String doseId;
        private Charge chargeAmount;
        //this is only used for storage purposes
        private UserPaymentOption availablePriceAdjustment;


        public Immunisation() {
        }

        public Immunisation newCopy() {
            Immunisation immunisation = new Immunisation();
            immunisation.setBatchNumber(getBatchNumber());
            immunisation.setBranch(getBranch());
            immunisation.setNextDose(getNextDose());
            immunisation.setImmunisationDate(getImmunisationDate());
            immunisation.setPriceAdjustment(getPriceAdjustment());
            immunisation.setDoseId(getDoseId());
            immunisation.setChargeAmount(getChargeAmount());
            immunisation.setAvailablePriceAdjustment(getAvailablePriceAdjustment());
            immunisation.setItemId(getItemId());
            return immunisation;
        }

        public void copy(Dose dose) {
            setDoseId(dose.getDoseId());
            setChargeAmount(dose.getPrice());
            setAvailablePriceAdjustment(dose.getPriceAdjustment());
        }


        public boolean compareMaxDiscountGiven(Dose dose) {
            if (priceAdjustment != null
                    && priceAdjustment.getPaymentType() != dose.getPriceAdjustment().getPaymentType()) {
                return false;
            } else
                return priceAdjustment == null || !(priceAdjustment.getDecreaseValue() > dose.getPriceAdjustment().getDecreaseValue());
        }


        public Immunisation(UserPaymentOption priceAdjustment) {
            this.priceAdjustment = priceAdjustment;
        }

        public void setBatchNumber(String batchNumber) {
            this.batchNumber = batchNumber;
        }

        public void setBranch(String branch) {
            this.branch = branch;
        }

        public void setNextDose(LocalDate nextDose) {
            this.nextDose = nextDose;
        }

        public void setImmunisationDate(LocalDate immunisationDate) {
            this.immunisationDate = immunisationDate;
        }

        public LocalDate getImmunisationDate() {
            return immunisationDate;
        }

        public String getBatchNumber() {
            return batchNumber;
        }

        public String getBranch() {
            return branch;
        }

        public LocalDate getNextDose() {
            return nextDose;
        }

        public UserPaymentOption getPriceAdjustment() {
            return priceAdjustment;
        }

        public void setPriceAdjustment(UserPaymentOption priceAdjustment) {
            this.priceAdjustment = priceAdjustment;
        }

        public String getDoseId() {
            return doseId;
        }

        public void setDoseId(String doseId) {
            this.doseId = doseId;
        }

        public Charge getChargeAmount() {
            return chargeAmount;
        }

        public void setChargeAmount(Charge chargeAmount) {
            this.chargeAmount = chargeAmount;
        }

        public UserPaymentOption getAvailablePriceAdjustment() {
            return availablePriceAdjustment;
        }

        public void setAvailablePriceAdjustment(UserPaymentOption availablePriceAdjustment) {
            this.availablePriceAdjustment = availablePriceAdjustment;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        @Override
        public String toString() {
            return "Immunisation{" +
                    "immunisationDate=" + immunisationDate +
                    ", batchNumber='" + batchNumber + '\'' +
                    ", branch='" + branch + '\'' +
                    ", nextDose=" + nextDose +
                    ", priceAdjustment=" + priceAdjustment +
                    ", itemId='" + itemId + '\'' +
                    ", doseId='" + doseId + '\'' +
                    ", chargeAmount=" + chargeAmount +
                    ", availablePriceAdjustment=" + availablePriceAdjustment +
                    '}';
        }
    }
}
