package com.bus.cleaning.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConfig {

    public static Connection getConnection(AppConfig cfg) throws Exception {
        return DriverManager.getConnection(cfg.dbUrl, cfg.dbUsername, cfg.dbPassword);
    }

    // Connect to server WITHOUT database (for create schema)
    public static Connection getServerConnection(AppConfig cfg) throws Exception {
        // db.url should be like jdbc:mysql://host:port/dbname?params
        // We remove "/dbname" part safely:
        String url = cfg.dbUrl;

        int idx = url.indexOf("://");
        if (idx < 0) return DriverManager.getConnection(cfg.dbUrl, cfg.dbUsername, cfg.dbPassword);

        // find first "/" after host:port
        int slash = url.indexOf("/", idx + 3);
        if (slash < 0) return DriverManager.getConnection(cfg.dbUrl, cfg.dbUsername, cfg.dbPassword);

        // keep params (?...) if present
        int q = url.indexOf("?", slash);
        String base = (q >= 0) ? url.substring(0, slash) + url.substring(q) : url.substring(0, slash);

        return DriverManager.getConnection(base, cfg.dbUsername, cfg.dbPassword);
    }
}