package main.services;

import main.models.Company;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RankingService {

    private double minimalROA = 20.0;

    public List<Company> makeRanking(List<Company> companies) {
        companies = filterByROA(companies);
        if (companies.isEmpty())
            throw new NullPointerException("No companies that You can invest in");
        return rankByPE(companies);
    }

    private List<Company> rankByPE(List<Company> companies) {
        companies.sort(Comparator.comparing(Company::getCape));
        return companies;
    }

    private List<Company> filterByROA(List<Company> companies) {
        return companies.stream().filter(company -> company.getRoaMean() >= minimalROA).collect(Collectors.toList());
    }
}
