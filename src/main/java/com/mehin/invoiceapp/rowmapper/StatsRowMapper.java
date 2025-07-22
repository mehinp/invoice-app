package com.mehin.invoiceapp.rowmapper;

import com.mehin.invoiceapp.domain.Role;
import com.mehin.invoiceapp.domain.Stats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class StatsRowMapper implements RowMapper<Stats> {
    @Override
    public Stats mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Stats.builder()
                .totalCustomers(rs.getInt("total_customers"))
                .totalInvoices(rs.getInt("total_invoices"))
                .totalBilled(rs.getDouble("total_billed"))
                .build();
    }
}
