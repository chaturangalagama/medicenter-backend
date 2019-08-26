package com.ilt.cms.pm.business.service.claim;

//import com.lippo.connector.mhcp.MHCPManager;
//import com.lippo.connector.mhcp.util.ConfigHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadClaimManagerConf {

    private static final Logger logger = LoggerFactory.getLogger(LoadClaimManagerConf.class);


    @Value("${claim.wsdl.balanceUrl}")
    private String claimsBalanceWsdlUrl;
    @Value("${claim.wsdl.submissionUrl}")
    private String claimsSubmissionWsdlUrl;

//    @Bean
//    public MHCPManager mhcpManager() {
//        try {
//            return new MHCPManager(new ConfigHelper(claimsBalanceWsdlUrl, claimsSubmissionWsdlUrl));
//        } catch (Throwable e) {
//            logger.error("Error loading the MHCP Manager : ", e.getMessage());
//            return new MHCPManager(null);
//        }
//    }
}