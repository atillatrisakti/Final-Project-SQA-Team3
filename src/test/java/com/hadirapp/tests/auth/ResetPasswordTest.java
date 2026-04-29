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

    @Test(description = "TC-RST-01 - Navigasi ke halaman Request Lupa Password")
    public void testNavigateToRequestResetPasswordPage() {
        resetPasswordPage.clickForgotPasswordButton();
        WaitUtils.waitForUrlToBe(driver, Constants.RESET_PASS_URL, 10);
        Assert.assertEquals(driver.getCurrentUrl(), Constants.RESET_PASS_URL);
    }

    @Test(description = "TC-RST-02 - Request reset password dengan email yang sudah terdaftar")
    public void testRequestResetPasswordValid(){
        resetPasswordPage.clickForgotPasswordButton();
        WaitUtils.waitForUrlToBe(driver, Constants.RESET_PASS_URL, 5);
        resetPasswordPage.requestResetPassword(Constants.EMAIL);
        Assert.assertTrue(resetPasswordPage.isAlertMessageDisplayed());
        Assert.assertTrue(resetPasswordPage.getAlertMessage().contains("Link reset password terkirim"));
    }

    @Test(description = "TC-RST-03 - Request reset password dengan email kosong")
    public void testRequestResetPasswordEmpty(){
        resetPasswordPage.clickForgotPasswordButton();
        WaitUtils.waitForUrlToBe(driver, Constants.RESET_PASS_URL, 5);
        resetPasswordPage.requestResetPassword(" ");
        Assert.assertTrue(resetPasswordPage.isAlertMessageDisplayed());
        Assert.assertTrue(resetPasswordPage.getAlertMessage().contains("User not found"));
    }

    @Test(description = "TC-RST-04 - Request reset password dengan email tidak terdaftar")
    public void testRequestResetPasswordInvalidEmail(){
        resetPasswordPage.clickForgotPasswordButton();
        WaitUtils.waitForUrlToBe(driver, Constants.RESET_PASS_URL, 5);
        resetPasswordPage.requestResetPassword("invalidemail@gmail.com");
        Assert.assertTrue(resetPasswordPage.isAlertMessageDisplayed());
        Assert.assertTrue(resetPasswordPage.getAlertMessage().contains("User not found"));
    }

    @Test(description = "TC-RST-05 - Request reset password dengan format email tidak valid")
    public void testRequestResetPasswordInvalidFormat(){
        resetPasswordPage.clickForgotPasswordButton();
        WaitUtils.waitForUrlToBe(driver, Constants.RESET_PASS_URL, 5);
        resetPasswordPage.requestResetPassword("invalid-email");
        Assert.assertTrue(resetPasswordPage.getActiveValidationMessage().contains("Please include an '@'"));
    }

    @Test(description = "TC-RST-06 - Memastikan tombol 'Kembali ke halaman Login' berfungsi")
    public void testNavigateToLoginPage() {
        resetPasswordPage.clickForgotPasswordButton();
        WaitUtils.waitForUrlToBe(driver, Constants.RESET_PASS_URL, 5);
        resetPasswordPage.clickBackToLoginButton();
        WaitUtils.waitForUrlToBe(driver, Constants.URL, 5);
        Assert.assertEquals(driver.getCurrentUrl(), Constants.URL);
    }

    @Test(description = "TC-RST-07 - Submit reset password dengan semua field kosong")
    public void testResetPasswordAllEmpty() {
        resetPasswordPage.clickForgotPasswordButton();
        WaitUtils.waitForUrlToBe(driver, Constants.RESET_PASS_URL, 5);
        resetPasswordPage.requestResetPassword(Constants.EMAIL);
        WaitUtils.waitForUrlContains(driver, "message=success", 10);
        Assert.assertTrue(driver.getCurrentUrl().contains("message=success"));
        resetPasswordPage.submitNewPassword("", "", "");
        Assert.assertEquals(resetPasswordPage.getActiveValidationMessage(), "Please fill out this field.");
    }

    @Test(description = "TC-RST-08 - Konfirmasi password tidak sama dengan password baru")
    public void testResetPasswordMismatch() {
        resetPasswordPage.clickForgotPasswordButton();
        WaitUtils.waitForUrlToBe(driver, Constants.RESET_PASS_URL, 5);
        resetPasswordPage.requestResetPassword(Constants.EMAIL);
        WaitUtils.waitForUrlContains(driver, "message=success", 10);
        Assert.assertTrue(driver.getCurrentUrl().contains("message=success"));
        resetPasswordPage.submitNewPassword("testing123", "testing321", "12345");
        Assert.assertTrue(resetPasswordPage.isAlertMessageDisplayed());
        Assert.assertTrue(resetPasswordPage.getAlertMessage().contains("password tidak sama"));
    }

    @Test(description = "TC-RST-09 - Memasukkan password < 8 karakter")
    public void testResetPasswordLength() {
        resetPasswordPage.clickForgotPasswordButton();
        WaitUtils.waitForUrlToBe(driver, Constants.RESET_PASS_URL, 5);
        resetPasswordPage.requestResetPassword(Constants.EMAIL);
        WaitUtils.waitForUrlContains(driver, "message=success", 10);
        Assert.assertTrue(driver.getCurrentUrl().contains("message=success"));
        resetPasswordPage.submitNewPassword("testing", "testing", "12345");
        Assert.assertTrue(resetPasswordPage.isAlertMessageDisplayed());
        Assert.assertTrue(resetPasswordPage.getAlertMessage().contains("password harus lebih"));
    }

    @Test(description = "TC-RST-10 - Submit reset password dengan field password kosong")
    public void testResetPasswordEmpty() {
        resetPasswordPage.clickForgotPasswordButton();
        WaitUtils.waitForUrlToBe(driver, Constants.RESET_PASS_URL, 5);
        resetPasswordPage.requestResetPassword(Constants.EMAIL);
        WaitUtils.waitForUrlContains(driver, "message=success", 10);
        Assert.assertTrue(driver.getCurrentUrl().contains("message=success"));
        resetPasswordPage.submitNewPassword("", "", "12345");
        Assert.assertTrue(resetPasswordPage.isAlertMessageDisplayed());
        Assert.assertTrue(resetPasswordPage.getAlertMessage().contains("password harus lebih"));
    }

    @Test(description = "TC-RST-11 - Submit reset password dengan field OTP kosong")
    public void testResetPasswordOTPEmpty() {
        resetPasswordPage.clickForgotPasswordButton();
        WaitUtils.waitForUrlToBe(driver, Constants.RESET_PASS_URL, 5);
        resetPasswordPage.requestResetPassword(Constants.EMAIL);
        WaitUtils.waitForUrlContains(driver, "message=success", 10);
        Assert.assertTrue(driver.getCurrentUrl().contains("message=success"));
        resetPasswordPage.submitNewPassword("testing123", "testing123", "");
        Assert.assertEquals(resetPasswordPage.getActiveValidationMessage(), "Please fill out this field.");
    }

    @Test(description = "TC-RST-12 - Submit reset password dengan data lengkap tapi OTP salah/invalid")
    public void testResetPasswordInvalidOTP() {
        resetPasswordPage.clickForgotPasswordButton();
        WaitUtils.waitForUrlToBe(driver, Constants.RESET_PASS_URL, 5);
        resetPasswordPage.requestResetPassword(Constants.EMAIL);
        WaitUtils.waitForUrlContains(driver, "message=success", 10);
        Assert.assertTrue(driver.getCurrentUrl().contains("message=success"));
        resetPasswordPage.submitNewPassword("testing123", "testing123", "12345");
        Assert.assertTrue(resetPasswordPage.isAlertMessageDisplayed());
        Assert.assertTrue(resetPasswordPage.getAlertMessage().contains("Reset Token is incorrect"));
    }

}
