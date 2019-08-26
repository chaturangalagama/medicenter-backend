package com.ilt.cms.core.entity.vaccination;

import com.ilt.cms.core.entity.PersistedObject;
import com.lippo.commons.util.CommonUtils;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;

@CompoundIndexes({
        @CompoundIndex(name = "index_unique", unique = true, def = "{'name' : 1}"),
        @CompoundIndex(name = "index_dose_id", def = "{'name' : 1, 'doses.doseId' : 1}"),
        @CompoundIndex(name = "index_dose_name", def = "{'name' : 1, 'doses.name' : 1}")
})
public class Vaccination extends PersistedObject {

    //@Indexed(unique = true)
    private String name;
    @Indexed(unique = true)
    private String code;
    /**
     * If this is 0 it should be taken at birth, zero is no validation
     */
    private int ageInMonths;
    private List<Dose> doses;

    public boolean areParametersValid() {
        return CommonUtils.isStringValid(name) && doses != null && doses
                .stream().allMatch(dose -> dose.areParametersValid());
    }

    public Dose findDose(String itemId) {
        for (Dose dose : doses) {
            if (dose.getDoseId().equals(itemId)) {
                return dose;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getAgeInMonths() {
        return ageInMonths;
    }

    public void setAgeInMonths(int ageInMonths) {
        this.ageInMonths = ageInMonths;
    }

    public List<Dose> getDoses() {
        return doses;
    }

    public void setDoses(List<Dose> doses) {
        this.doses = doses;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Vaccination{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", ageInMonths=" + ageInMonths +
                ", doses=" + doses +
                '}';
    }
}
