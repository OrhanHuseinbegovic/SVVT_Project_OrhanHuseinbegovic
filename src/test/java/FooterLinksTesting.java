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

public class FooterLinksTesting {

    private static WebDriver webDriver;
    private static String baseUrl;

    @BeforeAll
    public static void setUp() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "/Users/orhanhuseinbegovic/Downloads/chromedriver-mac-arm64/chromedriver"); // specify the path to chromedriver
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        webDriver = new ChromeDriver(options);
        baseUrl = "https://www.apartmanija.hr";

        webDriver.get(baseUrl);
        Thread.sleep(1000);
        WebElement cookiesSubmit = webDriver.findElement(By.cssSelector(".btn_ok"));
        Thread.sleep(1000);

        cookiesSubmit.click();
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
        String[][] footerLinks = {
                {"/html/body/div[20]/div/div[2]/div[2]/a[1]","https://www.apartmanija.hr/apartmani/dubrovnik"},
                {"/html/body/div[20]/div/div[2]/div[2]/a[2]","https://www.apartmanija.hr/apartmani/zadar"},
                {"/html/body/div[20]/div/div[2]/div[2]/a[3]","https://www.apartmanija.hr/apartmani/sibenik"},
                {"/html/body/div[20]/div/div[2]/div[2]/a[4]","https://www.apartmanija.hr/apartmani/biograd-na-moru"},
                {"/html/body/div[20]/div/div[2]/div[2]/a[5]","https://www.apartmanija.hr/apartmani/split"},
                {"/html/body/div[20]/div/div[2]/div[3]/a[1]","https://www.viamichelin.com/"},
                {"/html/body/div[20]/div/div[2]/div[3]/a[2]","https://meteo.hr/"},
                {"/html/body/div[20]/div/div[2]/div[3]/a[3]","https://croatia.hr/"},
                {"/html/body/div[20]/div/div[2]/div[3]/a[4]","https://www.apartmanija.hr/oglasavanje-smjestaja"},
                {"/html/body/div[20]/div/div[2]/div[3]/a[5]","https://www.udaljenosti.com/"},
                {"/html/body/div[20]/div/div[2]/div[4]/a[1]","https://www.apartmanija.hr/apartmani/vrata-od-pila-dubrovnik"},
                {"/html/body/div[20]/div/div[2]/div[4]/a[2]","https://www.apartmanija.hr/apartmani/onofrijeva-cesma-dubrovnik"},
                {"/html/body/div[20]/div/div[2]/div[4]/a[3]","https://www.apartmanija.hr/apartmani/martinje-v-kumrovcu"},
                {"/html/body/div[20]/div/div[2]/div[4]/a[4]","https://www.apartmanija.hr/apartmani/jazz-fair-cakovec"},
                {"/html/body/div[20]/div/div[2]/div[4]/a[5]","https://www.apartmanija.hr/apartmani/fog-festival-u-karlovcu"},
                {"/html/body/div[20]/div/div[2]/div[1]/a[5]/img","https://www.cimerfraj.hr/"},
                {"/html/body/div[20]/div/div[3]/div[2]/a[1]","https://www.apartmanija.hr/kontakt"},
                {"/html/body/div[20]/div/div[3]/div[2]/a[2]","https://www.apartmanija.hr/opci-uvjeti-koristenja"},
                {"/html/body/div[20]/div/div[3]/div[2]/a[3]","https://www.apartmanija.hr/opci-uvjeti-oglasavanja"},
        };

        WebElement footerLogo = webDriver.findElement(By.xpath("/html/body/div[20]/div/div[1]/div[1]/img"));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", footerLogo);
        Thread.sleep(1000);


        for (String[] data : footerLinks) {
            String link = data[0];
            String expectedUrl = data[1];

            footerLogo = webDriver.findElement(By.xpath("/html/body/div[20]/div/div[1]/div[1]/img"));
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", footerLogo);
            Thread.sleep(1000);

            WebElement otherCard = new WebDriverWait(webDriver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(By.xpath(link)));

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
