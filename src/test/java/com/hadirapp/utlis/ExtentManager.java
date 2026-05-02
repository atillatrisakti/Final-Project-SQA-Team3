package com.hadirapp.utlis;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {
    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter("target/extent-reports/Automation-Report.html");
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setDocumentTitle("Hadir App Automation Report");
            spark.config().setReportName("Test Execution Report");

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Project", "Hadir App Automation");
            extent.setSystemInfo("Author", "SQA Team 3");
            extent.setSystemInfo("Environment", "QA");
        }
        return extent;
    }
}
