package com.hadirapp.base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.hadirapp.drivers.DriverSingleton;
import com.hadirapp.utils.Constants;
import com.hadirapp.utils.WaitUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseTest {
    
    protected WebDriver driver;
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @BeforeMethod
    public void setup() {
        log.info("Starting test...");
        DriverSingleton.getInstance(Constants.CHROME);
        driver = DriverSingleton.getDriver();
        driver.get(Constants.URL);
        WaitUtils.waitForUrlToBe(driver, Constants.URL, 20);
    }

    @AfterMethod
    public void teardown() {
        log.info("Closing browser and ending test session.");
        DriverSingleton.closeObjectInstance();
    }
}
