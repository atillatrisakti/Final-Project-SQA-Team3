package com.hadirapp.tests.requests;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.hadirapp.base.BaseTest;
import com.hadirapp.pages.Attendance.AbsenPage;
import com.hadirapp.pages.Auth.LoginPage;
import com.hadirapp.pages.Requests.LemburPage;
import com.hadirapp.utils.Constants;
import com.hadirapp.utils.WaitUtils;

public class LemburTest extends BaseTest{
    private LoginPage loginPage;
    private AbsenPage absenPage;
    private LemburPage lemburPage;
    private boolean testCaseResultLogged;

    @BeforeMethod
    public void setUp() {
        loginPage = new LoginPage(driver);
        absenPage = new AbsenPage(driver);
        lemburPage = new LemburPage(driver);
    }

    @Test(priority = 1, description = "TC-LMB-01 - Verifikasi fungsi klik tombol Lembur")
    public void tcLmb01VerifyClickLemburButton() {
        runTestCase("TC-LMB-01", () -> {
            // Tahap 1 sampai 3: mulai dari login valid lalu masuk ke modul absensi melalui klik icon Absensi
            // agar tetap sesuai dengan objective TC-LMB-01.
            loginToAbsentModuleByMenu();

            // Tahap 4: klik tombol Lembur dan tunggu marker "Ajukan Lembur" muncul sebagai bukti aksi berhasil diproses.
            absenPage.clickLemburButton();
            WaitUtils.waitForUrlContains(driver, "overtime-client", 20);

            // Tahap 5: validasi akhir TC-LMB-01, tombol atau marker pengajuan lembur harus terlihat.
            boolean ajukanLemburVisible = absenPage.isAjukanLemburVisible();
            validateTestCaseResult("TC-LMB-01", ajukanLemburVisible,
                    "TC-LMB-01 gagal: tombol atau marker Ajukan Lembur tidak muncul.");
        });
    }

    @Test(priority = 2, description = "TC-LMB-02 - Pengajuan lembur dengan data valid")
    public void tcLmb02SubmitOvertimeWithValidData() {
        runTestCase("TC-LMB-02", () -> {
            // Tahap 1: mulai dari login valid lalu masuk ke halaman absensi secara langsung agar test case berdiri sendiri
            // dan lebih stabil saat dijalankan berurutan dalam suite.
            loginToAbsentPage();

            // Tahap 2: klik tombol Lembur lalu tunggu area pengajuan lembur siap digunakan.
            absenPage.clickLemburButton();
            WaitUtils.waitForUrlContains(driver, "overtime-client", 20);

            // Tahap 3: klik tombol Ajukan Lembur dan tunggu field form tampil agar input tidak dieksekusi terlalu cepat.
            lemburPage.clickAjukanLembur();

            // Tahap 4: isi semua field dengan data valid pada hari yang sama, memastikan jam masuk lebih kecil dari jam keluar
            // dan kedua jam tidak melewati 24:00.
            LocalDate tanggalLembur = LocalDate.now();
            LocalDateTime jamMasuk = tanggalLembur.atTime(8, 0);
            LocalDateTime jamKeluar = tanggalLembur.atTime(23, 0);
            lemburPage.fillOvertimeForm(
                    jamMasuk,
                    jamKeluar,
                    "Menyelesaikan regression test modul absensi dan pengajuan lembur.",
                    "Pengajuan dibuat otomatis oleh Selenium dengan data valid.");

            // Tahap 5: klik button Ajukan dan tunggu pesan hasil submit atau kembalinya state halaman sebagai mitigasi server lemot.
            lemburPage.submitOvertime();
            boolean submitResultVisible = lemburPage.waitForSubmissionResult();

            // Tahap 6: validasi akhir TC-LMB-02 dengan memastikan feedback hasil submit atau kembalinya state halaman berhasil muncul.
            validateTestCaseResult("TC-LMB-02", submitResultVisible,
                    "TC-LMB-02 gagal: feedback hasil submit lembur tidak muncul.");
        });
    }

