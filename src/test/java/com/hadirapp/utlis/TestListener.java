package com.hadirapp.utlis;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.hadirapp.drivers.DriverSingleton;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
    private static ExtentReports extent = ExtentManager.getInstance();
    private static ThreadLocal<ExtentTest> testInfo = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        System.out.println("Memulai eksekusi Test Suite: " + context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        String testName = (description != null && !description.isEmpty()) ? description : methodName;

        ExtentTest test = extent.createTest(testName);
        testInfo.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        testInfo.get().log(Status.PASS, MarkupHelper.createLabel("Test Passed", ExtentColor.GREEN));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        testInfo.get().log(Status.FAIL, MarkupHelper.createLabel("Test Failed: " + result.getThrowable().getMessage(), ExtentColor.RED));

        // Ambil screenshot jika driver masih hidup
        if (DriverSingleton.getDriver() != null) {
            try {
                String base64Screenshot = "data:image/png;base64," + ((TakesScreenshot) DriverSingleton.getDriver()).getScreenshotAs(OutputType.BASE64);
                testInfo.get().addScreenCaptureFromBase64String(base64Screenshot, "Failure Screenshot");
            } catch (Exception e) {
                testInfo.get().log(Status.FAIL, "Gagal mengambil screenshot: " + e.getMessage());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        testInfo.get().log(Status.SKIP, MarkupHelper.createLabel("Test Skipped", ExtentColor.ORANGE));
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
}
