package com.hadirapp.drivers;

import org.openqa.selenium.WebDriver;
import com.epam.healenium.SelfHealingDriver;
import com.hadirapp.drivers.strategies.DriverStrategy;
import com.hadirapp.drivers.strategies.DriverStrategyImplementer;
import com.hadirapp.utils.Constants;

import java.time.Duration;
import java.util.Locale;

public class DriverSingleton {

    private static DriverSingleton instance = null;
    private static WebDriver driver;

    private DriverSingleton(String strategy) {
        ensureLogDirectory();
        
        String browser = System.getProperty("browser");
        if (browser == null || browser.isEmpty()) {
            browser = strategy;
        }
        
        DriverStrategy driverStrategy = DriverStrategyImplementer.chooseStrategy(browser);
        driver = driverStrategy.setStrategy(browser);
        
        // Integrasi Healenium
        driver = SelfHealingDriver.create(driver);
        
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Constants.TIMEOUT));
        
        if (!browser.toLowerCase(Locale.ROOT).contains("headless")) {
            driver.manage().window().maximize();
        }
    }

    private static void ensureLogDirectory() {
        java.nio.file.Path logPath = java.nio.file.Paths.get("log");
        if (java.nio.file.Files.notExists(logPath)) {
            try {
                java.nio.file.Files.createDirectories(logPath);
            } catch (java.io.IOException e) {
                System.err.println("Gagal membuat folder log: " + logPath.toAbsolutePath());
            }
        }
    }

    public static DriverSingleton getInstance(String strategy) {
        if (instance == null) {
            instance = new DriverSingleton(strategy);
        }
        return instance;
    }

    public static WebDriver getDriver() {
        if (driver == null) {
            // Fallback default jika getInstance belum dipanggil
            getInstance(Constants.CHROME);
        }
        return driver;
    }

    public static void closeObjectInstance() {
        if (instance != null && driver != null) {
            driver.quit();
            instance = null;
            driver = null;
        }
    }
}
