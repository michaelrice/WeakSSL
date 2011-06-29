package com.blogspot.hartsock.ssl.weak;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 *
 * @author Shawn Hartsock
 */
public class WeakHostnameVerifier implements HostnameVerifier {
    public String[] exemptNames = {"localhost"};
    /**
     * @param hostname
     * @param session
     * @return
     */
    public boolean verify(String hostname, SSLSession session) {
        boolean exempt = false;
        if(exemptNames != null) {
            for(String name:exemptNames) {
                if(hostname.toLowerCase().endsWith(name)) {
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
     * @param propertyName
     */
    public static void init(String propertyName) throws Exception {
        try {
            String names = System.getProperty(propertyName);
            if(names != null) {
                init(names.split(","));
            }
            else {
                System.out.println("The system property " + propertyName + " was not set");
                init();
            }
        } catch (NullPointerException npe) {
            throw new Exception("System.getProperty('"
                    + propertyName +
                    "') returned '" + System.getProperty(propertyName) + "'");
        }
    }

    /**
     * Initializes the hostname verifier with your list of arbitrary names that are
     * exempt from SSL certificate double checking.
     * @param names
     */
    public static void init(String[] names) {
        WeakHostnameVerifier verifier = new WeakHostnameVerifier();
        verifier.exemptNames = names;
        HttpsURLConnection.setDefaultHostnameVerifier(verifier);
    }
}
