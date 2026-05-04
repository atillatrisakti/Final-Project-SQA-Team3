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

    @FindBy(xpath = "//img[@alt='Cuti']")
    private WebElement cutiBtn;

    @FindBy(xpath = "//button[text()='Ajukan Cuti']")
    private WebElement ajukanCutiBtn;

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

    
    
    public void clickCutiBtn() {
        cutiBtn.click();
    }

    public void clickAjukanCutiBtn() {
        ajukanCutiBtn.click();
    }

    public void pilihTipeCuti(String tipeCuti) {
        wait.until(ExpectedConditions.elementToBeClickable(jenisCutiDropdown)).click();

        String xpathOption = "//li[@role='option' and contains(text(),'" + tipeCuti + "')]";
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathOption)));
        option.click();
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

    // Cek apakah halaman list cuti tampil setelah submit
    public boolean isHalamanCutiDisplayed() {
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

}
