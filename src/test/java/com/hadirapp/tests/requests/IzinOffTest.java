package com.hadirapp.tests.requests;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.hadirapp.base.BaseTest;
import com.hadirapp.pages.Auth.LoginPage;
import com.hadirapp.pages.Requests.IzinPage;
import com.hadirapp.utils.Constants;
import com.hadirapp.utils.WaitUtils;

public class IzinOffTest extends BaseTest{
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
    
    @Test(priority = 1, description = "TC-IZN-01 - Verifikasi navigasi ke halaman Izin")
    public void testNavigasiIzin() {
        log.info("TC-IZN-01 - Verifikasi navigasi ke halaman Izin");
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("permit") || currentUrl.contains("izin"), 
            "URL saat ini bukan Halaman Izin: " + currentUrl);
    }

    @Test(priority = 2, description = "TC-IZN-04 - Verifikasi tab Izin Off")
    public void testTabIzinOff() {
        log.info("TC-IZN-04 - Verifikasi tab Izin Off");
        izinPage.pilihTab("Izin Off");
        Assert.assertTrue(izinPage.btnAjukanIzinOff.isDisplayed());
    }

    @Test(priority = 3, description = "TC-IZN-05 - Pengajuan izin off dengan data valid")
    public void testIzinOffValid() {
        log.info("TC-IZN-05 - Pengajuan izin off dengan data valid");
        String hariIni = Constants.getTodayDate();
        izinPage.isiFormIzinOff(hariIni, "Acara Keluarga");
        Assert.assertTrue(izinPage.isHalamanIzinDisplayed(),
            "Card izin tidak tampil setelah submit!");
        String cardText = izinPage.getIzinTerbaruText();
        Assert.assertFalse(cardText.isEmpty(), "Card izin terbaru tidak ditemukan!");
    }
    
    @Test(priority = 4, description = "TC-IZN-06 - Pengajuan izin off dengan tanggal kosong")
    public void testTanggalKosong() {
        log.info("TC-IZN-06 - Pengajuan izin off dengan tanggal kosong");
        izinPage.isiFormIzinOff("", "Acara Keluarga");
        Assert.assertEquals(izinPage.getErrorMessage("Pilih tanggal off!."), "Pilih tanggal off!.");
    }

    @Test(priority = 5, description = "TC-IZN-07 - Pengajuan izin off dengan keterangan kosong")
    public void testKeteranganKosong() {
        log.info("TC-IZN-07 - Pengajuan izin off dengan keterangan kosong");
        String hariIni = Constants.getTodayDate();
        izinPage.isiFormIzinOff(hariIni, "");
        Assert.assertEquals(izinPage.getErrorMessage("Alasan dan mengambill izin off."), "Alasan dan mengambill izin off.");
    }

    @Test(priority = 6, description = "TC-IZN-08 - Pengajuan izin off dengan semua field kosong")
    public void testSemuaFieldKosong() {
        log.info("TC-IZN-08 - Pengajuan izin off dengan semua field kosong");
        izinPage.isiFormIzinOff("", "");
        List<String> errors = izinPage.getAllErrorMessages();
        Assert.assertTrue(errors.contains("Pilih tanggal off!."), "Error untuk tanggal kosong tidak muncul");
        Assert.assertTrue(errors.contains("Alasan dan mengambill izin off."), "Error untuk keterangan kosong tidak muncul");
    }

}
