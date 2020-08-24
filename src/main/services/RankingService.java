package main.services;

import main.models.Company;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RankingService {

    private double minimalROA = 0.0;

    public List<Company> makeRanking(List<Company> companies) {
        companies = filterByROA(companies);
        companies = rankByROA(companies);
        companies = rankByPE(companies);
        companies = sortBySum(companies);
        if (companies.isEmpty())
            throw new NullPointerException("No companies that You can invest in");
        return rankByPE(companies);
    }

    private List<Company> sortBySum(List<Company> companies) {
        companies.sort(Comparator.comparing(Company::getIndicatorsSum));
        return companies;
    }

    private List<Company> rankByPE(List<Company> companies) {
        companies.sort(Comparator.comparing(Company::getCape));
        int pos = 1;
        for (Company company : companies)
            company.setPERankingPos(pos++);
        return companies;
    }

    private List<Company> rankByROA(List<Company> companies) {
        companies.sort(Comparator.comparing(Company::getRoaMean));
        Collections.reverse(companies);
        int pos = 1;
        for (Company company : companies)
            company.setPERankingPos(pos++);
        return companies;

    }

    private List<Company> filterByROA(List<Company> companies) {
        return companies.stream().filter(company -> company.getRoaMean() >= minimalROA).collect(Collectors.toList());
    }
}
