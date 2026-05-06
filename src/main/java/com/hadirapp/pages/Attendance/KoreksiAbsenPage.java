package com.hadirapp.pages.Attendance;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;

import com.hadirapp.utlis.WaitUtils;

import java.time.Duration;
import java.util.List;

public class KoreksiAbsenPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    // Locators
    @FindBy(xpath = "//button[normalize-space()='Ajukan Koreksi']")
    private WebElement btnBukaForm;

    @FindBy(xpath = "//button[@type='submit' and text()='Ajukan']")
    public WebElement ajukanSubmitButton;

    @FindBy(xpath = "//div[contains(@class, 'css-pampsj')]//following::button[contains(@aria-label, 'Choose date')][1]")
    private WebElement jamMasukInput;

    @FindBy(xpath = "//div[contains(@class, 'css-pampsj')]//following::button[contains(@aria-label, 'Choose date')][2]")
    private WebElement jamKeluarInput;

    @FindBy(id = "is_wfh")
    private WebElement dropdownTipeAbsen;

    @FindBy(xpath = "//div[@role='alert'] | //div[contains(@class, 'MuiAlert-message')]")
    private WebElement alertMessage;

    @FindBy(xpath = "//p[text()='Koreksi Absen']")
    private WebElement iconKoreksiAbsen;

    // --- Constructor ---
    // Method koreksi absen page
    public KoreksiAbsenPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.js = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }

    // klik menu koreksi absen dan buka form
    public void klikMenuKoreksiAbsen() {
        WebElement menu = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//p[text()='Koreksi Absen']")));
        js.executeScript("arguments[0].click();", menu);
        WaitUtils.waitForUrlContains(driver, "correction", 20);
        WebElement btnBuka = wait.until(ExpectedConditions.visibilityOf(btnBukaForm));
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", btnBuka);
        js.executeScript("arguments[0].click();", btnBuka);
    }

    // isi data koreksi absen
    public void isiDataKoreksi(String jamMasuk, String jamKeluar, String tipe) {
        if (!jamMasuk.isEmpty()) {
            openJamMasuk();
            pilihTanggal("30");
            pilihWaktuManual("8", "00");
            WaitUtils.waitForElementClickable(driver, ajukanSubmitButton, 10);
        }

        if (!jamKeluar.isEmpty()) {
            openJamKeluar();
            pilihTanggal("30");
            pilihWaktuManual("17", "00");
            WaitUtils.waitForElementClickable(driver, ajukanSubmitButton, 10);
        }

        if (!tipe.isEmpty()) {
            selectTipeAbsen(tipe);
            WaitUtils.waitForElementClickable(driver, ajukanSubmitButton, 10);
        }

        WaitUtils.waitForElementClickable(driver, ajukanSubmitButton, 10);
        try {
            ajukanSubmitButton.click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", ajukanSubmitButton);
        }
    }

    // error message
    public String getErrorMessage(String expectedMessage) {
        try {
            String xpath = "//*[contains(text(), '" + expectedMessage + "')]";
            WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
            js.executeScript("arguments[0].scrollIntoView(true);", errorElement);

            return errorElement.getText();
        } catch (Exception e) {
            return "Error message '" + expectedMessage + "' tidak ditemukan";
        }
    }

    // Cek apakah halaman koreksi (List Koreksi) tampil
    public boolean isHalamanKoreksiDisplayed() {
        try {
            WebElement header = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(),'List Koreksi')] | //*[contains(text(),'Halaman Koreksi')]")
                )
            );
            return header.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Ambil teks dari card koreksi terbaru (pertama di list)
    public String getKoreksiTerbaruText() {
        try {
            WebElement firstCard = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("(//div[contains(@class, 'MuiCard-root')])[1]")
                )
            );
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", firstCard);
            return firstCard.getText();
        } catch (Exception e) {
            System.out.println("Card koreksi terbaru tidak ditemukan: " + e.getMessage());
            return "";
        }
    }

    // --- Helper Methods ---
    // Method untuk membuka time picker Jam Masuk
    public void openJamMasuk() {
        wait.until(ExpectedConditions.elementToBeClickable(jamMasukInput)).click();
    }

    // Method untuk membuka time picker Jam Keluar
    public void openJamKeluar() {
        wait.until(ExpectedConditions.elementToBeClickable(jamKeluarInput)).click();
    }

    // Method untuk memilih tipe absen (WFO/WFH)
    public void selectTipeAbsen(String tipe) {
        try {
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", dropdownTipeAbsen);
            Thread.sleep(500);

            wait.until(ExpectedConditions.elementToBeClickable(dropdownTipeAbsen)).click();
            Thread.sleep(800); 
            String xpathOpsi = "//li[@role='option' and (contains(translate(text(), 'wfo', 'WFO'), '"
                    + tipe.toUpperCase() + "'))]";
            WebElement opsi = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathOpsi)));

            new Actions(driver)
                    .moveToElement(opsi)
                    .click()
                    .pause(Duration.ofMillis(1000))
                    .sendKeys(Keys.TAB) 
                    .perform();

            System.out.println("Tipe absen '" + tipe + "' berhasil dipilih.");
        } catch (Exception e) {
            System.out.println("Gagal memilih tipe absen: " + e.getMessage());
        }
    }

    // method untuk memilih tanggal dari date picker
    public void pilihTanggal(String tgl) {
        By tglLocator = By.xpath("//button[@role='gridcell' and text()='" + tgl + "']");
        WebElement day = wait.until(ExpectedConditions.elementToBeClickable(tglLocator));
        day.click();
    }

    // Method untuk memilih jam dan menit secara manual dari time picker
    private void pilihWaktuManual(String jam, String menit) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("MuiClock-clock")));

            WebElement jamTarget = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[@role='option' and @aria-label='" + jam + " hours']")));
            new Actions(driver).moveToElement(jamTarget).click().perform();

            Thread.sleep(800); 
            WebElement menitTarget = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[@role='option' and @aria-label='" + menit + " minutes']")));
            new Actions(driver).moveToElement(menitTarget).click().perform();

            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("Gagal memilih waktu: " + e.getMessage());
            driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
        }
    }

    // method untuk scroll ke element tertentu
    private void scrollToElement(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
    }
}