package com.hadirapp.pages.Requests;

import java.time.Duration;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.hadirapp.utlis.WaitUtils;


public class CutiPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    public CutiPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.js = (JavascriptExecutor) driver;
    }

    @FindBy(xpath = "//a[@class='user__menu__item']//p[text()='Cuti']")
    private WebElement cutiBtn;

    @FindBy(xpath = "//button[text()='Ajukan Cuti']")
    private WebElement ajukanCutiBtn;

    @FindBy(xpath = "//p[text()='Ajukan Cuti']")
    private WebElement titleModalAjukanCuti;

    @FindBy(xpath = "//button[text()='Info Cuti']")
    private WebElement tabInfoCuti;

    @FindBy(xpath = "//span[text()='Total Cuti']")
    private WebElement labelTotalCuti;

    @FindBy(xpath = "//div[@id='leave_type_id']")
    private WebElement jenisCutiDropdown;

    @FindBy(xpath = "//*[@data-testid='AccessAlarmIcon']/ancestor::div[contains(@class,'MuiBox-root')][1]")
    private WebElement fieldPilihTanggal;

    @FindBy(xpath = "//button[text()='Simpan']")
    private WebElement simpanButton;

    @FindBy(id = "notes")
    private WebElement notesInput;

    @FindBy(xpath = "//button[text()='Ajukan']")
    private WebElement ajukanButton;

    @FindBy(xpath = "//div[contains(@class, 'MuiSnackbarContent-message')]")
    private WebElement snackbarErrorMsg;

    @FindBy(xpath = "//button[text()='Reset']")
    private WebElement resetBtn;

    @FindBy(id = "leave_type_id")
    private WebElement dropdownTipeCuti;

    @FindBy(xpath = "//label[text()='Pilih Tanggal']/following-sibling::div//p")
    private WebElement displayTanggal;


    public void clickResetButton() {
        WaitUtils.waitForElementClickable(driver, resetBtn, 10);
        resetBtn.click();
    }

    public void clickCutiBtn() {
        WaitUtils.waitForElementClickable(driver, cutiBtn, 10);
        try {
            cutiBtn.click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", cutiBtn);
        }
    }

    public void clickAjukanCutiBtn() {
        WaitUtils.waitForElementClickable(driver, ajukanCutiBtn, 10);
        ajukanCutiBtn.click();
    }

    public boolean isModalAjukanCutiDisplayed() {
        try {
            WaitUtils.waitForElementVisible(driver, jenisCutiDropdown, 5);
            return jenisCutiDropdown.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getModalTitleText() {
        return titleModalAjukanCuti.getText();
    }

    public void clickTabInfoCuti() {
        WaitUtils.waitForElementClickable(driver, tabInfoCuti, 5);
        tabInfoCuti.click();
    }

    public boolean isLabelTotalCutiDisplayed() {
        try {
            WaitUtils.waitForElementVisible(driver, labelTotalCuti, 5);
            return labelTotalCuti.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getLabelTotalCutiText() {
        return labelTotalCuti.getText();
    }

    public void pilihTipeCuti(String tipeCuti) {
        WaitUtils.waitForElementClickable(driver, jenisCutiDropdown, 10);
        try {
            jenisCutiDropdown.click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", jenisCutiDropdown);
        }

        String xpathOption = "//li[@role='option' and contains(text(),'" + tipeCuti + "')]";
        WebElement option = WaitUtils.waitForElementClickable(driver, By.xpath(xpathOption), 10);
        try {
            option.click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", option);
        }
    }

    public void clickElement(By locator) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        } catch (Exception e) {
            WebElement el = driver.findElement(locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    public void pilihTanggal(String bulan, String tahun, String startDay, String endDay) {
        fieldPilihTanggal.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".rdrCalendarWrapper")));

        new Select(driver.findElement(By.cssSelector(".rdrMonthPicker select")))
                .selectByVisibleText(bulan);

        new Select(driver.findElement(By.cssSelector(".rdrYearPicker select")))
                .selectByVisibleText(tahun);

        By start = By.xpath("//button[contains(@class,'rdrDay') and not(contains(@class,'rdrDayPassive')) and .//span[text()='" + startDay + "']]");
        By end = By.xpath("//button[contains(@class,'rdrDay') and not(contains(@class,'rdrDayPassive')) and .//span[text()='" + endDay + "']]");

        clickElement(start);
        clickElement(end);

        simpanButton.click();
    }

    public void fillNotes(String notes) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("notes")));
        notesInput.sendKeys(notes);
    }

    public void clickAjukanButton() {
        ajukanButton.click();
    }

    public void doCuti() {
        clickCutiBtn();
        clickAjukanCutiBtn();
    }

    // Cek apakah halaman list cuti tampil berdasarkan judul "Halaman Cuti"
    public boolean isHalamanCutiDisplayed() {
        try {
            WebElement title = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//p[contains(@class, 'MuiTypography-root') and contains(normalize-space(.), 'Halaman Cuti')]")
                )
            );
            return title.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Ambil teks dari card cuti terbaru (pertama di list)
    public String getCutiTerbaruText() {
        try {
            WebElement firstCard = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("(//div[contains(@class, 'MuiCard-root')])[1]")
                )
            );
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", firstCard);
            return firstCard.getText();
        } catch (Exception e) {
            System.out.println("Card cuti terbaru tidak ditemukan: " + e.getMessage());
            return "";
        }
    }

    public String getErrorMessageText() {
        try {
            WaitUtils.waitForElementVisible(driver, snackbarErrorMsg, 5);
            return snackbarErrorMsg.getText();
        } catch (Exception e) {
            return "Error message tidak muncul";
        }
    }

    public boolean isErrorMessageDisplayed() {
        try {
            WaitUtils.waitForElementVisible(driver, snackbarErrorMsg, 10);
            return snackbarErrorMsg.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getTipeCutiValue() {
    return dropdownTipeCuti.getText();
    }

    public String getTanggalValue() {
        return displayTanggal.getText().trim(); 
    }

    public String getCatatanValue() {
        return notesInput.getAttribute("value");
    }

}