    @Test(priority = 3, description = "TC-LMB-03 - Validasi field Jam Masuk wajib diisi")
    public void tcLmb03SubmitOvertimeWithoutJamMasuk() {
        runTestCase("TC-LMB-03", () -> {
            // Tahap 1: mulai dari login valid lalu masuk ke halaman absensi secara langsung agar test case berdiri sendiri
            // dan lebih stabil saat dijalankan berurutan dalam suite.
            loginToAbsentPage();

            // Tahap 2: klik tombol Lembur lalu tunggu area pengajuan lembur siap digunakan.
            absenPage.clickLemburButton();
            WaitUtils.waitForUrlContains(driver, "overtime-client", 20);

            // Tahap 3: klik tombol Ajukan Lembur dan tunggu field form tampil agar validasi field wajib bisa diuji.
            lemburPage.clickAjukanLembur();

            // Tahap 4: isi semua field kecuali Jam Masuk, tetap memastikan Jam Keluar menggunakan format valid yang tidak melewati 24:00.
            LocalDate tanggalLembur = LocalDate.now();
            LocalDateTime jamKeluar = tanggalLembur.atTime(20, 0);
            lemburPage.fillOvertimeFormWithoutJamMasuk(
                    jamKeluar,
                    "Pengajuan lembur tanpa jam masuk untuk memvalidasi field wajib.");

            // Tahap 5: klik button Ajukan dan tunggu pesan validasi pada field Jam Masuk muncul sebagai mitigasi server lemot.
            lemburPage.submitOvertime();
            boolean jamMasukValidationVisible = lemburPage.waitForJamMasukRequiredMessage();

            // Tahap 6: validasi akhir TC-LMB-03, sistem harus menampilkan pesan bahwa Jam Masuk wajib diisi.
            validateTestCaseResult("TC-LMB-03", jamMasukValidationVisible,
                    "TC-LMB-03 gagal: pesan validasi 'Jam masuk harus di isi!' tidak muncul.");
        });
    }

    @Test(priority = 4, description = "TC-LMB-04 - Validasi field Jam Keluar wajib diisi")
    public void tcLmb04SubmitOvertimeWithoutJamKeluar() {
        runTestCase("TC-LMB-04", () -> {
            // Tahap 1: mulai dari login valid lalu masuk ke halaman absensi secara langsung agar test case berdiri sendiri
            // dan lebih stabil saat dijalankan berurutan dalam suite.
            loginToAbsentPage();

            // Tahap 2: klik tombol Lembur lalu tunggu area pengajuan lembur siap digunakan.
            absenPage.clickLemburButton();
            WaitUtils.waitForUrlContains(driver, "overtime-client", 20);

            // Tahap 3: klik tombol Ajukan Lembur dan tunggu field form tampil agar validasi field wajib bisa diuji.
            lemburPage.clickAjukanLembur();

            // Tahap 4: isi semua field kecuali Jam Keluar, tetap memastikan Jam Masuk menggunakan format valid dan berada sebelum batas 24:00.
            LocalDate tanggalLembur = LocalDate.now();
            LocalDateTime jamMasuk = tanggalLembur.atTime(18, 0);
            lemburPage.fillOvertimeFormWithoutJamKeluar(
                    jamMasuk,
                    "Pengajuan lembur tanpa jam keluar untuk memvalidasi field wajib.");

            // Tahap 5: klik button Ajukan dan tunggu pesan validasi pada field Jam Keluar muncul sebagai mitigasi server lemot.
            lemburPage.submitOvertime();
            boolean jamKeluarValidationVisible = lemburPage.waitForJamKeluarRequiredMessage();

            // Tahap 6: validasi akhir TC-LMB-04, sistem harus menampilkan pesan bahwa Jam Keluar wajib diisi.
            validateTestCaseResult("TC-LMB-04", jamKeluarValidationVisible,
                    "TC-LMB-04 gagal: pesan validasi 'Jam Keluar harus di isi!' tidak muncul.");
        });
    }

    @Test(priority = 5, description = "TC-LMB-05 - Validasi field Catatan minimal 5 karakter")
    public void tcLmb05SubmitOvertimeWithoutCatatan() {
        runTestCase("TC-LMB-05", () -> {
            // Tahap 1: mulai dari login valid lalu masuk ke halaman absensi secara langsung agar test case berdiri sendiri
            // dan lebih stabil saat dijalankan berurutan dalam suite.
            loginToAbsentPage();

            // Tahap 2: klik tombol Lembur lalu tunggu area pengajuan lembur siap digunakan.
            absenPage.clickLemburButton();
            WaitUtils.waitForUrlContains(driver, "overtime-client", 20);

            // Tahap 3: klik tombol Ajukan Lembur dan tunggu field form tampil agar validasi field catatan bisa diuji.
            lemburPage.clickAjukanLembur();

            // Tahap 4: isi semua field kecuali Catatan dengan data jam valid, memastikan jam masuk lebih kecil dari jam keluar
            // dan keduanya tidak melewati 24:00.
            LocalDate tanggalLembur = LocalDate.now();
            LocalDateTime jamMasuk = tanggalLembur.atTime(18, 0);
            LocalDateTime jamKeluar = tanggalLembur.atTime(20, 0);
            lemburPage.fillOvertimeFormWithoutCatatan(jamMasuk, jamKeluar);

            // Tahap 5: klik button Ajukan dan tunggu pesan validasi minimal karakter muncul di bawah field Catatan.
            lemburPage.submitOvertime();
            boolean catatanValidationVisible = lemburPage.waitForCatatanMinimumCharacterMessage();

            // Tahap 6: validasi akhir TC-LMB-05, sistem harus menampilkan pesan bahwa Catatan minimal 5 karakter.
            validateTestCaseResult("TC-LMB-05", catatanValidationVisible,
                    "TC-LMB-05 gagal: pesan validasi 'Masukan minimal 5 karakter' tidak muncul.");
        });
    }

