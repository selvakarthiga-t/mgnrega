package com.ourvoice.mgnrega.dto;

import java.math.BigDecimal;

public class MgnregaMetricDTO {
    private String finYear;
    private String month;
    private String districtName;
    private BigDecimal averageWageRate;
    private BigDecimal avgDaysEmployment;
    private BigDecimal totalExp;
    private Long totalIndividualsWorked;
    private Long totalHouseholdsWorked;
    private BigDecimal percentNRM;
    private BigDecimal percentAgriWorks;
    private BigDecimal percentPayment15Days;

    // Getters and Setters
    public String getFinYear() { return finYear; }
    public void setFinYear(String finYear) { this.finYear = finYear; }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public String getDistrictName() { return districtName; }
    public void setDistrictName(String districtName) { this.districtName = districtName; }

    public BigDecimal getAverageWageRate() { return averageWageRate; }
    public void setAverageWageRate(BigDecimal averageWageRate) { this.averageWageRate = averageWageRate; }

    public BigDecimal getAvgDaysEmployment() { return avgDaysEmployment; }
    public void setAvgDaysEmployment(BigDecimal avgDaysEmployment) { this.avgDaysEmployment = avgDaysEmployment; }

    public BigDecimal getTotalExp() { return totalExp; }
    public void setTotalExp(BigDecimal totalExp) { this.totalExp = totalExp; }

    public Long getTotalIndividualsWorked() { return totalIndividualsWorked; }
    public void setTotalIndividualsWorked(Long totalIndividualsWorked) { this.totalIndividualsWorked = totalIndividualsWorked; }

    public Long getTotalHouseholdsWorked() { return totalHouseholdsWorked; }
    public void setTotalHouseholdsWorked(Long totalHouseholdsWorked) { this.totalHouseholdsWorked = totalHouseholdsWorked; }

    public BigDecimal getPercentNRM() { return percentNRM; }
    public void setPercentNRM(BigDecimal percentNRM) { this.percentNRM = percentNRM; }

    public BigDecimal getPercentAgriWorks() { return percentAgriWorks; }
    public void setPercentAgriWorks(BigDecimal percentAgriWorks) { this.percentAgriWorks = percentAgriWorks; }

    public BigDecimal getPercentPayment15Days() { return percentPayment15Days; }
    public void setPercentPayment15Days(BigDecimal percentPayment15Days) { this.percentPayment15Days = percentPayment15Days; }
}
