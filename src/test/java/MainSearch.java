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

public class MainSearch {

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
        assertEquals("Apartmani, sobe i privatni smještaj | Apartmanija.hr", actualTitle, "Title does not match");
        Thread.sleep(1000);
    }

    @Test
    void testRedirect() throws InterruptedException {
        webDriver.get(baseUrl);
        Thread.sleep(2000);
        String currentUrl = webDriver.getCurrentUrl();
        System.out.println("Current URL is: " + currentUrl);
        assertEquals("https://www.apartmanija.hr/", currentUrl);
    }

    @Test
    void testEmptySearchForm() throws InterruptedException {
        // Navigate to the login page

    }

    @Test
    void testSearchWithArgumentEqualsZeroResult() throws InterruptedException {
        webDriver.get(baseUrl);
        Thread.sleep(1000);
        WebElement cookiesSubmit = webDriver.findElement(By.cssSelector(".btn_ok"));
        cookiesSubmit.click();

        webDriver.findElement(By.cssSelector(".dsel_dd > div")).click();
        Thread.sleep(3000);
        webDriver.findElement(By.cssSelector(".dest_sel_cont li:nth-child(1)")).click();
        Thread.sleep(1000);
        webDriver.findElement(By.id("c532")).click();
        Thread.sleep(1000);
        webDriver.findElement(By.cssSelector(".search_go_btn")).click();

        boolean isZero = webDriver.findElement(By.xpath("/html/body/div[11]/div/div[1]")).getText().contains("0");
        WebElement resultNumber = webDriver.findElement(By.xpath("/html/body/div[11]/div/div[1]"));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", resultNumber);
        Thread.sleep(3000);

        assertTrue(isZero, "Wrong number of results");
    }

    @Test
    void testSearchFilterPriceLowToHigh() throws InterruptedException {
        webDriver.get(baseUrl);
        Thread.sleep(1000);
        WebElement cookiesSubmit = webDriver.findElement(By.cssSelector(".btn_ok"));
        cookiesSubmit.click();

        WebElement firstClick = webDriver.findElement(By.cssSelector(".dsel_dd > div"));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", firstClick);
        Thread.sleep(2000);
        firstClick.click();
        WebElement child = webDriver.findElement(By.cssSelector(".dest_sel_cont li:nth-child(6)"));
        //((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", child);
        Thread.sleep(1000);
        child.click();
        Thread.sleep(3000);
        webDriver.findElement(By.xpath("//*[@id=\"c285\"]")).click();
        webDriver.findElement(By.cssSelector(".search_go_btn")).click();

        Thread.sleep(3000);
        WebElement resultNumber = webDriver.findElement(By.xpath("/html/body/div[11]/div/div[1]"));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", resultNumber);
        Thread.sleep(2000);
        Thread.sleep(3000);
        WebElement firstApartmentPrice = webDriver.findElement(By.xpath("//*[@id=\"ads_items\"]/div[1]/a/div[1]/div[2]/div[2]/div[2]/b"));

        Thread.sleep(3000);
        webDriver.findElement(By.xpath("/html/body/div[11]/div/div[2]/div[2]/button")).click();
        Thread.sleep(3000);
        webDriver.findElement(By.linkText("Cijeni: od niže prema višoj")).click();
        Thread.sleep(3000);
        WebElement firstApartmentPriceSorted = webDriver.findElement(By.xpath("//*[@id=\"ads_items\"]/div[1]/a/div[1]/div[2]/div[2]/div[2]/b"));
        Thread.sleep(3000);
        assertNotEquals(firstApartmentPriceSorted, firstApartmentPrice);
    }

    //c285
    @Test
    void testSearchWithArgumentGreaterThanZeroResult() throws InterruptedException {
        webDriver.get(baseUrl);
        Thread.sleep(1000);
        WebElement cookiesSubmit = webDriver.findElement(By.cssSelector(".btn_ok"));
        cookiesSubmit.click();

        WebElement firstClick = webDriver.findElement(By.cssSelector(".dsel_dd > div"));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", firstClick);
        Thread.sleep(2000);
        firstClick.click();
        WebElement child = webDriver.findElement(By.cssSelector(".dest_sel_cont li:nth-child(6)"));
        //((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", child);
        Thread.sleep(1000);
        child.click();
        Thread.sleep(3000);
        webDriver.findElement(By.xpath("//*[@id=\"c285\"]")).click();
        webDriver.findElement(By.cssSelector(".search_go_btn")).click();

        Thread.sleep(3000);
        WebElement resultNumber = webDriver.findElement(By.xpath("/html/body/div[11]/div/div[1]"));

        // Get the text of the element
        String resultText = resultNumber.getText();

        // Convert the text to a number (e.g., int)
        try {
            int number = Integer.parseInt(resultText.trim());
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", resultNumber);
            Thread.sleep(2000);
            Thread.sleep(3000);
            boolean isBiggerThan0 =  number > 0;
            assertTrue(isBiggerThan0);
        } catch (NumberFormatException e) {
            System.out.println("Error: Unable to convert the text to a number. The text was: " + resultText);
        }
    }

    @Test
    void testDollarCurrencySearch() throws InterruptedException {
        webDriver.get(baseUrl);

        WebElement cookiesSubmit = webDriver.findElement(By.cssSelector(".btn_ok"));
        Thread.sleep(1000);

        cookiesSubmit.click();

        webDriver.findElement(By.xpath("//*[@id=\"servicesDropdown\"]")).click();
        Thread.sleep(1000);

        webDriver.findElement(By.xpath("//*[@id=\"curr_list\"]/li[13]/a")).click();
        Thread.sleep(1000);

        WebElement search = webDriver.findElement(By.xpath("//*[@id=\"inp_dest\"]"));
        search.sendKeys("Vis");
        Thread.sleep(1000);

        webDriver.findElement(By.cssSelector(".search_go_btn")).click();
        Thread.sleep(2000);

        WebElement firstApartmentCurrency = webDriver.findElement(By.xpath("//*[@id=\"ads_items\"]/div[1]/a/div[1]/div[2]/div[2]/div[2]/b"));
        boolean containsDollar = firstApartmentCurrency.getText().contains("$");

        assertTrue(containsDollar);
    }

}