    @Test(priority = 6, description = "TC-LMB-06 - Validasi semua field wajib saat form dikosongkan")
    public void tcLmb06SubmitOvertimeWithEmptyForm() {
        runTestCase("TC-LMB-06", () -> {
            // Tahap 1: mulai dari login valid lalu masuk ke halaman absensi secara langsung agar test case berdiri sendiri
            // dan lebih stabil saat dijalankan berurutan dalam suite.
            loginToAbsentPage();

            // Tahap 2: klik tombol Lembur lalu tunggu area pengajuan lembur siap digunakan.
            absenPage.clickLemburButton();
            WaitUtils.waitForUrlContains(driver, "overtime-client", 20);

            // Tahap 3: klik tombol Ajukan Lembur dan tunggu field form tampil agar validasi seluruh field wajib bisa diuji.
            lemburPage.clickAjukanLembur();

            // Tahap 4: kosongkan semua field dengan tidak mengisi Jam Masuk, Jam Keluar, dan Catatan.
            // Form langsung disubmit untuk memicu seluruh validasi mandatory secara bersamaan.

            // Tahap 5: klik button Ajukan dan tunggu seluruh pesan validasi muncul sebagai mitigasi server lemot.
            lemburPage.submitOvertime();
            boolean jamMasukValidationVisible = lemburPage.waitForJamMasukRequiredMessage();
            boolean jamKeluarValidationVisible = lemburPage.waitForJamKeluarRequiredMessage();
            boolean catatanValidationVisible = lemburPage.waitForCatatanMinimumCharacterMessage();

            // Tahap 6: validasi akhir TC-LMB-06, sistem harus menampilkan semua pesan error pada field yang wajib diisi.
            boolean allRequiredValidationVisible = jamMasukValidationVisible
                    && jamKeluarValidationVisible
                    && catatanValidationVisible;
            validateTestCaseResult("TC-LMB-06", allRequiredValidationVisible,
                    "TC-LMB-06 gagal: pesan validasi wajib belum lengkap. "
                            + "Jam Masuk=" + jamMasukValidationVisible
                            + ", Jam Keluar=" + jamKeluarValidationVisible
                            + ", Catatan=" + catatanValidationVisible);
        });
    }

    @Test(priority = 7, description = "TC-LMB-07 - Verifikasi tombol Reset mengosongkan semua field")
    public void tcLmb07ResetOvertimeForm() {
        runTestCase("TC-LMB-07", () -> {
            // Tahap 1: mulai dari login valid lalu masuk ke halaman absensi secara langsung agar test case berdiri sendiri
            // dan lebih stabil saat dijalankan berurutan dalam suite.
            loginToAbsentPage();

            // Tahap 2: klik tombol Lembur lalu tunggu area pengajuan lembur siap digunakan.
            absenPage.clickLemburButton();
            WaitUtils.waitForUrlContains(driver, "overtime-client", 20);

            // Tahap 3: klik tombol Ajukan Lembur dan tunggu field form tampil agar form siap diisi.
            lemburPage.clickAjukanLembur();

            // Tahap 4: isi semua field dengan data valid, memastikan jam masuk lebih kecil dari jam keluar
            // dan kedua jam tidak melewati 24:00.
            LocalDate tanggalLembur = LocalDate.now();
            LocalDateTime jamMasuk = tanggalLembur.atTime(18, 0);
            LocalDateTime jamKeluar = tanggalLembur.atTime(20, 0);
            lemburPage.fillOvertimeForm(
                    jamMasuk,
                    jamKeluar,
                    "Menguji fungsi reset pada form pengajuan lembur.",
                    "Data ini akan dibersihkan kembali menggunakan tombol reset.");

            // Tahap 5: klik tombol Reset untuk mengosongkan semua field.
            lemburPage.clickReset();

            // Tahap 6: validasi akhir TC-LMB-07, semua field harus kembali kosong setelah tombol Reset ditekan.
            boolean allFieldsEmptyAfterReset = lemburPage.waitForFormFieldsEmpty();
            validateTestCaseResult("TC-LMB-07", allFieldsEmptyAfterReset,
                    "TC-LMB-07 gagal: masih ada field yang terisi setelah tombol Reset diklik. "
                            + lemburPage.getFormFieldValuesSummary());
        });
    }

