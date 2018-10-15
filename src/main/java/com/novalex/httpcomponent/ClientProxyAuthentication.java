package com.novalex.httpcomponent;

import java.io.File;
import java.io.IOException;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

public class ClientProxyAuthentication {

  private RequestConfig config;
  private HttpHost target;
  private HttpHost proxy;
  private HttpClientContext ctx;
  private CloseableHttpClient httpclient;

  public ClientProxyAuthentication() throws Exception{
    // Trust own CA and all self-signed certs
    SSLContext sslcontext = SSLContexts.custom()
        .loadTrustMaterial(new File(
                "/Library/Java/JavaVirtualMachines/jdk1.8.0_161.jdk/Contents/Home/jre/lib/security/cacerts"),
            "changeit".toCharArray(),
            new TrustSelfSignedStrategy())
        .build();

    // Allow TLSv1 protocol only
    SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
        sslcontext, new String[]{"TLSv1"},
        null,
        SSLConnectionSocketFactory.getDefaultHostnameVerifier());

    CredentialsProvider credsProvider = new BasicCredentialsProvider();
    credsProvider.setCredentials(
        new AuthScope("proxy.crawlera.com", 8010),
        new UsernamePasswordCredentials("977a9cd01e5745268e2ed879826d70f3", ""));

    httpclient = HttpClients.custom()
        .setDefaultCredentialsProvider(credsProvider)
        .setSSLSocketFactory(sslsf)
        .build();
    target = new HttpHost("www.amazon.ca", 443, "https");
    proxy = new HttpHost("proxy.crawlera.com", 8010);

    AuthCache authCache = new BasicAuthCache();

    BasicScheme basicAuth = new BasicScheme();
    basicAuth.processChallenge(
        new BasicHeader(HttpHeaders.PROXY_AUTHENTICATE,
            "Basic realm=\"Crawlera\""));
    authCache.put(proxy, basicAuth);

    ctx = HttpClientContext.create();
    ctx.setAuthCache(authCache);

    config = RequestConfig.custom().setProxy(proxy).build();
  }

  public String get(String path){
    HttpGet httpget = new HttpGet(path);
    httpget.setConfig(config);

    try (CloseableHttpResponse response = httpclient.execute(
        target, httpget, ctx)) {
      return EntityUtils.toString(response.getEntity());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}