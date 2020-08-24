package main.services;

import main.models.Company;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CompaniesService {

    public List<Company> getCompanies(List<String> navigationURLs) throws IOException {
        List<Company> companies = new ArrayList<>();
        System.out.printf("%60s          %20s     %10s", "COMPANY NAME", "CAPE", "ROA");
        System.out.println();
        for (String url : navigationURLs)
            companies.add(getCompany(url));
        return companies;
    }

    private Company getCompany(String url) throws IOException {
        String name = getName(url);
        // P/E
        url = url.replace("notowania", "wskazniki-wartosci-rynkowej");
        List<Double> peValues = getValues(url, "CZCurrent");
        // ROA
        url = url.replace("wskazniki-wartosci-rynkowej", "wskazniki-rentownosci");
        List<Double> roaValues = getValues(url, "ROA");

        Company company = Company.builder()
                .name(name)
                .pricesPerEarning(peValues)
                .returnsOfAssets(roaValues)
                .build();

        System.out.format("%60s          %20s     %10s", company.getName(), company.getCape(), company.getRoaMean());
        System.out.println();
        return company;
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

    private List<Double> getValues(String url, String tableRow) throws IOException {
        Document document = Jsoup.connect(url).get();
        List<Double> values = new ArrayList<>();
        Element table = document.getElementsByClass("report-table").first();
        if (table == null)
            return values;
        Element row = table.getElementsByAttributeValue("data-field", tableRow).first();
        if (row == null)
            return values;
        Elements rowElems = row.getElementsByTag("td");
        for (Element elem : rowElems) {
            if (elem.childNodeSize() > 0 && elem.className().contains("h"))
                values.add(getValueFromElement(elem));


        }
        if (values.size() > 45)
            values = shortenToTenYears(values);
        return values;
    }

    private List<Double> shortenToTenYears(List<Double> values) {
        // It takes a values from the latest ten years -> 10 years * 4 quater
        int stop = values.size();
        int start = stop - 40;
        return values.subList(start, stop);

    }

    private double getValueFromElement(Element element) {
        try {
            String html = element.getElementsByClass("value").first().html();
            String value = html.replaceAll("\\<.*?>", "")
                    .replace("%", "")
                    .replace(" ", "");
            return Double.parseDouble(value);
        }
        catch (Exception e) {
            System.out.println("Wrong element : " + element.toString());
        }
        return 0;
    }
}
