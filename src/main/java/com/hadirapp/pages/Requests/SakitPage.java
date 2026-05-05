package com.hadirapp.pages.Requests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class SakitPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    @FindBy(xpath = "//p[text()='Sakit']")
    public WebElement menuSakit;

    @FindBy(xpath = "//button[contains(text(), 'Ajukan Sakit')]")
    public WebElement btnAjukanSakit;

    @FindBy(xpath = "//*[local-name()='svg' and @data-testid='AccessAlarmIcon']")
    public WebElement iconJamKalender;

    @FindBy(xpath = "//input[@type='file']")
    public WebElement inputUploadFile;

    @FindBy(xpath = "//button[text()='Simpan']")
    public WebElement btnSimpanKalender;

    @FindBy(xpath = "//button[@type='submit' and text()='Ajukan']")
    public WebElement btnSubmit;

    @FindBy(xpath = "//button[text()='Reset']")
    public WebElement btnReset;

    @FindBy(xpath = "//span[contains(text(), '.png')]")
    public WebElement labelFileName;

    @FindBy(xpath = "//input[@placeholder='Pilih Tanggal']")
    public WebElement inputTanggal;

    @FindBy(xpath = "//div[contains(@class, 'MuiBox-root')]//p[text()=' / ' or text()='/']")
    public WebElement textTanggal;

    @FindBy(xpath = "//div[@role='alert']//div[contains(text(), 'sudah melakukan izin sakit')]")
    public WebElement toastErrorDuplicate;

    @FindBy(xpath = "//p[contains(@class, 'MuiFormHelperText-root')]")
    public List<WebElement> listErrorMessages;

    @FindBy(xpath = "//div[@role='alert']//div[contains(text(), 'Incorrect datetime')]")
    public WebElement toastErrorInvalidDate;

    // Constructor
    public SakitPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    // Method untuk memilih rentang tanggal sakit
    public void pilihRentangTanggal(String tglMulai, String tglSelesai) {
        wait.until(ExpectedConditions.elementToBeClickable(iconJamKalender)).click();
        clickTanggal(tglMulai);
        clickTanggal(tglSelesai);
        wait.until(ExpectedConditions.elementToBeClickable(btnSimpanKalender)).click();
    }

    // method pilih tanggal
    private void clickTanggal(String tgl) {
        String xpathTgl = String.format("//button[contains(@class, 'rdrDay')]//span[text()='%s']", tgl);

        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathTgl)));
            element.click();
        } catch (Exception e) {
            WebElement element = driver.findElement(By.xpath(xpathTgl));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    // Method untuk upload dokumen
    public void uploadDokumen(String path) {
        inputUploadFile.sendKeys(path);
    }

    // Method untuk klik tombol Ajukan
    public void klikTombolAjukan() {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btnSubmit);
            Thread.sleep(500);

            wait.until(ExpectedConditions.elementToBeClickable(btnSubmit)).click();
        } catch (Exception e) {
            System.out.println("Klik normal gagal, mencoba JavaScript Click...");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", btnSubmit);
        }
    }

    // Method untuk mendapatkan teks jika tanggal kosong
    public String getTeksTanggal() {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOf(textTanggal));
            return element.getText().trim();
        } catch (Exception e) {
            WebElement element = driver
                    .findElement(By.xpath("//label[text()='Pilih Tanggal']/following-sibling::div//p"));
            return element.getText().trim();
        }
    }

    // Method untuk klik tombol Reset
    public void klikReset() {
        btnReset.click();
    }

    //method error message
    public String getErrorMessage(String expectedText) {
        String xpath = "//p[contains(text(), '" + expectedText + "')]";
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath))).getText();
    }

    // Method untuk mendapatkan semua pesan error yang muncul
    public List<String> getAllErrorMessages() {
        wait.until(ExpectedConditions.visibilityOfAllElements(listErrorMessages));
        List<String> messages = new ArrayList<>();
        for (WebElement error : listErrorMessages) {
            messages.add(error.getText());
        }
        return messages;
    }
}