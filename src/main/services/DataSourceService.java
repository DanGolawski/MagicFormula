package main.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataSourceService {

    private String startURL = "https://www.biznesradar.pl/gielda/akcje_gpw";
    private String siteBaseURL = "https://www.biznesradar.pl";

    public List<String> getCompanyPageURLs() throws IOException {
        Document document = Jsoup.connect(startURL).get();
        Element fullTable = document.getElementsByClass("qTableFull").first();
        Elements tableRows = fullTable.getElementsByAttributeValueContaining("id", "qtable-s");
        List<String> urls = new ArrayList<>();
        for (Element row : tableRows)
            urls.add(getCompanyPageURL(row));
        return urls;
    }

    private String getCompanyPageURL(Element element) {
        String urlPart = element.getElementsByTag("a").attr("href");
        return siteBaseURL + urlPart;
    }

    public List<String> getTableColumnsTitles(String url) throws IOException {
        List<String> titles = new ArrayList<>();
        Document document = Jsoup.connect(url).get();
        Element table = document.getElementsByClass("report-table").first();
        if (table == null)
            return titles;
        table = table.children().first();
        String html = table.children().first().html();
        Pattern pattern = Pattern.compile("[0-9]{4}/Q[1-4]");
        Matcher m = pattern.matcher(html);
        while (m.find()) {
            titles.add(m.group(0));
        }
        return titles;
    }

    public List<String> getValuesFromTableRow(String url, String tableRow) throws IOException {
        Document document = Jsoup.connect(url).get();
        Element table = document.getElementsByClass("report-table").first();
        List<String> values = new ArrayList<>();
        if (table == null)
            return values;
        Element row = table.getElementsByAttributeValue("data-field", tableRow).first();
        for (Element col : row.children()) {
            if (col.className().contains("h") && !col.className().equals("ch"))
                values.add(extractNumberFromCell(col));
        }
        return values;
    }

    private String extractNumberFromCell(Element col) {
        Pattern pattern = Pattern.compile("[-+]?[0-9]*\\.[0-9]+");
        if (col.childNodeSize() == 0)
            return "";
        Element tag = col.getElementsByClass("value").first();
        return extractValueFromTag(tag, pattern);

    }

    private String extractValueFromTag(Element tag, Pattern pattern) {
        String html = tag.html().replace(" ", "");
        Matcher matcher = pattern.matcher(html);
        if (matcher.find())
            return matcher.group(0);
        else
            return "";
    }

//    private double getValueFromElement(Element element) {
//        try {
//            String html = element.getElementsByClass("value").first().html();
//            String value = html.replaceAll("\\<.*?>", "")
//                    .replace("%", "")
//                    .replace(" ", "");
//            return Double.parseDouble(value);
//        }
//        catch (Exception e) {
//            System.out.println("Wrong element : " + element.toString());
//        }
//        return 0;
//    }
}
