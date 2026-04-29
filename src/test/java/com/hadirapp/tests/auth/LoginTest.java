package com.hadirapp.tests.auth;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.hadirapp.base.BaseTest;
import com.hadirapp.pages.Auth.LoginPage;
import com.hadirapp.pages.Auth.RegisterPage;
import com.hadirapp.utlis.Constants;
import com.hadirapp.utlis.WaitUtils;

public class LoginTest extends BaseTest {
    private LoginPage loginPage;
    private RegisterPage registerPage;

    @BeforeMethod
    public void setUpPages() {
        loginPage = new LoginPage(driver);
        registerPage = new RegisterPage(driver);
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

    @Test(description = "TC-LOG-07 - Navigasi ke halaman Registrasi")
    public void testNavigateToRegisterPage(){
        registerPage.clickRegisterButton();
        WaitUtils.waitForUrlToBe(driver, Constants.REGISTER_URL, 10);
        Assert.assertEquals(driver.getCurrentUrl(), Constants.REGISTER_URL);
    }
}
