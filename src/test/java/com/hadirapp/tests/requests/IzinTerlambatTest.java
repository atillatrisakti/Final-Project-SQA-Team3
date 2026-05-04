package com.hadirapp.tests.requests;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.hadirapp.base.BaseTest;
import com.hadirapp.pages.Auth.LoginPage;
import com.hadirapp.pages.Requests.IzinPage;
import com.hadirapp.utlis.Constants;
import com.hadirapp.utlis.WaitUtils;

public class IzinTerlambatTest extends BaseTest{
    private LoginPage loginPage;
    private IzinPage izinPage;

    @BeforeMethod
    public void setUpPages() {
        loginPage = new LoginPage(driver);
        izinPage = new IzinPage(driver);
        loginPage.doLogin(Constants.EMAIL_2, Constants.PASSWORD_2);
        WaitUtils.waitForUrlContains(driver, "apps", 20);
        izinPage.klikMenuIzin();
        WaitUtils.waitForUrlContains(driver, "permit", 20);
    }

    @Test(priority = 1, description = "TC-IZN-02 - Verifikasi tab Terlambat")
    public void testTabTerlambat() {
        log.info("TC-IZN-02 - Verifikasi tab Terlambat");
        izinPage.pilihTab("Terlambat");
        Assert.assertTrue(izinPage.btnAjukanTerlambat.isDisplayed());
    }

    @Test(priority = 2, description = "TC-IZN-03 - Pengajuan terlambat dengan data valid")
    public void testPengajuanTerlambatValid() {
        log.info("TC-IZN-03 - Pengajuan terlambat dengan data valid");
        String hariIni = Constants.getTodayDate();
        izinPage.isiFormTerlambat(hariIni, "8", "Terjebak Macet");
        Assert.assertTrue(izinPage.isHalamanIzinDisplayed(),
            "Card izin tidak tampil setelah submit!");
        String cardText = izinPage.getIzinTerbaruText();
        Assert.assertFalse(cardText.isEmpty(), "Card izin terbaru tidak ditemukan!");
    }

    @Test(priority = 3, description = "TC-IZN-04 - Pengajuan terlambat dengan tanggal kosong")
    public void testTanggalKosong() {
        log.info("TC-IZN-04 - Pengajuan terlambat dengan tanggal kosong");
        izinPage.isiFormTerlambat("", "8", "Terjebak Macet");
        Assert.assertEquals(izinPage.getErrorMessage("Tanggal Harus diisi!"), "Tanggal Harus diisi!");
    }

    @Test(priority = 4, description = "TC-IZN-05 - Pengajuan terlambat dengan jam kosong")
    public void testJamKosong() {
        log.info("TC-IZN-05 - Pengajuan terlambat dengan jam kosong");
        String hariIni = Constants.getTodayDate();
        izinPage.isiFormTerlambat(hariIni, "", "Terjebak Macet");
        Assert.assertEquals(izinPage.getErrorMessage("Jam Harus diisi!"), "Jam Harus diisi!");
    }

    @Test(priority = 5, description = "TC-IZN-06 - Pengajuan terlambat dengan keterangan kosong")
    public void testNotesKosong() {
        log.info("TC-IZN-06 - Pengajuan terlambat dengan keterangan kosong");
        String hariIni = Constants.getTodayDate();
        izinPage.isiFormTerlambat(hariIni, "8", "");
        Assert.assertEquals(izinPage.getErrorMessage("Keterangan Harus diisi!"), "Keterangan Harus diisi!");
    }

    @Test(priority = 6, description = "TC-IZN-07 - Pengajuan terlambat dengan semua field kosong")
    public void testSemuaFieldKosong() {
        log.info("TC-IZN-07 - Pengajuan terlambat dengan semua field kosong");
        izinPage.isiFormTerlambat("", "", "");
        List<String> errors = izinPage.getAllErrorMessages();
        Assert.assertTrue(errors.contains("Tanggal Harus diisi!"));
        Assert.assertTrue(errors.contains("Jam Harus diisi!"));
        Assert.assertTrue(errors.contains("Keterangan Harus diisi!"));
    }

}
