package com.ilt.cms.pm.integration.mapper;

import com.ilt.cms.api.entity.billPayment.CopayAmount;
import com.ilt.cms.api.entity.charge.ChargeEntity;
import com.ilt.cms.core.entity.UserId;
import com.ilt.cms.core.entity.UserPaymentOption;
import com.ilt.cms.core.entity.charge.Charge;
import com.ilt.cms.core.entity.price.master.OverrideDefaultCharge;
import com.ilt.cms.core.entity.common.ContactPerson;
import com.ilt.cms.core.entity.common.CorporateAddress;
import com.ilt.cms.core.entity.common.Relationship;


import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Mapper {


    public static Charge mapToChargeCore(ChargeEntity chargeEntity){
        if(chargeEntity == null){
            return null;
        }
        Charge charge = new Charge();
        charge.setPrice(chargeEntity.getPrice());
        charge.setTaxIncluded(chargeEntity.isTaxIncluded());
        return charge;
    }

    public static ChargeEntity mapToChargeEntity(Charge charge){
        if(charge == null){
            return null;
        }
        ChargeEntity chargeEntity = new ChargeEntity();
        chargeEntity.setPrice(charge.getPrice());
        chargeEntity.setTaxIncluded(charge.isTaxIncluded());
        return chargeEntity;
    }

    public static UserPaymentOption mapToUserPaymentOtpionCore(com.ilt.cms.api.entity.common.UserPaymentOption userPaymentOptionEntity){
        if(userPaymentOptionEntity == null){
            return null;
        }
        UserPaymentOption userPaymentOption = new UserPaymentOption(userPaymentOptionEntity.getDecreaseValue(),
                userPaymentOptionEntity.getIncreaseValue(),
                UserPaymentOption.PaymentType.valueOf(userPaymentOptionEntity.getPaymentType().name()),
                userPaymentOptionEntity.getRemark());
        return userPaymentOption;

    }

    public static com.ilt.cms.api.entity.common.UserPaymentOption mapToUserPaymentOtpionEntity(UserPaymentOption userPaymentOption){
        if(userPaymentOption == null){
            return null;
        }
        com.ilt.cms.api.entity.common.UserPaymentOption  userPaymentOptionEntity = new com.ilt.cms.api.entity.common.UserPaymentOption (userPaymentOption.getDecreaseValue(),
                userPaymentOption.getIncreaseValue(),
                com.ilt.cms.api.entity.common.UserPaymentOption.PaymentType.valueOf(userPaymentOption.getPaymentType().name()),
                userPaymentOption.getRemark());
        return userPaymentOptionEntity;

    }

    public static com.ilt.cms.api.entity.common.CorporateAddress mapToCorporateAddressEntity(CorporateAddress corporateAddress){
        if(corporateAddress == null){
            return null;
        }
        com.ilt.cms.api.entity.common.CorporateAddress corporateAddress1 = new com.ilt.cms.api.entity.common.CorporateAddress();
        corporateAddress1.setAttentionTo(corporateAddress.getAttentionTo());
        corporateAddress1.setAddress(corporateAddress.getAddress());
        corporateAddress1.setPostalCode(corporateAddress.getPostalCode());
        return corporateAddress1;
    }

    public static CorporateAddress mapToCorporateAddressCore(com.ilt.cms.api.entity.common.CorporateAddress corporateAddress){
        if(corporateAddress == null){
            return null;
        }
        CorporateAddress corporateAddress1 = new CorporateAddress(corporateAddress.getAttentionTo(), corporateAddress.getAddress(), corporateAddress.getPostalCode());
        return corporateAddress1;
    }

    public static ContactPerson mapToContactPersonCore(com.ilt.cms.api.entity.common.ContactPerson contactPersonEntity){
        if(contactPersonEntity == null){
            return null;
        }
        return new ContactPerson(contactPersonEntity.getName(),
                contactPersonEntity.getTitle(),
                contactPersonEntity.getDirectNumber(),
                contactPersonEntity.getMobileNumber(),
                contactPersonEntity.getFaxNumber(),
                contactPersonEntity.getEmail());
    }

    public static com.ilt.cms.api.entity.common.ContactPerson mapToContactPersonEntity(ContactPerson contactPerson){
        if(contactPerson == null){
            return null;
        }
        return new com.ilt.cms.api.entity.common.ContactPerson(contactPerson.getName(),
                contactPerson.getTitle(),
                contactPerson.getDirectNumber(),
                contactPerson.getMobileNumber(),
                contactPerson.getFaxNumber(),
                contactPerson.getEmail());
    }

    public static CopayAmount mapToCopayAmountEntity(com.ilt.cms.core.entity.CopayAmount copayAmount){
        if(copayAmount == null){
            return null;
        }
        CopayAmount copayAmountEntity = new CopayAmount(copayAmount.getValue(), CopayAmount.PaymentType.valueOf(copayAmount.getPaymentType().name()));
        return copayAmountEntity;
    }

    public static com.ilt.cms.core.entity.CopayAmount mapToCopayAmountCore(CopayAmount copayAmountEntity){
        if(copayAmountEntity == null){
            return null;
        }
        com.ilt.cms.core.entity.CopayAmount copayAmount = new com.ilt.cms.core.entity.CopayAmount(copayAmountEntity.getValue(), com.ilt.cms.core.entity.CopayAmount.PaymentType.valueOf(copayAmountEntity.getPaymentType().name()));
        return copayAmount;
    }

    public static UserId mapToUserIdCore(com.ilt.cms.api.entity.common.UserId userIdEntity){
        if(userIdEntity == null){
            return null;
        }
        if(userIdEntity.getIdType() != null) {
            return new UserId(UserId.IdType.valueOf(userIdEntity.getIdType().name()), userIdEntity.getNumber());
        }
        return null;
    }

    public static com.ilt.cms.api.entity.common.UserId mapToUserIdEntity(UserId userId){
        if(userId == null){
            return null;
        }
        if(userId.getIdType() != null) {
            return new com.ilt.cms.api.entity.common.UserId(com.ilt.cms.api.entity.common.UserId.IdType.valueOf(userId.getIdType().name()), userId.getNumber());
        }
        return null;
    }

    private static Function<com.ilt.cms.api.entity.common.Relationship, Relationship> mapToRelationshipCore(){
        return relationshipEntity -> Relationship.valueOf(relationshipEntity.name());
    }

    private static Function<Relationship, com.ilt.cms.api.entity.common.Relationship> mapToRelationshipEntity(){
        return relationship -> com.ilt.cms.api.entity.common.Relationship.valueOf(relationship.name());
    }
}
