package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.models.Company;
import main.services.CompaniesService;
import main.services.DataService;
import main.services.DataSourceService;
import main.services.RankingService;

public class MagicFormula {

    DataSourceService dataSourceService;
    CompaniesService companiesService;
    RankingService rankingService;
    DataService dataService;

    public MagicFormula() {
        dataSourceService = new DataSourceService();
        companiesService = new CompaniesService();
        rankingService = new RankingService();
        dataService = new DataService();
    }

    public void calculate() throws IOException {
//        List<String> navigationURLs = dataSourceService.getCompanyPageURLs();
//        List<Company> companies = companiesService.getCompanies(navigationURLs);
        List<Company> companies = dataService.getDataFromFile();
        companies = rankingService.makeRanking(companies);
        displayCompanies(companies);
    }

    public void updateDatabaseFile() throws IOException {
        List<String> navigationURLs = dataSourceService.getCompanyPageURLs();
        List<Company> companies = companiesService.getCompanies(navigationURLs);
        dataService.saveDataInFile(companies);
    }

    private void displayCompanies(List<Company> companies) {
        System.out.println("\n\n\n\n\n");
        System.out.println("-------------------------------------------------------------------------------------------------------");
        System.out.printf("%60s          %20s     %10s", "COMPANY NAME", "CAPE", "ROA");
        System.out.println();
        System.out.println("-------------------------------------------------------------------------------------------------------");
        for(Company company : companies){
            System.out.format("%60s          %20s     %10s",
                    company.getName(), company.getCape(), company.getRoaMean());
            System.out.println();
        }
        System.out.println("-------------------------------------------------------------------------------------------------------");
    }
}
