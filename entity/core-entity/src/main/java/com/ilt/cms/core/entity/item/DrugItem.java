package com.ilt.cms.core.entity.item;

public class DrugItem extends Item {

    private String dosageUom;

    private String brandName;
    private String brandNameJapanese;
    private String brandNameChinese;
    private String genericName;
    private String genericNameJapanese;
    private String genericNameChinese;
    private boolean printDrugLabel;
    private String remarks;
    private String mimsClassification;
    private String drugType;
    private String drugBrandedType;
    private String modeOfAdministration;
    private int dosageQty;
    private String frequency;
    private String frequencyCode;
    private String dosageInstructionCode;
    private String precautionaryInstructions;
    private String indications;
    private boolean controlledDrug;
    private int dispensingMultiples;


    public DrugItem() {
        super(true, ItemType.DRUG);
    }

    public String getDosageUom() {
        return dosageUom;
    }

    public void setDosageUom(String dosageUom) {
        this.dosageUom = dosageUom;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandNameJapanese() {
        return brandNameJapanese;
    }

    public void setBrandNameJapanese(String brandNameJapanese) {
        this.brandNameJapanese = brandNameJapanese;
    }

    public String getBrandNameChinese() {
        return brandNameChinese;
    }

    public void setBrandNameChinese(String brandNameChinese) {
        this.brandNameChinese = brandNameChinese;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getGenericNameJapanese() {
        return genericNameJapanese;
    }

    public void setGenericNameJapanese(String genericNameJapanese) {
        this.genericNameJapanese = genericNameJapanese;
    }

    public String getGenericNameChinese() {
        return genericNameChinese;
    }

    public void setGenericNameChinese(String genericNameChinese) {
        this.genericNameChinese = genericNameChinese;
    }

    public boolean isPrintDrugLabel() {
        return printDrugLabel;
    }

    public void setPrintDrugLabel(boolean printDrugLabel) {
        this.printDrugLabel = printDrugLabel;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getMimsClassification() {
        return mimsClassification;
    }

    public void setMimsClassification(String mimsClassification) {
        this.mimsClassification = mimsClassification;
    }

    public String getDrugType() {
        return drugType;
    }

    public void setDrugType(String drugType) {
        this.drugType = drugType;
    }

    public String getDrugBrandedType() {
        return drugBrandedType;
    }

    public void setDrugBrandedType(String drugBrandedType) {
        this.drugBrandedType = drugBrandedType;
    }

    public String getModeOfAdministration() {
        return modeOfAdministration;
    }

    public void setModeOfAdministration(String modeOfAdministration) {
        this.modeOfAdministration = modeOfAdministration;
    }

    public int getDosageQty() {
        return dosageQty;
    }

    public void setDosageQty(int dosageQty) {
        this.dosageQty = dosageQty;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getFrequencyCode() {
        return frequencyCode;
    }

    public void setFrequencyCode(String frequencyCode) {
        this.frequencyCode = frequencyCode;
    }

    public String getPrecautionaryInstructions() {
        return precautionaryInstructions;
    }

    public void setPrecautionaryInstructions(String precautionaryInstructions) {
        this.precautionaryInstructions = precautionaryInstructions;
    }

    public String getIndications() {
        return indications;
    }

    public void setIndications(String indications) {
        this.indications = indications;
    }

    public boolean isControlledDrug() {
        return controlledDrug;
    }

    public void setControlledDrug(boolean controlledDrug) {
        this.controlledDrug = controlledDrug;
    }

    public int getDispensingMultiples() {
        return dispensingMultiples;
    }

    public void setDispensingMultiples(int dispensingMultiples) {
        this.dispensingMultiples = dispensingMultiples;
    }

    public String getDosageInstructionCode() {
        return dosageInstructionCode;
    }

    public void setDosageInstructionCode(String dosageInstructionCode) {
        this.dosageInstructionCode = dosageInstructionCode;
    }

    @Override
    public String toString() {
        return "DrugItem{" +
                "dosageUom='" + dosageUom + '\'' +
                ", brandName='" + brandName + '\'' +
                ", brandNameJapanese='" + brandNameJapanese + '\'' +
                ", brandNameChinese='" + brandNameChinese + '\'' +
                ", genericName='" + genericName + '\'' +
                ", genericNameJapanese='" + genericNameJapanese + '\'' +
                ", genericNameChinese='" + genericNameChinese + '\'' +
                ", printDrugLabel=" + printDrugLabel +
                ", remarks='" + remarks + '\'' +
                ", mimsClassification='" + mimsClassification + '\'' +
                ", drugType='" + drugType + '\'' +
                ", drugBrandedType='" + drugBrandedType + '\'' +
                ", modeOfAdministration='" + modeOfAdministration + '\'' +
                ", dosageQty=" + dosageQty +
                ", frequency='" + frequency + '\'' +
                ", frequencyCode='" + frequencyCode + '\'' +
                ", dosageInstructionCode='" + dosageInstructionCode + '\'' +
                ", precautionaryInstructions='" + precautionaryInstructions + '\'' +
                ", indications='" + indications + '\'' +
                ", controlledDrug=" + controlledDrug +
                ", dispensingMultiples=" + dispensingMultiples +
                '}';
    }
}
