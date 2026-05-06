package com.hadirapp.tests.auth;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.hadirapp.base.BaseTest;
import com.hadirapp.pages.Auth.LoginPage;
import com.hadirapp.pages.Auth.LogoutPage;
import com.hadirapp.utlis.Constants;
import com.hadirapp.utlis.WaitUtils;

public class LogoutTest extends BaseTest {
    private LoginPage loginPage;
    private LogoutPage logoutPage;

    @BeforeMethod
    public void setUpPages() {
        loginPage = new LoginPage(driver);
        logoutPage = new LogoutPage(driver);
    }

    @Test(priority = 1, description = "TC-OUT-01 - Logout berhasil")
    public void testLogoutSuccess(){
        log.info("Starting test: TC-OUT-01 - Logout berhasil");
        loginPage.doLogin(Constants.EMAIL, Constants.PASSWORD);
        WaitUtils.waitForUrlToBe(driver, Constants.DASHBOARD_URL, 20);
        logoutPage.doLogout();
        WaitUtils.waitForUrlToBe(driver, Constants.URL, 20);
        Assert.assertEquals(driver.getCurrentUrl(), Constants.URL);
    }

    @Test(priority = 2, description = "TC-OUT-02 - Akses halaman Dashboard/Home setelah logout (Back Button)")
    public void testLogoutBackBtn(){
        log.info("Starting test: TC-OUT-02 - Akses halaman Dashboard/Home setelah logout (Back Button)");
        loginPage.doLogin(Constants.EMAIL, Constants.PASSWORD);
        WaitUtils.waitForUrlToBe(driver, Constants.DASHBOARD_URL, 20);
        logoutPage.doLogout();
        WaitUtils.waitForUrlToBe(driver, Constants.URL, 20);
        driver.navigate().back();
        WaitUtils.waitForUrlToBe(driver, Constants.URL, 20);
        Assert.assertEquals(driver.getCurrentUrl(), Constants.URL);
    }

    @Test(priority = 3, description = "TC-OUT-03 - Akses halaman Dashboard/Home setelah logout (Direct URL)")
    public void testLogoutDirectUrl(){
        log.info("Starting test: TC-OUT-03 - Akses halaman Dashboard/Home setelah logout (Direct URL)");
        loginPage.doLogin(Constants.EMAIL, Constants.PASSWORD);
        WaitUtils.waitForUrlToBe(driver, Constants.DASHBOARD_URL, 20);
        logoutPage.doLogout();
        WaitUtils.waitForUrlToBe(driver, Constants.URL, 20);
        driver.get(Constants.DASHBOARD_URL);
        WaitUtils.waitForUrlToBe(driver, Constants.URL, 20);
        Assert.assertEquals(driver.getCurrentUrl(), Constants.URL);
    }
}
