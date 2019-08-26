package business.mock;

import com.ilt.cms.core.entity.allergy.AllergyGroup;

import java.util.Arrays;

public class MockAllergyGroup {
    public static AllergyGroup mockAllergyGroup(){
        AllergyGroup allergyGroup = new AllergyGroup();
        allergyGroup.setGroupCode("DG002");
        allergyGroup.setDescription("Drug group description");
        allergyGroup.setDrugIds(Arrays.asList("DR001", "DR002"));
        return allergyGroup;
    }
}
