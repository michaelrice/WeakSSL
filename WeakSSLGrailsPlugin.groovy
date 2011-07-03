import com.blogspot.hartsock.ssl.weak.GrailsAutoTrustModeSSL
import org.codehaus.groovy.grails.commons.GrailsApplication
import com.blogspot.hartsock.ssl.weak.WeakSSLService

class WeakSSLGrailsPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3.6 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
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
        // TODO Implement additions to web.xml (optional), this event occurs before 
    }

    def doWithSpring = {
        def trustedHosts = ['localhost']
        if(application.config.weakssl?.trustedhosts instanceof List) {
            trustedHosts =  application.config.weakssl?.trustedhosts
        }
        def autoTrustMode = true
        if(application.config.weakssl?.autoTrust == false) {
            autoTrustMode = false
        }
        def trustAll = application.config.weakssl?.trustAll?:false
        weakSSLService(WeakSSLService) {
            trustedHosts = trustedHosts
            autoTrustMode = autoTrustMode
            trustAll = trustAll
        }
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        configSSLMode(application)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        configSSLMode(application)
    }

    void configSSLMode(GrailsApplication application) {
        def trustAll = application.config?.trustAll
        if (trustAll) {
            GrailsAutoTrustModeSSL.init()
        }
    }

}
