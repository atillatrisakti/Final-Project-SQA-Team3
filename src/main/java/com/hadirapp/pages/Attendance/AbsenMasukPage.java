package com.hadirapp.pages.Attendance;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.hadirapp.utils.WaitUtils;

public class AbsenMasukPage {
    private WebDriver driver;
    private JavascriptExecutor js;

    @FindBy(xpath = "//button[contains(., 'Absen Masuk') or normalize-space()='Absen Masuk']")
    public WebElement btnMenuAbsenMasuk;

    @FindBy(xpath = "//video") // Menunggu elemen video/kamera muncul
    public WebElement videoElement;

    // Locator spesifik untuk tombol capture berdasarkan icon SVG dengan class feather-camera
    @FindBy(xpath = "//button[descendant::*[local-name()='svg' and contains(@class, 'feather-camera')]]") 
    public WebElement btnCaptureCamera;

    @FindBy(id = "mui-component-select-is_wfh")
    public WebElement dropdownTipeAbsen;

    @FindBy(name = "notes")
    public WebElement fieldNote;

    @FindBy(xpath = "//button[@type='submit' and contains(., 'Absen Masuk')]")
    public WebElement btnSubmitAbsen;

    public AbsenMasukPage(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }

    public void klikMenuAbsenMasuk() {
        WaitUtils.waitForElementClickable(driver, btnMenuAbsenMasuk, 20);
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", btnMenuAbsenMasuk);
        btnMenuAbsenMasuk.click();
    }

    public void ambilFoto() {
        WaitUtils.waitForElementVisible(driver, videoElement, 20);
        try { Thread.sleep(2000); } catch (InterruptedException e) {} // Tunggu stream stabil
        WaitUtils.waitForElementClickable(driver, btnCaptureCamera, 20).click();
    }

    public void pilihTipeAbsen(String tipe) {
        WaitUtils.waitForElementClickable(driver, dropdownTipeAbsen, 20).click();
        try { Thread.sleep(1000); } catch (InterruptedException e) {} // Tunggu animasi dropdown MUI

        if (tipe.equalsIgnoreCase("WFO")) {
            By optionWfo = By.xpath("//li[@data-value='false']");
            WaitUtils.waitForElementClickable(driver, optionWfo, 20).click();
        } else if (tipe.equalsIgnoreCase("WFH")) {
            By optionWfh = By.xpath("//li[@data-value='true']");
            WaitUtils.waitForElementClickable(driver, optionWfh, 20).click();
        }
    }

    public void isiNote(String note) {
        WaitUtils.waitForElementVisible(driver, fieldNote, 20).clear();
        fieldNote.sendKeys(note);
    }

    public void submitAbsenMasuk() {
        WaitUtils.waitForElementClickable(driver, btnSubmitAbsen, 20).click();
    }
}
