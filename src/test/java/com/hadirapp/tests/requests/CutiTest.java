package com.hadirapp.tests.requests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.hadirapp.base.BaseTest;
import com.hadirapp.pages.Auth.LoginPage;
import com.hadirapp.pages.Requests.CutiPage;
import com.hadirapp.utlis.Constants;
import com.hadirapp.utlis.WaitUtils;

public class CutiTest extends BaseTest {
    private LoginPage loginPage;
    private CutiPage cutiPage;

    @BeforeMethod
    public void setUp() {
        loginPage = new LoginPage(driver);
        cutiPage = new CutiPage(driver);
    }

    @Test(priority = 1, description = "TC-CUTI-01 - Cuti")
    public void testCuti() {
        log.info("TC-CUTI-01 - Cuti");
        loginPage.doLogin(Constants.EMAIL, Constants.PASSWORD);
        WaitUtils.waitForUrlContains(driver, "apps", 20);
        cutiPage.doCuti();
        cutiPage.pilihTipeCuti("Cuti Tahunan");
        cutiPage.pilihTanggal("May", "2026", "5", "8");
        cutiPage.fillNotes("Cuti");
        cutiPage.clickAjukanButton();
        Assert.assertTrue(cutiPage.isHalamanCutiDisplayed(), "Halaman Cuti tidak tampil setelah submit");
        String cutiTerbaru = cutiPage.getCutiTerbaruText();
        Assert.assertFalse(cutiTerbaru.isEmpty(), "Card cuti terbaru tidak ditemukan!");
    }
}
