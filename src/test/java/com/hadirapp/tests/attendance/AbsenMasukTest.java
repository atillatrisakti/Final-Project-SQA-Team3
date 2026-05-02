package com.hadirapp.tests.attendance;

import com.hadirapp.base.BaseTest;
import com.hadirapp.pages.Auth.LoginPage;
import com.hadirapp.pages.Attendance.AbsenMasukPage;
import com.hadirapp.utlis.Constants;
import com.hadirapp.utlis.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AbsenMasukTest extends BaseTest {
    private LoginPage loginPage;
    private AbsenMasukPage absenMasukPage;

    @BeforeMethod
    public void setUpPages() {
        loginPage = new LoginPage(driver);
        absenMasukPage = new AbsenMasukPage(driver);
    }

    @Test(priority = 1, description = "TC-ABS-02 - Absen masuk dengan Tipe WFO")
    public void testAbsenMasukSukses() {
        log.info("Starting test: TC-ABS-02 - Absen masuk dengan Tipe WFO");
        loginPage.doLogin(Constants.EMAIL, Constants.PASSWORD);
        WaitUtils.waitForUrlContains(driver, "apps", 10);
        absenMasukPage.klikMenuAbsenMasuk();
        absenMasukPage.ambilFoto();
        absenMasukPage.pilihTipeAbsen("WFO");
        absenMasukPage.isiNote("Hadir Tepat Waktu - Via Automation");
        absenMasukPage.submitAbsenMasuk();
        WebElement btnKeluar = WaitUtils.waitForElementVisible(driver, By.xpath("//button[.//p[text()='Keluar']]"), 10);
        Assert.assertTrue(btnKeluar.isDisplayed(), "Tombol Keluar seharusnya muncul di history setelah berhasil absen");
    }

    @Test(priority = 2, description = "TC-ABS-03 - Absen masuk dengan Tipe WFH")
    public void testAbsenMasukWfh() {
        log.info("Starting test: TC-ABS-03 - Absen masuk dengan Tipe WFH");
        loginPage.doLogin(Constants.EMAIL_2, Constants.PASSWORD_2);
        WaitUtils.waitForUrlContains(driver, "apps", 10);
        absenMasukPage.klikMenuAbsenMasuk();
        absenMasukPage.ambilFoto();
        absenMasukPage.pilihTipeAbsen("WFH");
        absenMasukPage.isiNote("WFH - Via Automation");
        absenMasukPage.submitAbsenMasuk();
        WebElement btnKeluar = WaitUtils.waitForElementVisible(driver, By.xpath("//button[.//p[text()='Keluar']]"), 10);
        Assert.assertTrue(btnKeluar.isDisplayed(), "Tombol Keluar seharusnya muncul di history setelah berhasil absen");
    }

    @Test(priority = 3, description = "TC-ABS-04 - Absen masuk tanpa Note")
    public void testAbsenMasukTanpaNote() {
        log.info("Starting test: TC-ABS-04 - Absen masuk tanpa Note");
        loginPage.doLogin(Constants.EMAIL_3, Constants.PASSWORD_3);
        WaitUtils.waitForUrlContains(driver, "apps", 10);
        absenMasukPage.klikMenuAbsenMasuk();
        absenMasukPage.ambilFoto();
        absenMasukPage.pilihTipeAbsen("WFO");
        absenMasukPage.submitAbsenMasuk();
        WebElement btnKeluar = WaitUtils.waitForElementVisible(driver, By.xpath("//button[.//p[text()='Keluar']]"), 10);
        Assert.assertTrue(btnKeluar.isDisplayed(), "Tombol Keluar seharusnya muncul di history setelah berhasil absen");
    }
}
