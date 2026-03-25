package utilities;

import java.io.File;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    private static ExtentReports extent;

    public static synchronized ExtentReports getReport() {

        if (extent == null) {
        	
            //  Jenkins + Local compatible base path
            String basePath = System.getProperty("user.dir");

            //  Target report directory
            String reportDir = basePath + File.separator + "target"
                    + File.separator + "extent-report";

            //  Ensure directory exists
            File dir = new File(reportDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            //  Standard file name (IMPORTANT for Jenkins)
            String reportPath = System.getProperty("user.dir")
        	        + "/target/extent-report/ExtentReport.html";
            //  Create Spark Reporter
            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);

            spark.config().setReportName("Automation Test Report");
            spark.config().setDocumentTitle("Adaptix Automation Report");

            // Optional UI improvements
            spark.config().setEncoding("utf-8");
            spark.config().setTimelineEnabled(true);

            //  Attach reporter
            extent = new ExtentReports();
            extent.attachReporter(spark);

            // Optional system info
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("User", System.getProperty("user.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));

            // Debug log (VERY IMPORTANT for Jenkins)
            System.out.println("Extent Report Path: " + reportPath);
        }

        return extent;
    }
}