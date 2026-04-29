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

    @Test(description = "TC-OUT-01 - Logout berhasil")
    public void testLogoutSuccess(){
        loginPage.doLogin(Constants.EMAIL, Constants.PASSWORD);
        WaitUtils.waitForUrlToBe(driver, Constants.DASHBOARD_URL, 10);
        logoutPage.doLogout();
        WaitUtils.waitForUrlToBe(driver, Constants.URL, 10);
        Assert.assertEquals(driver.getCurrentUrl(), Constants.URL);
    }

    @Test(description = "TC-OUT-02 - Akses halaman Dashboard/Home setelah logout (Back Button)")
    public void testLogoutBackBtn(){
        loginPage.doLogin(Constants.EMAIL, Constants.PASSWORD);
        WaitUtils.waitForUrlToBe(driver, Constants.DASHBOARD_URL, 10);
        logoutPage.doLogout();
        WaitUtils.waitForUrlToBe(driver, Constants.URL, 10);
        driver.navigate().back();
        WaitUtils.waitForUrlToBe(driver, Constants.URL, 10);
        Assert.assertEquals(driver.getCurrentUrl(), Constants.URL);
    }

    @Test(description = "TC-OUT-03 - Akses halaman Dashboard/Home setelah logout (Direct URL)")
    public void testLogoutDirectUrl(){
        loginPage.doLogin(Constants.EMAIL, Constants.PASSWORD);
        WaitUtils.waitForUrlToBe(driver, Constants.DASHBOARD_URL, 10);
        logoutPage.doLogout();
        WaitUtils.waitForUrlToBe(driver, Constants.URL, 10);
        driver.get(Constants.DASHBOARD_URL);
        WaitUtils.waitForUrlToBe(driver, Constants.URL, 10);
        Assert.assertEquals(driver.getCurrentUrl(), Constants.URL);
    }
}
