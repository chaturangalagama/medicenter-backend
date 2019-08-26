package com.ilt.cms.api.entity.supplier;

import com.ilt.cms.api.entity.common.ContactPerson;
import com.ilt.cms.api.entity.common.CorporateAddress;
import com.ilt.cms.api.entity.common.Status;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SupplierEntity {

    private String id;
    private String name;
    private CorporateAddress address;
    private List<ContactPerson> contacts;
    private Status status;
}
