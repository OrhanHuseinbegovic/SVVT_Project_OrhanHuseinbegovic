import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApartmentUnitsTest {
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
        webDriver.manage().window().setSize(new Dimension(1552, 832));

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
    void testApartmentCardAndItsUnits() throws InterruptedException {
        //WebElement apartmentCardText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='card']/a/div[@class='img-wrapper']/div[@class='cont-wrapper']/div[@class='ttl-wrapper']/div[@class='float-start']")));

        WebElement apartmentCard = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='card']/a")));
        String hrefValue = apartmentCard.getAttribute("href");
        // Scroll into view
        js.executeScript("arguments[0].scrollIntoView(true);", apartmentCard);
        js.executeScript("arguments[0].click();", apartmentCard);

        assertEquals(hrefValue,webDriver.getCurrentUrl(),"Page mismatch.");

        //$x('//div[@class="property_units"]/div[@class="property_unit"]')
        List<WebElement> propertyUnits = webDriver.findElements(By.xpath("//div[@class='property_unit']"));

        for (int i = 0; i < propertyUnits.size(); i++) {
            String unitID = propertyUnits.get(i).getAttribute("id");
            System.out.println("Processing unit with ID: " + unitID);
            WebElement unitTitle = webDriver.findElement(By.xpath("//div[@id='" + unitID +"']/a"));
            String title = unitTitle.getAttribute("title");
            System.out.println("Processing unit title: " + title);
            String trimedTitle = title.replaceAll(".*\\(ID:(\\d+)\\).*", "$1");
            System.out.println();

            js.executeScript("arguments[0].scrollIntoView(true);", unitTitle);
            Thread.sleep(1000);

            //$x('/div[@class="property_unit_info_right"]/a[contains(.,"Pošalji upit")]')
            //WebElement unitButton = webDriver.findElement(By.xpath("//div[@id='" + unitID +"']/div[1]/div[2]/a[2]"));
            WebElement unitButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='" + unitID +"']/div[1]/div[2]/a[2]")));
            js.executeScript("arguments[0].scrollIntoView(true);", unitButton);
            Thread.sleep(1000);
            js.executeScript("arguments[0].click();", unitButton);

            Thread.sleep(1000);

            // Fetch the selected option
            Select select = new Select(webDriver.findElement(By.id("acm_unit")));
            String selectedOption = select.getFirstSelectedOption().getAttribute("value");
            System.out.println("Selected option in form: " + selectedOption);

            // Compare the unit title with the selected option in the form
            assertTrue(selectedOption.contains(trimedTitle), "Unit mismatch: Title not found in form.");
        }
    }
}
