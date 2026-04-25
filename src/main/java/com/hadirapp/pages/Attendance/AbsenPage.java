package com.hadirapp.pages.Attendance;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.hadirapp.pages.BasePage;
import com.hadirapp.utlis.Constants;

public class AbsenPage extends BasePage {

    private final By absensiMenu = By.xpath("//img[@alt='Absensi']");
    private final By lemburButtonPrimary = By.xpath("//a[contains(@class,'user__menu__item')][.//img[@alt='Lembur']]");
    private final By lemburButtonSecondary = By.xpath("//img[@alt='Lembur']/ancestor::a[1]");
    private final By lemburButtonByText = By.xpath("//*[self::a or self::div or self::span or self::p][contains(normalize-space(.),'Lembur')]");
    private final By ajukanLemburMarker = By.xpath("//button[contains(normalize-space(.),'Ajukan Lembur')] | //*[contains(normalize-space(.),'Ajukan Lembur')]");

    public AbsenPage(WebDriver driver) {
        super(driver);
    }

    public void openAbsentModule() {
        click(absensiMenu);
        waitUntilUrlContains("/apps/absent");
    }

    public void ensureAbsentPage() {
        if (!driver.getCurrentUrl().contains("/apps/absent")) {
            driver.get(Constants.ABSENT_URL);
        }
        waitUntilUrlContains("/apps/absent");
    }

    public void clickLemburButton() {
        clickAnyVisible(lemburButtonPrimary, lemburButtonSecondary, lemburButtonByText);
        waitAnyVisible(ajukanLemburMarker);
    }

    public boolean isAjukanLemburVisible() {
        return isAnyVisible(ajukanLemburMarker);
    }
}
