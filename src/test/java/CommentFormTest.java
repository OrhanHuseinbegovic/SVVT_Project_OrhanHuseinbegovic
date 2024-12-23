import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommentFormTest {
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

    @Test
    void commentFormTest () throws InterruptedException {
        webDriver.get("https://www.apartmanija.hr/apartmani/vir/apartmani-malibu-imperial-resort/28735");
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        Actions actions = new Actions(webDriver);

        WebElement cookieButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class = 'btn_ok']")));
        cookieButton.click();
        Thread.sleep(800);

        WebElement reviewLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("u")));
        reviewLink.click();

        // Select apartment from dropdown
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(By.name("acmu")));
        dropdown.click();
        WebElement option = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//option[@value='74295']")));
        option.click();

        Thread.sleep(800);

        // Select rating
        WebElement rating = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".reviews_form_vote > label:nth-child(2)")));
        rating.click();

        Thread.sleep(800);

        webDriver.findElement(By.name("rev_title")).sendKeys("Test");
        Thread.sleep(800);

        webDriver.findElement(By.name("rev_text")).sendKeys("Test");
        Thread.sleep(800);

        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//label[@class='checkbox']/input[@type='checkbox' and @name='act']")));
        checkbox.click();
        Thread.sleep(800);

        webDriver.findElement(By.xpath("//div[@class='row']" +
                "/div[@class='col-xs-12 col-md-6']/input[@name = 'fname']")).sendKeys("Test");
        Thread.sleep(800);

        webDriver.findElement(By.xpath("//div[@class='row']" +
                "/div[@class='col-xs-12 col-md-6']/input[@name = 'nick']")).sendKeys("Test");
        Thread.sleep(800);

        webDriver.findElement(By.xpath("//div[@class='row']" +
                "/div[@class='col-xs-12 col-md-6']/input[@name = 'email']")).sendKeys("testsvvt@gmail.com");
        Thread.sleep(800);

        webDriver.findElement(By.name("city")).sendKeys("Test");
        Thread.sleep(800);

        webDriver.findElement(By.xpath("//select[@name = 'country']/option[@value = 'AU']")).click();
        Thread.sleep(2000);

        webDriver.findElement(By.id("rev_form")).submit();
        Thread.sleep(1000);

        WebElement errorModal = webDriver.findElement(By.id("mod_rev_msg"));

        assertEquals("Došlo je do greške, molimo pokušajte kasnije!", errorModal.getText());

    }
}
