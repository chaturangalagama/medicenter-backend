package com.lippo.cms.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SnmpLogUtil {
    private static Logger snmpLogger = LoggerFactory.getLogger("snmp-logger");
    private static Logger logger = LoggerFactory.getLogger(SnmpLogUtil.class);

    private static Map<String, String> previousLogs = new ConcurrentHashMap<>();
    protected static final String FAILED = "FAILED";
    protected static final String SUCCESS = "SUCCESS";

    private static ExecutorService service = Executors.newSingleThreadExecutor();

    private static HashMap<String, Long> keyAndTimestampMap = new HashMap<>();

    public static void trap(final String key, final String message, final Object... args) {
        boolean sendTrap = isRequiredToSendTrap(key);
        if (sendTrap) {
            logger.info("Send the trap for key {}", key);
            logMessage(message, args);
            previousLogs.put(key, FAILED);
        } else {
            logger.trace("Will not send the trap for key {} since it is already sent, {}", key, previousLogs);
        }
    }

    public static void timeTrap(final String key, final int timeInSeconds, final String message, final Object ...args) {
        boolean sendTrap = isRequiredToSendTrap(key);
        if (sendTrap) {
            logger.info("Send the trap for key {}", key);
            timeLog(key, timeInSeconds, message, args);
            previousLogs.put(key, FAILED);
        } else {
            logger.trace("Will not send the trap for key {} since it is already sent, {}", key, previousLogs);
        }
    }

    protected static boolean isRequiredToSendTrap(String key) {
        return previousLogs.get(key) == null || previousLogs.get(key).equals(SUCCESS);
    }

    public static void clearTrap(final String key, final String message, final Object... args) {
        final boolean sendClearTrap = isRequiredToSendClearTrap(key);
        if (sendClearTrap) {
            logger.info("Send the clear trap for key {}", key);
            logMessage(message, args);
            previousLogs.put(key, SUCCESS);
        } else {
            logger.trace("Will not send the clear trap for key {}, {}", key, previousLogs);
        }
    }

    static boolean  isRequiredToSendClearTrap(String key) {
        return previousLogs.get(key) == null || previousLogs.get(key).equals(FAILED);
    }

    static void transferState(HashMap<String, String> currentState) {
        SnmpLogUtil.previousLogs = currentState;
    }

    public static void log(String message, Object... args) {
        logMessage(message, args);
    }

    public static void timeLog(final String key, final int seconds, final String message, final Object... args) {
        if(keyAndTimestampMap.containsKey(key)) {

            long timeDiff = System.currentTimeMillis() - (keyAndTimestampMap.get(key));

            if(timeDiff >= seconds * 1000) {
                logMessage(message, args);
                keyAndTimestampMap.put(key, System.currentTimeMillis());
            }
        } else {
            keyAndTimestampMap.put(key, System.currentTimeMillis());
            logMessage(message, args);
        }
    }

    private static void logMessage(final String message, final Object ... args) {
        service.execute(() -> snmpLogger.info(message, args));
    }

    protected static void setSnmpLogger (Logger logger) {
        snmpLogger = logger;
    }
}
