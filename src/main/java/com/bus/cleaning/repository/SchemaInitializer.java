package com.bus.cleaning.repository;

import java.sql.Connection;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bus.cleaning.config.AppConfig;
import com.bus.cleaning.config.DbConfig;

public class SchemaInitializer {

    private static final Logger logger = LogManager.getLogger(SchemaInitializer.class);

    // Ensure database exists
    public void ensureDatabaseExists(AppConfig cfg, String dbName) throws Exception {

        try (Connection conn = DbConfig.getServerConnection(cfg);
             Statement st = conn.createStatement()) {

            st.execute("CREATE DATABASE IF NOT EXISTS " + dbName);
            logger.info("Database ensured: {}", dbName);
        }
    }
}