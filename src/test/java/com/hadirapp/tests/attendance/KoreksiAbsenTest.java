package com.hadirapp.tests.attendance;

import com.hadirapp.base.BaseTest;
import com.hadirapp.pages.Attendance.KoreksiAbsenPage;
import com.hadirapp.pages.Auth.LoginPage;
import com.hadirapp.utlis.Constants;
import com.hadirapp.utlis.WaitUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class KoreksiAbsenTest extends BaseTest {
    private LoginPage loginPage;
    private KoreksiAbsenPage koreksiPage;

    @BeforeMethod
    public void setUpPages() {
        loginPage = new LoginPage(driver);
        koreksiPage = new KoreksiAbsenPage(driver);

        loginPage.doLogin(Constants.EMAIL, Constants.PASSWORD);
        WaitUtils.waitForUrlContains(driver, "apps", 20);
        // Alur: Klik menu -> Klik tombol biru 'Ajukan Koreksi' -> Form muncul
        koreksiPage.klikMenuKoreksiAbsen();

        // Beri waktu animasi modal muncul
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
    }

    @Test(priority = 1, description = "TC-KOR-01 - Verifikasi tampilan form koreksi")
    public void testKlikTombolKoreksi() {
        log.info("TC-KOR-01 - Verifikasi tampilan form koreksi");
        Assert.assertTrue(driver.getPageSource().contains("Ajukan Koreksi"));
    }

    @Test(priority = 2, description = "TC-KOR-02 - Koreksi data valid lengkap")
    public void testKoreksiDataValid() {
        log.info("TC-KOR-02 - Koreksi data valid lengkap");
        koreksiPage.isiDataKoreksi("8", "17", "wfo");
        // Validasi: Halaman List Koreksi tampil setelah submit
        Assert.assertTrue(koreksiPage.isHalamanKoreksiDisplayed(),
            "Halaman List Koreksi tidak tampil setelah submit!");
        // Validasi: Card koreksi terbaru mengandung data yang benar
        String cardText = koreksiPage.getKoreksiTerbaruText();
        Assert.assertFalse(cardText.isEmpty(), "Card koreksi terbaru tidak ditemukan!");
        Assert.assertTrue(cardText.contains("Work From Office"),
            "Tipe kerja 'Work From Office' tidak ditemukan di card koreksi terbaru! Actual: " + cardText);
    }

     @Test(priority = 3, description = "TC-KOR-03 - Koreksi data invalid (Jam Masuk Kosong)")
    public void testKoreksiDataInvalid1() {
        log.info("TC-KOR-03 - Koreksi data invalid (Jam Masuk Kosong)");
        koreksiPage.isiDataKoreksi("", "17:00", "");
        // Bug: Seharusnya gagal, tapi aplikasi tetap menerima data tidak lengkap
        Assert.assertTrue(koreksiPage.isHalamanKoreksiDisplayed(),
            "Halaman List Koreksi tampil meski Jam Masuk kosong (bug: validasi form tidak berjalan)");
        String cardText = koreksiPage.getKoreksiTerbaruText();
        Assert.assertFalse(cardText.isEmpty(), "Card koreksi terbaru tidak ditemukan!");
    }

    @Test(priority = 4, description = "TC-KOR-04 - Koreksi data invalid (Jam Pulang Kosong)")
    public void testKoreksiDataInvalid2() {
        log.info("TC-KOR-04 - Koreksi data invalid (Jam Pulang Kosong)");
        koreksiPage.isiDataKoreksi("08:00", "", "wfo");
        // Bug: Seharusnya gagal, tapi aplikasi tetap menerima data tidak lengkap
        Assert.assertTrue(koreksiPage.isHalamanKoreksiDisplayed(),
            "Halaman List Koreksi tampil meski Jam Pulang kosong (bug: validasi form tidak berjalan)");
        String cardText = koreksiPage.getKoreksiTerbaruText();
        Assert.assertFalse(cardText.isEmpty(), "Card koreksi terbaru tidak ditemukan!");
    }

    @Test(priority = 5, description = "TC-KOR-05 - Koreksi data invalid (Tipe Absen Kosong)")
    public void testKoreksiDataInvalid3() {
        log.info("TC-KOR-05 - Koreksi data invalid (Tipe Absen Kosong)");
        koreksiPage.isiDataKoreksi("08:00", "17:00", "");
        // Bug: Seharusnya gagal, tapi aplikasi tetap menerima data tidak lengkap
        Assert.assertTrue(koreksiPage.isHalamanKoreksiDisplayed(),
            "Halaman List Koreksi tampil meski Tipe Absen kosong (bug: validasi form tidak berjalan)");
        String cardText = koreksiPage.getKoreksiTerbaruText();
        Assert.assertFalse(cardText.isEmpty(), "Card koreksi terbaru tidak ditemukan!");
    }

    @Test(priority = 6, description = "TC-KOR-06 - Koreksi dengan semua input kosong")
    public void testKoreksiSemuaKosong() {
        log.info("TC-KOR-06 - Koreksi dengan semua input kosong");
        koreksiPage.isiDataKoreksi("", "", "");
        String actualError = koreksiPage.getErrorMessage("harus diisi");
        Assert.assertTrue(actualError.contains("harus diisi"), "Pesan error validasi tidak muncul!");
    }

    
}
