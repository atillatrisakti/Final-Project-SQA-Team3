package com.hadirapp.tests.requests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.hadirapp.base.BaseTest;
import com.hadirapp.pages.Auth.LoginPage;
import com.hadirapp.pages.Requests.CutiPage;
import com.hadirapp.utils.Constants;
import com.hadirapp.utils.WaitUtils;

public class CutiTest extends BaseTest {
    private LoginPage loginPage;
    private CutiPage cutiPage;

    @BeforeMethod
    public void setUp() {
        loginPage = new LoginPage(driver);
        cutiPage = new CutiPage(driver);
        loginPage.doLogin(Constants.EMAIL_3, Constants.PASSWORD_3);
        WaitUtils.waitForUrlContains(driver, "apps", 20);
    }

    @Test(priority = 1, description = "TC-CTI-01 - Verifikasi fungsi klik tombol 'Cuti'")
    public void testVerifikasiFungsiKlikTombolCuti() {
        log.info("TC-CTI-01 - Verifikasi fungsi klik tombol 'Cuti'");
        cutiPage.clickCutiBtn();
        WaitUtils.waitForUrlContains(driver, "leave-client", 20);
        Assert.assertEquals(driver.getCurrentUrl(), Constants.DASHBOARD_URL + "/leave-client");
        Assert.assertTrue(cutiPage.isHalamanCutiDisplayed(), "Halaman Cuti tidak tampil setelah klik tombol 'Cuti'");
    }

    @Test(priority = 2, description = "TC-CTI-02 - Verifikasi fungsi klik tombol 'Ajukan Cuti'")
    public void testVerifikasiFungsiKlikTombolAjukanCuti() {
        log.info("TC-CTI-02 - Verifikasi fungsi klik tombol 'Ajukan Cuti'");
        cutiPage.clickCutiBtn();
        WaitUtils.waitForUrlContains(driver, "leave-client", 20);
        cutiPage.clickAjukanCutiBtn();
        Assert.assertTrue(cutiPage.isModalAjukanCutiDisplayed(), "Modal Ajukan Cuti tidak tampil setelah klik tombol 'Ajukan Cuti'");
        Assert.assertEquals(cutiPage.getModalTitleText(), "Ajukan Cuti");
    }

    @Test(priority = 3, description = "TC-CTI-03 - Verifikasi fungsi klik tab 'Info Cuti'")
    public void testVerifikasiFungsiKlikTabInfoCuti() {
        log.info("TC-CTI-03 - Verifikasi fungsi klik tab 'Info Cuti'");
        cutiPage.clickCutiBtn();
        WaitUtils.waitForUrlContains(driver, "leave-client", 20);
        cutiPage.clickAjukanCutiBtn();
        cutiPage.clickTabInfoCuti();
        Assert.assertTrue(cutiPage.isLabelTotalCutiDisplayed(), "Label 'Total Cuti' tidak tampil setelah klik tab 'Info Cuti'");
        Assert.assertEquals(cutiPage.getLabelTotalCutiText(), "Total Cuti", "Teks label tidak sesuai!");
    }

    @Test(priority = 4, description = "TC-CTI-04 - Pengajuan cuti dengan data valid")
    public void testCuti() {
        log.info("TC-CTI-04 - Pengajuan cuti dengan data valid");
        cutiPage.doCuti();
        cutiPage.pilihTipeCuti("Pernikahan diri sendiri");
        cutiPage.pilihTanggal("May", "2026", "20", "21");
        cutiPage.fillNotes("Cuti");
        cutiPage.clickAjukanButton();
        Assert.assertTrue(cutiPage.isHalamanCutiDisplayed(), "Halaman Cuti tidak tampil setelah submit");
        String cutiTerbaru = cutiPage.getCutiTerbaruText();
        Assert.assertFalse(cutiTerbaru.isEmpty(), "Card cuti terbaru tidak ditemukan!");
    }

    @Test(priority = 5, description = "TC-CTI-05 - Pengajuan cuti dengan tipe cuti kosong")
    public void testPengajuanCutiDenganTipeCutiKosong() {
        log.info("TC-CTI-05 - Pengajuan cuti dengan tipe cuti kosong");
        cutiPage.doCuti();
        cutiPage.pilihTanggal("May", "2026", "11", "15");
        cutiPage.fillNotes("Cuti");
        cutiPage.clickAjukanButton();
        Assert.assertTrue(cutiPage.isErrorMessageDisplayed(), "Error message tidak tampil!");
        String actualError = cutiPage.getErrorMessageText();
        String expectedError = "Cannot read properties of null (reading 'hris_leave_type_id')";
        Assert.assertEquals(actualError, expectedError, "Pesan error tidak sesuai atau tidak muncul!");
    }

