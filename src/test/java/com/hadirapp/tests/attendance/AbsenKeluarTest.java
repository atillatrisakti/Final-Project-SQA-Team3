package com.hadirapp.tests.attendance;

import com.hadirapp.pages.Auth.LoginPage;
import com.hadirapp.utlis.Constants;
import com.hadirapp.utlis.WaitUtils;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.hadirapp.base.BaseTest;
import com.hadirapp.pages.Attendance.AbsenKeluarPage;



public class AbsenKeluarTest extends BaseTest {
    private LoginPage loginPage;
    private AbsenKeluarPage absenKeluarPage;

    @BeforeMethod
    public void setUpPages() {
        loginPage = new LoginPage(driver);
        absenKeluarPage = new AbsenKeluarPage(driver);
    }

    @Test(priority = 1, description = "TC-ABS-06 - Absen Pulang Tanpa Note")
    public void testAbsenPulangMafiraTanpaNote() {
        log.info("TC-ABS-06 - Absen Pulang Tanpa Note");
        loginPage.doLogin(Constants.EMAIL, Constants.PASSWORD);
        
        WaitUtils.waitForUrlContains(driver, "apps", 20);
        absenKeluarPage.klikKeluar(); 
        absenKeluarPage.isiNote(""); // Tanpa Note
        absenKeluarPage.klikAbsenPulang();
        absenKeluarPage.logout();
    }

    @Test(priority = 2, description = "TC-ABS-07 - Absen Pulang Dengan Note")
    public void testAbsenPulangHadirSqaDenganNote() {
        log.info("TC-ABS-07 - Absen Pulang Dengan Note");
        loginPage.doLogin(Constants.EMAIL_2, Constants.PASSWORD_2);
        
        WaitUtils.waitForUrlContains(driver, "apps", 20);
        absenKeluarPage.klikKeluar();
        absenKeluarPage.isiNote("Selesai Bekerja"); // Dengan Note
        absenKeluarPage.klikAbsenPulang();
        absenKeluarPage.logout();
    }

}