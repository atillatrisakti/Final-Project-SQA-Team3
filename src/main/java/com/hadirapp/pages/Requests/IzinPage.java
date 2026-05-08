package com.hadirapp.pages.Requests;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.hadirapp.utils.WaitUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class IzinPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    // Locators
    @FindBy(xpath = "//p[text()='Izin']")
    public WebElement iconIzin;

    @FindBy(xpath = "//button[@role='tab' and text()='Terlambat']")
    public WebElement tabTerlambat;

    @FindBy(xpath = "//button[@role='tab' and text()='Pulang Cepat']")
    public WebElement tabPulangCepat;

    @FindBy(xpath = "//button[@role='tab' and text()='Izin Off']")
    public WebElement tabIzinOff;

    @FindBy(xpath = "//button[text()='Ajukan Izin Terlambat']")
    public WebElement btnAjukanTerlambat;

    @FindBy(xpath = "//button[text()='Ajukan Pulang Cepat']")
    public WebElement btnAjukanPulangCepat;

    @FindBy(xpath = "//button[text()='Ajukan Izin Off']")
    public WebElement btnAjukanIzinOff;

    @FindBy(xpath = "//button[contains(@aria-label, 'Choose date')]")
    private WebElement inputTanggal;

    @FindBy(xpath = "//button[contains(@aria-label, 'Choose time')]")
    private WebElement inputJam;

    @FindBy(id = "notes") 
    private WebElement inputNotes;

    @FindBy(id = "reason") 
    private WebElement inputReason;

    @FindBy(xpath = "//button[@type='submit' and text()='Ajukan']")
    public WebElement ajukanSubmitButton;

    @FindBy(xpath = "//p[contains(@class, 'MuiFormHelperText-root')]")
    public List<WebElement> listErrorMessages;

    // Constructor
    // Method izin page
    public IzinPage(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // Method untuk scroll ke elemen tertentu
    private void scrollToElement(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        try { Thread.sleep(500); } catch (InterruptedException e) {} 
    }

    // Method untuk klik menu Izin
    public void klikMenuIzin() {
        WaitUtils.waitForElementClickable(driver, iconIzin, 20);
        try {
            iconIzin.click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", iconIzin);
        }
    }

    // Method untuk memilih tab berdasarkan nama
    public void pilihTab(String tabName) {
        WebElement tab = null;
        if (tabName.equals("Terlambat")) tab = tabTerlambat;
        else if (tabName.equals("Pulang Cepat")) tab = tabPulangCepat;
        else if (tabName.equals("Izin Off")) tab = tabIzinOff;
    
        scrollToElement(tab);
        WaitUtils.waitForElementClickable(driver, tab, 15);
        try {
            tab.click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", tab);
        }
    }

    // Method untuk klik tombol Tanggal (kalender)
    public void klikTombolTanggal() {
        WebElement btnTanggal = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@aria-label='Choose date']")));
        js.executeScript("arguments[0].click();", btnTanggal);
    }

    // Method untuk klik tombol Jam (time picker)
    public void pilihJamDariPicker(String jam) {
        String xpath = "//span[@role='option' and contains(@aria-label, '" + jam + " hours')]";
        WebElement targetJam = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        
        new Actions(driver)
            .moveToElement(targetJam, 0, 0)
            .click()
            .perform();
    }

    // Method untuk mengisi form Terlambat
    public void isiFormTerlambat(String tanggal, String jam, String notes) {
        WaitUtils.waitForElementClickable(driver, tabTerlambat, 10);
        tabTerlambat.click();
        WaitUtils.waitForElementClickable(driver, btnAjukanTerlambat, 15);
        try {
            btnAjukanTerlambat.click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", btnAjukanTerlambat);
        }

        if (!tanggal.isEmpty()) {
            WebElement iconKalender = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@aria-label='Choose date']")));
            js.executeScript("arguments[0].click();", iconKalender);
            
            try { Thread.sleep(1000); } catch (InterruptedException e) {}

            String xpathTanggal = "//button[@role='gridcell' and text()='" + tanggal + "']";
            WebElement pilihTanggal = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathTanggal)));
            js.executeScript("arguments[0].click();", pilihTanggal);
            
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
        }

            if (!jam.isEmpty()) {
            WebElement iconJam = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@aria-label='Choose time']")));
            js.executeScript("arguments[0].click();", iconJam);
            
            pilihJamDariPicker(jam); 
            
            try { Thread.sleep(500); } catch (InterruptedException e) {}
        }

        if (!notes.isEmpty()) {
        WebElement fieldNotes = wait.until(ExpectedConditions.elementToBeClickable(inputNotes));
        
        fieldNotes.click(); 
        fieldNotes.clear();
        fieldNotes.sendKeys(notes);
    }
        js.executeScript("arguments[0].click();", ajukanSubmitButton);
    }

    // Method untuk mengisi form Pulang Cepat
    public void isiFormPulangCepat(String tanggal, String jam, String notes) {
        WaitUtils.waitForElementClickable(driver, tabPulangCepat, 10);
        tabPulangCepat.click();
        WaitUtils.waitForElementClickable(driver, btnAjukanPulangCepat, 15);
        try {
            btnAjukanPulangCepat.click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", btnAjukanPulangCepat);
        }

        if (!tanggal.isEmpty()) {
            WebElement iconKalender = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@aria-label='Choose date']")));
            js.executeScript("arguments[0].click();", iconKalender);
            
            try { Thread.sleep(1000); } catch (InterruptedException e) {}

            String xpathTanggal = "//button[@role='gridcell' and text()='" + tanggal + "']";
            WebElement pilihTanggal = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathTanggal)));
            js.executeScript("arguments[0].click();", pilihTanggal);
            
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
        }

            if (!jam.isEmpty()) {
            WebElement iconJam = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@aria-label='Choose time']")));
            js.executeScript("arguments[0].click();", iconJam);
            
            pilihJamDariPicker(jam); 
            
            try { Thread.sleep(500); } catch (InterruptedException e) {}
        }

        if (!notes.isEmpty()) {
        WebElement fieldNotes = wait.until(ExpectedConditions.elementToBeClickable(inputNotes));
        
        fieldNotes.click(); 
        fieldNotes.clear();
        fieldNotes.sendKeys(notes);
    }
        js.executeScript("arguments[0].click();", ajukanSubmitButton);
    }

    // Method untuk mengisi form Izin Off
    public void isiFormIzinOff(String tanggal, String reason) {
        WaitUtils.waitForElementClickable(driver, tabIzinOff, 10);
        tabIzinOff.click();
        WaitUtils.waitForElementClickable(driver, btnAjukanIzinOff, 15);
        try {
            btnAjukanIzinOff.click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", btnAjukanIzinOff);
        }
        if (!tanggal.isEmpty()) {
            WebElement iconKalender = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@aria-label='Choose date']")));
            js.executeScript("arguments[0].click();", iconKalender);
            
            try { Thread.sleep(1000); } catch (InterruptedException e) {}

            String xpathTanggal = "//button[@role='gridcell' and text()='" + tanggal + "']";
            WebElement pilihTanggal = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathTanggal)));
            js.executeScript("arguments[0].click();", pilihTanggal);
            
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
        }
        if (!reason.isEmpty()) {
        WebElement fieldNotes = wait.until(ExpectedConditions.elementToBeClickable(inputReason));
        
        fieldNotes.click(); 
        fieldNotes.clear();
        fieldNotes.sendKeys(reason);
    }
        js.executeScript("arguments[0].click();", ajukanSubmitButton);
    }

    // Method untuk mendapatkan semua pesan error yang muncul
    public List<String> getAllErrorMessages() {
        wait.until(ExpectedConditions.visibilityOfAllElements(listErrorMessages));
        
        List<String> messages = new ArrayList<>();
        for (WebElement error : listErrorMessages) {
            if (error.isDisplayed()) {
                messages.add(error.getText());
            }
        }
        return messages;
    }

    // Method untuk mendapatkan pesan error spesifik berdasarkan teks yang diharapkan
    public String getErrorMessage(String expectedMessage) {
        String xpath = "//p[contains(text(), '" + expectedMessage + "')]";
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath))).getText();
    }

    // Cek apakah halaman list izin tampil setelah submit
    public boolean isHalamanIzinDisplayed() {
        try {
            WebElement card = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("(//div[contains(@class, 'MuiCard-root')])[1]")
                )
            );
            return card.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Ambil teks dari card izin terbaru (pertama di list)
    public String getIzinTerbaruText() {
        try {
            WebElement firstCard = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("(//div[contains(@class, 'MuiCard-root')])[1]")
                )
            );
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", firstCard);
            return firstCard.getText();
        } catch (Exception e) {
            System.out.println("Card izin terbaru tidak ditemukan: " + e.getMessage());
            return "";
        }
    }
}