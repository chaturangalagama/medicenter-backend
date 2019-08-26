package com.ilt.cms.api.entity.charge;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * <code>{@link ClaimableInvoiceView}</code> -
 * API entity for Claimable Invoices.
 * </p>
 *
 * @author prabath.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ClaimableInvoiceView extends InvoiceView {

    private String visitId;

    // TODO - We need to map Claim later

}
