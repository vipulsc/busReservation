package com.bus.cleaning.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConfig {

    public static Connection getConnection(AppConfig cfg) throws Exception {
        return DriverManager.getConnection(cfg.dbUrl, cfg.dbUsername, cfg.dbPassword);
    }
}