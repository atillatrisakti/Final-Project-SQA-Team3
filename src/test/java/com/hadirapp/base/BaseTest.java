package com.hadirapp.base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.hadirapp.drivers.DriverSingleton;
import com.hadirapp.utlis.Constants;

public class BaseTest {
    
    protected WebDriver driver;

    @BeforeMethod
    public void setup() {
        DriverSingleton.getInstance(Constants.CHROME);
        driver = DriverSingleton.getDriver();
        driver.get(Constants.URL);
    }

    @AfterMethod
    public void teardown() {
        DriverSingleton.closeObjectInstance();
    }
}