    @Test(priority = 8, description = "TC-LMB-08 - Pengajuan lembur saat belum absen keluar dan belum ada jam lembur di hari yang sama")
    public void tcLmb08SubmitOvertimeBeforeClockOutWithoutExistingOvertime() {
        runTestCase("TC-LMB-08", () -> {
            // Precondition: user berhasil login, belum melakukan absensi keluar, dan belum ada jam lembur di hari yang sama.
            // Test dimulai dari login valid lalu masuk ke halaman absensi secara langsung.
            loginToAbsentPage();

            // Tahap 1: klik tombol Lembur.
            absenPage.clickLemburButton();
            WaitUtils.waitForUrlContains(driver, "overtime-client", 20);

            // Tahap 2: klik tombol Ajukan Lembur.
            lemburPage.clickAjukanLembur();

            // Tahap 3: isi semua field dengan data yang sama dengan TC-LMB-02.
            LocalDate tanggalLembur = LocalDate.now();
            LocalDateTime jamMasuk = tanggalLembur.atTime(8, 0);
            LocalDateTime jamKeluar = tanggalLembur.atTime(23, 0);
            lemburPage.fillOvertimeForm(
                    jamMasuk,
                    jamKeluar,
                    "Menyelesaikan regression test modul absensi dan pengajuan lembur.",
                    "Pengajuan dibuat otomatis oleh Selenium dengan data valid.");

            // Tahap 4: klik button Ajukan dan tunggu popup proses validator muncul.
            lemburPage.submitOvertime();
            boolean validatorPopupVisible = lemburPage.waitForValidatorProcessingPopup();

            // Expected result: muncul popup "permintaan request lembur anda sedang diproses validator".
            validateTestCaseResult("TC-LMB-08", validatorPopupVisible,
                    "TC-LMB-08 gagal: popup 'permintaan request lembur anda sedang diproses validator' tidak muncul.");
        });
    }

    @Test(priority = 9, description = "TC-LMB-09 - Verifikasi form pengajuan lembur tampil setelah klik Ajukan Lembur")
    public void tcLmb09VerifyOvertimeFormDisplayed() {
        runTestCase("TC-LMB-09", () -> {
            // Precondition: user berhasil login dan belum melakukan absensi keluar.
            // Tahap 1: masuk ke halaman absensi secara langsung agar test case berdiri sendiri.
            loginToAbsentPage();

            // Tahap 2: klik tombol Lembur lalu tunggu area pengajuan lembur siap digunakan.
            absenPage.clickLemburButton();
            WaitUtils.waitForUrlContains(driver, "overtime-client", 20);

            // Tahap 3: klik tombol Ajukan Lembur untuk membuka form pengajuan.
            lemburPage.clickAjukanLembur();

            // Tahap 4: validasi akhir TC-LMB-09, form pengajuan lembur beserta field jam harus tampil.
            boolean overtimeFormVisible = lemburPage.waitForOvertimeFormVisible();
            validateTestCaseResult("TC-LMB-09", overtimeFormVisible,
                    "TC-LMB-09 gagal: form pengajuan lembur tidak tampil setelah tombol Ajukan Lembur diklik.");
        });
    }

