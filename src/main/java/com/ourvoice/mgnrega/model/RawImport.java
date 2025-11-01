package com.ourvoice.mgnrega.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "raw_imports")
public class RawImport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="source_api", length=512)
    private String sourceApi;

    @Column(name="status_code")
    private Integer statusCode;

    @Column(name="response_body", columnDefinition = "longtext")
    private String responseBody;

    @Column(name="fetched_at")
    private LocalDateTime fetchedAt = LocalDateTime.now();

    public RawImport() {}

    public RawImport(String sourceApi, Integer statusCode, String responseBody) {
        this.sourceApi = sourceApi;
        this.statusCode = statusCode;
        this.responseBody = responseBody;
        this.fetchedAt = LocalDateTime.now();
    }

    // getters/setters...
}
