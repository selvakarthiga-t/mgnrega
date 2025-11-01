package com.ourvoice.mgnrega.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class DataFetchScheduler {

    private final JdbcTemplate jdbc;
    private final RestTemplate rest;
    private final String mgnregaBase;
    private final String datagovKey;

    public DataFetchScheduler(JdbcTemplate jdbc, @Value("${datagov.api.key:}") String datagovKey,
                              @Value("${mgnrega.api.base:https://api.data.gov.in/resource/}") String mgnregaBase) {
        this.jdbc = jdbc;
        this.rest = new RestTemplate(); // simple client, avoids apache httpclient issues
        this.datagovKey = datagovKey;
        this.mgnregaBase = mgnregaBase;
    }

    // Run every 6 hours (tweak as required) — cron or fixedRate allowed
    @Scheduled(cron = "0 0 */6 * * *")
    public void syncData() {
        try {
            // Example URL — replace with actual DataGov resource id + params
            String resource = "district-wise-mgnrega-data-glance"; // example
            String url = mgnregaBase + resource + "?api-key=" + datagovKey + "&format=json&limit=1000";

            // exponential backoff attempts
            int attempts = 0;
            int maxAttempts = 3;
            long wait = 2000;
            String json = null;
            while (attempts < maxAttempts) {
                try {
                    ResponseEntity<Map> resp = rest.getForEntity(url, Map.class);
                    if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
                        Map body = resp.getBody();
                        Object records = body.get("records");
                        if (records instanceof List) {
                            List<Map<String,Object>> list = (List<Map<String,Object>>) records;
                            // upsert into DB
                            upsertRecords(list);
                        }
                        // store last-updated stamp
                        jdbc.update("REPLACE INTO sync_meta (k, v) VALUES (?, ?)", "last_sync", LocalDateTime.now().toString());
                        break;
                    } else {
                        attempts++;
                        Thread.sleep(wait);
                        wait *= 2;
                    }
                } catch (Exception ex) {
                    attempts++;
                    Thread.sleep(wait);
                    wait *= 2;
                }
            }
        } catch (Exception ex) {
            // log and swallow to avoid crashing app
            ex.printStackTrace();
        }
    }

    private void upsertRecords(List<Map<String,Object>> records) {
        // convert fields according to your mgnrega_data schema.
        // Simple approach: for each record, either insert or update by (fin_year, month, state_name, district_name).
        String upsert = """
            INSERT INTO mgnrega_data(fin_year, month, state_name, district_name,
              Approved_Labour_Budget, Average_Wage_rate_per_day_per_person,
              Average_days_of_employment_provided_per_Household, Total_Exp,
              Total_Households_Worked, Total_Individuals_Worked, Wages, Women_Persondays,
              percent_of_NRM_Expenditure, percent_of_Expenditure_on_Agriculture_Allied_Works,
              percentage_payments_gererated_within_15_days)
            VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
            ON DUPLICATE KEY UPDATE
              Approved_Labour_Budget=VALUES(Approved_Labour_Budget),
              Average_Wage_rate_per_day_per_person=VALUES(Average_Wage_rate_per_day_per_person),
              Average_days_of_employment_provided_per_Household=VALUES(Average_days_of_employment_provided_per_Household),
              Total_Exp=VALUES(Total_Exp),
              Total_Households_Worked=VALUES(Total_Households_Worked),
              Total_Individuals_Worked=VALUES(Total_Individuals_Worked),
              Wages=VALUES(Wages),
              Women_Persondays=VALUES(Women_Persondays),
              percent_of_NRM_Expenditure=VALUES(percent_of_NRM_Expenditure),
              percent_of_Expenditure_on_Agriculture_Allied_Works=VALUES(percent_of_Expenditure_on_Agriculture_Allied_Works),
              percentage_payments_gererated_within_15_days=VALUES(percentage_payments_gererated_within_15_days)
            """;

        for (Map<String,Object> r : records) {
            // map keys carefully according to API; use safe parsing
            String finYear = String.valueOf(r.getOrDefault("fin_year",""));
            String month = String.valueOf(r.getOrDefault("month",""));
            String state = String.valueOf(r.getOrDefault("state_name",""));
            String district = String.valueOf(r.getOrDefault("district_name",""));

            // parse numeric fields defensively
            Long apprBudget = parseLong(r.get("Approved_Labour_Budget"));
            Double avgWage = parseDouble(r.get("Average_Wage_rate_per_day_per_person"));
            Double avgDays = parseDouble(r.get("Average_days_of_employment_provided_per_Household"));
            Double totalExp = parseDouble(r.get("Total_Exp"));
            Integer hhWorked = parseInt(r.get("Total_Households_Worked"));
            Integer indWorked = parseInt(r.get("Total_Individuals_Worked"));
            Double wages = parseDouble(r.get("Wages"));
            Long women = parseLong(r.get("Women_Persondays"));
            Double pctNRM = parseDouble(r.get("percent_of_NRM_Expenditure"));
            Double pctAgri = parseDouble(r.get("percent_of_Expenditure_on_Agriculture_Allied_Works"));
            Double pctPay15 = parseDouble(r.get("percentage_payments_gererated_within_15_days"));

            jdbc.update(upsert,
                    finYear, month, state, district,
                    apprBudget, avgWage, avgDays, totalExp,
                    hhWorked, indWorked, wages, women, pctNRM, pctAgri, pctPay15,
                    // values for ON DUPLICATE
                    apprBudget, avgWage, avgDays, totalExp, hhWorked, indWorked, wages, women, pctNRM, pctAgri, pctPay15);
        }
    }

    private Long parseLong(Object o){ try{ return o==null?0L:Long.parseLong(String.valueOf(o)); }catch(Exception e){return 0L;}}
    private Integer parseInt(Object o){ try{ return o==null?0:Integer.parseInt(String.valueOf(o)); }catch(Exception e){return 0;}}
    private Double parseDouble(Object o){ try{ return o==null?0.0:Double.parseDouble(String.valueOf(o)); }catch(Exception e){return 0.0;}}
}
