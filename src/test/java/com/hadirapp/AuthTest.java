package com.hadirapp;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.hadirapp.drivers.DriverSingleton;
import com.hadirapp.pages.Auth.LoginPage;
import com.hadirapp.utlis.Constants;
import com.hadirapp.pages.Auth.LogoutPage;
import com.hadirapp.utlis.WaitUtils;

public class AuthTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private LogoutPage logoutPage;


    @BeforeMethod
    public void setUp() {
        DriverSingleton.getInstance("chrome");  
        driver = DriverSingleton.getDriver();
        driver.get(Constants.URL);
        loginPage = new LoginPage(driver);
        logoutPage = new LogoutPage(driver);
    }

    @AfterMethod
    public void tearDown() {
        DriverSingleton.closeObjectInstance();
    }

    @Test(description = "TC-LOG-01- Login dengan data valid")
    public void testLoginValidData() {
        loginPage.doLogin(Constants.EMAIL, Constants.PASSWORD);
        WaitUtils.waitForUrlToBe(driver, Constants.DASHBOARD_URL, 10);
        Assert.assertEquals(driver.getCurrentUrl(), Constants.DASHBOARD_URL);
    }

    @Test(description = "TC-LOG-02 - Login dengan email kosong")
    public void testLoginEmailEmpty() {
        loginPage.doLogin("", Constants.PASSWORD);
        Assert.assertTrue(loginPage.isErrorMessageDisplayed());
        Assert.assertEquals(loginPage.getErrorMessage(), "Akun tidak ditemukan");
    }

    @Test(description = "TC-LOG-03 - Login dengan password kosong")
    public void testLoginPasswordEmpty() {
        loginPage.doLogin(Constants.EMAIL, "");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed());
        Assert.assertEquals(loginPage.getErrorMessage(), "Email atau password salah");
    }

    @Test(description = "TC-LOG-04 - Login dengan semua field kosong")
    public void testLoginAllEmpty() {
        loginPage.doLogin("", "");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed());
        Assert.assertEquals(loginPage.getErrorMessage(), "Akun tidak ditemukan");
    }

    @Test(description = "TC-LOG-05 - Login dengan email salah")
    public void testLoginEmailSalah() {
        loginPage.doLogin("wrongemail@gmail.com", Constants.PASSWORD);
        Assert.assertTrue(loginPage.isErrorMessageDisplayed());
        Assert.assertEquals(loginPage.getErrorMessage(), "Akun tidak ditemukan");
    }

    @Test(description = "TC-LOG-06 - Login dengan password salah")
    public void testLoginPasswordSalah() {
        loginPage.doLogin(Constants.EMAIL, "wrongpassword");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed());
        Assert.assertEquals(loginPage.getErrorMessage(), "Email atau password salah");
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
