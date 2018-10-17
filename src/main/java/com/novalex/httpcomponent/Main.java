package com.novalex.httpcomponent;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

public class Main {

  private static int counter = 0;
  private static ClientProxyAuthentication proxy;

  public static void main(String[] args) throws Exception {
    proxy = new ClientProxyAuthentication();
    FileWriter out = new FileWriter("resources/result.csv");
    CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
    print(printer);
  }


  public static void print(CSVPrinter printer) throws Exception {
    Reader in = new FileReader("resources/raw.csv");
    Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
    int i = 0;
    for (CSVRecord record : records) {
      if (i >= counter) {
        String keyword = record.get(0);
        List<String> row = row(keyword);
        System.out.println("timestamp: " + new Date().toString() + " counter: " + counter + " " + row.get(0) + " " + row.get(2));
        printer.printRecord(row);
        printer.flush();
        i++;
        counter++;
      }
    }
  }

  public static List<String> row(String keyword) throws Exception {
    String getDetailUrlPath = "/s/ref=nb_sb_noss?url=me%3DA3KD4QR2WFJR66&field-keywords=" + keyword;
    String detailUrl = getDetailUrl(getDetailUrlPath);
    String detailPath = detailUrl.substring(21);
    return getDetail(keyword, detailPath);
  }

  public static List<String> getDetail(String keyword, String detailUrl)
      throws Exception {
    String html = proxy.get(detailUrl);
    List<String> row = new LinkedList<String>();
    ItemLookUp detail = new ItemLookUp(html);
    row.add(keyword);
    row.add(detail.getItem().getAsinCode());
    row.add(detail.getTitles().get(0));
    row.add(detail.getImages().get(0));
    if (row.size() < 4) {
      return getDetail(keyword, detailUrl);
    } else {
      return row;
    }
  }

  public static String getDetailUrl(String keyword) throws Exception {
    String html = proxy.get(keyword);
    ItemLookUp search = new ItemLookUp(html);
    String detailUrl = search.getDetailUrls().get(0);
    if (detailUrl == null) {
      System.out.println("timestamp " + new Date() +
          " count:" + counter + " scrape keyword " + keyword + ", but is blocked by Amazon");
      return getDetailUrl(keyword);
    } else {
      return detailUrl;
    }
  }

}
