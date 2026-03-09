package com.bus.cleaning.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConfig {

    public static Connection getConnection(AppConfig cfg) throws Exception {
        return DriverManager.getConnection(cfg.dbUrl, cfg.dbUsername, cfg.dbPassword);
    }


    public static Connection getServerConnection(AppConfig cfg) throws Exception {
        String url = cfg.dbUrl;

        int idx = url.indexOf("://");
        if (idx < 0) return DriverManager.getConnection(cfg.dbUrl, cfg.dbUsername, cfg.dbPassword);

        int slash = url.indexOf("/", idx + 3);
        if (slash < 0) return DriverManager.getConnection(cfg.dbUrl, cfg.dbUsername, cfg.dbPassword);

        int q = url.indexOf("?", slash);
        String base = (q >= 0) ? url.substring(0, slash) + url.substring(q) : url.substring(0, slash);

        return DriverManager.getConnection(base, cfg.dbUsername, cfg.dbPassword);
    }
}