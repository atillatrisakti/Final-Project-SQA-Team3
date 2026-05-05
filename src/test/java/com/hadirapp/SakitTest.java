package com.hadirapp;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.hadirapp.pages.Auth.LoginPage;
import com.hadirapp.pages.Requests.SakitPage;
import com.hadirapp.utlis.WaitUtils;
import java.time.Duration;
import io.github.bonigarcia.wdm.WebDriverManager;

public class SakitTest {
    WebDriver driver;
    WebDriverWait wait;
    SakitPage sakitPage;

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
        sakitPage = new SakitPage(driver);
    }

    @BeforeMethod
    public void navigate() {
        if (!driver.getCurrentUrl().contains("/sick")) {
            driver.get("https://magang.dikahadir.com/apps/absent/sick");
        }
    }

    @Test(priority = 1, description = "TC-SKT-01 - Verifikasi navigasi ke halaman Sakit")
    public void testNavigasiSakit() {
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("sick"),
                "URL saat ini bukan Halaman Sakit: " + currentUrl);
    }

    @Test(priority = 2, description = "TC-SKT-02 - Sukses ajukan sakit dengan data valid")
    public void testAjukanSakitValid() {
        sakitPage.btnAjukanSakit.click();
        sakitPage.pilihRentangTanggal("6", "8");
        String imagePath = "C:\\project\\java\\Final-Project-SQA-Team3\\Final-Project-SQA-Team3\\src\\test\\java\\com\\hadirapp\\resources\\testdata\\surat_sakit.png";
        sakitPage.uploadDokumen(imagePath);
        sakitPage.btnSubmit.click();
        WaitUtils.waitForUrlContains(driver, "sick", 20);
        Assert.assertTrue(driver.getCurrentUrl().contains("sick"));
    }

    @Test(priority = 3, description = "TC-SKT-03 - Sukses ajukan sakit dengan Foto Kosong")
    public void testAjukanSakitUploadKosong() {
        sakitPage.btnAjukanSakit.click();
        sakitPage.pilihRentangTanggal("6", "8");
        sakitPage.btnSubmit.click();
        WaitUtils.waitForUrlContains(driver, "sick", 20);
        Assert.assertTrue(driver.getCurrentUrl().contains("sick"));
    }

    @Test(priority = 4, description = "TC-SKT-04 - Pengajuan sakit dengan tanggal kosong")
    public void testSakitTanggalKosong() {
        sakitPage.btnAjukanSakit.click();
        String imagePath = "C:\\project\\java\\Final-Project-SQA-Team3\\Final-Project-SQA-Team3\\src\\test\\java\\com\\hadirapp\\resources\\testdata\\surat_sakit.png";
        sakitPage.uploadDokumen(imagePath);
        sakitPage.btnSubmit.click();
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement errorToast = shortWait.until(ExpectedConditions.visibilityOf(sakitPage.toastErrorInvalidDate));
        Assert.assertTrue(errorToast.getText().contains("Incorrect datetime value"),
                "Pesan error toast tidak muncul atau teks salah!");
    }

    @Test(priority = 5, description = "TC-SKT-05 - Pengajuan sakit dengan semua field kosong")
    public void testSakitSemuaFieldKosong() {
        sakitPage.btnAjukanSakit.click();
        sakitPage.klikTombolAjukan();
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement errorToast = shortWait.until(ExpectedConditions.visibilityOf(sakitPage.toastErrorInvalidDate));
        Assert.assertTrue(errorToast.getText().contains("Incorrect datetime value"),
                "Pesan error tidak muncul!");
    }

    @Test(priority = 6, description = "TC-SKT-06 - Error saat input tanggal yang sudah pernah diajukan")
    public void testSakitDataDuplikat() {
        sakitPage.btnAjukanSakit.click();
        sakitPage.pilihRentangTanggal("6", "8");
        String imagePath = "C:\\project\\java\\Final-Project-SQA-Team3\\Final-Project-SQA-Team3\\src\\test\\java\\com\\hadirapp\\resources\\testdata\\surat_sakit.png";
        sakitPage.uploadDokumen(imagePath);
        sakitPage.klikTombolAjukan();
        WebElement errorToast = wait.until(ExpectedConditions.visibilityOf(sakitPage.toastErrorDuplicate));
        Assert.assertTrue(errorToast.getText().contains("sudah melakukan izin sakit"));
    }

    @Test(priority = 7, description = "TC-SKT-07 - Memastikan tombol Reset mengosongkan form")
    public void testResetForm() {
        sakitPage.btnAjukanSakit.click();
        sakitPage.pilihRentangTanggal("10", "12");
        sakitPage.klikReset();
        String valueTanggal = sakitPage.getTeksTanggal();
        System.out.println("Teks tanggal yang didapat sekarang: '" + valueTanggal + "'");
        Assert.assertTrue(valueTanggal.contains("/"),
                "Harusnya dapet '/', tapi malah dapet: " + valueTanggal);
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