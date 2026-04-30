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

public class IzinPulangCepatTest {
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

    @Test(priority = 2, description = "TC-IZN-03 - Verifikasi tab Pulang Cepat")
    public void testTabPulangCepat() {
        izinPage.pilihTab("Pulang Cepat");
        Assert.assertTrue(izinPage.btnAjukanPulangCepat.isDisplayed());
    }

    @Test(priority = 3, description = "TC-IZN-04 - Pengajuan pulang cepat dengan data valid")
    public void testPulangCepatValid() {
        // Gunakan parameter "26" untuk tanggal dan "14" untuk jam
        izinPage.isiFormPulangCepat("27", "14", "Acara Keluarga");
    }

    @Test(priority = 4, description = "TC-IZN-5 - Pengajuan pulang cepat dengan tanggal kosong")
    public void testTanggalKosong() {
        izinPage.isiFormPulangCepat("", "14", "Acara Keluarga");
        Assert.assertEquals(izinPage.getErrorMessage("Tanggal Harus diisi!"), "Tanggal Harus diisi!");
    }

    @Test(priority = 5, description = "TC-IZN-11 - Pengajuan pulang cepat dengan jam kosong")
    public void testJamKosong() {
        izinPage.isiFormPulangCepat("27", "", "Acara Keluarga");
        Assert.assertEquals(izinPage.getErrorMessage("Jam Harus diisi!"), "Jam Harus diisi!");
    }

    @Test(priority = 6, description = "TC-IZN-12 - Pengajuan pulang cepat dengan keterangan kosong")
    public void testKeteranganKosong() {
        izinPage.isiFormPulangCepat("27", "14", "");
        Assert.assertEquals(izinPage.getErrorMessage("Keterangan Harus diisi!"), "Keterangan Harus diisi!");
    }

    @Test(priority = 7, description = "TC-IZN-13 - Pengajuan pulang cepat dengan semua field kosong")
    public void testSemuaFieldKosong() {
        izinPage.isiFormPulangCepat("", "", "");
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
