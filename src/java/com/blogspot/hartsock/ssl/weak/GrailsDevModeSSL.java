package com.blogspot.hartsock.ssl.weak;

import grails.util.Environment;
import grails.util.GrailsUtil;

import java.io.File;
import java.security.Security;

/**
 * Detects the Grails development environment running with a generated keystore and basically turns off SSL
 * certificate validation. All SSL certificates will be trusted if the file detects you have a grails generated
 * SSL keystore in your runtime environment.
 * Fixes:
 * sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
 */
public class GrailsDevModeSSL {

    public static void init() throws Exception {
        if(Environment.getCurrent() == Environment.PRODUCTION) return;
        System.out.println("Development/Testing mode detected, attempting to pull in generated certificate");

        String userHome = System.getProperty("user.home");
        String grailsVersion = GrailsUtil.getGrailsVersion();

        File grailsKeystoreFile = new File(
                new File(
                        new File(
                                new File(
                                        new File(
                                                userHome
                                        ),
                                        ".grails"
                                ),
                                grailsVersion
                        ),
                        "ssl"
                ),
                "keystore"
        );
        if(!grailsKeystoreFile.isFile()||!grailsKeystoreFile.canRead()) {
            System.out.println("Could not read the file " + grailsKeystoreFile.toString());
            return;
        }
        System.out.println("Found file " + grailsKeystoreFile.toString());
        System.out.println("Registering TrustingProvider");
        // Install the all-trusting trust manager
        Security.addProvider(new TrustingProvider());
        Security.setProperty(
                "ssl.TrustManagerFactory.algorithm",
                "TrustAllCertificates");
        System.out.println("TrustAllCertificates mode enabled for development and testing.");
    }
}
