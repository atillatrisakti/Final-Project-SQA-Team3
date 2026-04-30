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

public class KoreksiAbsenTest {
    WebDriver driver;
    WebDriverWait wait;
    KoreksiAbsenPage koreksiPage;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("https://magang.dikahadir.com/absen/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("mafira@gmail.com", "mafira123");

        wait.until(ExpectedConditions.urlContains("apps"));
        koreksiPage = new KoreksiAbsenPage(driver);
    }

    @BeforeMethod
    public void prepareForm() {
        driver.get("https://magang.dikahadir.com/apps/absent");
        wait.until(ExpectedConditions.urlContains("absent"));

        // Alur: Klik menu -> Klik tombol biru 'Ajukan Koreksi' -> Form muncul
        koreksiPage.klikMenuKoreksiAbsen();

        // Beri waktu animasi modal muncul
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
    }

    @Test(priority = 1, description = "TC-KOR-01 - Verifikasi tampilan form koreksi")
    public void testKlikTombolKoreksi() {
        Assert.assertTrue(driver.getPageSource().contains("Ajukan Koreksi"));
    }

    @Test(priority = 2, description = "TC-KOR-02 - Koreksi data valid lengkap")
    public void testKoreksiDataValid() {
        koreksiPage.isiDataKoreksi("8", "17", "wfo");
    }

    @Test(priority = 3, description = "TC-KOR-03 - Koreksi data invalid (Jam Masuk Kosong)")
    public void testKoreksiDataInvalid1() {
        koreksiPage.isiDataKoreksi("", "17:00", "");
    }

    @Test(priority = 4, description = "TC-KOR-04 - Koreksi data valid (Jam Pulang Kosong)")
    public void testKoreksiDataInvalid2() {
        koreksiPage.isiDataKoreksi("08:00", "", "wfo");
    }

    @Test(priority = 5, description = "TC-KOR-05 - Koreksi data invalid (Keterangan Kosong)")
    public void testKoreksiDataInvalid3() {
        koreksiPage.isiDataKoreksi("08:00", "17:00", "");
    }

    @Test(priority = 6, description = "TC-KOR-06 - Koreksi dengan semua input kosong")
    public void testKoreksiSemuaKosong() {
        koreksiPage.isiDataKoreksi("", "", "");
        String actualError = koreksiPage.getErrorMessage("Harus diisi");
        Assert.assertTrue(actualError.contains("Harus diisi"), "Pesan error validasi tidak muncul!");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}