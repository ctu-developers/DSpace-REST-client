package cz.cvut.dspace.rest.client.impl;

import cz.cvut.dspace.rest.client.Configuration;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJacksonProvider;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * @author Petr Karel
 * @version $Revision: 4 $
 */
public class PooledDSpaceRESTClient extends AbstractDSpaceRESTClient {

    private PoolingClientConnectionManager connectionManager;
    private HttpClient httpClient;
    private ApacheHttpClient4Engine engine;

    public PooledDSpaceRESTClient(Configuration configuration) {
        super(configuration);
    }

    public void create() {
        // Add jackson provider to builder
        ResteasyJacksonProvider resteasyJacksonProvider = new ResteasyJacksonProvider();
        ResteasyClientBuilder builder = new ResteasyClientBuilder().register(resteasyJacksonProvider);

        // Create schema registry for trusting all certs
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        TrustStrategy trustStrategy = new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        };

        // Create SSLFactory
        SSLSocketFactory factory = null;
        try {
            factory = new SSLSocketFactory(trustStrategy, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (NoSuchAlgorithmException | KeyManagementException | UnrecoverableKeyException | KeyStoreException e) {
            e.printStackTrace();
        }
        registry.register(new Scheme("https", 443, factory));

        // Create pooling connection manager
        connectionManager = new PoolingClientConnectionManager(registry);
        if (configuration.getMaxTotal() != null) {
            connectionManager.setMaxTotal(configuration.getMaxTotal());
        }
        if (configuration.getMaxPerRoute() != null) {
            connectionManager.setDefaultMaxPerRoute(configuration.getMaxPerRoute());
        }

        // Create rest client
        httpClient = new DefaultHttpClient(connectionManager);
        engine = new ApacheHttpClient4Engine(httpClient);
        client = builder.httpEngine(engine).disableTrustManager().establishConnectionTimeout(120, TimeUnit.SECONDS).build();
    }

    @Override
    public void destroy() {
        try {
            //client.close();
            //engine.finalize();
            connectionManager.shutdown();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
}
