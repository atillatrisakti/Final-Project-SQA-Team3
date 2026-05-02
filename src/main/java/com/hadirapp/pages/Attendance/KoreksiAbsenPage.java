package com.hadirapp.pages.Attendance;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.*;
import com.hadirapp.utlis.WaitUtils;

public class KoreksiAbsenPage {
    private WebDriver driver;
    private JavascriptExecutor js;

    @FindBy(xpath = "//button[normalize-space()='Ajukan Koreksi']")
    public WebElement ajukanKoreksiButton;

    @FindBy(xpath = "//button[normalize-space()='Ajukan']")
    private WebElement ajukanButton;

    @FindBy(xpath = "//input[contains(@placeholder, 'hh:mm')]/following-sibling::div//button[@type='button']")
    private WebElement btnJamMasuk;

    @FindBy(xpath = "//input[contains(@placeholder, 'hh:mm')]/following::button[@aria-label='Choose time'][2]")
    private WebElement btnJamKeluar;

    @FindBy(xpath = "//button[contains(@class, 'MuiPickersDay-root') and not(contains(@class, 'Mui-disabled'))]")
    private java.util.List<WebElement> daftarTanggal;

    @FindBy(id = "is_wfh")
    private WebElement dropdownTipeAbsen;

    @FindBy(xpath = "//p[contains(@class, 'Mui-error')]")
    private WebElement errorMessage;

    public KoreksiAbsenPage(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }

    public void klikIkonJam(WebElement element) {
        try {
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);


            js.executeScript(
                    "arguments[0].dispatchEvent(new MouseEvent('click', {view: window, bubbles:true, cancelable: true}));",
                    element);
        } catch (Exception e) {
            System.out.println("Gagal klik ikon jam: " + e.getMessage());
        }
    }

    public boolean isBtnJamMasukDisplayed() {
        try {
            return WaitUtils.waitForElementVisible(driver, btnJamMasuk, 15).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void debugPageSource() {

        System.out.println(driver.getPageSource());
    }

    public void pilihTanggalHariIni() {
        WaitUtils.waitForElementClickable(driver, daftarTanggal.get(0), 15).click();

        WaitUtils.waitForElementVisible(driver, By.xpath("//input[@aria-label='Time']"), 15);
    }

    public void pilihJamDariPicker(String jam) {
        String label = jam + " hours";
        String xpath = "//span[@role='option' and @aria-label='" + label + "']";

        WebElement targetJam = WaitUtils.waitForElementVisible(driver, By.xpath(xpath), 15);

        new Actions(driver)
                .moveToElement(targetJam, 0, 0)
                .click()
                .perform();

        System.out.println("Berhasil memilih jam " + jam + " menggunakan metode IzinPage");
    }

    public void klikJamViaKoordinat(String jam) {
    String label = jam + " hours";
    WebElement targetJam = driver.findElement(By.xpath("//span[@aria-label='" + label + "']"));
    
    int x = targetJam.getLocation().getX();
    int y = targetJam.getLocation().getY();
    
    new Actions(driver)
        .moveByOffset(x + 5, y + 5) 
        .click()
        .perform();
}

    public void pilihTipeAbsen(String tipe) {
        WaitUtils.waitForElementClickable(driver, dropdownTipeAbsen, 15);
        dropdownTipeAbsen.click();
        String optionXpath = "//li[contains(text(), '" + tipe.toUpperCase() + "')]";
        WebElement option = WaitUtils.waitForElementClickable(driver, By.xpath(optionXpath), 15);
        option.click();
    }

    public void isiDataKoreksi(String tanggal, String jamMasuk, String jamKeluar, String tipe) {
        js.executeScript("arguments[0].click();", btnJamMasuk);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        WaitUtils.waitForElementClickable(driver, By.xpath("//button[text()='27']"), 15).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        pilihTanggalHariIni();

        driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        js.executeScript("var style = document.createElement('style'); " +
                "style.innerHTML = '* { transition: none !important; }'; " +
                "document.head.appendChild(style);");

        pilihJamDariPicker(jamMasuk);

        js.executeScript("arguments[0].click();", btnJamKeluar);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        WaitUtils.waitForAllElementsVisible(driver, daftarTanggal, 15);
        js.executeScript("arguments[0].click();", daftarTanggal.get(0));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        pilihJamDariPicker(jamKeluar);

        if (!tipe.isEmpty()) {
            WaitUtils.waitForElementClickable(driver, dropdownTipeAbsen, 15);
            js.executeScript("arguments[0].click();", dropdownTipeAbsen);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            WebElement option = driver.findElement(By.xpath("//li[text()='" + tipe.toUpperCase() + "']"));
            js.executeScript("arguments[0].click();", option);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }

        js.executeScript("arguments[0].click();", ajukanButton);
    }

    public String getAlertSuccessMessage() {
        By alertLocator = By
                .xpath("//div[contains(@class, 'success-message') or contains(text(), 'berhasil melakukan koreksi')]");
        return WaitUtils.waitForElementVisible(driver, alertLocator, 15).getText();
    }

    public String getErrorMessage(String expectedMessage) {
        String xpath = "//p[contains(text(), '" + expectedMessage + "')]";
        return WaitUtils.waitForElementVisible(driver, By.xpath(xpath), 15).getText();
    }
}