    @Test(priority = 10, description = "TC-LMB-10 - Validasi field Catatan kurang dari 5 karakter")
    public void tcLmb10SubmitOvertimeWithLessThanMinimumCatatan() {
        runTestCase("TC-LMB-10", () -> {
            // Precondition: user berhasil login dan belum melakukan absensi keluar.
            // Tahap 1: masuk ke halaman absensi secara langsung agar test case berdiri sendiri.
            loginToAbsentPage();

            // Tahap 2: klik tombol Lembur lalu tunggu area pengajuan lembur siap digunakan.
            absenPage.clickLemburButton();
            WaitUtils.waitForUrlContains(driver, "overtime-client", 20);

            // Tahap 3: klik tombol Ajukan Lembur dan tunggu field form tampil agar validasi field catatan bisa diuji.
            lemburPage.clickAjukanLembur();

            // Tahap 4: isi Jam Masuk dan Jam Keluar dengan data valid, lalu isi Catatan hanya 4 karakter.
            LocalDate tanggalLembur = LocalDate.now();
            LocalDateTime jamMasuk = tanggalLembur.atTime(18, 0);
            LocalDateTime jamKeluar = tanggalLembur.atTime(20, 0);
            lemburPage.fillOvertimeForm(
                    jamMasuk,
                    jamKeluar,
                    "Test",
                    "Catatan sengaja dibuat kurang dari 5 karakter.");

            // Tahap 5: klik button Ajukan dan tunggu pesan validasi minimal karakter muncul di bawah field Catatan.
            lemburPage.submitOvertime();
            boolean catatanValidationVisible = lemburPage.waitForCatatanMinimumCharacterMessage();

            // Tahap 6: validasi akhir TC-LMB-10, sistem harus menampilkan pesan bahwa Catatan minimal 5 karakter.
            validateTestCaseResult("TC-LMB-10", catatanValidationVisible,
                    "TC-LMB-10 gagal: pesan validasi 'Masukan minimal 5 karakter' tidak muncul.");
        });
    }

    @Test(priority = 11, description = "TC-LMB-11 - Validasi submit lembur hanya mengisi Jam Masuk")
    public void tcLmb11SubmitOvertimeWithOnlyJamMasuk() {
        runTestCase("TC-LMB-11", () -> {
            // Precondition: user berhasil login dan belum melakukan absensi keluar.
            // Tahap 1: masuk ke halaman absensi secara langsung agar test case berdiri sendiri.
            loginToAbsentPage();

            // Tahap 2: klik tombol Lembur lalu tunggu area pengajuan lembur siap digunakan.
            absenPage.clickLemburButton();
            WaitUtils.waitForUrlContains(driver, "overtime-client", 20);

            // Tahap 3: klik tombol Ajukan Lembur dan tunggu field form tampil agar validasi field kosong bisa diuji.
            lemburPage.clickAjukanLembur();

            // Tahap 4: isi hanya field Jam Masuk dengan format valid, sedangkan Jam Keluar dan Catatan dibiarkan kosong.
            LocalDateTime jamMasuk = LocalDate.now().atTime(18, 0);
            lemburPage.fillOnlyJamMasuk(jamMasuk);

            // Tahap 5: klik button Ajukan dan tunggu pesan validasi pada field yang belum diisi.
            lemburPage.submitOvertime();
            boolean jamKeluarValidationVisible = lemburPage.waitForJamKeluarRequiredMessage();
            boolean catatanValidationVisible = lemburPage.waitForCatatanMinimumCharacterMessage();

            // Tahap 6: validasi akhir TC-LMB-11, sistem harus menampilkan validasi Jam Keluar dan Catatan.
            boolean expectedValidationVisible = jamKeluarValidationVisible && catatanValidationVisible;
            validateTestCaseResult("TC-LMB-11", expectedValidationVisible,
                    "TC-LMB-11 gagal: validasi belum lengkap. Jam Keluar=" + jamKeluarValidationVisible
                            + ", Catatan=" + catatanValidationVisible);
        });
    }

    @Test(priority = 12, description = "TC-LMB-12 - Validasi submit lembur hanya mengisi Jam Keluar")
    public void tcLmb12SubmitOvertimeWithOnlyJamKeluar() {
        runTestCase("TC-LMB-12", () -> {
            // Precondition: user berhasil login dan belum melakukan absensi keluar.
            // Tahap 1: masuk ke halaman absensi secara langsung agar test case berdiri sendiri.
            loginToAbsentPage();

            // Tahap 2: klik tombol Lembur lalu tunggu area pengajuan lembur siap digunakan.
            absenPage.clickLemburButton();
            WaitUtils.waitForUrlContains(driver, "overtime-client", 20);

            // Tahap 3: klik tombol Ajukan Lembur dan tunggu field form tampil agar validasi field kosong bisa diuji.
            lemburPage.clickAjukanLembur();

            // Tahap 4: isi hanya field Jam Keluar dengan format valid, sedangkan Jam Masuk dan Catatan dibiarkan kosong.
            LocalDateTime jamKeluar = LocalDate.now().atTime(20, 0);
            lemburPage.fillOnlyJamKeluar(jamKeluar);

            // Tahap 5: klik button Ajukan dan tunggu pesan validasi pada field yang belum diisi.
            lemburPage.submitOvertime();
            boolean jamMasukValidationVisible = lemburPage.waitForJamMasukRequiredMessage();
            boolean catatanValidationVisible = lemburPage.waitForCatatanMinimumCharacterMessage();

            // Tahap 6: validasi akhir TC-LMB-12, sistem harus menampilkan validasi Jam Masuk dan Catatan.
            boolean expectedValidationVisible = jamMasukValidationVisible && catatanValidationVisible;
            validateTestCaseResult("TC-LMB-12", expectedValidationVisible,
                    "TC-LMB-12 gagal: validasi belum lengkap. Jam Masuk=" + jamMasukValidationVisible
                            + ", Catatan=" + catatanValidationVisible);
        });
    }

