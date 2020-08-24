package main.models;

import java.util.List;

public class Company {

    private String name;
    private List<Double> pricesPerEarning;
    private double cape;
    private List<Double> returnsOfAssets;
    private double roaMean;
    private int PERankingPos;
    private int ROARankingPos;

    private int capitalization;

    public static Builder builder() {
        return new Builder();
    }

    // SPRAWDZIC POPRAWNOSC DZIALANIA
    private void calculateCape() {
        if (pricesPerEarning.isEmpty())
            return;
        double sum = pricesPerEarning.stream().mapToDouble(Double::doubleValue).sum();
        this.cape = sum / pricesPerEarning.size();
    }

    private void calculateRoaMean() {
        if (returnsOfAssets.isEmpty())
            return;
        double sum = returnsOfAssets.stream().mapToDouble(Double::doubleValue).sum();
        this.roaMean = sum / returnsOfAssets.size();
    }

    public double getRoaMean() {
        return roaMean;
    }

    public double getCape() {
        return cape;
    }

    public String getName() {
        return name;
    }

    public int getPERankingPos() {
        return PERankingPos;
    }

    public void setPERankingPos(int PERankingPos) {
        this.PERankingPos = PERankingPos;
    }

    public int getROARankingPos() {
        return ROARankingPos;
    }

    public void setROARankingPos(int ROARankingPos) {
        this.ROARankingPos = ROARankingPos;
    }

    public static final class Builder {
        private String name;
        private List<Double> pricesPerEarning;
        private double cape;
        private int PERankingPos;
        private int ROARankingPos;
        private List<Double> returnsOfAssets;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder pricesPerEarning(List<Double> pricesPerEarning) {
            this.pricesPerEarning = pricesPerEarning;
            return this;
        }

        public Builder returnsOfAssets(List<Double> returnsOfAssets) {
            this.returnsOfAssets = returnsOfAssets;
            return this;
        }

        public Company build() {
            Company company = new Company();
            company.name = this.name;
            company.pricesPerEarning = this.pricesPerEarning;
            company.calculateCape();
            company.returnsOfAssets = this.returnsOfAssets;
            company.calculateRoaMean();
            return company;
        }
    }
}
