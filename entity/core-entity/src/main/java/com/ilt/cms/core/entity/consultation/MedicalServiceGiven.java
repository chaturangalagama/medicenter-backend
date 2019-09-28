package com.ilt.cms.core.entity.consultation;

import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.UserPaymentOption;
import com.ilt.cms.core.entity.charge.Charge;
import com.ilt.cms.core.entity.service.MedicalServiceItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MedicalServiceGiven extends PersistedObject {

    private List<MedicalService> medicalServices = new ArrayList<>();

    public List<MedicalService> getMedicalServices() {
        return medicalServices;
    }

    public void setMedicalServices(List<MedicalService> medicalServices) {
        this.medicalServices = medicalServices;
    }

    @Override
    public String toString() {
        return "MedicalServiceGiven{" +
                "medicalServices=" + medicalServices +
                '}';
    }

    public static class MedicalService {
        private String itemId;
        private String serviceItemId;
        private UserPaymentOption priceAdjustment;
        private String name;
        private Charge chargeAmount;
        //this is only used for storage purposes
        private UserPaymentOption availablePriceAdjustment;

        public MedicalService() {
        }

        public MedicalService(String itemId, String serviceItemId, UserPaymentOption priceAdjustment) {
            this.itemId = itemId;
            this.serviceItemId = serviceItemId;
            this.priceAdjustment = priceAdjustment;
        }

        public boolean compareMaxDiscountGiven(MedicalServiceItem medicalServiceItem) {
            if (priceAdjustment != null
                    && priceAdjustment.getPaymentType() != medicalServiceItem.getPriceAdjustment().getPaymentType()) {
                return false;
            } else
                return priceAdjustment == null || !(priceAdjustment.getDecreaseValue() > medicalServiceItem.getPriceAdjustment().getDecreaseValue());
        }

        public void copy(MedicalServiceItem medicalServiceItem) {
            setName(medicalServiceItem.getName());
            setChargeAmount(medicalServiceItem.getChargeAmount());
            setAvailablePriceAdjustment(medicalServiceItem.getPriceAdjustment());
        }

        public void copy(MedicalService medicalService) {
            setName(medicalService.getName());
            setChargeAmount(medicalService.getChargeAmount());
            setAvailablePriceAdjustment(medicalService.getAvailablePriceAdjustment());
        }

        public String getItemId() {
            return itemId;
        }

        public UserPaymentOption getAvailablePriceAdjustment() {
            return availablePriceAdjustment;
        }

        public void setAvailablePriceAdjustment(UserPaymentOption availablePriceAdjustment) {
            this.availablePriceAdjustment = availablePriceAdjustment;
        }

        public String getServiceItemId() {
            return serviceItemId;
        }

        public UserPaymentOption getPriceAdjustment() {
            return priceAdjustment;
        }

        public void setPriceAdjustment(UserPaymentOption priceAdjustment) {
            this.priceAdjustment = priceAdjustment;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public void setServiceItemId(String serviceItemId) {
            this.serviceItemId = serviceItemId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Charge getChargeAmount() {
            return chargeAmount;
        }

        public void setChargeAmount(Charge chargeAmount) {
            this.chargeAmount = chargeAmount;
        }

        @Override
        public String toString() {
            return "MedicalService{" +
                    "itemId='" + itemId + '\'' +
                    ", serviceItemId='" + serviceItemId + '\'' +
                    ", priceAdjustment=" + priceAdjustment +
                    ", name='" + name + '\'' +
                    ", chargeAmount=" + chargeAmount +
                    ", availablePriceAdjustment=" + availablePriceAdjustment +
                    '}';
        }
    }
}
