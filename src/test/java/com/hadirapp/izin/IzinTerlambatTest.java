package com.hadirapp.izin;

import com.hadirapp.pages.Auth.LoginPage;
import com.hadirapp.pages.Requests.IzinPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import java.time.Duration;
import java.util.List;

public class IzinTerlambatTest {
    WebDriver driver;
    WebDriverWait wait;
    IzinPage izinPage;

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
        izinPage = new IzinPage(driver);
    }

    @BeforeMethod
    public void navigate() {
        if (!driver.getCurrentUrl().contains("/permit")) { 
            driver.get("https://magang.dikahadir.com/apps/absent/permit"); 
        }
    }

    @Test(priority = 1, description = "TC-IZN-01 - Verifikasi navigasi ke halaman Izin")
    public void testNavigasiIzin() {
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("permit") || currentUrl.contains("izin"), 
            "URL saat ini bukan Halaman Izin: " + currentUrl);
    }

    @Test(priority = 2, description = "TC-IZN-02 - Verifikasi tab Terlambat")
    public void testTabTerlambat() {
        izinPage.pilihTab("Terlambat");
        Assert.assertTrue(izinPage.btnAjukanTerlambat.isDisplayed());
    }

    @Test(priority = 3, description = "TC-IZN-03 - Pengajuan terlambat dengan data valid")
    public void testPengajuanTerlambatValid() {
        izinPage.isiFormTerlambat("28", "8", "Terjebak Macet");
    }

    @Test(priority = 4, description = "TC-IZN-04 - Pengajuan terlambat dengan tanggal kosong")
    public void testTanggalKosong() {
        izinPage.isiFormTerlambat("", "8", "Terjebak Macet");
        Assert.assertEquals(izinPage.getErrorMessage("Tanggal Harus diisi!"), "Tanggal Harus diisi!");
    }

    @Test(priority = 5, description = "TC-IZN-05 - Pengajuan terlambat dengan jam kosong")
    public void testJamKosong() {
        izinPage.isiFormTerlambat("28", "", "Terjebak Macet");
        Assert.assertEquals(izinPage.getErrorMessage("Jam Harus diisi!"), "Jam Harus diisi!");
    }

    @Test(priority = 6, description = "TC-IZN-06 - Pengajuan terlambat dengan keterangan kosong")
    public void testNotesKosong() {
        izinPage.isiFormTerlambat("28", "8", "");
        Assert.assertEquals(izinPage.getErrorMessage("Keterangan Harus diisi!"), "Keterangan Harus diisi!");
    }

    @Test(priority = 7, description = "TC-IZN-07 - Pengajuan terlambat dengan semua field kosong")
    public void testSemuaFieldKosong() {
        izinPage.isiFormTerlambat("", "", "");
        List<String> errors = izinPage.getAllErrorMessages();
        Assert.assertTrue(errors.contains("Tanggal Harus diisi!"));
        Assert.assertTrue(errors.contains("Jam Harus diisi!"));
        Assert.assertTrue(errors.contains("Keterangan Harus diisi!"));
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    @AfterMethod
    public void cleanUp() {
        driver.navigate().refresh();
    }
    
}