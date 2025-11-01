package com.ourvoice.mgnrega.repository;

import com.ourvoice.mgnrega.model.District;
import com.ourvoice.mgnrega.model.MgnregaMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MgnregaMetricRepository extends JpaRepository<MgnregaMetric, Long> {

    List<MgnregaMetric> findByDistrictNameOrderByMetricYearDescMetricMonthDesc(String districtName);

    // convenience: find by district entity
    List<MgnregaMetric> findAllByDistrictOrderByMetricYearDescMetricMonthDesc(District district);

    Optional<MgnregaMetric> findByDistrictAndMetricMonthAndMetricYear(District district, int month, int year);

    Optional<MgnregaMetric> findTopByOrderByLastUpdatedDesc();

    List<MgnregaMetric> findByDistrictAndMetricYearOrderByMetricMonthAsc(District district, int metricYear);
}
