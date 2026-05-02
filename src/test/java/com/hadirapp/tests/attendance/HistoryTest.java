package com.hadirapp.tests.attendance;

import com.hadirapp.base.BaseTest;
import com.hadirapp.pages.Attendance.HistoryPage;
import com.hadirapp.pages.Auth.LoginPage;
import com.hadirapp.utlis.Constants;
import com.hadirapp.utlis.WaitUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HistoryTest extends BaseTest {
    private HistoryPage historyPage;
    private LoginPage loginPage;

    @BeforeMethod
    public void setUpPages() {
        loginPage = new LoginPage(driver);
        historyPage = new HistoryPage(driver);
        loginPage.doLogin(Constants.EMAIL, Constants.PASSWORD);
        WaitUtils.waitForUrlToBe(driver, Constants.DASHBOARD_URL, 10);
    }

    @Test(priority = 1, description = "TC-HIS-01 - Verifikasi navigasi ke halaman activity melalui tombol Selengkapnya")
    public void testKlikSelengkapnya() {
        log.info("Starting test: TC-HIS-01 - Verifikasi navigasi ke halaman activity melalui tombol Selengkapnya");
        historyPage.klikSelengkapnya();
        WaitUtils.waitForUrlContains(driver, "/activity", 20);
        Assert.assertTrue(driver.getCurrentUrl().contains("/activity"), "Gagal navigasi ke halaman Activity");
    }

    @Test(priority = 2, description = "TC-HIS-02 - Verifikasi navigasi ke halaman activity melalui menu Absensi")
    public void testKlikAbsensi() {
        log.info("Starting test: TC-HIS-02 - Verifikasi navigasi ke halaman activity melalui menu Absensi");
        historyPage.klikAbsensi();
        WaitUtils.waitForUrlContains(driver, "/activity", 20);
        Assert.assertTrue(driver.getCurrentUrl().contains("/activity"), "Gagal navigasi ke halaman Activity");
    }
}
