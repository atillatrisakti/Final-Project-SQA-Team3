package com.hadirapp.tests.attendance;

import com.hadirapp.base.BaseTest;
import com.hadirapp.pages.Attendance.AbsenMasukPage;
import com.hadirapp.pages.Auth.LoginPage;
import com.hadirapp.utlis.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AbsenMasukBlockCameraTest extends BaseTest {

    private LoginPage loginPage;
    private AbsenMasukPage absenMasukPage;

    @BeforeMethod
    @Override
    public void setup() {
        System.setProperty("camera.permission", "2"); 
        
        super.setup();
        
        loginPage = new LoginPage(driver);
        absenMasukPage = new AbsenMasukPage(driver);
    }

    @Test(priority = 1, description = "TC-ABS-01 - Absensi (Masuk) - Penolakan akses kamera (Block)")
    public void testAbsenMasukTolakKamera() {
        log.info("Starting test: TC-ABS-01 - Absensi (Masuk) - Penolakan akses kamera (Block)");
        loginPage.doLogin("dada@mail.com", "trisakti");
        WaitUtils.waitForUrlContains(driver, "apps", 10);
        
        absenMasukPage.klikMenuAbsenMasuk();
        
        org.openqa.selenium.Alert alert = WaitUtils.waitForAlert(driver, 10);
        String alertText = alert.getText();
        log.info("Native Browser Alert muncul dengan teks: {}", alertText);
        String alertTextLower = alertText.toLowerCase();
        Assert.assertTrue(alertTextLower.contains("permission denied") || 
                        alertTextLower.contains("notallowederror") || 
                        alertTextLower.contains("notfounderror") || 
                        alertTextLower.contains("kamera") || 
                        alertTextLower.contains("izin"), 
                "Alert native browser tidak sesuai ekspektasi. Actual alert: " + alertText);
        alert.accept();
        
        WebElement modalPeringatan = WaitUtils.waitForElementVisible(driver, By.xpath("//*[contains(text(), 'Akses kamera harus diizinkan')]"), 10);
        Assert.assertTrue(modalPeringatan.isDisplayed(), "Modal peringatan dari aplikasi tidak muncul");
        
        WebElement btnOke = WaitUtils.waitForElementClickable(driver, By.xpath("//button[contains(text(), 'Oke') or contains(text(), 'OK')]"), 10);
        btnOke.click();
        
        log.info("Test TC-ABS-01: Skenario Block Camera berhasil divalidasi");
    }

    @AfterMethod
    @Override
    public void teardown() {
        super.teardown();
        System.clearProperty("camera.permission");
    }
}
