package com.ilt.cms.pm.integration.mapper.clinic.billing;

import com.googlecode.jmapper.JMapper;
import com.googlecode.jmapper.api.JMapperAPI;
import com.googlecode.jmapper.api.enums.MappingType;
import com.ilt.cms.api.entity.charge.InvoiceView;
import com.ilt.cms.core.entity.sales.Invoice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static com.googlecode.jmapper.api.JMapperAPI.*;

@Service("invoiceMapperService")
public class InvoiceMapper {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceMapper.class);

    private JMapper<InvoiceView, Invoice> coreToApiMapper;

    @PostConstruct
    public void init(){

        JMapperAPI coreToApiMapperConfiguration = new JMapperAPI()
                .add(mappedClass(InvoiceView.class)
                        .add(attribute("invoiceId").value("invoiceNumber"))
                        .add(attribute("paymentInfos").value("paymentInfos"))
                        .add(attribute("invoiceState").value("invoiceState"))
                        .add(attribute("invoiceTime").value("invoiceTime"))
                        .add(attribute("paidTime").value("paidTime"))
                        .add(attribute("invoiceType").value("invoiceType"))
                        .add(attribute("payableAmount").value("payableAmount"))
                        .add(attribute("paidAmount").value("paidAmount"))
                        .add(attribute("includedTaxAmount").value("taxAmount"))
                        .add(attribute("planId").value("planId"))
                )
                .add(mappedClass(InvoiceMapper.class).add(global().targetClasses(InvoiceMapper.class)));

        this.coreToApiMapper = new JMapper<>(InvoiceView.class, Invoice.class, coreToApiMapperConfiguration);
    }

    public InvoiceView mapToApiEntity(Invoice invoice){
        logger.debug("Mapping invoice [{}] to InvoiceView", invoice);
        return this.coreToApiMapper.getDestination(invoice, MappingType.ALL_FIELDS);
    }
}
