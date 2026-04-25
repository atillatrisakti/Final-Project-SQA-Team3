package com.hadirapp.drivers.strategies;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Chrome implements DriverStrategy {

    @Override
    public WebDriver setStrategy(String browser) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-session-crashed-bubble");
        options.addArguments("--incognito");
        options.setExperimentalOption("excludeSwitches",
                new java.util.ArrayList<>(java.util.Arrays.asList("enable-automation")));

        if (browser.contains("headless")) {
            options.addArguments("--headless=new");
        }

        return new org.openqa.selenium.chrome.ChromeDriver(options);
    }

    @Override
    public WebDriver setStrategy() {
        return setStrategy("chrome");
    }
}
