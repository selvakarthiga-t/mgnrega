package com.ourvoice.mgnrega.service;

import com.ourvoice.mgnrega.dto.MgnregaMetricDTO;
import org.springframework.stereotype.Service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MetricsService {

    private final JdbcTemplate jdbcTemplate;

    public MetricsService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> getAllDistricts() {
        return jdbcTemplate.queryForList(
                "SELECT DISTINCT district_name FROM mgnrega_data ORDER BY district_name",
                String.class
        );
    }

    public List<String> getAllYears() {
        return jdbcTemplate.queryForList(
                "SELECT DISTINCT fin_year FROM mgnrega_data ORDER BY fin_year",
                String.class
        );
    }

    public List<MgnregaMetricDTO> getMetrics(String district, String year) {
        String sql = """
    SELECT fin_year, month, district_name,
           Average_Wage_rate_per_day_per_person AS averageWageRate,
           Average_days_of_employment_provided_per_Household AS avgDaysEmployment,
           Total_Exp AS totalExp,
           Total_Individuals_Worked AS totalIndividualsWorked,
           Total_Households_Worked AS totalHouseholdsWorked,
           percent_of_NRM_Expenditure AS percentNRM,
           percent_of_Expenditure_on_Agriculture_Allied_Works AS percentAgriWorks,
           percentage_payments_gererated_within_15_days AS percentPayment15Days
    FROM mgnrega_data
    WHERE LOWER(TRIM(district_name)) = LOWER(TRIM(?))
      AND TRIM(fin_year) = TRIM(?)
    ORDER BY month
""";


        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            MgnregaMetricDTO dto = new MgnregaMetricDTO();
            dto.setFinYear(rs.getString("fin_year"));
            dto.setMonth(rs.getString("month"));
            dto.setDistrictName(rs.getString("district_name"));
            dto.setAverageWageRate(rs.getBigDecimal("averageWageRate"));
            dto.setAvgDaysEmployment(rs.getBigDecimal("avgDaysEmployment"));
            dto.setTotalExp(rs.getBigDecimal("totalExp"));
            dto.setTotalIndividualsWorked(rs.getLong("totalIndividualsWorked"));
            dto.setTotalHouseholdsWorked(rs.getLong("totalHouseholdsWorked"));
            dto.setPercentNRM(rs.getBigDecimal("percentNRM"));
            dto.setPercentAgriWorks(rs.getBigDecimal("percentAgriWorks"));
            dto.setPercentPayment15Days(rs.getBigDecimal("percentPayment15Days"));
            return dto;
        }, district, year);
    }

    public String getLastSync() {
        try {
            return jdbcTemplate.queryForObject("SELECT v FROM sync_meta WHERE k='last_sync'", String.class);
        } catch (Exception e) {
            return java.time.LocalDateTime.now().toString();
        }
    }

}
