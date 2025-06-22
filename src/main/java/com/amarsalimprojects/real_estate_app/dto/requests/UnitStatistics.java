package com.amarsalimprojects.real_estate_app.dto.requests;

import java.math.BigDecimal;

// Statistics DTO class
public class UnitStatistics {

    private Long totalUnits;
    private Long availableUnits;
    private Long reservedUnits;
    private Long soldUnits;
    private Long studioUnits;
    private Long oneBrUnits;
    private Long twoBrUnits;
    private Long threeBrUnits;
    private Long fourBrUnits;
    private Long penthouseUnits;
    private BigDecimal averageAvailablePrice;
    private BigDecimal minAvailablePrice;
    private BigDecimal maxAvailablePrice;

    // Constructors
    public UnitStatistics() {
    }

    public UnitStatistics(Long totalUnits, Long availableUnits, Long reservedUnits, Long soldUnits,
            Long studioUnits, Long oneBrUnits, Long twoBrUnits, Long threeBrUnits,
            Long fourBrUnits, Long penthouseUnits, BigDecimal averageAvailablePrice,
            BigDecimal minAvailablePrice, BigDecimal maxAvailablePrice) {
        this.totalUnits = totalUnits;
        this.availableUnits = availableUnits;
        this.reservedUnits = reservedUnits;
        this.soldUnits = soldUnits;
        this.studioUnits = studioUnits;
        this.oneBrUnits = oneBrUnits;
        this.twoBrUnits = twoBrUnits;
        this.threeBrUnits = threeBrUnits;
        this.fourBrUnits = fourBrUnits;
        this.penthouseUnits = penthouseUnits;
        this.averageAvailablePrice = averageAvailablePrice;
        this.minAvailablePrice = minAvailablePrice;
        this.maxAvailablePrice = maxAvailablePrice;
    }

    // Builder pattern
    public static UnitStatisticsBuilder builder() {
        return new UnitStatisticsBuilder();
    }

    public static class UnitStatisticsBuilder {

        private Long totalUnits;
        private Long availableUnits;
        private Long reservedUnits;
        private Long soldUnits;
        private Long studioUnits;
        private Long oneBrUnits;
        private Long twoBrUnits;
        private Long threeBrUnits;
        private Long fourBrUnits;
        private Long penthouseUnits;
        private BigDecimal averageAvailablePrice;
        private BigDecimal minAvailablePrice;
        private BigDecimal maxAvailablePrice;

        public UnitStatisticsBuilder totalUnits(Long totalUnits) {
            this.totalUnits = totalUnits;
            return this;
        }

        public UnitStatisticsBuilder availableUnits(Long availableUnits) {
            this.availableUnits = availableUnits;
            return this;
        }

        public UnitStatisticsBuilder reservedUnits(Long reservedUnits) {
            this.reservedUnits = reservedUnits;
            return this;
        }

        public UnitStatisticsBuilder soldUnits(Long soldUnits) {
            this.soldUnits = soldUnits;
            return this;
        }

        public UnitStatisticsBuilder studioUnits(Long studioUnits) {
            this.studioUnits = studioUnits;
            return this;
        }

        public UnitStatisticsBuilder oneBrUnits(Long oneBrUnits) {
            this.oneBrUnits = oneBrUnits;
            return this;
        }

        public UnitStatisticsBuilder twoBrUnits(Long twoBrUnits) {
            this.twoBrUnits = twoBrUnits;
            return this;
        }

        public UnitStatisticsBuilder threeBrUnits(Long threeBrUnits) {
            this.threeBrUnits = threeBrUnits;
            return this;
        }

        public UnitStatisticsBuilder fourBrUnits(Long fourBrUnits) {
            this.fourBrUnits = fourBrUnits;
            return this;
        }

        public UnitStatisticsBuilder penthouseUnits(Long penthouseUnits) {
            this.penthouseUnits = penthouseUnits;
            return this;
        }

        public UnitStatisticsBuilder averageAvailablePrice(BigDecimal averageAvailablePrice) {
            this.averageAvailablePrice = averageAvailablePrice;
            return this;
        }

        public UnitStatisticsBuilder minAvailablePrice(BigDecimal minAvailablePrice) {
            this.minAvailablePrice = minAvailablePrice;
            return this;
        }

        public UnitStatisticsBuilder maxAvailablePrice(BigDecimal maxAvailablePrice) {
            this.maxAvailablePrice = maxAvailablePrice;
            return this;
        }

        public UnitStatistics build() {
            return new UnitStatistics(totalUnits, availableUnits, reservedUnits, soldUnits,
                    studioUnits, oneBrUnits, twoBrUnits, threeBrUnits, fourBrUnits, penthouseUnits,
                    averageAvailablePrice, minAvailablePrice, maxAvailablePrice);
        }
    }

    // Getters and Setters
    public Long getTotalUnits() {
        return totalUnits;
    }

    public void setTotalUnits(Long totalUnits) {
        this.totalUnits = totalUnits;
    }

    public Long getAvailableUnits() {
        return availableUnits;
    }

    public void setAvailableUnits(Long availableUnits) {
        this.availableUnits = availableUnits;
    }

    public Long getReservedUnits() {
        return reservedUnits;
    }

    public void setReservedUnits(Long reservedUnits) {
        this.reservedUnits = reservedUnits;
    }

    public Long getSoldUnits() {
        return soldUnits;
    }

    public void setSoldUnits(Long soldUnits) {
        this.soldUnits = soldUnits;
    }

    public Long getStudioUnits() {
        return studioUnits;
    }

    public void setStudioUnits(Long studioUnits) {
        this.studioUnits = studioUnits;
    }

    public Long getOneBrUnits() {
        return oneBrUnits;
    }

    public void setOneBrUnits(Long oneBrUnits) {
        this.oneBrUnits = oneBrUnits;
    }

    public Long getTwoBrUnits() {
        return twoBrUnits;
    }

    public void setTwoBrUnits(Long twoBrUnits) {
        this.twoBrUnits = twoBrUnits;
    }

    public Long getThreeBrUnits() {
        return threeBrUnits;
    }

    public void setThreeBrUnits(Long threeBrUnits) {
        this.threeBrUnits = threeBrUnits;
    }

    public Long getFourBrUnits() {
        return fourBrUnits;
    }

    public void setFourBrUnits(Long fourBrUnits) {
        this.fourBrUnits = fourBrUnits;
    }

    public Long getPenthouseUnits() {
        return penthouseUnits;
    }

    public void setPenthouseUnits(Long penthouseUnits) {
        this.penthouseUnits = penthouseUnits;
    }

    public BigDecimal getAverageAvailablePrice() {
        return averageAvailablePrice;
    }

    public void setAverageAvailablePrice(BigDecimal averageAvailablePrice) {
        this.averageAvailablePrice = averageAvailablePrice;
    }

    public BigDecimal getMinAvailablePrice() {
        return minAvailablePrice;
    }

    public void setMinAvailablePrice(BigDecimal minAvailablePrice) {
        this.minAvailablePrice = minAvailablePrice;
    }

    public BigDecimal getMaxAvailablePrice() {
        return maxAvailablePrice;
    }

    public void setMaxAvailablePrice(BigDecimal maxAvailablePrice) {
        this.maxAvailablePrice = maxAvailablePrice;
    }
}
