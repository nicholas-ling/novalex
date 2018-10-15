package com.novalex.jsoup;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents an Amazon product.
 *
 * You can get the following data:
 * the product title, its category, if it's available and how many there are in stock, the price formatted as a String,
 * the price value as a Float, the product url, and its ASIN code.
 Logo
 Scrapinghub Support Center
 Welcome
 Login
 HomeSolutionsForumsTraining All Systems Operational
 How can we help you today?

 Enter your search term here...

 SEARCH
 Solution home Crawlera Crawlera Examples
 Using Crawlera with Java
 Modified on: Wed, 3 May, 2017 at 10:24 PM



 NOTE

 Because of HTTPCLIENT-1649 you should use version 4.5 of HttpComponents Client or later.



 Extending an example published at The Apache HttpComponents™ project website and inserting Crawlera details:



 Java
 import java.io.File;
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

 public static void main(String[] args) throws Exception {

 // Trust own CA and all self-signed certs
 SSLContext sslcontext = SSLContexts.custom()
 .loadTrustMaterial(new File("/path/to/jre/lib/security/cacerts"),
 "changeit".toCharArray(),
 new TrustSelfSignedStrategy())
 .build();

 // Allow TLSv1 protocol only
 SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
 sslcontext, new String[] {"TLSv1"},
 null,
 SSLConnectionSocketFactory.getDefaultHostnameVerifier());

 CredentialsProvider credsProvider = new BasicCredentialsProvider();
 credsProvider.setCredentials(
 new AuthScope("proxy.crawlera.com", 8010),
 new UsernamePasswordCredentials("<API KEY>", ""));

 try (CloseableHttpClient httpclient = HttpClients.custom()
 .setDefaultCredentialsProvider(credsProvider)
 .setSSLSocketFactory(sslsf)
 .build())
 {
 HttpHost target = new HttpHost("twitter.com", 443, "https");
 HttpHost proxy = new HttpHost("proxy.crawlera.com", 8010);

 AuthCache authCache = new BasicAuthCache();

 BasicScheme basicAuth = new BasicScheme();
 basicAuth.processChallenge(
 new BasicHeader(HttpHeaders.PROXY_AUTHENTICATE,
 "Basic realm=\"Crawlera\""));
 authCache.put(proxy, basicAuth);

 HttpClientContext ctx = HttpClientContext.create();
 ctx.setAuthCache(authCache);

 RequestConfig config = RequestConfig.custom()
 .setProxy(proxy)
 .build();

 HttpGet httpget = new HttpGet("/");
 httpget.setConfig(config);

 System.out.println("Executing request " + httpget.getRequestLine() +
 " to " + target + " via " + proxy);

 try (CloseableHttpResponse response = httpclient.execute(
 target, httpget, ctx))
 {
 System.out.println("----------------------------------------");
 System.out.println(response.getStatusLine());
 System.out.println("----------------------------------------");
 System.out.println(EntityUtils.toString(response.getEntity()));
 EntityUtils.consume(response.getEntity());
 }
 }
 }
 }


 crawlera-ca.crt should be added to keystore, for instance with keytool:



 Java
 keytool -import -file /path/to/crawlera-ca.crt -storepass changeit -keystore $JAVA_HOME/jre/lib/security/cacerts -alias crawleracert


 Warning



 Some HTTP client libraries including Apache HttpComponents Client and .NET don't send authentication headers by default. This can result in doubled requests so pre-emptive authentication should be enabled where this is the case.



 In the above example we are making HTTPS requests to https://twitter.com through Crawlera. It is assumed that Crawlera Certificate has been installed, since CONNECT method will be employed.



 Did you find it helpful? Yes No

 Related Articles
 Using Crawlera with Selenium and Polipo
 Using Crawlera with Splash & Scrapy
 Using Crawlera with Splash & Python requests library
 Crawlera addon
 Crawlera FAQ
 Using Crawlera with R
 Using Crawlera with Ruby
 Using Crawlera with C#
 Using Crawlera with Puppeteer
 Using Crawlera with PHP
 Home Solutions Forums Training
 *
 * Note that a value could be null if invalid or not available.
 */
public class Item {

    private String title;
    private String category;
    private String availability;
    private String formattedPrice;
    private Float priceValue;
    private String itemUrl;
    private String asinCode;

    // Pattern to check if a string could be an ASIN code.
    final Pattern asinCodePattern = Pattern.compile("[A-Z0-9]{10}");

    public Item(
            String title,
            String category,
            String availability,
            String formattedPrice,
            Float priceValue,
            String itemUrl) {

        this.title = title;
        this.category = category;
        this.availability = availability;
        this.formattedPrice = formattedPrice;
        this.priceValue = priceValue;
        this.itemUrl = itemUrl;
        this.asinCode = null;

        // Try to get the ASIN code:
        if (itemUrl != null) {
            // The ASIN code should be the first occurrence of a group of ten capital letters or numbers.
            Matcher matcher = asinCodePattern.matcher(itemUrl);
            if (matcher.find()) {
                this.asinCode = matcher.group(0);
            }
        }

    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getAvailability() {
        return availability;
    }

    public String getFormattedPrice() {
        return formattedPrice;
    }

    public Float getPriceValue() {
        return priceValue;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public String getAsinCode() {
        return asinCode;
    }

}