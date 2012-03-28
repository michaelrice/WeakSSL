import com.blogspot.hartsock.ssl.weak.GrailsAutoTrustModeSSL
import org.codehaus.groovy.grails.commons.GrailsApplication
import grails.util.Environment
import com.blogspot.hartsock.ssl.weak.TrustingProvider
import com.blogspot.hartsock.ssl.weak.WeakHostnameVerifier

class WeakSSLGrailsPlugin {
    // the plugin version
    def version = "1.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3.6 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app"
    ]

    // TODO Fill in these fields
    def author = "Shawn Hartsock"
    def authorEmail = "hartsock@acm.org"
    def title = "WeakSSL"
    def description = '''\\
This plugin deliberately breaks SSL for you by accepting
any SSL certificate.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/weak-ssl"

    def doWithWebDescriptor = { xml ->
    }

    def doWithSpring = {
    }

    def doWithDynamicMethods = { ctx ->
    }

    def doWithApplicationContext = { applicationContext ->
        configSSLMode(application)
    }

    def onChange = { event ->
        configSSLMode(application)
    }

    def onConfigChange = { event ->
        configSSLMode(application)
    }

    void configSSLMode(GrailsApplication application) {
        GrailsAutoTrustModeSSL.init()
        def trustAll = application.config?.trustAll
        if(trustAll == null) {
            trustAll =  ! Environment.PRODUCTION.equals(Environment.getCurrent())
        }
        if (trustAll) {
            if (Environment.PRODUCTION.equals(Environment.getCurrent())) {
                log.error "You are using the TrustingProvider in PRODUCTION!"
            }
            TrustingProvider.registerTrustingProvider()
        }
        List hosts = ['localhost']
        if (application.config.weakssl?.trustedhosts instanceof List) {
            hosts = application.config.weakssl?.trustedhosts
        }
        String[] trustedHostsArray = (String[]) hosts.toArray();
        WeakHostnameVerifier.init(trustedHostsArray)
    }

}
