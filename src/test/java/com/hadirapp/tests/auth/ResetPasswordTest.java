package com.hadirapp.tests.auth;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.hadirapp.base.BaseTest;
import com.hadirapp.pages.Auth.ResetPasswordPage;
import com.hadirapp.utlis.Constants;
import com.hadirapp.utlis.WaitUtils;

public class ResetPasswordTest extends BaseTest {

    private ResetPasswordPage resetPasswordPage;

    @BeforeMethod
    public void setUpPages() {
        resetPasswordPage = new ResetPasswordPage(driver);
    }

    @Test(priority = 1, description = "TC-RST-01 - Navigasi ke halaman Request Lupa Password")
    public void testNavigateToRequestResetPasswordPage() {
        log.info("Starting test: TC-RST-01 - Navigasi ke halaman Request Lupa Password");
        resetPasswordPage.clickForgotPasswordButton();
        WaitUtils.waitForUrlToBe(driver, Constants.RESET_PASS_URL, 20);
        Assert.assertEquals(driver.getCurrentUrl(), Constants.RESET_PASS_URL);
    }

    @Test(priority = 2, description = "TC-RST-02 - Request reset password dengan email yang sudah terdaftar")
    public void testRequestResetPasswordValid(){
        log.info("Starting test: TC-RST-02 - Request reset password dengan email yang sudah terdaftar");
        resetPasswordPage.clickForgotPasswordButton();
        WaitUtils.waitForUrlToBe(driver, Constants.RESET_PASS_URL, 20);
        resetPasswordPage.requestResetPassword(Constants.EMAIL);
        Assert.assertTrue(resetPasswordPage.isAlertMessageDisplayed());
        Assert.assertTrue(resetPasswordPage.getAlertMessage().contains("Link reset password terkirim"));
    }

    @Test(priority = 3, description = "TC-RST-03 - Request reset password dengan email kosong")
    public void testRequestResetPasswordEmpty(){
        log.info("Starting test: TC-RST-03 - Request reset password dengan email kosong");
        resetPasswordPage.clickForgotPasswordButton();
        WaitUtils.waitForUrlToBe(driver, Constants.RESET_PASS_URL, 20);
        resetPasswordPage.requestResetPassword(" ");
        Assert.assertTrue(resetPasswordPage.isAlertMessageDisplayed());
        Assert.assertTrue(resetPasswordPage.getAlertMessage().contains("User not found"));
    }

    @Test(priority = 4, description = "TC-RST-04 - Request reset password dengan email tidak terdaftar")
    public void testRequestResetPasswordInvalidEmail(){
        log.info("Starting test: TC-RST-04 - Request reset password dengan email tidak terdaftar");
        resetPasswordPage.clickForgotPasswordButton();
        WaitUtils.waitForUrlToBe(driver, Constants.RESET_PASS_URL, 20);
        resetPasswordPage.requestResetPassword("invalidemail@gmail.com");
        Assert.assertTrue(resetPasswordPage.isAlertMessageDisplayed());
        Assert.assertTrue(resetPasswordPage.getAlertMessage().contains("User not found"));
    }

    @Test(priority = 5, description = "TC-RST-05 - Request reset password dengan format email tidak valid")
    public void testRequestResetPasswordInvalidFormat(){
        log.info("Starting test: TC-RST-05 - Request reset password dengan format email tidak valid");
        resetPasswordPage.clickForgotPasswordButton();
        WaitUtils.waitForUrlToBe(driver, Constants.RESET_PASS_URL, 20);
        resetPasswordPage.requestResetPassword("invalid-email");
        Assert.assertTrue(resetPasswordPage.getActiveValidationMessage().contains("Please include an '@'"));
    }

    @Test(priority = 6, description = "TC-RST-06 - Memastikan tombol 'Kembali ke halaman Login' berfungsi")
    public void testNavigateToLoginPage() {
        log.info("Starting test: TC-RST-06 - Memastikan tombol 'Kembali ke halaman Login' berfungsi");
        resetPasswordPage.clickForgotPasswordButton();
        WaitUtils.waitForUrlToBe(driver, Constants.RESET_PASS_URL, 20);
        resetPasswordPage.clickBackToLoginButton();
        WaitUtils.waitForUrlToBe(driver, Constants.URL, 20);
        Assert.assertEquals(driver.getCurrentUrl(), Constants.URL);
    }

    @Test(priority = 7, description = "TC-RST-07 - Submit reset password dengan semua field kosong")
    public void testResetPasswordAllEmpty() {
        log.info("Starting test: TC-RST-07 - Submit reset password dengan semua field kosong");
        resetPasswordPage.clickForgotPasswordButton();
        WaitUtils.waitForUrlToBe(driver, Constants.RESET_PASS_URL, 20);
        resetPasswordPage.requestResetPassword(Constants.EMAIL);
        WaitUtils.waitForUrlContains(driver, "message=success", 20);
        Assert.assertTrue(driver.getCurrentUrl().contains("message=success"));
        resetPasswordPage.submitNewPassword("", "", "");
        Assert.assertEquals(resetPasswordPage.getActiveValidationMessage(), "Please fill out this field.");
    }

