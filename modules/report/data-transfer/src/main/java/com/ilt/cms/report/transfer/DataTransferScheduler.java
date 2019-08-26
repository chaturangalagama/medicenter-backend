package com.ilt.cms.report.transfer;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DataTransferScheduler {


    @Scheduled(cron = "0 5 * * * *")
    public void taskChecker() {
    }

}
