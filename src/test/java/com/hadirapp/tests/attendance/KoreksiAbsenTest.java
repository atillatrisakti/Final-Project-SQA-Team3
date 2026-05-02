package com.hadirapp.tests.attendance;

import com.hadirapp.base.BaseTest;
import com.hadirapp.pages.Attendance.KoreksiAbsenPage;
import com.hadirapp.pages.Auth.LoginPage;
import com.hadirapp.utlis.Constants;
import com.hadirapp.utlis.WaitUtils;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class KoreksiAbsenTest extends BaseTest {
    private LoginPage loginPage;
    private KoreksiAbsenPage koreksiPage;

    @BeforeMethod
    public void setUpPages() {
        loginPage = new LoginPage(driver);
        koreksiPage = new KoreksiAbsenPage(driver);
        
        if (driver.getCurrentUrl().contains("login")) {
            loginPage.doLogin(Constants.EMAIL, Constants.PASSWORD);
            WaitUtils.waitForUrlContains(driver, "apps", 10);
        }

        if (!driver.getCurrentUrl().contains("/correction")) {
            driver.get(Constants.URL.replace("absen/login", "apps/absent/correction"));
        }
    }

    @Test(priority = 1, description = "TC-KOR-01 - Verifikasi tampilan form koreksi")
    public void testKlikTombolKoreksi() {
        log.info("Starting test: TC-KOR-01 - Verifikasi tampilan form koreksi");
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("correction") || currentUrl.contains("Koreksi Absen"),
                "URL saat ini bukan Halaman Koreksi Absen: " + currentUrl);
    }

    @Test(priority = 2, description = "TC-KOR-02 - Koreksi absen dengan data valid")
    public void testKoreksiDataValid() {
        log.info("Starting test: TC-KOR-02 - Koreksi absen dengan data valid");
        WaitUtils.getExplicitWait(driver, 10).until(ExpectedConditions.elementToBeClickable(koreksiPage.ajukanKoreksiButton)).click();
        koreksiPage.isiDataKoreksi("28", "8", "17", "WFO");
        String actualMessage = koreksiPage.getAlertSuccessMessage();
        Assert.assertTrue(actualMessage.contains("Berhasil koreksi absen"),
                "Pesan sukses tidak sesuai! Teks yang muncul: " + actualMessage);
    }

    @Test(priority = 3, description = "TC-KOR-03 - Koreksi absen dengan jam masuk kosong")
    public void testKoreksiJamMasukKosong() {
        log.info("Starting test: TC-KOR-03 - Koreksi absen dengan jam masuk kosong");
        koreksiPage.ajukanKoreksiButton.click();
        koreksiPage.isiDataKoreksi("28", "", "17", "WFO");
    }

    @Test(priority = 4, description = "TC-KOR-04 - Koreksi absen dengan jam keluar kosong")
    public void testKoreksiJamKeluarKosong() {
        log.info("Starting test: TC-KOR-04 - Koreksi absen dengan jam keluar kosong");
        koreksiPage.ajukanKoreksiButton.click();
        koreksiPage.isiDataKoreksi("28", "8", "", "WFO");
    }

    @Test(priority = 5, description = "TC-KOR-05 - Koreksi absen dengan tipe absen kosong")
    public void testKoreksiTipeKosong() {
        log.info("Starting test: TC-KOR-05 - Koreksi absen dengan tipe absen kosong");
        koreksiPage.ajukanKoreksiButton.click();
        koreksiPage.isiDataKoreksi("28", "8", "17", "");
    }

    @Test(priority = 6, description = "TC-KOR-06 - Koreksi absen dengan semua field kosong")
    public void testKoreksiSemuaKosong() {
        log.info("Starting test: TC-KOR-06 - Koreksi absen dengan semua field kosong");
        koreksiPage.ajukanKoreksiButton.click();
        koreksiPage.isiDataKoreksi("", "", "", "");
        Assert.assertEquals(koreksiPage.getErrorMessage("Salah satu harus diisi!"), "Salah satu harus diisi!");
    }
}
