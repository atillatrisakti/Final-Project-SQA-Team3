package com.hadirapp.utlis;

import io.github.cdimascio.dotenv.Dotenv;

public class Constants {

    public static final String CHROME = "chrome";
    public static final String CHROME_HEADLESS = "chrome-headless";
    public static final String FIREFOX = "firefox";
    public static final String FIREFOX_HEADLESS = "firefox-headless";

    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMalformed().ignoreIfMissing().load();

    public static final String LOGIN_URL = dotenv.get("BASE_URL", "https://magang.dikahadir.com/absen/login");
    public static final String ABSENT_URL = dotenv.get("ABSENT_URL", "https://magang.dikahadir.com/apps/absent");
    public static final long TIMEOUT = 10;
    public static final long SLOW_SERVER_TIMEOUT = 30;
    public static final long POLLING_MILLIS = 500;

}
