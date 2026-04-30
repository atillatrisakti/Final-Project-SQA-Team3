package com.hadirapp.tests.requests;

import com.hadirapp.base.BaseTest;
import com.hadirapp.pages.Auth.LoginPage;
import com.hadirapp.pages.Requests.IzinPage;
import com.hadirapp.utlis.Constants;
import com.hadirapp.utlis.WaitUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class IzinTerlambatTest extends BaseTest {
    private IzinPage izinPage;
    private LoginPage loginPage;

    @BeforeMethod
    public void setUpPages() {
        loginPage = new LoginPage(driver);
        izinPage = new IzinPage(driver);
        
        loginPage.doLogin(Constants.EMAIL, Constants.PASSWORD);
        WaitUtils.waitForUrlContains(driver, "apps", 10);

        if (!driver.getCurrentUrl().contains("/permit")) { 
            driver.get(Constants.URL.replace("absen/login", "apps/absent/permit")); 
        }
    }

    @Test(priority = 1, description = "TC-IZN-01 - Verifikasi navigasi ke halaman Izin")
    public void testNavigasiIzin() {
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("permit") || currentUrl.contains("izin"), 
            "URL saat ini bukan Halaman Izin: " + currentUrl);
    }

    @Test(priority = 2, description = "TC-IZN-02 - Verifikasi tab Terlambat")
    public void testTabTerlambat() {
        izinPage.pilihTab("Terlambat");
        Assert.assertTrue(izinPage.btnAjukanTerlambat.isDisplayed());
    }

    @Test(priority = 3, description = "TC-IZN-03 - Pengajuan terlambat dengan data valid")
    public void testPengajuanTerlambatValid() {
        izinPage.isiFormTerlambat("28", "8", "Terjebak Macet");
    }

    @Test(priority = 4, description = "TC-IZN-04 - Pengajuan terlambat dengan tanggal kosong")
    public void testTanggalKosong() {
        izinPage.isiFormTerlambat("", "8", "Terjebak Macet");
        Assert.assertEquals(izinPage.getErrorMessage("Tanggal Harus diisi!"), "Tanggal Harus diisi!");
    }

    @Test(priority = 5, description = "TC-IZN-05 - Pengajuan terlambat dengan jam kosong")
    public void testJamKosong() {
        izinPage.isiFormTerlambat("28", "", "Terjebak Macet");
        Assert.assertEquals(izinPage.getErrorMessage("Jam Harus diisi!"), "Jam Harus diisi!");
    }

    @Test(priority = 6, description = "TC-IZN-06 - Pengajuan terlambat dengan keterangan kosong")
    public void testNotesKosong() {
        izinPage.isiFormTerlambat("28", "8", "");
        Assert.assertEquals(izinPage.getErrorMessage("Keterangan Harus diisi!"), "Keterangan Harus diisi!");
    }

    @Test(priority = 7, description = "TC-IZN-07 - Pengajuan terlambat dengan semua field kosong")
    public void testSemuaFieldKosong() {
        izinPage.isiFormTerlambat("", "", "");
        List<String> errors = izinPage.getAllErrorMessages();
        Assert.assertTrue(errors.contains("Tanggal Harus diisi!"));
        Assert.assertTrue(errors.contains("Jam Harus diisi!"));
        Assert.assertTrue(errors.contains("Keterangan Harus diisi!"));
    }
}