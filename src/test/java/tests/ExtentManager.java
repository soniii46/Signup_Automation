package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    public static ExtentReports extent;
    public static ExtentTest test;

    public static ExtentReports initReport() {

        ExtentSparkReporter reporter =
                new ExtentSparkReporter("reports/SignupReport.html");

        reporter.config().setDocumentTitle("Signup Automation Report");
        reporter.config().setReportName("Signup Test Results");

        extent = new ExtentReports();
        extent.attachReporter(reporter);

        extent.setSystemInfo("Tester", "Soni");
        extent.setSystemInfo("Environment", "QA");

        return extent;
    }
}
