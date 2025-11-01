package com.ourvoice.mgnrega.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "mgnrega_metrics",
        uniqueConstraints = @UniqueConstraint(columnNames = {"district_id", "metric_month", "metric_year"}))
public class MgnregaMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id", nullable = false)
    private District district;

    @Column(name = "metric_month", nullable = false)
    private int metricMonth;

    @Column(name = "metric_year", nullable = false)
    private int metricYear;

    @Column(name = "person_days_generated")
    private Long personDaysGenerated;

    @Column(name = "total_expenditure_lakhs", precision = 12, scale = 2)
    private BigDecimal totalExpenditureLakhs;

    @Column(name = "avg_days_employment", precision = 6, scale = 2)
    private BigDecimal avgDaysEmployment;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @PrePersist
    @PreUpdate
    public void touch() {
        lastUpdated = LocalDateTime.now();
    }

    // getters & setters...
    // (same as earlier you had)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public District getDistrict() { return district; }
    public void setDistrict(District district) { this.district = district; }
    public int getMetricMonth() { return metricMonth; }
    public void setMetricMonth(int metricMonth) { this.metricMonth = metricMonth; }
    public int getMetricYear() { return metricYear; }
    public void setMetricYear(int metricYear) { this.metricYear = metricYear; }
    public Long getPersonDaysGenerated() { return personDaysGenerated; }
    public void setPersonDaysGenerated(Long personDaysGenerated) { this.personDaysGenerated = personDaysGenerated; }
    public BigDecimal getTotalExpenditureLakhs() { return totalExpenditureLakhs; }
    public void setTotalExpenditureLakhs(BigDecimal totalExpenditureLakhs) { this.totalExpenditureLakhs = totalExpenditureLakhs; }
    public BigDecimal getAvgDaysEmployment() { return avgDaysEmployment; }
    public void setAvgDaysEmployment(BigDecimal avgDaysEmployment) { this.avgDaysEmployment = avgDaysEmployment; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}
