package com.hadirapp.tests.requests;
import com.hadirapp.pages.Auth.LoginPage;
import com.hadirapp.pages.Requests.IzinPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.*;
import java.time.Duration;
import java.util.List;

public class IzinOffTest {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
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
        loginPage.doLogin("mafira@gmail.com", "mafira123");
        
        wait.until(ExpectedConditions.urlContains("apps"));
        izinPage = new IzinPage(driver);
    }

    @BeforeMethod
    public void navigate() {
        // Hanya buka halaman sekali di awal atau jika URL bukan di halaman izin
        if (!driver.getCurrentUrl().contains("/permit")) { // sesuaikan dengan URL halaman izinmu
            driver.get("https://magang.dikahadir.com/apps/absent/permit"); // URL langsung halaman izin
        }
        
        // Hapus izinPage.klikMenuIzin() dari sini jika kamu ingin langsung tes tab
    }

    @Test(priority = 1, description = "TC-IZN-01 - Verifikasi navigasi ke halaman Izin")
    public void testNavigasiIzin() {
        log.info("Starting test: TC-IZN-01 - Verifikasi navigasi ke halaman Izin");
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("permit") || currentUrl.contains("izin"), 
            "URL saat ini bukan Halaman Izin: " + currentUrl);
    }

    @Test(priority = 2, description = "TC-IZN-04 - Verifikasi tab Izin Off")
    public void testTabIzinOff() {
        log.info("Starting test: TC-IZN-04 - Verifikasi tab Izin Off");
        izinPage.pilihTab("Izin Off");
        Assert.assertTrue(izinPage.btnAjukanIzinOff.isDisplayed());
    }

    @Test(priority = 3, description = "TC-IZN-05 - Pengajuan izin off dengan data valid")
    public void testIzinOffValid() {
        log.info("Starting test: TC-IZN-05 - Pengajuan izin off dengan data valid");
        izinPage.isiFormIzinOff("27", "Acara Keluarga");
    }

    @Test(priority = 4, description = "TC-IZN-06 - Pengajuan izin off dengan tanggal kosong")
    public void testTanggalKosong() {
        log.info("Starting test: TC-IZN-06 - Pengajuan izin off dengan tanggal kosong");
        izinPage.isiFormIzinOff("", "Acara Keluarga");
        Assert.assertEquals(izinPage.getErrorMessage("Pilih tanggal off!."), "Pilih tanggal off!.");
    }

    @Test(priority = 5, description = "TC-IZN-11 - Pengajuan izin off dengan keterangan kosong")
    public void testKeteranganKosong() {
        log.info("Starting test: TC-IZN-11 - Pengajuan izin off dengan keterangan kosong");
        izinPage.isiFormIzinOff("27", "");
        Assert.assertEquals(izinPage.getErrorMessage("Alasan dan mengambill izin off."), "Alasan dan mengambill izin off.");
    }

    @Test(priority = 6, description = "TC-IZN-12 - Pengajuan izin off dengan semua field kosong")
    public void testSemuaFieldKosong() {
        log.info("Starting test: TC-IZN-12 - Pengajuan izin off dengan semua field kosong");
        izinPage.isiFormIzinOff("", "");
        List<String> errors = izinPage.getAllErrorMessages();
        Assert.assertTrue(errors.contains("Pilih tanggal off!."), "Error untuk tanggal kosong tidak muncul");
        Assert.assertTrue(errors.contains("Alasan dan mengambill izin off."), "Error untuk keterangan kosong tidak muncul");
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
