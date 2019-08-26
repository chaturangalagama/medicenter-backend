package com.ilt.cms.report.ui.report;

import com.ibm.icu.util.ULocale;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.IDesignEngine;
import org.eclipse.birt.report.model.api.IDesignEngineFactory;
import org.eclipse.birt.report.model.api.SessionHandle;

public class BirtReportConfiguration {

    private static final Logger logger = LogManager.getLogger(BirtReportConfiguration.class);

    private SessionHandle session;
    private String mySqlUsername;
    private String mySqlPassword;
    private String mySqlUrl;
    private String mySqlDriver;
    private String reportLocation;
    private String reportCssLocation;
    private String reportResourceLocation;
    private int noOfRowsPerPage;


    public SessionHandle createReportSession() {
        // Create a session handle. This is used to manage all open designs. Your app need create the session only once.
        //Configure the Engine and start the Platform
        DesignConfig config = new DesignConfig();

        IDesignEngine engine;
        try {
            Platform.startup(config);
            IDesignEngineFactory factory = (IDesignEngineFactory) Platform.createFactoryObject(
                    IDesignEngineFactory.EXTENSION_DESIGN_ENGINE_FACTORY);
            engine = factory.createDesignEngine(config);
            session = engine.newSessionHandle(ULocale.ENGLISH);
        } catch (BirtException e1) {
            logger.error("Birt Internal error occurred {} ", e1);
        }

        return session;
    }

    public String getMySqlUsername() {
        return mySqlUsername;
    }

    public void setMySqlUsername(String mySqlUsername) {
        this.mySqlUsername = mySqlUsername;
    }

    public String getMySqlPassword() {
        return mySqlPassword;
    }

    public void setMySqlPassword(String mySqlPassword) {
        this.mySqlPassword = mySqlPassword;
    }

    public String getMySqlUrl() {
        return mySqlUrl;
    }

    public void setMySqlUrl(String mySqlUrl) {
        this.mySqlUrl = mySqlUrl;
    }

    public String getMySqlDriver() {
        return mySqlDriver;
    }

    public void setMySqlDriver(String mySqlDriver) {
        this.mySqlDriver = mySqlDriver;
    }

    public String getReportLocation() {
        return getClass().getResource("/").getPath().replace("WEB-INF/classes/", "") + "/" + reportLocation;
    }

    public void setReportLocation(String reportLocation) {
        this.reportLocation = reportLocation;
    }

    public String getReportCssLocation() {
        return getClass().getResource("/").getPath().replace("WEB-INF/classes/", "") + "/" + reportCssLocation;
    }

    public void setReportCssLocation(String reportCssLocation) {
        this.reportCssLocation = reportCssLocation;
    }


    public String getReportResourceLocation() {
        return getClass().getResource("/").getPath().replace("WEB-INF/classes/", "") + "/" + reportResourceLocation;
    }

    public void setReportResourceLocation(String reportResourceLocation) {
        this.reportResourceLocation = reportResourceLocation;
    }

    public void setNoOfRowsPerPage(int noOfRowsPerPage) {
        this.noOfRowsPerPage = noOfRowsPerPage;
    }

    public int getNoOfRowsPerPage() {
        return noOfRowsPerPage;
    }

}