    @Test(priority = 6, description = "TC-CTI-06 - Pengajuan cuti dengan tanggal kosong")
    public void testPengajuanCutiDenganTanggalKosong() {
        log.info("TC-CTI-06 - Pengajuan cuti dengan tanggal kosong");
        cutiPage.doCuti();
        cutiPage.pilihTipeCuti("Cuti Tahunan");
        cutiPage.fillNotes("Cuti");
        cutiPage.clickAjukanButton();
        Assert.assertTrue(cutiPage.isErrorMessageDisplayed(), "Error message tidak tampil!");
        String actualError = cutiPage.getErrorMessageText();
        String expectedError = "Incorrect DATE value: 'Invalid date'";
        Assert.assertEquals(actualError, expectedError, "Pesan error tidak sesuai atau tidak muncul!");
    }

    @Test(priority = 7, description = "TC-CTI-07 - Pengajuan cuti dengan catatan kosong")
    public void testPengajuanCutiDenganCatatanKosong() {
        log.info("TC-CTI-07 - Pengajuan cuti dengan catatan kosong");
        cutiPage.doCuti();
        cutiPage.pilihTipeCuti("Cuti Tahunan");
        cutiPage.pilihTanggal("May", "2026", "11", "15");
        cutiPage.fillNotes("");
        cutiPage.clickAjukanButton();
        Assert.assertTrue(cutiPage.isHalamanCutiDisplayed(), "Halaman Cuti tidak tampil setelah submit");
        String cutiTerbaru = cutiPage.getCutiTerbaruText();
        Assert.assertFalse(cutiTerbaru.isEmpty(), "Card cuti terbaru tidak ditemukan!");
    }

    @Test(priority = 8, description = "TC-CTI-08 - Pengajuan cuti dengan semua field kosong")
    public void testPengajuanCutiDenganSemuaFieldKosong() {
        log.info("TC-CTI-08 - Pengajuan cuti dengan semua field kosong");
        cutiPage.doCuti();
        cutiPage.clickAjukanButton();
        Assert.assertTrue(cutiPage.isErrorMessageDisplayed(), "Error message tidak tampil!");
        String actualError = cutiPage.getErrorMessageText();
        String expectedError = "Cannot read properties of null (reading 'hris_leave_type_id')";
        Assert.assertEquals(actualError, expectedError, "Pesan error tidak sesuai atau tidak muncul!");
    }

    @Test(priority = 9, description = "TC-CTI-09 - Pengajuan cuti dengan jumlah yang diajukan melebihi sisa cuti yang tersedia")
    public void testPengajuanCutiDenganJumlahYangDiajukanMelebihiSisaCutiYangTersedia() {
        log.info("TC-CTI-09 - Pengajuan cuti dengan jumlah yang diajukan melebihi sisa cuti yang tersedia");
        cutiPage.doCuti();
        cutiPage.pilihTipeCuti("Cuti Tahunan");
        cutiPage.pilihTanggal("May", "2026", "22", "29");
        cutiPage.fillNotes("Cuti");
        cutiPage.clickAjukanButton();
        Assert.assertTrue(cutiPage.isErrorMessageDisplayed(), "Error message tidak tampil!");
        String actualError = cutiPage.getErrorMessageText();
        String expectedError = "Permintaan cuti melebihi kapasitas total sisa cuti";
        Assert.assertEquals(actualError, expectedError, "Pesan error tidak sesuai atau tidak muncul!");
    }

    @Test(priority = 10, description = "TC-CTI-10 - Pengajuan cuti yang melebihi batas maksimal tipe cuti")
    public void testPengajuanCutiYangMelebihiBatasMaksimalTipeCuti() {
        log.info("TC-CTI-10 - Pengajuan cuti yang melebihi batas maksimal tipe cuti");
        cutiPage.doCuti();
        cutiPage.pilihTipeCuti("Khitanan");
        cutiPage.pilihTanggal("May", "2026", "11", "15");
        cutiPage.fillNotes("Cuti");
        cutiPage.clickAjukanButton();
        Assert.assertTrue(cutiPage.isErrorMessageDisplayed(), "Error message tidak tampil!");
        String actualError = cutiPage.getErrorMessageText();
        String expectedError = "Jumlah hari cuti harus sesuai dengan total jumlah cuti tipe spesial yang dipilih";
        Assert.assertEquals(actualError, expectedError, "Pesan error tidak sesuai atau tidak muncul!");
    }

    @Test(priority = 11, description = "TC-CTI-11 - Verifikasi fungsi tombol 'Reset' pada formulir cuti")
    public void testVerifikasiTombolReset() {
        log.info("TC-CTI-11 - Verifikasi fungsi tombol 'Reset' pada formulir cuti");
        cutiPage.doCuti();
        cutiPage.pilihTipeCuti("Cuti Tahunan");
        cutiPage.pilihTanggal("May", "2026", "11", "15");
        cutiPage.fillNotes("Cuti");
        cutiPage.clickResetButton();
        Assert.assertEquals(cutiPage.getTipeCutiValue(), "Pilih Tipe Absen", 
        "Dropdown Tipe Cuti tidak ter-reset!");
        Assert.assertTrue(cutiPage.getTanggalValue().contains("-"), 
        "Field Tanggal tidak kembali ke format default '-'!");
        Assert.assertEquals(cutiPage.getCatatanValue(), "", 
        "Field Catatan masih berisi teks setelah reset!");
    }
}
