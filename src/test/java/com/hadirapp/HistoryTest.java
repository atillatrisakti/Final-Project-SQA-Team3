package com.hadirapp;

import com.hadirapp.pages.Attendance.HistoryPage;
import com.hadirapp.pages.Auth.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import java.time.Duration;

public class HistoryTest {
    WebDriver driver;
    HistoryPage historyPage;
    LoginPage loginPage;
    WebDriverWait wait;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        driver.get("https://magang.dikahadir.com/absen/login");
        loginPage = new LoginPage(driver);
        loginPage.login("mafira@gmail.com", "mafira123");

        wait.until(ExpectedConditions.urlContains("apps"));
        historyPage = new HistoryPage(driver);
    }

    @BeforeMethod
    public void navigateToAbsent() {
        driver.get("https://magang.dikahadir.com/apps/absent");
        wait.until(ExpectedConditions.urlContains("absent"));
    }

    @Test(priority = 1, description = "TC-HIS-01 - Verifikasi navigasi ke halaman activity melalui menu Absensi")
    public void testKlikAbsensi() {
        historyPage.klikAbsensi();
        wait.until(ExpectedConditions.urlContains("/activity"));
        Assert.assertTrue(driver.getCurrentUrl().contains("/activity"), "Gagal navigasi ke halaman Activity");
    }

    @Test(priority = 2, description = "TC-HIS-02 - Verifikasi navigasi ke halaman activity melalui tombol Selengkapnya")
    public void testKlikSelengkapnya() {
        historyPage.klikSelengkapnya();
        wait.until(ExpectedConditions.urlContains("/activity"));
        Assert.assertTrue(driver.getCurrentUrl().contains("/activity"), "Gagal navigasi ke halaman Activity");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}