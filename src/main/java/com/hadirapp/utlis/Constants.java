package com.hadirapp.utlis;

import io.github.cdimascio.dotenv.Dotenv;
import java.time.LocalDate;

public class Constants {

    public static final String CHROME = "chrome";
    public static final String CHROME_HEADLESS = "chrome-headless";
    public static final String FIREFOX = "firefox";
    public static final String FIREFOX_HEADLESS = "firefox-headless";

    private static final Dotenv dotenv = Dotenv.load();
    public static final String URL = dotenv.get("BASE_URL");
    public static final String EMAIL = dotenv.get("EMAIL");
    public static final String PASSWORD = dotenv.get("PASSWORD");
    public static final String EMAIL_2 = dotenv.get("EMAIL_2");
    public static final String PASSWORD_2 = dotenv.get("PASSWORD_2");
    public static final String EMAIL_3 = dotenv.get("EMAIL_3");
    public static final String PASSWORD_3 = dotenv.get("PASSWORD_3");
    public static final String DASHBOARD_URL = dotenv.get("DASHBOARD_URL");
    public static final String REGISTER_URL = dotenv.get("REGISTER_URL");
    public static final String RESET_PASS_URL = dotenv.get("RESET_PASS_URL");
    public static final long TIMEOUT = 10;

    public static String getTodayDate() {
        return String.valueOf(LocalDate.now().getDayOfMonth());
    }

}