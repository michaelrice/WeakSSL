package com.blogspot.hartsock.ssl.weak;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import org.apache.log4j.Logger;

/**
 * @author Shawn Hartsock
 */
public class WeakHostnameVerifier implements HostnameVerifier {
    public String[] exemptNames = {"localhost"};

    private static Logger log = Logger.getLogger(WeakHostnameVerifier.class);
    /**
     * @param hostname
     * @param session
     * @return
     */
    public boolean verify(String hostname, SSLSession session) {
        log.trace("Verify hostname: " + hostname);
        boolean exempt = false;
        if (exemptNames != null) {
            for (String name : exemptNames) {
                log.trace("Checking name: " + name);
                if (hostname.toLowerCase().endsWith(name)) {
                    log.trace("Name exempt: " + name);
                    exempt = true;
                }
            }
        }
        return exempt;
    }

    /**
     * Injects the HttpsURLConnection with the right verifier...
     */
    public static void init() {
        HttpsURLConnection.setDefaultHostnameVerifier(new WeakHostnameVerifier());
    }

    /**
     * Initialize the HostnameVerifier using the list of exempt host names in System properties.
     *
     * @param propertyName
     */
    public static void init(String propertyName) throws Exception {
        log.trace("Fetching property: " + propertyName);
        try {
            String names = System.getProperty(propertyName);
            if (names != null) {
                init(names.split(","));
            } else {
                log.debug("The system property " + propertyName + " was not set");
                init();
            }
        } catch (NullPointerException npe) {
            log.error("NullPointerException Caught trying to access: " + propertyName);
            throw new Exception("System.getProperty('"
                    + propertyName +
                    "') returned '" + System.getProperty(propertyName) + "'");
        }
    }

    /**
     * Initializes the hostname verifier with your list of arbitrary names that are
     * exempt from SSL certificate double checking.
     *
     * @param names
     */
    public static void init(String[] names) {
        for (String name: names) {
            log.trace("Name: " + name);
        }
        WeakHostnameVerifier verifier = new WeakHostnameVerifier();
        verifier.exemptNames = names;
        HttpsURLConnection.setDefaultHostnameVerifier(verifier);
    }
}
