package com.blogspot.hartsock.ssl.weak;

import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactorySpi;
import javax.net.ssl.X509TrustManager;
import java.security.KeyStore;
import java.security.Provider;
import java.security.Security;
import java.security.cert.X509Certificate;

/**
 * Trusts all certificates... do not use in production!
 */
public class TrustingProvider  extends Provider {
    public TrustingProvider() {
      super( "TrustingProvider", 1.0, "Trust certificates" );
      put( "TrustManagerFactory.TrustAllCertificates",
         TrustingTrustManagerFactory.class.getName() );
   }
   protected static class TrustingTrustManagerFactory
         extends TrustManagerFactorySpi {
      public TrustingTrustManagerFactory() {}
      protected void engineInit( KeyStore keystore ) {}
      protected void engineInit(
         ManagerFactoryParameters mgrparams ) {}
      protected TrustManager[] engineGetTrustManagers() {
         return new TrustManager[] {
            new TrustingX509TrustManager()
         };
      }
   }

   protected static class TrustingX509TrustManager
         implements X509TrustManager {
      public void checkClientTrusted(
         X509Certificate[] chain, String authType) {}
      public void checkServerTrusted(
         X509Certificate[] chain, String authType) {}
      public X509Certificate[] getAcceptedIssuers() {
         return null;
      }
   }

   /**
    * Registers this All Trusting provider as
    * this JVM's provider.
    */
   public synchronized static void registerTrustingProvider() {
      // Install the all-trusting trust manager
      Security.addProvider(new TrustingProvider());
      Security.setProperty(
         "ssl.TrustManagerFactory.algorithm",
         "TrustAllCertificates"
      );
   }

}
