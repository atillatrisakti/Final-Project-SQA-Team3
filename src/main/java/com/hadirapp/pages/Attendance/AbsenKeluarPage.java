package com.hadirapp.pages.Attendance;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.hadirapp.utlis.WaitUtils;

public class AbsenKeluarPage {
    private WebDriver driver;
    private WebDriverWait wait;

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
        this.wait = WaitUtils.getExplicitWait(driver, 20);
        PageFactory.initElements(driver, this);
    }

    public void klikKeluar() {
        wait.until(ExpectedConditions.elementToBeClickable(btnKeluar));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btnKeluar);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnKeluar);
    }

    public void isiNote(String note) {
        wait.until(ExpectedConditions.visibilityOf(fieldNote)).clear();
        fieldNote.sendKeys(note);
    }

    public void klikAbsenPulang() {
        wait.until(ExpectedConditions.elementToBeClickable(btnAbsenPulang)).click();
    }

    // Menggabungkan alur absen agar test lebih ringkas
    public void melakukanAbsenPulang(String note) {
        if (note != null && !note.isEmpty()) {
            isiNote(note);
        }
        klikAbsenPulang();
    }
    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(btnMenu)).click();
        wait.until(ExpectedConditions.elementToBeClickable(btnLogout)).click();
        wait.until(ExpectedConditions.urlContains("login"));
    }
}