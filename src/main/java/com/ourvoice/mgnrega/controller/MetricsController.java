package com.ourvoice.mgnrega.controller;

import com.ourvoice.mgnrega.dto.MgnregaMetricDTO;
import com.ourvoice.mgnrega.service.MetricsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/metrics")
public class MetricsController {

    private final MetricsService service;

    public MetricsController(MetricsService service) { this.service = service; }

    @GetMapping("/districts")
    public ResponseEntity<List<String>> getDistricts() {
        return ResponseEntity.ok(service.getAllDistricts());
    }

    @GetMapping("/years")
    public ResponseEntity<List<String>> getYears() {
        return ResponseEntity.ok(service.getAllYears());
    }

    @GetMapping("/data")
    public ResponseEntity<List<MgnregaMetricDTO>> getData(@RequestParam String district, @RequestParam String year) {
        List<MgnregaMetricDTO> rows = service.getMetrics(district, year);
        return rows.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(rows);
    }

    @GetMapping("/last-updated")
    public ResponseEntity<Map<String,String>> lastUpdated() {
        return ResponseEntity.ok(Map.of("lastUpdated", service.getLastSync()));
    }

    @GetMapping("/auto")
    public ResponseEntity<String> autoDetectDistrict(
            @RequestParam double lat,
            @RequestParam double lng) {

        // 🗺️ Step 1: Define approximate lat/lng for all Tamil Nadu districts
        Map<String, double[]> districtCoords = Map.ofEntries(
                Map.entry("Chennai", new double[]{13.0827, 80.2707}),
                Map.entry("Coimbatore", new double[]{11.0168, 76.9558}),
                Map.entry("Madurai", new double[]{9.9252, 78.1198}),
                Map.entry("Tiruchirappalli", new double[]{10.7905, 78.7047}),
                Map.entry("Salem", new double[]{11.6643, 78.1460}),
                Map.entry("Erode", new double[]{11.3410, 77.7172}),
                Map.entry("Tirunelveli", new double[]{8.7139, 77.7567}),
                Map.entry("Vellore", new double[]{12.9165, 79.1325}),
                Map.entry("Thanjavur", new double[]{10.7870, 79.1378}),
                Map.entry("Theni", new double[]{10.0104, 77.4768}),
                Map.entry("Kanchipuram", new double[]{12.8376, 79.7000}),
                Map.entry("Villupuram", new double[]{11.9397, 79.4973}),
                Map.entry("Dharmapuri", new double[]{12.1357, 78.1560}),
                Map.entry("Namakkal", new double[]{11.2196, 78.1674}),
                Map.entry("Krishnagiri", new double[]{12.5186, 78.2137}),
                Map.entry("Dindigul", new double[]{10.3624, 77.9695}),
                Map.entry("Nagapattinam", new double[]{10.7656, 79.8424}),
                Map.entry("Cuddalore", new double[]{11.7480, 79.7714}),
                Map.entry("Tiruvannamalai", new double[]{12.2253, 79.0747}),
                Map.entry("Ramanathapuram", new double[]{9.3639, 78.8375}),
                Map.entry("Thiruvarur", new double[]{10.7723, 79.6380}),
                Map.entry("Perambalur", new double[]{11.2342, 78.8840}),
                Map.entry("Karur", new double[]{10.9601, 78.0766}),
                Map.entry("Nilgiris", new double[]{11.4102, 76.6935})
                // ➕ Add more if you have 38 total
        );

        // 🧮 Step 2: Find nearest district by Haversine formula
        String nearestDistrict = null;
        double minDistance = Double.MAX_VALUE;

        for (var entry : districtCoords.entrySet()) {
            double[] coords = entry.getValue();
            double dist = haversine(lat, lng, coords[0], coords[1]);
            if (dist < minDistance) {
                minDistance = dist;
                nearestDistrict = entry.getKey();
            }
        }

        if (nearestDistrict != null) {
            return ResponseEntity.ok(nearestDistrict);
        } else {
            return ResponseEntity.status(404).body("District not found");
        }
    }

    // Haversine formula to calculate distance between two lat/lng points
    private static double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of Earth in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

}
