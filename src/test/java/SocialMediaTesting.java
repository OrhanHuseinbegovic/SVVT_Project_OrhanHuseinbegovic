import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SocialMediaTesting {

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
    void testSocialMediaRedirecting() throws InterruptedException {
        webDriver.get(baseUrl);

        String[][] socialMediaData = {
                {"/html/body/div[20]/div/div[2]/div[1]/a[1]", "https://www.facebook.com/apartmanija"},
                {"/html/body/div[20]/div/div[2]/div[1]/a[3]", "https://x.com/apartmanija?mx=2"},
                {"/html/body/div[20]/div/div[2]/div[1]/a[2]", "https://www.instagram.com/apartmanija.hr/"},
                {"/html/body/div[20]/div/div[2]/div[1]/a[4]", "https://www.apartmanija.hr/kontakt"}
        };

        WebElement footer = webDriver.findElement(By.xpath("/html/body/div[20]/div"));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", footer);
        Thread.sleep(1000);

        for (String[] data : socialMediaData) {
            String iconXPath = data[0];
            String expectedUrl = data[1];

             WebElement socialMediaIcon = new WebDriverWait(webDriver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(By.xpath(iconXPath)));

            Thread.sleep(1000);
            socialMediaIcon.click();

            new WebDriverWait(webDriver, Duration.ofSeconds(10)).until(driver -> webDriver.getWindowHandles().size() > 1);

            String originalWindow = webDriver.getWindowHandle();
            for (String windowHandle : webDriver.getWindowHandles()) {
                if (!windowHandle.equals(originalWindow)) {
                    webDriver.switchTo().window(windowHandle);
                    break;
                }
            }

            String currentUrl = webDriver.getCurrentUrl();
            Thread.sleep(1000);
            assertTrue(currentUrl.contains(expectedUrl), "Expected URL to contain: " + expectedUrl + ", but got: " + currentUrl);
            Thread.sleep(1000);

            webDriver.close();
            webDriver.switchTo().window(originalWindow);
        }
    }

    @Test
    void testSocialMediaOnApartment() throws InterruptedException {
        webDriver.get(baseUrl);

        Thread.sleep(500);
        WebElement cookiesSubmit = webDriver.findElement(By.cssSelector(".btn_ok"));
        Thread.sleep(1000);

        cookiesSubmit.click();

        Thread.sleep(500);
        WebElement firstApartment = webDriver.findElement(By.xpath("/html/body/div[18]/div/div[1]"));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", firstApartment);
        Thread.sleep(500);

        firstApartment.click();
        Thread.sleep(500);

        WebElement podijeli = webDriver.findElement(By.linkText("Podijeli"));
        podijeli.click();
        Thread.sleep(500);

        webDriver.findElement(By.xpath("//*[@id=\"mod-social-share\"]/div/div/div[2]/a[9]/i")).click();
        Thread.sleep(500);
        String link = webDriver.findElement(By.xpath("//*[@id=\"mod-copy-link\"]/div/div/div[2]/input")).getAttribute("value");

        assertEquals(webDriver.getCurrentUrl(), link);
    }

}
