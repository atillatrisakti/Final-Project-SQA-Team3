package com.hadirapp;

import com.hadirapp.pages.Auth.LoginPage;
import com.hadirapp.pages.Attendance.AbsenKeluarPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import java.time.Duration;

public class AbsenKeluarTest {
    WebDriver driver;
    WebDriverWait wait;
    LoginPage loginPage;
    AbsenKeluarPage absenKeluarPage;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        
        loginPage = new LoginPage(driver);
        absenKeluarPage = new AbsenKeluarPage(driver);
    }

    @Test(priority = 1)
    public void testAbsenPulangMafiraTanpaNote() {
        driver.get("https://magang.dikahadir.com/absen/login");
        loginPage.login("mafira@gmail.com", "mafira123");
        
        wait.until(ExpectedConditions.urlContains("apps"));
        driver.get("https://magang.dikahadir.com/apps/absent");
        
        absenKeluarPage.klikKeluar(); 
        absenKeluarPage.isiNote(""); // Tanpa Note
        absenKeluarPage.klikAbsenPulang();
        absenKeluarPage.logout();
    }

    @Test(priority = 2)
    public void testAbsenPulangHadirSqaDenganNote() {
        driver.get("https://magang.dikahadir.com/absen/login");
        loginPage.login("hadirsqa1@gmail.com", "SQA@Hadir12345");
        
        wait.until(ExpectedConditions.urlContains("apps"));
        driver.get("https://magang.dikahadir.com/apps/absent");
        
        absenKeluarPage.klikKeluar();
        absenKeluarPage.isiNote("Selesai Bekerja"); // Dengan Note
        absenKeluarPage.klikAbsenPulang();
        absenKeluarPage.logout();
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}