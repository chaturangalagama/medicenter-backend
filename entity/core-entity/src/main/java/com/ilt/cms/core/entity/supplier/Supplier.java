package com.ilt.cms.core.entity.supplier;

import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.common.ContactPerson;
import com.ilt.cms.core.entity.common.CorporateAddress;

import java.util.List;

public class Supplier extends PersistedObject {

    private String name;
    private CorporateAddress address;
    private List<ContactPerson> contacts;
    private Status status;

    public Supplier() {
    }

    public Supplier(String name, CorporateAddress address, List<ContactPerson> contacts, Status status) {
        this.name = name;
        this.address = address;
        this.contacts = contacts;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CorporateAddress getAddress() {
        return address;
    }

    public void setAddress(CorporateAddress address) {
        this.address = address;
    }

    public List<ContactPerson> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactPerson> contacts) {
        this.contacts = contacts;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "name='" + name + '\'' +
                ", address=" + address +
                ", contacts=" + contacts +
                ", status=" + status +
                '}';
    }
}
