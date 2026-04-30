package com.hadirapp;

import com.hadirapp.pages.Attendance.KoreksiAbsenPage;
import com.hadirapp.pages.Auth.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import java.time.Duration;
import java.util.List;

public class KoreksiAbsenTest {
    WebDriver driver;
    WebDriverWait wait;
    LoginPage loginPage;
    KoreksiAbsenPage koreksiPage;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("https://magang.dikahadir.com/absen/login");
        loginPage = new LoginPage(driver);
        loginPage.login("mafira@gmail.com", "mafira123");

        wait.until(ExpectedConditions.urlContains("apps"));
        koreksiPage = new KoreksiAbsenPage(driver);
    }

    @BeforeMethod
    public void navigate() {
        if (!driver.getCurrentUrl().contains("/correction")) {
            driver.get("https://magang.dikahadir.com/apps/absent/correction");
        }
    }

    @Test(priority = 1, description = "TC-KOR-01 - Verifikasi tampilan form koreksi")
    public void testKlikTombolKoreksi() {
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("correction") || currentUrl.contains("Koreksi Absen"),
                "URL saat ini bukan Halaman Koreksi Absen: " + currentUrl);
    }

    @Test(priority = 2, description = "TC-KOR-02 - Koreksi absen dengan data valid")
    public void testKoreksiDataValid() {
        // Tambahkan wait agar tidak klik tombol sebelum elemen siap
        wait.until(ExpectedConditions.elementToBeClickable(koreksiPage.ajukanKoreksiButton)).click();
        koreksiPage.isiDataKoreksi("28", "8", "17", "WFO");
        String actualMessage = koreksiPage.getAlertSuccessMessage();
        Assert.assertTrue(actualMessage.contains("Berhasil koreksi absen"),
                "Pesan sukses tidak sesuai! Teks yang muncul: " + actualMessage);
    }

    @Test(priority = 3, description = "TC-KOR-03 - Koreksi absen dengan jam masuk kosong")
    public void testKoreksiJamMasukKosong() {
        koreksiPage.ajukanKoreksiButton.click();
        // Mengirim string kosong untuk jam masuk
        koreksiPage.isiDataKoreksi("28", "", "17", "WFO");
    }

    @Test(priority = 4, description = "TC-KOR-04 - Koreksi absen dengan jam keluar kosong")
    public void testKoreksiJamKeluarKosong() {
        koreksiPage.ajukanKoreksiButton.click();
        // Mengirim string kosong untuk jam keluar
        koreksiPage.isiDataKoreksi("28", "8", "", "WFO");
    }

    @Test(priority = 5, description = "TC-KOR-05 - Koreksi absen dengan tipe absen kosong")
    public void testKoreksiTipeKosong() {
        koreksiPage.ajukanKoreksiButton.click();
        // Mengirim string kosong untuk tipe
        koreksiPage.isiDataKoreksi("28", "8", "17", "");
    }

    @Test(priority = 6, description = "TC-KOR-06 - Koreksi absen dengan semua field kosong")
    public void testKoreksiSemuaKosong() {
        koreksiPage.ajukanKoreksiButton.click();
        koreksiPage.isiDataKoreksi("", "", "", "");
        Assert.assertEquals(koreksiPage.getErrorMessage("Salah satu harus diisi!"), "Salah satu harus diisi!");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null)
            driver.quit();
    }

    @AfterMethod
    public void cleanUp() {
        driver.navigate().refresh();
    }
}