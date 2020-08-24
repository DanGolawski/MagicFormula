package main.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
}
