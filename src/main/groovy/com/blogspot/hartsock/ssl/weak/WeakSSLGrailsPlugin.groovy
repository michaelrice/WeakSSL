package com.blogspot.hartsock.ssl.weak

import grails.plugins.Plugin
import grails.util.Environment
import grails.util.Holders
import groovy.util.logging.Slf4j

@Slf4j
class WeakSSLGrailsPlugin extends Plugin {

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "3.1.1 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app"
    ]

    def profiles = ['web']

    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def author = "Shawn Hartsock"
    def authorEmail = "hartsock@acm.org"
    def title = "WeakSSL"
    def description = '''\\
This plugin deliberately breaks SSL for you by accepting
any SSL certificate.
'''

    def developers = [
            [
                    name : "Michael Rice",
                    email: "michael@michaelrice.org"
            ],
            [
                    name : "Sachin Verma",
                    email: "v.sachin.v@gmail.com"
            ]
    ]
    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/weak-ssl"


    Closure doWithSpring() {
        { ->

        }
    }

    void doWithDynamicMethods() {

    }

    void doWithApplicationContext() {
        configureSSLMode()
    }

    void onChange(Map<String, Object> event) {
        configureSSLMode()
    }

    void onConfigChange(Map<String, Object> event) {
        configureSSLMode()
    }

    void onShutdown(Map<String, Object> event) {

    }

    private void configureSSLMode() {
        GrailsAutoTrustModeSSL.init()
        def trustAll = Holders.config.getProperty('trustAll', Boolean)
        if (trustAll == null) {
            log.trace "trustAll not set. If Environment not production setting trustAll = true"
            trustAll = Environment.PRODUCTION != Environment.getCurrent()
        }
        if (trustAll) {
            if (Environment.PRODUCTION == Environment.getCurrent()) {
                log.warn "You are using the TrustingProvider in PRODUCTION!"
            }
            TrustingProvider.registerTrustingProvider()
        }
        List hosts = ['localhost']
        if (Holders.config.getProperty('weakssl.trustedhosts', List)) {
            hosts = Holders.config.getProperty('weakssl.trustedhosts', List)
        }
        String[] trustedHostsArray = (String[]) hosts.toArray();
        WeakHostnameVerifier.init(trustedHostsArray)
    }
}
