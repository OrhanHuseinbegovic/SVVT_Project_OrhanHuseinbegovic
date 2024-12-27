
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SupportFormTest {
    private static WebDriver webDriver;

    @BeforeAll
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver",
                "C:/Users/rakov/Documents/IBU/Fall 3/svvt/chromedriver-win64/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        webDriver = new ChromeDriver(options);
    }

    @AfterAll
    public static void tearDown() {
        // Close the browser
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    //It passed, but now it FAILS, check why
    @Test
    void supportFormTest () throws InterruptedException {
        webDriver.get("https://www.apartmanija.hr/kontakt");
        webDriver.manage().window().setSize(new Dimension(1552, 832));

        Thread.sleep(500);

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

        WebElement cookieButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class = 'btn_ok']")));
        cookieButton.click();
        Thread.sleep(800);

        WebElement fnameField = webDriver.findElement(By.id("fname"));
        WebElement lnameField = webDriver.findElement(By.name("lname"));
        WebElement emailField = webDriver.findElement(By.name("email"));
        WebElement mobileField = webDriver.findElement(By.name("mobile"));
        WebElement inquiryField = webDriver.findElement(By.name("inquiry"));
        WebElement submitButton = webDriver.findElement(By.xpath("//input[@class = 'ad_btn']"));
        Thread.sleep(1000);

        fnameField.sendKeys("Test");
        lnameField.sendKeys("Test");
        emailField.sendKeys("testsvvt@gmail.com");
        mobileField.sendKeys("+385 98 765 432");
        inquiryField.sendKeys("test");
        Thread.sleep(1000);

        Thread.sleep(15000);

        submitButton.click();
        Thread.sleep(1000);

        assertEquals("https://www.apartmanija.hr/kontakt/sc", webDriver.getCurrentUrl());

    }
}
