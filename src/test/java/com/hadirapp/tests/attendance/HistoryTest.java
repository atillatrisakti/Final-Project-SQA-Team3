package com.hadirapp.tests.attendance;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.hadirapp.base.BaseTest;
import com.hadirapp.pages.Attendance.HistoryPage;
import com.hadirapp.pages.Auth.LoginPage;
import com.hadirapp.utlis.Constants;
import com.hadirapp.utlis.WaitUtils;

public class HistoryTest extends BaseTest {
    private LoginPage loginPage;
    private HistoryPage historyPage;

    @BeforeMethod
    public void setUpPages() {
        loginPage = new LoginPage(driver);
        historyPage = new HistoryPage(driver);
    }

     @Test(priority = 1, description = "TC-HIS-01 - Verifikasi navigasi ke halaman activity melalui menu Absensi")
    public void testKlikAbsensi() {
        log.info("TC-HIS-01 - Verifikasi navigasi ke halaman activity melalui menu Absensi");   
        loginPage.doLogin(Constants.EMAIL, Constants.PASSWORD);
        WaitUtils.waitForUrlContains(driver, "apps", 20);
        historyPage.klikAbsensi();
        WaitUtils.waitForUrlContains(driver, "activity", 10);
        Assert.assertTrue(driver.getCurrentUrl().contains("/activity"), "Gagal navigasi ke halaman Activity");
    }

    @Test(priority = 2, description = "TC-HIS-02 - Verifikasi navigasi ke halaman activity melalui tombol Selengkapnya")
    public void testKlikSelengkapnya() {
        log.info("TC-HIS-02 - Verifikasi navigasi ke halaman activity melalui tombol Selengkapnya");
        loginPage.doLogin(Constants.EMAIL, Constants.PASSWORD);
        WaitUtils.waitForUrlContains(driver, "apps", 20);
        historyPage.klikSelengkapnya();
        WaitUtils.waitForUrlContains(driver, "activity", 20);
        Assert.assertTrue(driver.getCurrentUrl().contains("/activity"), "Gagal navigasi ke halaman Activity");
    }

}
