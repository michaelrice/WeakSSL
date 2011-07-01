package com.blogspot.hartsock.ssl.weak

import org.springframework.beans.factory.InitializingBean
import java.security.Security

class WeakSSLService implements InitializingBean {
    static transactional = true
    def devMode
    def trustedHosts
    def trustAll

    void afterPropertiesSet() {
        if (devMode) GrailsDevModeSSL.init()
        if (trustedHosts) WeakHostnameVerifier.init((String[]) trustedHosts.toArray())
        if (trustAll) {
            Security.addProvider(new TrustingProvider());
            Security.setProperty(
                    "ssl.TrustManagerFactory.algorithm",
                    "TrustAllCertificates");
        }
    }
}
