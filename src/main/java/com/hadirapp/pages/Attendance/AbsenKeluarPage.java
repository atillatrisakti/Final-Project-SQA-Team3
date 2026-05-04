package com.hadirapp.pages.Attendance;

import com.hadirapp.utlis.WaitUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AbsenKeluarPage {
    private WebDriver driver;

    @FindBy(name = "notes")
    private WebElement fieldNote;

    @FindBy(xpath = "//button[.//p[text()='Keluar']]")
    private WebElement btnKeluar;

    @FindBy(xpath = "//button[@type='submit' and contains(text(), 'Absen Pulang')]")
    private WebElement btnAbsenPulang;

    @FindBy(xpath = "//button//img[@alt='menu']")
    private WebElement btnMenu;

    @FindBy(xpath = "//button[contains(text(), 'Logout')]")
    private WebElement btnLogout;

    public AbsenKeluarPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void klikKeluar() {
        WaitUtils.waitForElementClickable(driver, btnKeluar, 20);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btnKeluar);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnKeluar);
    }

    public void isiNote(String note) {
        WaitUtils.waitForElementVisible(driver, fieldNote, 20).clear();
        fieldNote.sendKeys(note);
    }

    public void klikAbsenPulang() {
        WaitUtils.waitForElementClickable(driver, btnAbsenPulang, 20).click();
    }

    // Menggabungkan alur absen agar test lebih ringkas
    public void melakukanAbsenPulang(String note) {
        if (note != null && !note.isEmpty()) {
            isiNote(note);
        }
        klikAbsenPulang();
    }

    public void logout() {
        WaitUtils.waitForElementClickable(driver, btnMenu, 20).click();
        WaitUtils.waitForElementClickable(driver, btnLogout, 20).click();
        WaitUtils.waitForUrlContains(driver, "login", 20);
    }
}