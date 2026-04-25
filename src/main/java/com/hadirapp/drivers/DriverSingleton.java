package com.hadirapp.drivers;



import org.openqa.selenium.WebDriver;

import com.hadirapp.drivers.strategies.DriverStrategy;
import com.hadirapp.drivers.strategies.DriverStrategyImplementer;
import com.hadirapp.utlis.Constants;

import java.time.Duration;

public class DriverSingleton {

    private static DriverSingleton instance = null;
    private static WebDriver driver;

    private DriverSingleton(String strategy) {
        String browser = System.getProperty("browser");
        if (browser == null || browser.isEmpty()) {
            browser = strategy;
        }
        DriverStrategy driverStrategy = DriverStrategyImplementer.chooseStrategy(browser);
        driver = driverStrategy.setStrategy(browser);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Constants.TIMEOUT));
        
        if (!browser.contains("headless")) {
            driver.manage().window().maximize();
        }
    }

    public static DriverSingleton getInstance(String strategy) {
        if (instance == null) {
            instance = new DriverSingleton(strategy);
        }
        return instance;
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static void closeObjectInstance() {
        instance = null;
        driver.quit();
    }
}
