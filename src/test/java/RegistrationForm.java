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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationForm {

    private static WebDriver webDriver;
    private static String baseUrl;
    private static HashMap<String, Object> vars = new HashMap<>();


    @BeforeAll
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", "/Users/orhanhuseinbegovic/Downloads/chromedriver-mac-arm64/chromedriver"); // specify the path to chromedriver
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        webDriver = new ChromeDriver(options);
        baseUrl = "https://www.apartmanija.hr/oglasavanje-smjestaja";
    }

    @AfterAll
    public static void tearDown() {
        // Close the browser
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    //Login scenario testing
    @Test
    void testEmptyRegistrationForm() throws InterruptedException {
        webDriver.get(baseUrl);
        //webDriver.manage().window().setSize(new Dimension(1470, 822));

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//form[@id='regform']/div/div[12]/button")));
        WebElement cookiesSubmit = webDriver.findElement(By.cssSelector(".btn_ok"));

        // Click the button
        cookiesSubmit.click();
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", submitButton);
        Thread.sleep(5000);
        //JavascriptExecutor js = (JavascriptExecutor) webDriver;
        //js.executeScript("window.scrollBy(0,3850)", "");
        //Thread.sleep(5000);

        WebDriverWait waitMore = new WebDriverWait(webDriver, Duration.ofSeconds(5));
        submitButton.click();
        /*
        WebElement submitButton = webDriver.findElement(By.xpath("//form[@id='regform']/div/div[12]/button"));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", submitButton);

        // Click the button
        submitButton.click();

         */

        // Wait for the error message to appear
        WebDriverWait waitEvenMore = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"regform_tos_errorloc\"]")));

        // Assert the error message text
        assertEquals("Molimo, označite da ste upoznati s pravilima oglašavanja", errorMessage.getText(), "Error message did not match the expected text.");

    }

    @Test
    public void testRegistrationTitle() throws InterruptedException {
        webDriver.get(baseUrl);
        String actualTitle = webDriver.getTitle();
        System.out.println("Actual title: " + actualTitle);
        assertEquals("Oglašavanje apartmana | Oglašavanje smještaja | Oglasnik Apartmanija.hr", actualTitle, "Title does not match");
        Thread.sleep(1000);
    }

    @Test
    public void testSameOibError() throws InterruptedException {
        webDriver.get(baseUrl);

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
        //WebElement cookiesSubmit = webDriver.findElement(By.cssSelector(".btn_ok"));
        WebElement oibInput = webDriver.findElement(By.id("oib"));

        //cookiesSubmit.click();
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", oibInput);
        Thread.sleep(5000);

        webDriver.findElement(By.id("oib")).sendKeys("34562345678");
        webDriver.findElement(By.id("email")).click();
        Thread.sleep(1000);
        WebElement errorMessage = webDriver.findElement(By.id("oib_err2"));

        Thread.sleep(5000);
        assertEquals("Korisnik s istim OIB-om već postoji!", errorMessage.getText(), "Error message did not match the expected text.");
    }

    @Test
    public void testLinkOpening() throws InterruptedException {
        webDriver.get(baseUrl);

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
        WebElement link = webDriver.findElement(By.xpath("//*[@id=\"regform\"]/div/div[4]/div/label[2]/a"));

        // Scroll into view if the link is not visible
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", link);
        Thread.sleep(3000);

        // Click the link (opens in a new tab/window)
        link.click();

        // Wait for the new tab to open
        Thread.sleep(3000);

        // Get all open window handles
        List<String> windowHandles = new ArrayList<>(webDriver.getWindowHandles());

        // Switch to the new tab (the last one in the list)
        webDriver.switchTo().window(windowHandles.get(1));

        // Get the current URL of the new tab
        String currentUrl = webDriver.getCurrentUrl();

        // Assert the expected URL
        assertEquals("https://saznaj-oib.porezna-uprava.hr/", currentUrl, "Wrong URL");
    }


}
