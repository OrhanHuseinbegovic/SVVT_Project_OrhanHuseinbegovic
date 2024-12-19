import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Testing {

    private static WebDriver webDriver;
    private static String baseUrl;

    @BeforeAll
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", "/Users/orhanhuseinbegovic/Downloads/chromedriver-mac-arm64/chromedriver"); // specify the path to chromedriver
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        webDriver = new ChromeDriver(options);
        baseUrl = "https://www.apartmanija.hr";
    }

    @AfterAll
    public static void tearDown() {
        // Close the browser
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    @Test
    public void testTitle() throws InterruptedException {
        // Get the title of the page and check if it matches the expected title
        webDriver.get(baseUrl);
        String actualTitle = webDriver.getTitle();
        System.out.println("Actual title: " + actualTitle);
        assertEquals("Apartmanija", actualTitle, "Title does not match");
        Thread.sleep(1000);
    }

    @Test
    void testRedirect() throws InterruptedException {
        webDriver.get("https://lms.ibu.edu.ba/");
        Thread.sleep(2000);
        String currentUrl = webDriver.getCurrentUrl();
        System.out.println("Current URL is: " + currentUrl);
        assertEquals("https://apartmanija.hr/", currentUrl);
    }

    @Test
    void supportFormTest () throws InterruptedException {
        webDriver.get("https://www.apartmanija.hr/kontakt");
        Thread.sleep(2000);

        WebElement fnameField = webDriver.findElement(By.id("fname"));
        WebElement lnameField = webDriver.findElement(By.name("lname"));
        WebElement emailField = webDriver.findElement(By.name("email"));
        WebElement mobileField = webDriver.findElement(By.name("mobile"));
        WebElement inquiryField = webDriver.findElement(By.name("inquiry"));
        WebElement submitButton = webDriver.findElement(By.cssSelector(".ad_btn:nth-child(2)"));
        Thread.sleep(1000);

        fnameField.sendKeys("Test");
        lnameField.sendKeys("Test");
        emailField.sendKeys("testsvvt@gmail.com");
        mobileField.sendKeys("+385 98 765 432");
        inquiryField.sendKeys("test");
        Thread.sleep(1000);

        submitButton.click();
        Thread.sleep(1000);

        WebElement fromAlert = webDriver.findElement(By.id("cform_captcha_err"));
        assertEquals("Obavezno polje", fromAlert.getText());
    }


    //Login scenario testing
    @Test
    void testEmptyLoginForm() {
        // Navigate to the login page
        webDriver.get("https://www.korisnici.apartmanija.hr/login");

        // Locate the form elements and submit button
        WebElement submitButton = webDriver.findElement(By.cssSelector("button[type='submit']"));

        // Click the submit button without filling in the form
        submitButton.click();

        // Wait for the error message to appear
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error-message-class"))); // Replace with the actual error message CSS selector

        // Assert that the error message is displayed and contains the expected text
        assertEquals("Please fill out this field.", errorMessage.getText(), "Error message did not match the expected text.");
    }

}
