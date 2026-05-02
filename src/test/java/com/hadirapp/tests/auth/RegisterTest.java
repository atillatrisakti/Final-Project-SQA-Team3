package com.hadirapp.tests.auth;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.hadirapp.base.BaseTest;
import com.hadirapp.pages.Auth.RegisterPage;
import com.hadirapp.utlis.Constants;

public class RegisterTest extends BaseTest {
    private RegisterPage registerPage;

    @BeforeMethod
    public void setUpPages() {
        registerPage = new RegisterPage(driver);
    }

    @Test(priority = 1, description = "TC-REG-01 - Registrasi dengan data valid")
    public void testRegisterValidData(){
        log.info("Starting test: TC-REG-01 - Registrasi dengan data valid");
        String projectPath = System.getProperty("user.dir");
        String imagePath = projectPath + "\\src\\test\\resources\\testdata\\selfie.jpg";
        registerPage.doRegister("D7231978", "Izzatun Nadhiroh", "izza@mail.com", "h@dir12345", imagePath);
        Assert.assertTrue(registerPage.isAlertMessageDisplayed());
        Assert.assertEquals(registerPage.getAlertMessage(), "berhasil register, silahkan menunggu di approve oleh admin");
    }

    @Test(priority = 2, description = "TC-REG-02 - Registrasi dengan NIK kosong")
    public void testRegisterNIKEmpty(){
        log.info("Starting test: TC-REG-02 - Registrasi dengan NIK kosong");
        String projectPath = System.getProperty("user.dir");
        String imagePath = projectPath + "\\src\\test\\resources\\testdata\\selfie.jpg";
        registerPage.doRegister("", "Izzatun Nadhiroh", "izza@mail.com", "h@dir12345", imagePath);
        Assert.assertEquals(registerPage.getActiveValidationMessage(), "Please fill out this field.");
    }

    @Test(priority = 3, description = "TC-REG-03 - Registrasi dengan nama lengkap kosong")
    public void testRegisterFullNameEmpty(){
        log.info("Starting test: TC-REG-03 - Registrasi dengan nama lengkap kosong");
        String projectPath = System.getProperty("user.dir");
        String imagePath = projectPath + "\\src\\test\\resources\\testdata\\selfie.jpg";
        registerPage.doRegister("D7231978", "", "izza@mail.com", "h@dir12345", imagePath);
        Assert.assertEquals(registerPage.getActiveValidationMessage(), "Please fill out this field.");
    }

    @Test(priority = 4, description = "TC-REG-04 - Registrasi dengan email kosong")
    public void testRegisterEmailEmpty(){
        log.info("Starting test: TC-REG-04 - Registrasi dengan email kosong");
        String projectPath = System.getProperty("user.dir");
        String imagePath = projectPath + "\\src\\test\\resources\\testdata\\selfie.jpg";
        registerPage.doRegister("D7231978", "Izzatun Nadhiroh", "", "h@dir12345", imagePath);
        Assert.assertEquals(registerPage.getActiveValidationMessage(), "Please fill out this field.");
    }

    @Test(priority = 5, description = "TC-REG-05 - Registrasi dengan password kosong")
    public void testRegisterPasswordEmpty(){
        log.info("Starting test: TC-REG-05 - Registrasi dengan password kosong");
        String projectPath = System.getProperty("user.dir");
        String imagePath = projectPath + "\\src\\test\\resources\\testdata\\selfie.jpg";
        registerPage.doRegister("D7231978", "Izzatun Nadhiroh", "izza@mail.com", "", imagePath);
        Assert.assertEquals(registerPage.getActiveValidationMessage(), "Please fill out this field.");
    }

    @Test(priority = 6, description = "TC-REG-06 - Registrasi dengan selfie kosong")
    public void testRegisterSelfieEmpty(){
        log.info("Starting test: TC-REG-06 - Registrasi dengan selfie kosong");
        registerPage.doRegister("D7231978", "Izzatun Nadhiroh", "izza@mail.com", "h@dir12345", "");
        Assert.assertEquals(registerPage.getActiveValidationMessage(), "Please select a file.");
    }

    @Test(priority = 7, description = "TC-REG-07 - Registrasi dengan semua field kosong")
    public void testRegisterAllEmpty(){
        log.info("Starting test: TC-REG-07 - Registrasi dengan semua field kosong");
        registerPage.doRegister("", "", "", "", "");
        Assert.assertEquals(registerPage.getActiveValidationMessage(), "Please fill out this field.");
    }

