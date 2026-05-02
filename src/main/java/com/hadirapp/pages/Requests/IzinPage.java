package com.hadirapp.pages.Requests;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.*;
import com.hadirapp.utlis.WaitUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class IzinPage {
    private WebDriver driver;
    private JavascriptExecutor js;

    // Locators
    @FindBy(xpath = "//button[normalize-space()='Izin']")
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
    }

    // Method untuk scroll ke elemen tertentu
    private void scrollToElement(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        try { Thread.sleep(500); } catch (InterruptedException e) {} 
    }

    // Method untuk klik menu Izin
    public void klikMenuIzin() {
        WebElement btn = WaitUtils.waitForElementClickable(driver, iconIzin, 20);
        js.executeScript("window.scrollBy(0, -100);"); 
        js.executeScript("arguments[0].click();", btn);
    }

    // Method untuk memilih tab berdasarkan nama
    public void pilihTab(String tabName) {
        WebElement tab = null;
        if (tabName.equals("Terlambat")) tab = tabTerlambat;
        else if (tabName.equals("Pulang Cepat")) tab = tabPulangCepat;
        else if (tabName.equals("Izin Off")) tab = tabIzinOff;
    
        scrollToElement(tab);
        js.executeScript("arguments[0].click();", tab);
    }

    // Method untuk klik tombol Tanggal (kalender)
    public void klikTombolTanggal() {
        WebElement btnTanggal = WaitUtils.waitForElementPresence(driver, By.xpath("//button[@aria-label='Choose date']"), 20);
        js.executeScript("arguments[0].click();", btnTanggal);
    }

    // Method untuk klik tombol Jam (time picker)
    public void pilihJamDariPicker(String jam) {
        String xpath = "//span[@role='option' and contains(@aria-label, '" + jam + " hours')]";
        WebElement targetJam = WaitUtils.waitForElementVisible(driver, By.xpath(xpath), 20);
        
        new Actions(driver)
            .moveToElement(targetJam, 0, 0)
            .click()
            .perform();
    }

    // Method untuk mengisi form Terlambat
    public void isiFormTerlambat(String tanggal, String jam, String notes) {
        tabTerlambat.click();
        btnAjukanTerlambat.click();

        if (!tanggal.isEmpty()) {
            WebElement iconKalender = WaitUtils.waitForElementClickable(driver, By.xpath("//button[@aria-label='Choose date']"), 20);
            js.executeScript("arguments[0].click();", iconKalender);
            
            try { Thread.sleep(1000); } catch (InterruptedException e) {}

            String xpathTanggal = "//button[@role='gridcell' and text()='" + tanggal + "']";
            WebElement pilihTanggal = WaitUtils.waitForElementClickable(driver, By.xpath(xpathTanggal), 20);
            js.executeScript("arguments[0].click();", pilihTanggal);
            
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
        }

            if (!jam.isEmpty()) {
            WebElement iconJam = WaitUtils.waitForElementClickable(driver, By.xpath("//button[@aria-label='Choose time']"), 20);
            js.executeScript("arguments[0].click();", iconJam);
            
            pilihJamDariPicker(jam); 
            
            try { Thread.sleep(500); } catch (InterruptedException e) {}
        }

        if (!notes.isEmpty()) {
        WebElement fieldNotes = WaitUtils.waitForElementClickable(driver, inputNotes, 20);
        
        fieldNotes.click(); 
        fieldNotes.clear();
        fieldNotes.sendKeys(notes);
    }
        js.executeScript("arguments[0].click();", ajukanSubmitButton);
    }

    // Method untuk mengisi form Pulang Cepat
    public void isiFormPulangCepat(String tanggal, String jam, String notes) {
        tabPulangCepat.click();
        btnAjukanPulangCepat.click();

        if (!tanggal.isEmpty()) {
            WebElement iconKalender = WaitUtils.waitForElementClickable(driver, By.xpath("//button[@aria-label='Choose date']"), 20);
            js.executeScript("arguments[0].click();", iconKalender);
            
            try { Thread.sleep(1000); } catch (InterruptedException e) {}

            String xpathTanggal = "//button[@role='gridcell' and text()='" + tanggal + "']";
            WebElement pilihTanggal = WaitUtils.waitForElementClickable(driver, By.xpath(xpathTanggal), 20);
            js.executeScript("arguments[0].click();", pilihTanggal);
            
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
        }

            if (!jam.isEmpty()) {
            WebElement iconJam = WaitUtils.waitForElementClickable(driver, By.xpath("//button[@aria-label='Choose time']"), 20);
            js.executeScript("arguments[0].click();", iconJam);
            
            pilihJamDariPicker(jam); 
            
            try { Thread.sleep(500); } catch (InterruptedException e) {}
        }

        if (!notes.isEmpty()) {
        WebElement fieldNotes = WaitUtils.waitForElementClickable(driver, inputNotes, 20);
        
        fieldNotes.click(); 
        fieldNotes.clear();
        fieldNotes.sendKeys(notes);
    }
        js.executeScript("arguments[0].click();", ajukanSubmitButton);
    }

    // Method untuk mengisi form Izin Off
    public void isiFormIzinOff(String tanggal, String reason) {
        tabIzinOff.click();
        btnAjukanIzinOff.click();
        if (!tanggal.isEmpty()) {
            WebElement iconKalender = WaitUtils.waitForElementClickable(driver, By.xpath("//button[@aria-label='Choose date']"), 20);
            js.executeScript("arguments[0].click();", iconKalender);
            
            try { Thread.sleep(1000); } catch (InterruptedException e) {}

            String xpathTanggal = "//button[@role='gridcell' and text()='" + tanggal + "']";
            WebElement pilihTanggal = WaitUtils.waitForElementClickable(driver, By.xpath(xpathTanggal), 20);
            js.executeScript("arguments[0].click();", pilihTanggal);
            
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
        }
        if (!reason.isEmpty()) {
        WebElement fieldNotes = WaitUtils.waitForElementClickable(driver, inputReason, 20);
        
        fieldNotes.click(); 
        fieldNotes.clear();
        fieldNotes.sendKeys(reason);
    }
        js.executeScript("arguments[0].click();", ajukanSubmitButton);
    }

    // Method untuk mendapatkan semua pesan error yang muncul
    public List<String> getAllErrorMessages() {
        WaitUtils.waitForAllElementsVisible(driver, listErrorMessages, 20);
        
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
        return WaitUtils.waitForElementVisible(driver, By.xpath(xpath), 20).getText();
    }
}