package com.blogspot.hartsock.ssl.weak;

import grails.util.Environment;
import grails.util.GrailsUtil;
import grails.util.Metadata;

import java.io.File;
import java.security.Security;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 * Detects the Grails development environment running with a generated keystore and basically turns off SSL
 * certificate validation. All SSL certificates will be trusted if the class detects you have a grails generated
 * SSL keystore in your runtime environment.
 * <p/>
 * <p>
 * Fixes:
 * sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
 * </p>
 *
 * @author Shawn Hartsock
 */
public class GrailsAutoTrustModeSSL {

    private static final Pattern V13X = Pattern.compile("1.3.\\d+?");
    //Pattern used for Grails version 2.x.x
    private static final Pattern V2X = Pattern.compile("2.\\d.\\d+?");
    private static Logger log = Logger.getLogger(GrailsAutoTrustModeSSL.class);

    /**
     * Will not register the trusting provider if the system is in production
     * or if we cannot find the associated SSL keystore.
     */
    public static void init() {
        log.trace("Initializing GrailsAutoTrustModeSSL");
        if (isProduction() || invalidGrailsKeystore()) {
            log.trace("AutoTrust isProduction: " + isProduction());
            log.trace("AutoTrust invalidGrailsKeystore: " + invalidGrailsKeystore());
            return;
        }
        TrustingProvider.registerTrustingProvider();
    }

    public static boolean isProduction() {
        return Environment.PRODUCTION.equals(Environment.getCurrent());
    }

    private static boolean invalidGrailsKeystore() {
        File grailsKeystoreFile = openGrailsKeystore();
        boolean invalid = (grailsKeystoreFile == null || !grailsKeystoreFile.isFile() || !grailsKeystoreFile.canRead());

        if (invalid) {
            log.error("Could not read the file " + grailsKeystoreFile);
        }

        return invalid;
    }

    /**
     * @return Grail's SSL Keystore
     * @author Shawn Hartsock
     * <p/>
     * Depends on the system property 'user.home' and on the GrailsUtil.getGrailsVersion() method to
     * properly feed to findGrailsKeystore
     * <p/>
     */
    public static File openGrailsKeystore() {
        String userHome = System.getProperty("user.home");
        String grailsVersion = GrailsUtil.getGrailsVersion();

        return findGrailsKeystore(userHome, grailsVersion);
    }

    /**
     * @param userHome
     * @param grailsVersion
     * @return Grails' SSL Keystore
     * @author Shawn Hartsock
     * <p/>
     * Platform independent Java to find the keystore on the file system.
     * Depends on the keystore being found at:
     * <p/>
     * <pre>
     *     ~/.grails/$GRAILS_VERSION/ssl/keystore
     * </pre>
     */
    public static File findGrailsKeystore(String userHome, String grailsVersion) {
        log.trace("Looking for grails keystore");
        File baseDir = new File(new File(userHome, ".grails"), grailsVersion);
        if (V13X.matcher(grailsVersion).find()) {
            return
                new File(
                    new File(
                        baseDir,
                        "ssl"
                    ),
                    "keystore"
                );
        } else if (V2X.matcher(grailsVersion).find()) {
            return
                new File(
                    new File(
                        new File(
                            new File(
                                baseDir,
                                "projects"
                            ),
                            Metadata.getCurrent().getApplicationName()
                        ),
                        "ssl"
                    ),
                    "keystore"
                );
        }
        return null;
    }

}