    @Test(priority = 13, description = "TC-LMB-13 - Validasi submit lembur hanya mengisi Catatan")
    public void tcLmb13SubmitOvertimeWithOnlyCatatan() {
        runTestCase("TC-LMB-13", () -> {
            // Precondition: user berhasil login dan belum melakukan absensi keluar.
            // Tahap 1: masuk ke halaman absensi secara langsung agar test case berdiri sendiri.
            loginToAbsentPage();

            // Tahap 2: klik tombol Lembur lalu tunggu area pengajuan lembur siap digunakan.
            absenPage.clickLemburButton();
            WaitUtils.waitForUrlContains(driver, "overtime-client", 20);

            // Tahap 3: klik tombol Ajukan Lembur dan tunggu field form tampil agar validasi field kosong bisa diuji.
            lemburPage.clickAjukanLembur();

            // Tahap 4: isi hanya field Catatan dengan minimal 5 karakter, sedangkan Jam Masuk dan Jam Keluar dibiarkan kosong.
            lemburPage.fillOnlyCatatan("Validasi field jam lembur kosong.");

            // Tahap 5: klik button Ajukan dan tunggu pesan validasi pada field jam yang belum diisi.
            lemburPage.submitOvertime();
            boolean jamMasukValidationVisible = lemburPage.waitForJamMasukRequiredMessage();
            boolean jamKeluarValidationVisible = lemburPage.waitForJamKeluarRequiredMessage();

            // Tahap 6: validasi akhir TC-LMB-13, sistem harus menampilkan validasi Jam Masuk dan Jam Keluar.
            boolean expectedValidationVisible = jamMasukValidationVisible && jamKeluarValidationVisible;
            validateTestCaseResult("TC-LMB-13", expectedValidationVisible,
                    "TC-LMB-13 gagal: validasi belum lengkap. Jam Masuk=" + jamMasukValidationVisible
                            + ", Jam Keluar=" + jamKeluarValidationVisible);
        });
    }

    @Test(priority = 14, description = "TC-LMB-14 - Verifikasi tombol Reset mengosongkan field Jam Masuk")
    public void tcLmb14ResetOvertimeFormAfterOnlyJamMasukFilled() {
        runTestCase("TC-LMB-14", () -> {
            // Precondition: user berhasil login dan belum melakukan absensi keluar.
            // Tahap 1: masuk ke halaman absensi secara langsung agar test case berdiri sendiri.
            loginToAbsentPage();

            // Tahap 2: klik tombol Lembur lalu tunggu area pengajuan lembur siap digunakan.
            absenPage.clickLemburButton();
            WaitUtils.waitForUrlContains(driver, "overtime-client", 20);

            // Tahap 3: klik tombol Ajukan Lembur dan tunggu field form tampil agar form siap diisi.
            lemburPage.clickAjukanLembur();

            // Tahap 4: isi hanya field Jam Masuk dengan format valid.
            lemburPage.fillOnlyJamMasuk(LocalDate.now().atTime(18, 0));

            // Tahap 5: klik tombol Reset untuk mengosongkan field yang sudah terisi.
            lemburPage.clickReset();

            // Tahap 6: validasi akhir TC-LMB-14, semua field harus kembali kosong setelah tombol Reset ditekan.
            boolean allFieldsEmptyAfterReset = lemburPage.waitForFormFieldsEmpty();
            validateTestCaseResult("TC-LMB-14", allFieldsEmptyAfterReset,
                    "TC-LMB-14 gagal: masih ada field yang terisi setelah tombol Reset diklik. "
                            + lemburPage.getFormFieldValuesSummary());
        });
    }

