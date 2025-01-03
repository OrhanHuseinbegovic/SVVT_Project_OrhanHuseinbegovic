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

public class SearchFilterTesting {

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
        WebElement cookiesSubmit = webDriver.findElement(By.cssSelector(".btn_ok"));
        Thread.sleep(1000);

        cookiesSubmit.click();

        WebElement search = webDriver.findElement(By.xpath("//*[@id=\"inp_dest\"]"));
        search.sendKeys("Vis");
        Thread.sleep(1000);

        webDriver.findElement(By.cssSelector(".search_go_btn")).click();
        Thread.sleep(2000);
    }

    @AfterAll
    public static void tearDown() {
        // Close the browser
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    @Test
    void testAdditionalFilter() throws InterruptedException {
        webDriver.findElement(By.xpath("//*[@id=\"dd_persons\"]")).click();
        WebElement nOfPeople = webDriver.findElement(By.xpath("//*[@id=\"inp_nump_val\"]"));
        nOfPeople.sendKeys("20");
        webDriver.findElement(By.xpath("//*[@id=\"search_form\"]/div/div/a[5]/span")).click();
        Thread.sleep(2000);

        WebElement filters = webDriver.findElement(By.xpath("/html/body/div[9]"));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", filters);
        Thread.sleep(1000);

        webDriver.findElement(By.xpath("/html/body/div[11]/div/div[2]/div[2]/button")).click();
        Thread.sleep(1000);
        webDriver.findElement(By.linkText("Cijeni: od niže prema višoj")).click();
        Thread.sleep(1000);
        webDriver.findElement(By.xpath("/html/body/div[9]/div[5]/button")).click();
        Thread.sleep(1000);
        webDriver.findElement(By.xpath("/html/body/div[9]/div[5]/div/div/div[6]")).click();
        Thread.sleep(1000);
        webDriver.findElement(By.xpath("/html/body/div[9]/div[5]/button")).click();
        Thread.sleep(1000);

        WebElement nOfApartments = webDriver.findElement(By.xpath("/html/body/div[11]/div/div[1]/span"));
        WebElement apartment = webDriver.findElement(By.xpath("//*[@id=\"ads_items\"]/div[1]/a/div[2]"));
        boolean apartmentName = apartment.getText().contains("Apartmani Eko");

        assertTrue(apartmentName);
    }

    @Test
    void testReviewed() throws InterruptedException {
        webDriver.get(baseUrl + "/pretraga/apartmani/r:split-i-okolica+c:gradac+osobe:1");
        Thread.sleep(1500);
        WebElement sort = webDriver.findElement(By.xpath("/html/body/div[9]/div[4]/button"));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", sort);
        Thread.sleep(1500);
        webDriver.findElement(By.xpath("/html/body/div[9]/div[4]/button")).click();
        Thread.sleep(1000);
        webDriver.findElement(By.xpath("/html/body/div[11]/div/div[2]/div[2]/button")).click();
        Thread.sleep(1000);
        webDriver.findElement(By.linkText("Broju ocjena")).click();

        String firstApartment = webDriver.findElement(By.xpath("//*[@id=\"ads_items\"]/div[1]/a/div[2]/span")).getText();

        WebElement lastAp = webDriver.findElement(By.xpath("//*[@id=\"ads_items\"]/div[21]/a/div[2]/span"));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", lastAp);
        Thread.sleep(1500);

        String lastApartment = webDriver.findElement(By.xpath("//*[@id=\"ads_items\"]/div[21]/a/div[2]/span")).getText();

        if (firstApartment.contains("Apartmani Lujanka")) {
            firstApartment = "Apartmani Lujanka";
        }
        if (lastApartment.contains("Apartmani Villa Ana")) {
            lastApartment = "Apartmani Villa Ana";
        }

        assertNotEquals(firstApartment, lastApartment, "There is no more than 1 apartment here");
        assertEquals("Apartmani Lujanka", firstApartment);
        assertEquals("Apartmani Villa Ana", lastApartment);
    }



}
