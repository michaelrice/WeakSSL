package com.blogspot.hartsock.ssl.weak

import org.springframework.beans.factory.InitializingBean
import grails.util.Environment

class WeakSSLService implements InitializingBean {
    static transactional = true
    def autoTrustMode
    List trustedHosts
    def trustAll

    void afterPropertiesSet() {
        initializeTrustedHosts()

        if (trustAll) {
            if(Environment.PRODUCTION.equals(Environment.getCurrent())) {
                log.error "You are using the TrustingProvider in PRODUCTION!"
            }
            TrustingProvider.registerTrustingProvider()
        }
        else if (autoTrustMode) {
            GrailsAutoTrustModeSSL.init()
        }
    }

    void initializeTrustedHosts() {
        if (trustedHosts) {
            String[] trustedHostsArray = (String[]) trustedHosts.toArray();
            WeakHostnameVerifier.init(trustedHostsArray)
        }
    }
}
