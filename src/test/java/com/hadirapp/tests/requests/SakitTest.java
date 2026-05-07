package com.hadirapp.tests.requests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.hadirapp.base.BaseTest;
import com.hadirapp.pages.Auth.LoginPage;
import com.hadirapp.pages.Requests.SakitPage;
import com.hadirapp.utlis.Constants;
import com.hadirapp.utlis.WaitUtils;

public class SakitTest extends BaseTest {
    private LoginPage loginPage;
    private SakitPage sakitPage;

    @BeforeMethod
    public void setUp(){
        loginPage = new LoginPage(driver);
        sakitPage = new SakitPage(driver);
        loginPage.doLogin(Constants.EMAIL_3, Constants.PASSWORD_3);
        WaitUtils.waitForUrlContains(driver, "apps", 20);
        sakitPage.klikMenuSakit();
        WaitUtils.waitForUrlContains(driver, "sick", 20);
    }

    @Test(priority = 1, description = "TC-SKT-01 - Verifikasi navigasi ke halaman Sakit")
    public void testNavigasiSakit() {
        log.info("TC-SKT-01 - Navigasi ke halaman Sakit");
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("sick"),
                "URL saat ini bukan Halaman Sakit: " + currentUrl);
    }

    @Test(priority = 2, description = "TC-SKT-02 - Sukses ajukan sakit dengan data valid")
    public void testAjukanSakitValid() {
        log.info("TC-SKT-02 - Ajukan sakit dengan data valid");
        sakitPage.klikTombolAjukanSakit();
        sakitPage.pilihRentangTanggal("11", "12");
        String imagePath = Constants.getTestDataPath("surat_sakit.png");
        sakitPage.uploadDokumen(imagePath);
        sakitPage.klikTombolSubmit();
        WaitUtils.waitForUrlContains(driver, "sick", 20);
        Assert.assertTrue(driver.getCurrentUrl().contains("sick"));
    }

    @Test(priority = 3, description = "TC-SKT-03 - Ajukan sakit dengan Foto Kosong")
    public void testAjukanSakitUploadKosong() {
        log.info("TC-SKT-03 - Ajukan sakit dengan Foto Kosong");
        sakitPage.klikTombolAjukanSakit();
        sakitPage.pilihRentangTanggal("13", "14");
        sakitPage.klikTombolSubmit();
        Assert.assertTrue(sakitPage.isErrorMessageDisplayed(), "Error message tidak tampil!");
        String actualError = sakitPage.getErrorMessageText();
        String expectedError = "Photo is Required";
        Assert.assertEquals(actualError, expectedError, "Pesan error tidak sesuai atau tidak muncul!");
    }

    @Test(priority = 4, description = "TC-SKT-04 - Pengajuan sakit dengan tanggal kosong")
    public void testSakitTanggalKosong() {
        log.info("TC-SKT-04 - Pengajuan sakit dengan tanggal kosong");
        sakitPage.klikTombolAjukanSakit();
        String imagePath = Constants.getTestDataPath("surat_sakit.png");
        sakitPage.uploadDokumen(imagePath);
        sakitPage.klikTombolSubmit();
        Assert.assertTrue(sakitPage.isErrorMessageDisplayed(), "Error message tidak tampil!");
        String actualError = sakitPage.getErrorMessageText();
        String expectedError = "Incorrect datetime value: 'Invalid date' for column 'sick_request_date_from' at row 1";
        Assert.assertEquals(actualError, expectedError, "Pesan error tidak sesuai atau tidak muncul!");
    }

    @Test(priority = 5, description = "TC-SKT-05 - Pengajuan sakit dengan semua field kosong")
    public void testSakitSemuaFieldKosong() {
        log.info("TC-SKT-05 - Pengajuan sakit dengan semua field kosong");
        sakitPage.klikTombolAjukanSakit();
        sakitPage.btnSubmit.click();
        Assert.assertTrue(sakitPage.isErrorMessageDisplayed(), "Error message tidak tampil!");
        String actualError = sakitPage.getErrorMessageText();
        String expectedError = "Incorrect datetime value: 'Invalid date' for column 'sick_request_date_from' at row 1";
        Assert.assertEquals(actualError, expectedError, "Pesan error tidak sesuai atau tidak muncul!");
    }

    @Test(priority = 6, description = "TC-SKT-06 - Error saat input tanggal yang sudah pernah diajukan")
    public void testSakitDataDuplikat() {
        log.info("TC-SKT-06 - Error saat input tanggal yang sudah pernah diajukan");
        sakitPage.klikTombolAjukanSakit();
        sakitPage.pilihRentangTanggal("6", "8");
        String imagePath = Constants.getTestDataPath("surat_sakit.png");
        sakitPage.uploadDokumen(imagePath);
        sakitPage.klikTombolSubmit();
        Assert.assertTrue(sakitPage.isErrorMessageDisplayed(), "Error message tidak tampil!");
        String actualError = sakitPage.getErrorMessageText();
        String expectedError = "Anda sudah melakukan izin sakit di tanggal tersebut";
        Assert.assertEquals(actualError, expectedError, "Pesan error tidak sesuai atau tidak muncul!");
    }

    @Test(priority = 7, description = "TC-SKT-07 - Memastikan tombol Reset mengosongkan form")
    public void testResetForm() {
        log.info("TC-SKT-07 - Memastikan tombol Reset mengosongkan form");
        sakitPage.klikTombolAjukanSakit();
        sakitPage.pilihRentangTanggal("10", "12");
        String imagePath = Constants.getTestDataPath("surat_sakit.png");
        sakitPage.uploadDokumen(imagePath);
        sakitPage.klikReset();
        String valueTanggal = sakitPage.getTeksTanggal();
        System.out.println("Teks tanggal yang didapat sekarang: '" + valueTanggal + "'");
        Assert.assertTrue(valueTanggal.contains("/"),
                "Harusnya dapet '/', tapi malah dapet: " + valueTanggal);
    }
}
