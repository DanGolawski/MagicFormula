package main.services;

import main.models.Company;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompaniesService {

    DataSourceService dataSourceService;
    DataService dataService;

    public CompaniesService() {
        dataSourceService = new DataSourceService();
        dataService = new DataService();
    }

    public List<Company> getCompanies(List<String> navigationURLs) throws IOException {
        List<Company> companies = new ArrayList<>();
        for (String url : navigationURLs)
            companies.add(getCompany(url));
        return companies;
    }

    private Company getCompany(String url) throws IOException {
        String name = getName(url);
        System.out.println(name);
        // P/E
        url = url.replace("notowania", "wskazniki-wartosci-rynkowej");
        Map<String, String> peValues = getValues(url, "CZCurrent");
        // ROA
        url = url.replace("wskazniki-wartosci-rynkowej", "wskazniki-rentownosci");
        Map<String, String> roaValues = getValues(url, "ROA");

        return Company.builder()
                .name(name)
                .pricesPerEarning(peValues)
                .returnsOfAssets(roaValues)
                .build();
    }

    private String getName(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        Element fullNameField = document.getElementById("fullname-container");
        if (fullNameField == null)
            return url;
        Element name = fullNameField.getElementsByTag("h2").first();
        if (name == null)
            return url;
        return name.text();
    }

    private Map<String, String> getValues(String url, String tableRow) throws IOException {
        List<String> colsTitles = dataSourceService.getTableColumnsTitles(url);
        List<String> rawDataValues = dataSourceService.getValuesFromTableRow(url, tableRow);
        return dataService.createMap(url, colsTitles, rawDataValues);
    }


    private List<Double> shortenToTenYears(List<Double> values) {
        // It takes a values from the latest ten years -> 10 years * 4 quater
        int stop = values.size();
        int start = stop - 40;
        return values.subList(start, stop);

    }


}