    @Test(priority = 8, description = "TC-REG-08 - Registrasi dengan file selfie format bukan gambar (invalid)")
    public void testRegisterSelfieNotImage(){
        log.info("Starting test: TC-REG-08 - Registrasi dengan file selfie format bukan gambar (invalid)");
        String projectPath = System.getProperty("user.dir");
        String imagePath = projectPath + "\\src\\test\\resources\\testdata\\data.pdf";
        registerPage.doRegister("D7231978", "Izzatun Nadhiroh", "izza@mail.com", "h@dir12345", imagePath);
        Assert.assertTrue(registerPage.isAlertMessageDisplayed());
        Assert.assertEquals(registerPage.getAlertMessage(), "*File harus berupa gambar");
    }

    @Test(priority = 9, description = "TC-REG-09 - Registrasi dengan email yang sudah terdaftar")
    public void testRegisterEmailAlreadyRegistered(){
        log.info("Starting test: TC-REG-09 - Registrasi dengan email yang sudah terdaftar");
        String projectPath = System.getProperty("user.dir");
        String imagePath = projectPath + "\\src\\test\\resources\\testdata\\selfie.jpg";
        registerPage.doRegister("D7231978", "Izzatun Nadhiroh", Constants.EMAIL, "h@dir12345", imagePath);
        Assert.assertTrue(registerPage.isAlertMessageDisplayed());
        Assert.assertEquals(registerPage.getAlertMessage(), "Email sudah terdaftar");
    }

    @Test(priority = 10, description = "TC-REG-10 - Registrasi dengan NIK yang salah")
    public void testRegisterNIKInvalid(){
        log.info("Starting test: TC-REG-10 - Registrasi dengan NIK yang salah");
        String projectPath = System.getProperty("user.dir");
        String imagePath = projectPath + "\\src\\test\\resources\\testdata\\selfie.jpg";
        registerPage.doRegister("1234567", "Izzatun Nadhiroh", "izza@mail.com", "h@dir12345", imagePath);
        Assert.assertTrue(registerPage.isAlertMessageDisplayed());
        Assert.assertEquals(registerPage.getAlertMessage(), "NIK Anda tidak ditemukan");
    }

    @Test(priority = 11, description = "TC-REG-11 - Registrasi dengan ukuran file selfie yang besar (> 2MB)")
    public void testRegisterSelfieLargeSize(){
        log.info("Starting test: TC-REG-11 - Registrasi dengan ukuran file selfie yang besar (> 2MB)");
        String projectPath = System.getProperty("user.dir");
        String imagePath = projectPath + "\\src\\test\\resources\\testdata\\bigselfie.jpg";
        registerPage.doRegister("D7231978", "Izzatun Nadhiroh", "izza@mail.com", "h@dir12345", imagePath);
        Assert.assertTrue(registerPage.isAlertMessageDisplayed());
        Assert.assertEquals(registerPage.getAlertMessage(), "*File harus dibawah 2 MB");
    }

    @Test(priority = 12, description = "TC-REG-12 - Registrasi dengan akun yang sudah didaftarkan dan belum diverifikasi admin")
    public void testRegisterAccountAlreadyRegisteredAndNotVerifiedAdmin(){
        log.info("Starting test: TC-REG-12 - Registrasi dengan akun yang sudah didaftarkan dan belum diverifikasi admin");
        String projectPath = System.getProperty("user.dir");
        String imagePath = projectPath + "\\src\\test\\resources\\testdata\\selfie.jpg";
        registerPage.doRegister("D7231978", "Izzatun Nadhiroh", "izza@mail.com", "h@dir12345", imagePath);
        Assert.assertTrue(registerPage.isAlertMessageDisplayed());
        Assert.assertEquals(registerPage.getAlertMessage(), "Akun sudah terdaftar di sistem dan belum terverifikasi");
    }

    @Test(priority = 13, description = "TC-REG-13 - Navigasi ke halaman Login")
    public void testRegisterNavigateToLogin(){
        log.info("Starting test: TC-REG-13 - Navigasi ke halaman Login");
        registerPage.clickRegisterButton();
        registerPage.clickBackToLoginButton();
        Assert.assertEquals(driver.getCurrentUrl(), Constants.URL);
    }
}
