package com.hadirapp.pages.Requests;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.hadirapp.pages.BasePage;

public class LemburPage extends BasePage {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd MM yyyy, HH:mm");

    private final By ajukanLemburButton = By.xpath("//button[contains(normalize-space(.),'Ajukan Lembur')]");
    private final By tanggalInput = By.xpath("//input[@type='tel' and @placeholder='dd mm yyyy, hh:mm']");
    private final By tanggalMasukInput = By.xpath("(//input[@type='tel' and @placeholder='dd mm yyyy, hh:mm'])[1]");
    private final By tanggalKeluarInput = By.xpath("(//input[@type='tel' and @placeholder='dd mm yyyy, hh:mm'])[2]");
    private final By keteranganInput = By.xpath(
            "//textarea[@name='notes' or contains(@name,'keterangan') or contains(@id,'keterangan') or contains(@name,'catatan')]"
                    + " | //input[contains(@name,'keterangan') or contains(@id,'keterangan') or contains(@name,'catatan')]"
                    + " | //label[contains(.,'Keterangan')]/following::textarea[1]"
                    + " | //label[contains(.,'Keterangan')]/following::input[1]"
                    + " | //label[contains(.,'Catatan')]/following::textarea[1]"
                    + " | //label[contains(.,'Catatan')]/following::input[1]"
                    + " | //label[contains(.,'Notes')]/following::textarea[1]");
    private final By submitButton = By.xpath("//button[contains(normalize-space(.),'Ajukan') and not(contains(normalize-space(.),'Ajukan Lembur'))]");
    private final By submitFallbackButton = By.xpath("//button[@type='submit']");
    private final By resetButton = By.xpath(
            "//button[contains(normalize-space(.),'Reset')]"
                    + " | //*[self::button or self::a or self::span][contains(normalize-space(.),'Reset')]");
    private final By jamMasukRequiredMessage = By.xpath("//*[contains(normalize-space(.),'Jam masuk harus di isi!')]");
    private final By jamKeluarRequiredMessage = By.xpath("//*[contains(normalize-space(.),'Jam Keluar harus di isi!')]");
    private final By catatanMinimumCharacterMessage = By.xpath("//*[contains(normalize-space(.),'Masukan minimal 5 karakter')]");
    private final By successMessage = By.xpath(
            "//*[contains(normalize-space(.),'berhasil')]"
                    + " | //*[contains(normalize-space(.),'Berhasil')]"
                    + " | //*[contains(normalize-space(.),'menunggu persetujuan')]"
                    + " | //*[contains(normalize-space(.),'diajukan')]");

    public LemburPage(WebDriver driver) {
        super(driver);
    }

    public void clickAjukanLembur() {
        clickAnyVisible(ajukanLemburButton);
        waitAnyVisible(tanggalInput, keteranganInput);
    }

    public void fillOvertimeForm(LocalDateTime jamMasuk, LocalDateTime jamKeluar, String aktivitas, String keterangan) {
        setDateTimeValue(tanggalMasukInput, jamMasuk);
        setDateTimeValue(tanggalKeluarInput, jamKeluar);
        type(keteranganInput, aktivitas + System.lineSeparator() + keterangan);
    }

    public void fillOvertimeFormWithoutJamMasuk(LocalDateTime jamKeluar, String keterangan) {
        setDateTimeValue(tanggalKeluarInput, jamKeluar);
        type(keteranganInput, keterangan);
    }

    public void fillOvertimeFormWithoutJamKeluar(LocalDateTime jamMasuk, String keterangan) {
        setDateTimeValue(tanggalMasukInput, jamMasuk);
        type(keteranganInput, keterangan);
    }

    public void fillOvertimeFormWithoutCatatan(LocalDateTime jamMasuk, LocalDateTime jamKeluar) {
        setDateTimeValue(tanggalMasukInput, jamMasuk);
        setDateTimeValue(tanggalKeluarInput, jamKeluar);
    }

    public void submitOvertime() {
        By activeSubmitButton = isAnyVisible(submitButton) ? submitButton : submitFallbackButton;
        click(activeSubmitButton);
    }

    public void clickReset() {
        clickAnyVisible(resetButton);
    }

    public boolean waitForSubmissionResult() {
        return waitAnyVisible(successMessage, ajukanLemburButton) != null;
    }

    public boolean waitForJamMasukRequiredMessage() {
        return waitAnyVisible(jamMasukRequiredMessage) != null;
    }

    public boolean waitForJamKeluarRequiredMessage() {
        return waitAnyVisible(jamKeluarRequiredMessage) != null;
    }

    public boolean waitForCatatanMinimumCharacterMessage() {
        return waitAnyVisible(catatanMinimumCharacterMessage) != null;
    }

    public boolean waitForFormFieldsEmpty() {
        try {
            wait.until(driver -> isFieldEmpty(tanggalMasukInput)
                    && isFieldEmpty(tanggalKeluarInput)
                    && isFieldEmpty(keteranganInput));
            return isFieldEmpty(tanggalMasukInput)
                    && isFieldEmpty(tanggalKeluarInput)
                    && isFieldEmpty(keteranganInput);
        } catch (Exception ex) {
            return false;
        }
    }

    private void setDateTimeValue(By locator, LocalDateTime value) {
        WebElement element = waitUntilVisible(locator);
        element.click();
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        element.sendKeys(value.format(DATE_TIME_FORMATTER));
    }

    private boolean isFieldEmpty(By locator) {
        WebElement element = waitUntilVisible(locator);
        String value = element.getAttribute("value");
        return value == null || value.trim().isEmpty();
    }
}
