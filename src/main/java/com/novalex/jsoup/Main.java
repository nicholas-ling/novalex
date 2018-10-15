package com.novalex.jsoup;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

/**
 * @author Nicholas L.
 */
public class Main {

  public static int counter = 0;

  public static void main(String[] args) throws IOException, InterruptedException {

    final String authUser = "977a9cd01e5745268e2ed879826d70f3";
    final String authPassword = "";
    Authenticator.setDefault(
        new Authenticator() {
          @Override
          public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(
                authUser, authPassword.toCharArray());
          }
        }
    );
    System.setProperty("http.proxyUser", authUser);
    System.setProperty("http.proxyPassword", authPassword);

//    System.setProperty("javax.net.ssl.trustStore","/Library/Java/JavaVirtualMachines/jdk1.8.0_162.jdk/Contents/Home/jre/lib/security/cacerts");
    FileWriter out = new FileWriter("book_new.csv");
    CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
    print(printer);
  }

  public static void print(CSVPrinter printer) throws IOException, InterruptedException {
    Reader in = new FileReader("/Users/nling/Downloads/MANFILTER.csv");
    Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
    int i=0;
    for (CSVRecord record : records) {
      if(i>=counter){
        String keyword = record.get(0);
        List<String> row = row(keyword);
        System.out.println("counter: " + counter + " " + row.get(0) + " " + row.get(1));
        printer.printRecord(row);
        printer.flush();
        i++; counter++;
      }
    }
  }

  public static List<String> row(String keyword) throws IOException, InterruptedException {
    String detailUrl = getDetailUrl(keyword);
    return getDetail(keyword, detailUrl);
  }

  public static List<String> getDetail(String keyword, String detailUrl)
      throws IOException, InterruptedException {
    List<String> row = new LinkedList<String>();
    ItemLookUp detail = new ItemLookUp(detailUrl);
    row.add(keyword);
    row.add(detail.getItem().getAsinCode());
    row.add(detail.getTitles().get(0));
    row.add(detail.getImages().get(0));
    if(row.size()<4){
      System.out.println("count:" + counter + " scrape detail " + keyword + ", but is blocked by Amazon");
      Thread.sleep(5000);
      return getDetail(keyword, detailUrl);
    }else{
      return row;
    }
  }

  public static String getDetailUrl(String keyword) throws IOException, InterruptedException {
    ItemLookUp search = new ItemLookUp(
        "https://www.amazon.ca/s/ref=nb_sb_noss?url=me%3DA3KD4QR2WFJR66&field-keywords=" + keyword);
    String detailUrl = search.getDetailUrls().get(0);
    if(detailUrl == null){
      System.out.println("count:" + counter + " scrape keyword " + keyword + ", but is blocked by Amazon");
      Thread.sleep(5000);
      return getDetailUrl(keyword);
    }else{
      return detailUrl;
    }
  }
}
