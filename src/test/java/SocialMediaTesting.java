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
        // Open the base URL
        webDriver.get(baseUrl);

        // Define the xpaths for social media icons and their expected URLs
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

            // Find the social media icon
            WebElement socialMediaIcon = new WebDriverWait(webDriver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(By.xpath(iconXPath)));

            Thread.sleep(1000);
            // Click the icon (opens a new tab or window)
            socialMediaIcon.click();

            // Wait for the new tab/window and switch to it
            new WebDriverWait(webDriver, Duration.ofSeconds(10)).until(driver -> webDriver.getWindowHandles().size() > 1);

            String originalWindow = webDriver.getWindowHandle();
            for (String windowHandle : webDriver.getWindowHandles()) {
                if (!windowHandle.equals(originalWindow)) {
                    webDriver.switchTo().window(windowHandle);
                    break;
                }
            }

            // Verify the URL of the new tab
            String currentUrl = webDriver.getCurrentUrl();
            Thread.sleep(1000);
            assertTrue(currentUrl.contains(expectedUrl), "Expected URL to contain: " + expectedUrl + ", but got: " + currentUrl);
            Thread.sleep(1000);

            // Close the new tab and switch back to the original window
            webDriver.close();
            webDriver.switchTo().window(originalWindow);
        }
    }


}
