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
import com.hadirapp.pages.Auth.RegisterPage;
import com.hadirapp.utlis.WaitUtils;

public class AuthTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private LogoutPage logoutPage;
    private RegisterPage registerPage;


    @BeforeMethod
    public void setUp() {
        DriverSingleton.getInstance("chrome");  
        driver = DriverSingleton.getDriver();
        driver.get(Constants.URL);
        loginPage = new LoginPage(driver);
        logoutPage = new LogoutPage(driver);
        registerPage = new RegisterPage(driver);
    }

    @AfterMethod
    public void tearDown() {
        DriverSingleton.closeObjectInstance();
    }

    // Login Test Cases
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

    // Logout Test Cases
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

    // Register Test Cases
    @Test(description = "TC-REG-01 - Registrasi dengan data valid")
    public void testRegisterValidData(){
        String projectPath = System.getProperty("user.dir");
        String imagePath = projectPath + "\\src\\test\\resources\\testdata\\selfie.jpg";
        registerPage.doRegister("D7231978", "Izzatun Nadhiroh", "izza@mail.com", "h@dir12345", imagePath);
        Assert.assertTrue(registerPage.isAlertMessageDisplayed());
        Assert.assertEquals(registerPage.getAlertMessage(), "berhasil register, silahkan menunggu di approve oleh admin");
    }

    @Test(description = "TC-REG-02 - Registrasi dengan NIK kosong")
    public void testRegisterNIKEmpty(){
        String projectPath = System.getProperty("user.dir");
        String imagePath = projectPath + "\\src\\test\\resources\\testdata\\selfie.jpg";
        registerPage.doRegister("", "Izzatun Nadhiroh", "izza@mail.com", "h@dir12345", imagePath);
        Assert.assertEquals(registerPage.getActiveValidationMessage(), "Please fill out this field.");
    }

    @Test(description = "TC-REG-03 - Registrasi dengan nama lengkap kosong")
    public void testRegisterFullNameEmpty(){
        String projectPath = System.getProperty("user.dir");
        String imagePath = projectPath + "\\src\\test\\resources\\testdata\\selfie.jpg";
        registerPage.doRegister("D7231978", "", "izza@mail.com", "h@dir12345", imagePath);
        Assert.assertEquals(registerPage.getActiveValidationMessage(), "Please fill out this field.");
    }

    @Test(description = "TC-REG-04 - Registrasi dengan email kosong")
    public void testRegisterEmailEmpty(){
        String projectPath = System.getProperty("user.dir");
        String imagePath = projectPath + "\\src\\test\\resources\\testdata\\selfie.jpg";
        registerPage.doRegister("D7231978", "Izzatun Nadhiroh", "", "h@dir12345", imagePath);
        Assert.assertEquals(registerPage.getActiveValidationMessage(), "Please fill out this field.");
    }

    @Test(description = "TC-REG-05 - Registrasi dengan password kosong")
    public void testRegisterPasswordEmpty(){
        String projectPath = System.getProperty("user.dir");
        String imagePath = projectPath + "\\src\\test\\resources\\testdata\\selfie.jpg";
        registerPage.doRegister("D7231978", "Izzatun Nadhiroh", "izza@mail.com", "", imagePath);
        Assert.assertEquals(registerPage.getActiveValidationMessage(), "Please fill out this field.");
    }

    @Test(description = "TC-REG-06 - Registrasi dengan selfie kosong")
    public void testRegisterSelfieEmpty(){
        registerPage.doRegister("D7231978", "Izzatun Nadhiroh", "izza@mail.com", "h@dir12345", "");
        Assert.assertEquals(registerPage.getActiveValidationMessage(), "Please select a file.");
    }

    @Test(description = "TC-REG-07 - Registrasi dengan semua field kosong")
    public void testRegisterAllEmpty(){
        registerPage.doRegister("", "", "", "", "");
        Assert.assertEquals(registerPage.getActiveValidationMessage(), "Please fill out this field.");
    }

    @Test(description = "TC-REG-08 - Registrasi dengan file selfie format bukan gambar (invalid)")
    public void testRegisterSelfieNotImage(){
        String projectPath = System.getProperty("user.dir");
        String imagePath = projectPath + "\\src\\test\\resources\\testdata\\data.pdf";
        registerPage.doRegister("D7231978", "Izzatun Nadhiroh", "izza@mail.com", "h@dir12345", imagePath);
        Assert.assertTrue(registerPage.isAlertMessageDisplayed());
        Assert.assertEquals(registerPage.getAlertMessage(), "*File harus berupa gambar");
    }

    @Test(description = "TC-REG-09 - Registrasi dengan email yang sudah terdaftar")
    public void testRegisterEmailAlreadyRegistered(){
        String projectPath = System.getProperty("user.dir");
        String imagePath = projectPath + "\\src\\test\\resources\\testdata\\selfie.jpg";
        registerPage.doRegister("D7231978", "Izzatun Nadhiroh", Constants.EMAIL, "h@dir12345", imagePath);
        Assert.assertTrue(registerPage.isAlertMessageDisplayed());
        Assert.assertEquals(registerPage.getAlertMessage(), "Email sudah terdaftar");
    }



}
