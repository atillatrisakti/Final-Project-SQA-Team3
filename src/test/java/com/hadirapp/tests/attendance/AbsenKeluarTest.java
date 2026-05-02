package com.hadirapp.tests.attendance;

import com.hadirapp.base.BaseTest;
import com.hadirapp.pages.Auth.LoginPage;
import com.hadirapp.pages.Attendance.AbsenKeluarPage;
import com.hadirapp.utlis.Constants;
import com.hadirapp.utlis.WaitUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AbsenKeluarTest extends BaseTest {
    private LoginPage loginPage;
    private AbsenKeluarPage absenKeluarPage;

    @BeforeMethod
    public void setUpPages() {
        loginPage = new LoginPage(driver);
        absenKeluarPage = new AbsenKeluarPage(driver);
    }

    @Test(priority = 1)
    public void testAbsenPulangMafiraTanpaNote() {
        log.info("Starting test: testAbsenPulangMafiraTanpaNote");
        loginPage.doLogin(Constants.EMAIL, Constants.PASSWORD);
        WaitUtils.waitForUrlContains(driver, "apps", 20);
        
        driver.get(Constants.DASHBOARD_URL);
        
        absenKeluarPage.klikKeluar(); 
        absenKeluarPage.isiNote(""); // Tanpa Note
        absenKeluarPage.klikAbsenPulang();
        absenKeluarPage.logout();
    }

    @Test(priority = 2)
    public void testAbsenPulangHadirSqaDenganNote() {
        log.info("Starting test: testAbsenPulangHadirSqaDenganNote");
        loginPage.doLogin(Constants.EMAIL_2, Constants.PASSWORD_2);
        WaitUtils.waitForUrlContains(driver, "apps", 20);
        
        driver.get(Constants.DASHBOARD_URL);
        
        absenKeluarPage.klikKeluar();
        absenKeluarPage.isiNote("Selesai Bekerja"); // Dengan Note
        absenKeluarPage.klikAbsenPulang();
        absenKeluarPage.logout();
    }
}
