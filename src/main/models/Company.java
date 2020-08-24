package main.models;

import java.util.List;
import java.util.Map;

public class Company {

    private String name;
    private Map<String, String> pricesPerEarning;
    private double cape;
    private Map<String, String> returnsOfAssets;
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
        double sum = pricesPerEarning.values().stream()
                .filter(value -> !value.equals(""))
                .mapToDouble(Double::parseDouble)
                .sum();
        this.cape = sum / pricesPerEarning.size();
    }

    private void calculateRoaMean() {
        if (returnsOfAssets.isEmpty())
            return;
        int mapSize = returnsOfAssets.size();
        int consideredElements = 4;
        if (mapSize < consideredElements)
            consideredElements = mapSize;
        double sum = returnsOfAssets.values().stream()
                .skip((long)mapSize - consideredElements)
                .filter(value -> !value.equals(""))
                .mapToDouble(Double::parseDouble)
                .sum();
        this.roaMean = sum / returnsOfAssets.size();
    }

    public double getIndicatorsSum() {
        return roaMean + cape;
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
        private Map<String, String> pricesPerEarning;
        private double cape;
        private int PERankingPos;
        private int ROARankingPos;
        private Map<String, String> returnsOfAssets;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder pricesPerEarning(Map<String, String> pricesPerEarning) {
            this.pricesPerEarning = pricesPerEarning;
            return this;
        }

        public Builder returnsOfAssets(Map<String, String> returnsOfAssets) {
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
