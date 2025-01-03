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

public class RegionCardTesting {

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
    void testRegionCardsRedirecting() throws InterruptedException {
        webDriver.get(baseUrl);

        String[][] regionCards = {
                {"//*[@id=\"reg0\"]/a", "https://www.apartmanija.hr/hrvatska/otoci"},
                {"//*[@id=\"reg1\"]/a", "https://www.apartmanija.hr/hrvatska/istra"},
                {"//*[@id=\"reg2\"]/a", "https://www.apartmanija.hr/hrvatska/kvarner"},
                {"//*[@id=\"reg3\"]/a", "https://www.apartmanija.hr/hrvatska/zadar"},
                {"//*[@id=\"reg4\"]/a","https://www.apartmanija.hr/hrvatska/sibenik"},
                {"//*[@id=\"reg5\"]/a","https://www.apartmanija.hr/hrvatska/split"}
        };

        String[][] otherCards = {
                {"/html/body/div[10]/div/div[9]/a/div", "https://www.apartmanija.hr/firstminute"},
                {"/html/body/div[10]/div/div[18]/a/div","https://www.apartmanija.hr/oglasavanje-smjestaja"}
        };

        WebElement regionCardsDiv = webDriver.findElement(By.xpath("//*[@id=\"reg0\"]/a"));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", regionCardsDiv);
        Thread.sleep(1000);

        for (String[] data : regionCards) {
            String cardXPath = data[0];
            String expectedUrl = data[1];

            regionCardsDiv = webDriver.findElement(By.xpath("//*[@id=\"reg0\"]/a"));
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", regionCardsDiv);
            Thread.sleep(1000);

            WebElement regionCard = new WebDriverWait(webDriver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(By.xpath(cardXPath)));

            Thread.sleep(1000);
            regionCard.click();

            Thread.sleep(3000);

            String currentUrl = webDriver.getCurrentUrl();
            Thread.sleep(1000);
            assertTrue(currentUrl.contains(expectedUrl), "Expected URL to contain: " + expectedUrl + ", but got: " + currentUrl);
            Thread.sleep(1000);

            webDriver.get(baseUrl);
        }

        for (String[] data : otherCards) {
            String iconXPath = data[0];
            String expectedUrl = data[1];

            regionCardsDiv = webDriver.findElement(By.xpath("//*[@id=\"reg0\"]/a"));
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", regionCardsDiv);
            Thread.sleep(1000);

            WebElement otherCard = new WebDriverWait(webDriver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(By.xpath(iconXPath)));

            Thread.sleep(1000);
            otherCard.click();

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


}
