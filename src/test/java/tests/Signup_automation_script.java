package tests;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.time.Duration;
import java.util.*;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.mailslurp.apis.*;
import com.mailslurp.apis.WaitForControllerApi;
import com.mailslurp.clients.*;
import com.mailslurp.models.*;
import org.testng.annotations.*;
import com.aventstack.extentreports.*;


public class Signup_automation_script {
    private static final String API_KEY = System.getenv("MAILSLURP_API_KEY");

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;
	public static String generateMobile() {
		    Random random = new Random();
		    String[] prefixes = {"98", "97"};
		    String prefix = prefixes[random.nextInt(prefixes.length)];
            return prefix + (10000000 + random.nextInt(90000000));
		}

    @BeforeSuite
    public void setupReport() {
        extent = ExtentManager.initReport();
    }

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }
    @Test
    public void signupTest() throws Exception {
        test = extent.createTest("Signup Test");
        test.info("Initializing MailSlurp");

		ApiClient client = Configuration.getDefaultApiClient();
	    client.setApiKey(API_KEY);

        InboxControllerApi inboxApi = new InboxControllerApi(client);
        WaitForControllerApi waitApi = new WaitForControllerApi(client);
        
        InboxDto inbox = inboxApi.createInboxWithDefaults();
        String email = inbox.getEmailAddress();
        System.out.println("Random MaxilSlurp Email: " + email);

		driver.get("https://authorized-partner.vercel.app/login");
        test.info("Opened login page");

		WebElement sign = driver.findElement(By.linkText("Sign Up"));
		sign.click();
        test.info("Clicked Sign Up");
        test.info("Opening signup page");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id = 'remember']"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Continue"))).click();

        test.info("Entering first name");
		WebElement fname = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("firstName")));
		fname.sendKeys("Soni");

        test.info("Entering last name");
		WebElement Lname = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("lastName")));
		Lname.sendKeys("Maharjan");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email"))).sendKeys(email);
		
		String mobile = generateMobile();
        test.info("Entering Phone Number");
		WebElement number = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("phoneNumber")));
		number.sendKeys(mobile);

        test.info("Entering password");
		WebElement pass = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password")));
		pass.sendKeys("S0niv@1234");

        test.info("Entering confirm password");
		WebElement conPass = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("confirmPassword")));
		conPass.sendKeys("S0niv@1234");
		
		WebElement Nextbtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@type = 'submit']")));
		Nextbtn.click();
        test.info("Signup form submitted");

        Email otpMail = waitApi.waitForLatestEmail(
                inbox.getId(), 300000L, true, null, null, null, null);

        Document doc = Jsoup.parse(otpMail.getBody());
        Element otpElement = doc.selectFirst("p[style*='font-size: 24px']");

        if (otpElement == null) {
            test.fail("OTP not found in email");
            throw new RuntimeException("OTP not found");
        }

	    String otp = otpElement.text().trim();
        test.pass("OTP received");

        test.info("Entering OTP");
        WebElement otpInput = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("input[autocomplete='one-time-code']")));
        otpInput.sendKeys(otp);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@type = 'submit']"))).click();

        test.info("Filling all signup necessary details");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("agency_name"))).sendKeys("Prime");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("role_in_agency"))).sendKeys("Student");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("agency_email"))).sendKeys("Prime@gmail.com");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("agency_website"))).sendKeys("www.prime.edu.np");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("agency_address"))).sendKeys("Kathmandu");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@role = 'combobox']"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text() = 'Canada']"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text() = 'Next']"))).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@role = 'combobox']"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@value = '3']"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@name = 'number_of_students_recruited_annually']"))).sendKeys("4");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("focus_area"))).sendKeys("Canada");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("success_metrics"))).sendKeys("80");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[.='Career Counseling']/preceding-sibling::button"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[.='Test Prepration']/preceding-sibling::button"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text() = 'Next']"))).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("business_registration_number"))).sendKeys("22222222");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button[role = 'combobox']"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text() = 'Canada']"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[.='Universities']/preceding-sibling::button"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[.='Colleges']/preceding-sibling::button"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("certification_details"))).sendKeys("ICEF Certified");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@type = 'file']"))).sendKeys("D:\\cv\\Soniva's CV.pdf");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@type = 'submit']"))).click();

        test.pass("Signup completed successfully");
	}
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterSuite
    public void flushReport() {
        extent.flush();
    }

}
