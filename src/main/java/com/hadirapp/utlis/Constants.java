package com.hadirapp.utlis;

import io.github.cdimascio.dotenv.Dotenv;

public class Constants {

    public static final String CHROME = "chrome";
    public static final String CHROME_HEADLESS = "chrome-headless";
    public static final String FIREFOX = "firefox";
    public static final String FIREFOX_HEADLESS = "firefox-headless";

    private static final Dotenv dotenv = Dotenv.load();
    public static final String URL = dotenv.get("BASE_URL");
    public static final String EMAIL = dotenv.get("EMAIL");
    public static final String PASSWORD = dotenv.get("PASSWORD");
    public static final String DASHBOARD_URL = dotenv.get("DASHBOARD_URL");
    public static final long TIMEOUT = 10;

}
