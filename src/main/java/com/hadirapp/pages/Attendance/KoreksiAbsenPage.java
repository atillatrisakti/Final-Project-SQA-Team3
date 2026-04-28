package com.hadirapp.pages.Attendance;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

public class KoreksiAbsenPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    @FindBy(xpath = "//button[normalize-space()='Ajukan Koreksi']")
    public WebElement ajukanKoreksiButton;

    @FindBy(xpath = "//button[normalize-space()='Ajukan']")
    private WebElement ajukanButton;

    // Ubah @FindBy di KoreksiAbsenPage.java menjadi:
    @FindBy(xpath = "//input[contains(@placeholder, 'hh:mm')]/following-sibling::div//button[@type='button']")
    private WebElement btnJamMasuk;

    @FindBy(xpath = "//input[contains(@placeholder, 'hh:mm')]/following::button[@aria-label='Choose time'][2]")
    private WebElement btnJamKeluar;

    // Gunakan list untuk tanggal agar tidak rusak saat tanggal ganti
    @FindBy(xpath = "//button[contains(@class, 'MuiPickersDay-root') and not(contains(@class, 'Mui-disabled'))]")
    private java.util.List<WebElement> daftarTanggal;

    @FindBy(id = "is_wfh")
    private WebElement dropdownTipeAbsen;

    @FindBy(xpath = "//p[contains(@class, 'Mui-error')]")
    private WebElement errorMessage;

    public KoreksiAbsenPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.js = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }

    // Method perbaikan untuk klik jam
    public void klikIkonJam(WebElement element) {
        try {
            // Scroll ke elemen
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);

            // Klik menggunakan JS
            js.executeScript(
                    "arguments[0].dispatchEvent(new MouseEvent('click', {view: window, bubbles:true, cancelable: true}));",
                    element);
        } catch (Exception e) {
            System.out.println("Gagal klik ikon jam: " + e.getMessage());
        }
    }

    // Tambahkan di dalam kelas KoreksiAbsenPage.java
    public boolean isBtnJamMasukDisplayed() {
        try {
            // Kita gunakan wait singkat untuk memastikan elemennya benar-benar ada di DOM
            return wait.until(ExpectedConditions.visibilityOf(btnJamMasuk)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void debugPageSource() {
        // Ini akan mencetak isi HTML di terminal agar Anda bisa melihat
        // apakah "Choose time" benar-benar ada atau tidak.
        System.out.println(driver.getPageSource());
    }

    public void pilihTanggalHariIni() {
        // Tunggu sampai tanggal bisa diklik
        wait.until(ExpectedConditions.elementToBeClickable(daftarTanggal.get(0))).click();

        // PENTING: Tambahkan ini untuk menunggu transisi UI dari Kalender ke Jam
        // Kita tunggu sampai elemen "Time Picker" (input jam) muncul di DOM
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@aria-label='Time']")));
    }

    // Method untuk klik tombol Jam (time picker) yang terbukti stabil
    public void pilihJamDariPicker(String jam) {
        // Gunakan XPath yang sangat spesifik berdasarkan aria-label
        String label = jam + " hours";
        String xpath = "//span[@role='option' and @aria-label='" + label + "']";

        // Tunggu sampai elemen jam benar-benar terlihat
        WebElement targetJam = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));

        // Gunakan Actions dengan offset (0,0) seperti di IzinPage
        // Ini akan mengklik tepat di titik pusat elemen span tersebut
        new Actions(driver)
                .moveToElement(targetJam, 0, 0)
                .click()
                .perform();

        System.out.println("Berhasil memilih jam " + jam + " menggunakan metode IzinPage");
    }

    public void klikJamViaKoordinat(String jam) {
    String label = jam + " hours";
    // Cari elemennya dulu untuk dapat posisinya
    WebElement targetJam = driver.findElement(By.xpath("//span[@aria-label='" + label + "']"));
    
    // Ambil lokasi elemen
    int x = targetJam.getLocation().getX();
    int y = targetJam.getLocation().getY();
    
    // Klik menggunakan Actions ke koordinat spesifik tersebut
    new Actions(driver)
        .moveByOffset(x + 5, y + 5) // +5 agar klik di dalam area
        .click()
        .perform();
}

    public void pilihTipeAbsen(String tipe) {
        // 1. Tunggu agar dropdown muncul di layar setelah trigger jam
        wait.until(ExpectedConditions.elementToBeClickable(dropdownTipeAbsen));

        // 2. Klik dropdown untuk membukanya
        dropdownTipeAbsen.click();

        // 3. Cari dan klik pilihan WFH atau WFO
        // Menggunakan XPath yang dinamis berdasarkan teks (WFH/WFO)
        // Biasanya di MUI, dropdown isinya adalah tag <li>
        String optionXpath = "//li[contains(text(), '" + tipe.toUpperCase() + "')]";
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(optionXpath)));
        option.click();
    }

    public void isiDataKoreksi(String tanggal, String jamMasuk, String jamKeluar, String tipe) {

        // 1. Proses Jam Masuk
        js.executeScript("arguments[0].click();", btnJamMasuk);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='27']"))).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        pilihTanggalHariIni(); // Menggunakan logic tanggal yang sudah kita buat

        // Paksa agar aplikasi keluar dari mode input teks dan masuk ke mode klik dial
        driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        // Paksa matikan semua transisi/animasi CSS agar elemen tidak bergerak-gerak
        js.executeScript("var style = document.createElement('style'); " +
                "style.innerHTML = '* { transition: none !important; }'; " +
                "document.head.appendChild(style);");

        // Tunggu dan pilih jam
        pilihJamDariPicker(jamMasuk);

        // 2. Proses Jam Keluar
        js.executeScript("arguments[0].click();", btnJamKeluar);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        // Tunggu dan pilih tanggal kembali (opsional, sesuaikan dengan kebutuhan
        // aplikasi)
        wait.until(ExpectedConditions.visibilityOfAllElements(daftarTanggal));
        js.executeScript("arguments[0].click();", daftarTanggal.get(0));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        // Pilih jam keluar
        pilihJamDariPicker(jamKeluar);

        // 3. Dropdown Tipe (Muncul setelah trigger)
        if (!tipe.isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(dropdownTipeAbsen));
            js.executeScript("arguments[0].click();", dropdownTipeAbsen);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            WebElement option = driver.findElement(By.xpath("//li[text()='" + tipe.toUpperCase() + "']"));
            js.executeScript("arguments[0].click();", option);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }

        // 4. Submit
        js.executeScript("arguments[0].click();", ajukanButton);
    }

    // Method untuk ambil pesan sukses (biasanya berupa Toast/Snackbar)
    public String getAlertSuccessMessage() {
        // Sesuaikan XPath ini dengan inspect element pesan sukses Anda
        By alertLocator = By
                .xpath("//div[contains(@class, 'success-message') or contains(text(), 'berhasil melakukan koreksi')]");
        return wait.until(ExpectedConditions.visibilityOfElementLocated(alertLocator)).getText();
    }

    // Method untuk cek apakah pesan error muncul saat form kosong
    public String getErrorMessage(String expectedMessage) {
        String xpath = "//p[contains(text(), '" + expectedMessage + "')]";
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath))).getText();
    }
}