package com.bus.cleaning.config;

import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

	public String inputPath;
	public String cleanedPath;
	public String rejectedPath;
	public String aggregationPath;

	public boolean dbEnabled;
	public String dbUrl;
	public String dbUsername;
	public String dbPassword;

	public static AppConfig load() throws Exception {
	    Properties p = new Properties();

	    try (InputStream is =
	                 AppConfig.class.getClassLoader()
	                         .getResourceAsStream("application.properties")) {

	        if (is == null) {
	            throw new RuntimeException("application.properties NOT FOUND in classpath");
	        }

	        p.load(is);
	    }

	    AppConfig c = new AppConfig();
	    c.inputPath = p.getProperty("input.path");
	    c.cleanedPath = p.getProperty("output.cleaned");
	    c.rejectedPath = p.getProperty("output.rejected");
	    c.aggregationPath = p.getProperty("output.aggregation");

	    c.dbEnabled = Boolean.parseBoolean(p.getProperty("db.enabled"));
	    c.dbUrl = p.getProperty("db.url");
	    c.dbUsername = p.getProperty("db.username");
	    c.dbPassword = p.getProperty("db.password");

	    return c;
	}
}