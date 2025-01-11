import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PopularSectionTest {
    private static WebDriver webDriver;
    WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
    JavascriptExecutor js = (JavascriptExecutor) webDriver;

    @BeforeAll
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver",
                "C:/Users/rakov/Documents/IBU/Fall 3/svvt/chromedriver-win64/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        webDriver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

        webDriver.get("https://www.apartmanija.hr");
        webDriver.manage().window().maximize();

        WebElement cookieButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class = 'btn_ok']")));
        cookieButton.click();
    }

    @AfterAll
    public static void tearDown() {
        // Close the browser
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    @Test
    void testPopularSection() throws InterruptedException {
        String[] cardLinks = {"https://www.apartmanija.hr/apartmani", "https://www.apartmanija.hr/vile",
                "https://www.apartmanija.hr/seoski-turizam", "https://www.apartmanija.hr/robinzonski-turizam"};

        List<WebElement> cards = webDriver.findElements(By.xpath("/html/body/div[15]/div/div/a"));
        //take only first 4
        List<WebElement> fist4cards = cards.subList(0, 4);

        assertEquals(cardLinks.length, fist4cards.size(), "Cards count mismatch!");

        for (int i = 0; i < cardLinks.length; i++) {
            // Capture the current window handle (main window)
            String originalWindow = webDriver.getWindowHandle();
            js.executeScript("arguments[0].scrollIntoView(true);", fist4cards.get(i));
            Thread.sleep(500);
            js.executeScript("arguments[0].click();", fist4cards.get(i));


            // Wait for new tab to open
            wait.until(ExpectedConditions.numberOfWindowsToBe(2));

            // Get all the window handles after clicking (should include the new tab)
            Set<String> allWindows = webDriver.getWindowHandles();
            assertEquals(2, allWindows.size()); // Ensure there's a new tab opened

            // Switch to the new tab
            for (String windowHandle : allWindows) {
                if (!windowHandle.equals(originalWindow)) {
                    webDriver.switchTo().window(windowHandle);
                    break;
                }
            }

            // Verify the new tab URL (update expected URL as needed)
            String currentUrl = webDriver.getCurrentUrl();
            System.out.println("Verified card URL: " + currentUrl);
            assertEquals(cardLinks[i], currentUrl, "Language URL mismatch!");

            // Close the new tab and switch back to the original window
            webDriver.close();
            webDriver.switchTo().window(originalWindow);
        }

    }

    @Test
    void testPopularSectionWithFilters() throws InterruptedException {
        String[] cardLinks = {"https://www.apartmanija.hr/apartmani#sort=activated,price=50", "https://www.apartmanija.hr/apartmani#sort=activated,price=100",
                "https://www.apartmanija.hr/apartmani#sort=activated,beach=50", "https://www.apartmanija.hr/apartmani#sort=activated,pool=",
                "https://www.apartmanija.hr/apartmani#sort=activated,pets=", "https://www.apartmanija.hr/apartmani#sort=activated,sea=,beach=50,pool=",
                "https://www.apartmanija.hr/apartmani#sort=activated,par=", "https://www.apartmanija.hr/apartmani#sort=activated,dor="};

        List<WebElement> cards = webDriver.findElements(By.xpath("/html/body/div[15]/div/div/a"));
        //take only first 4
        List<WebElement> fist4cards = cards.subList(4, cards.size());

        assertEquals(cardLinks.length, fist4cards.size(), "Cards count mismatch!");

        for (int i = 0; i < cardLinks.length; i++) {
            // Capture the current window handle (main window)
            String originalWindow = webDriver.getWindowHandle();
            js.executeScript("arguments[0].scrollIntoView(true);", fist4cards.get(i));
            Thread.sleep(500);
            js.executeScript("arguments[0].click();", fist4cards.get(i));
            Thread.sleep(500);

            // Wait for new tab to open
            wait.until(ExpectedConditions.numberOfWindowsToBe(2));

            // Get all the window handles after clicking (should include the new tab)
            Set<String> allWindows = webDriver.getWindowHandles();
            assertEquals(2, allWindows.size()); // Ensure there's a new tab opened

            // Switch to the new tab
            for (String windowHandle : allWindows) {
                if (!windowHandle.equals(originalWindow)) {
                    webDriver.switchTo().window(windowHandle);
                    break;
                }
            }

            // Verify the new tab URL (update expected URL as needed)
            String currentUrl = webDriver.getCurrentUrl();
            System.out.println("Verified card URL: " + currentUrl);
            assertEquals(cardLinks[i], currentUrl, "Language URL mismatch!");
            Thread.sleep(500);

            WebElement noFiltersButton = webDriver.findElement(By.xpath("//button[contains(.,'Ukloni filtere')]"));
            noFiltersButton.click();
            Thread.sleep(500);

            assertNotEquals(cardLinks[i], webDriver.getCurrentUrl());

            // Close the new tab and switch back to the original window
            webDriver.close();
            webDriver.switchTo().window(originalWindow);
        }

    }


}