    @Test(priority = 15, description = "TC-LMB-15 - Verifikasi tombol Reset mengosongkan field Jam Keluar")
    public void tcLmb15ResetOvertimeFormAfterOnlyJamKeluarFilled() {
        runTestCase("TC-LMB-15", () -> {
            // Precondition: user berhasil login dan belum melakukan absensi keluar.
            // Tahap 1: masuk ke halaman absensi secara langsung agar test case berdiri sendiri.
            loginToAbsentPage();

            // Tahap 2: klik tombol Lembur lalu tunggu area pengajuan lembur siap digunakan.
            absenPage.clickLemburButton();
            WaitUtils.waitForUrlContains(driver, "overtime-client", 20);

            // Tahap 3: klik tombol Ajukan Lembur dan tunggu field form tampil agar form siap diisi.
            lemburPage.clickAjukanLembur();

            // Tahap 4: isi hanya field Jam Keluar dengan format valid.
            lemburPage.fillOnlyJamKeluar(LocalDate.now().atTime(20, 0));

            // Tahap 5: klik tombol Reset untuk mengosongkan field yang sudah terisi.
            lemburPage.clickReset();

            // Tahap 6: validasi akhir TC-LMB-15, semua field harus kembali kosong setelah tombol Reset ditekan.
            boolean allFieldsEmptyAfterReset = lemburPage.waitForFormFieldsEmpty();
            validateTestCaseResult("TC-LMB-15", allFieldsEmptyAfterReset,
                    "TC-LMB-15 gagal: masih ada field yang terisi setelah tombol Reset diklik. "
                            + lemburPage.getFormFieldValuesSummary());
        });
    }

    @Test(priority = 16, description = "TC-LMB-16 - Verifikasi tombol Reset mengosongkan field Catatan")
    public void tcLmb16ResetOvertimeFormAfterOnlyCatatanFilled() {
        runTestCase("TC-LMB-16", () -> {
            // Precondition: user berhasil login dan belum melakukan absensi keluar.
            // Tahap 1: masuk ke halaman absensi secara langsung agar test case berdiri sendiri.
            loginToAbsentPage();

            // Tahap 2: klik tombol Lembur lalu tunggu area pengajuan lembur siap digunakan.
            absenPage.clickLemburButton();
            WaitUtils.waitForUrlContains(driver, "overtime-client", 20);

            // Tahap 3: klik tombol Ajukan Lembur dan tunggu field form tampil agar form siap diisi.
            lemburPage.clickAjukanLembur();

            // Tahap 4: isi hanya field Catatan dengan minimal 5 karakter.
            lemburPage.fillOnlyCatatan("Catatan ini akan dibersihkan melalui tombol reset.");

            // Tahap 5: klik tombol Reset untuk mengosongkan field yang sudah terisi.
            lemburPage.clickReset();

            // Tahap 6: validasi akhir TC-LMB-16, semua field harus kembali kosong setelah tombol Reset ditekan.
            boolean allFieldsEmptyAfterReset = lemburPage.waitForFormFieldsEmpty();
            validateTestCaseResult("TC-LMB-16", allFieldsEmptyAfterReset,
                    "TC-LMB-16 gagal: masih ada field yang terisi setelah tombol Reset diklik. "
                            + lemburPage.getFormFieldValuesSummary());
        });
    }

    @Test(priority = 17, description = "TC-LMB-17 - Verifikasi pengisian form lembur valid selama 1 jam")
    public void tcLmb17FillOneHourOvertimeBeforeClockOut() {
        runTestCase("TC-LMB-17", () -> {
            // Precondition: user berhasil login dan belum melakukan absensi keluar.
            // Tahap 1: masuk ke halaman absensi secara langsung agar test case berdiri sendiri.
            loginToAbsentPage();

            // Tahap 2: klik tombol Lembur lalu tunggu area pengajuan lembur siap digunakan.
            absenPage.clickLemburButton();
            WaitUtils.waitForUrlContains(driver, "overtime-client", 20);

            // Tahap 3: klik tombol Ajukan Lembur dan tunggu field form tampil agar form siap diisi.
            lemburPage.clickAjukanLembur();

            // Tahap 4: isi semua field dengan data valid selama 1 jam pada hari yang sama.
            LocalDate tanggalLembur = LocalDate.now();
            LocalDateTime jamMasuk = tanggalLembur.atTime(19, 0);
            LocalDateTime jamKeluar = tanggalLembur.atTime(20, 0);
            lemburPage.fillOvertimeForm(
                    jamMasuk,
                    jamKeluar,
                    "Pengajuan lembur selama satu jam sebelum absen keluar.",
                    "Data valid dengan durasi lembur satu jam.");

            // Tahap 5: ambil ringkasan value form untuk memastikan data yang diinput tetap tersimpan pada field.
            String formFieldValues = lemburPage.getFormFieldValuesSummary();
            boolean oneHourOvertimeDataFilled = formFieldValues.contains("19:00")
                    && formFieldValues.contains("20:00")
                    && formFieldValues.contains("Pengajuan lembur selama satu jam sebelum absen keluar.");

            // Tahap 6: validasi akhir TC-LMB-17, seluruh data lembur valid selama 1 jam harus tampil pada form.
            validateTestCaseResult("TC-LMB-17", oneHourOvertimeDataFilled,
                    "TC-LMB-17 gagal: data lembur valid selama 1 jam tidak tersimpan pada form. " + formFieldValues);
        });
    }

