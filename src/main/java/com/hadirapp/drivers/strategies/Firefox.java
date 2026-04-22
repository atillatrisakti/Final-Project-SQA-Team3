package com.hadirapp.drivers.strategies;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class Firefox implements DriverStrategy {

    @Override
    public WebDriver setStrategy(String browser) {
        FirefoxOptions options = new FirefoxOptions();
        if (browser.contains("headless")) {
            options.addArguments("-headless");
        }
        return new FirefoxDriver(options);
    }

    @Override
    public WebDriver setStrategy() {
        return setStrategy("firefox");
    }
}
