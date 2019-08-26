package com.ilt.cms.core.entity.claim;

import com.lippo.commons.util.StatusCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClaimsBalance {
    private StatusCode balanceCheckStatusCode;
    private int availableBalance;

    public ClaimsBalance() {
    }

    public ClaimsBalance(int availableBalance) {
        this.balanceCheckStatusCode = StatusCode.S0000;
        this.availableBalance = availableBalance;
    }

    public ClaimsBalance(StatusCode balanceCheckStatus, int availableBalance) {
        this.balanceCheckStatusCode = balanceCheckStatus;
        this.availableBalance = availableBalance;
    }
}