    @Test(priority = 18, description = "TC-LMB-18 - Verifikasi pengisian form lembur valid mendekati batas akhir hari")
    public void tcLmb18FillOvertimeNearEndOfDayBeforeClockOut() {
        runTestCase("TC-LMB-18", () -> {
            // Precondition: user berhasil login dan belum melakukan absensi keluar.
            // Tahap 1: masuk ke halaman absensi secara langsung agar test case berdiri sendiri.
            loginToAbsentPage();

            // Tahap 2: klik tombol Lembur lalu tunggu area pengajuan lembur siap digunakan.
            absenPage.clickLemburButton();
            WaitUtils.waitForUrlContains(driver, "overtime-client", 20);

            // Tahap 3: klik tombol Ajukan Lembur dan tunggu field form tampil agar form siap diisi.
            lemburPage.clickAjukanLembur();

            // Tahap 4: isi semua field dengan data valid mendekati akhir hari dan tetap tidak melewati 24:00.
            LocalDate tanggalLembur = LocalDate.now();
            LocalDateTime jamMasuk = tanggalLembur.atTime(22, 0);
            LocalDateTime jamKeluar = tanggalLembur.atTime(23, 59);
            lemburPage.fillOvertimeForm(
                    jamMasuk,
                    jamKeluar,
                    "Pengajuan lembur mendekati batas akhir hari.",
                    "Data valid dengan jam keluar sebelum pergantian hari.");

            // Tahap 5: ambil ringkasan value form untuk memastikan data yang diinput tetap tersimpan pada field.
            String formFieldValues = lemburPage.getFormFieldValuesSummary();
            boolean nearEndOfDayOvertimeDataFilled = formFieldValues.contains("22:00")
                    && formFieldValues.contains("23:59")
                    && formFieldValues.contains("Pengajuan lembur mendekati batas akhir hari.");

            // Tahap 6: validasi akhir TC-LMB-18, seluruh data lembur valid mendekati akhir hari harus tampil pada form.
            validateTestCaseResult("TC-LMB-18", nearEndOfDayOvertimeDataFilled,
                    "TC-LMB-18 gagal: data lembur valid mendekati akhir hari tidak tersimpan pada form. "
                            + formFieldValues);
        });
    }


    private void loginToAbsentModuleByMenu() {
        loginPage.doLogin(Constants.EMAIL, Constants.PASSWORD);
        WaitUtils.waitForUrlToBe(driver, Constants.DASHBOARD_URL, 20);
        absenPage.openAbsentModule();
    }

    private void loginToAbsentPage() {
        loginPage.doLogin(Constants.EMAIL, Constants.PASSWORD);
        WaitUtils.waitForUrlToBe(driver, Constants.DASHBOARD_URL, 20);
        absenPage.ensureAbsentPage();
    }

    private void runTestCase(String testCaseId, Runnable steps) {
        testCaseResultLogged = false;
        try {
            steps.run();
        } catch (AssertionError | RuntimeException ex) {
            if (!testCaseResultLogged) {
                logTestCaseResult(testCaseId, "Fail");
            }
            throw ex;
        }
    }

    private void validateTestCaseResult(String testCaseId, boolean condition, String failureMessage) {
        if (condition) {
            logTestCaseResult(testCaseId, "Pass");
        } else if (!condition) {
            logTestCaseResult(testCaseId, "Fail");
        }

        Assert.assertTrue(condition, failureMessage);
    }

    private void logTestCaseResult(String testCaseId, String result) {
        String message = testCaseId + " = " + result;
        testCaseResultLogged = true;
        System.out.println(message);
        Reporter.log(message, false);
    }
}