    @Test(priority = 8, description = "TC-RST-08 - Konfirmasi password tidak sama dengan password baru")
    public void testResetPasswordMismatch() {
        log.info("Starting test: TC-RST-08 - Konfirmasi password tidak sama dengan password baru");
        resetPasswordPage.clickForgotPasswordButton();
        WaitUtils.waitForUrlToBe(driver, Constants.RESET_PASS_URL, 20);
        resetPasswordPage.requestResetPassword(Constants.EMAIL);
        WaitUtils.waitForUrlContains(driver, "message=success", 20);
        Assert.assertTrue(driver.getCurrentUrl().contains("message=success"));
        resetPasswordPage.submitNewPassword("testing123", "testing321", "12345");
        Assert.assertTrue(resetPasswordPage.isAlertMessageDisplayed());
        Assert.assertTrue(resetPasswordPage.getAlertMessage().contains("password tidak sama"));
    }

    @Test(priority = 9, description = "TC-RST-09 - Memasukkan password < 8 karakter")
    public void testResetPasswordLength() {
        log.info("Starting test: TC-RST-09 - Memasukkan password < 8 karakter");
        resetPasswordPage.clickForgotPasswordButton();
        WaitUtils.waitForUrlToBe(driver, Constants.RESET_PASS_URL, 20);
        resetPasswordPage.requestResetPassword(Constants.EMAIL);
        WaitUtils.waitForUrlContains(driver, "message=success", 20);
        Assert.assertTrue(driver.getCurrentUrl().contains("message=success"));
        resetPasswordPage.submitNewPassword("testing", "testing", "12345");
        Assert.assertTrue(resetPasswordPage.isAlertMessageDisplayed());
        Assert.assertTrue(resetPasswordPage.getAlertMessage().contains("password harus lebih"));
    }

    @Test(priority = 10, description = "TC-RST-10 - Submit reset password dengan field password kosong")
    public void testResetPasswordEmpty() {
        log.info("Starting test: TC-RST-10 - Submit reset password dengan field password kosong");
        resetPasswordPage.clickForgotPasswordButton();
        WaitUtils.waitForUrlToBe(driver, Constants.RESET_PASS_URL, 20);
        resetPasswordPage.requestResetPassword(Constants.EMAIL);
        WaitUtils.waitForUrlContains(driver, "message=success", 20);
        Assert.assertTrue(driver.getCurrentUrl().contains("message=success"));
        resetPasswordPage.submitNewPassword("", "", "12345");
        Assert.assertTrue(resetPasswordPage.isAlertMessageDisplayed());
        Assert.assertTrue(resetPasswordPage.getAlertMessage().contains("password harus lebih"));
    }

    @Test(priority = 11, description = "TC-RST-11 - Submit reset password dengan field OTP kosong")
    public void testResetPasswordOTPEmpty() {
        log.info("Starting test: TC-RST-11 - Submit reset password dengan field OTP kosong");
        resetPasswordPage.clickForgotPasswordButton();
        WaitUtils.waitForUrlToBe(driver, Constants.RESET_PASS_URL, 20);
        resetPasswordPage.requestResetPassword(Constants.EMAIL);
        WaitUtils.waitForUrlContains(driver, "message=success", 20);
        Assert.assertTrue(driver.getCurrentUrl().contains("message=success"));
        resetPasswordPage.submitNewPassword("testing123", "testing123", "");
        Assert.assertEquals(resetPasswordPage.getActiveValidationMessage(), "Please fill out this field.");
    }

    @Test(priority = 12, description = "TC-RST-12 - Submit reset password dengan data lengkap tapi OTP salah/invalid")
    public void testResetPasswordInvalidOTP() {
        log.info("Starting test: TC-RST-12 - Submit reset password dengan data lengkap tapi OTP salah/invalid");
        resetPasswordPage.clickForgotPasswordButton();
        WaitUtils.waitForUrlToBe(driver, Constants.RESET_PASS_URL, 20);
        resetPasswordPage.requestResetPassword(Constants.EMAIL);
        WaitUtils.waitForUrlContains(driver, "message=success", 20);
        Assert.assertTrue(driver.getCurrentUrl().contains("message=success"));
        resetPasswordPage.submitNewPassword("testing123", "testing123", "12345");
        Assert.assertTrue(resetPasswordPage.isAlertMessageDisplayed());
        Assert.assertTrue(resetPasswordPage.getAlertMessage().contains("Reset Token is incorrect"));
    }

}
