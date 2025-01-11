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

    @Test
    void testApartmentUnitForm() throws InterruptedException {
        WebElement apartmentCard = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='card']/a")));
        String hrefValue = apartmentCard.getAttribute("href");
        // Scroll into view
        js.executeScript("arguments[0].scrollIntoView(true);", apartmentCard);
        js.executeScript("arguments[0].click();", apartmentCard);

        assertEquals(hrefValue,webDriver.getCurrentUrl(),"Page mismatch.");

        WebElement unitTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='unit_2']/div[1]/div[1]/span")));
        js.executeScript("arguments[0].scrollIntoView(true);", unitTitle);
        Thread.sleep(500);
        WebElement unitButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='unit_2']/div[1]/div[2]/a[2]")));
        unitButton.click();
        Thread.sleep(500);

        WebElement fname = webDriver.findElement(By.id("fname"));
        js.executeScript("arguments[0].scrollIntoView(true);", fname);
        fname.sendKeys("Test");
        Thread.sleep(500);

        WebElement lname = webDriver.findElement(By.xpath("//*[@id='cform']/div[1]/div[2]/input"));
        lname.sendKeys("Test");
        Thread.sleep(500);

        WebElement email = webDriver.findElement(By.xpath("//*[@id='cform']/div[1]/div[3]/input"));
        js.executeScript("arguments[0].scrollIntoView(true);", email);
        email.sendKeys("test@test.com");
        Thread.sleep(500);

        WebElement phone = webDriver.findElement(By.xpath("//*[@id='cform']/div[1]/div[4]/input"));
        phone.sendKeys("385 98 765 432");
        Thread.sleep(500);


        WebElement arrival = webDriver.findElement(By.id("arrival"));
        arrival.click();
        Thread.sleep(500);
        WebElement arrivalDate = webDriver.findElement(By.xpath("/html/body/div[23]/div[1]/div[2]/table/tbody/tr[5]/td[1]"));
        arrivalDate.click();
        Thread.sleep(500);

        WebElement departure = webDriver.findElement(By.id("departure"));
        departure.click();
        Thread.sleep(500);
        WebElement departureDate = webDriver.findElement(By.xpath("/html/body/div[25]/div[1]/div[2]/table/tbody/tr[5]/td[5]"));
        departureDate.click();
        Thread.sleep(500);

        Select adults = new Select(webDriver.findElement(By.xpath("//*[@id='cform']/div[1]/div[8]/select")));
        adults.selectByValue("2");
        Thread.sleep(500);

        Select children = new Select(webDriver.findElement(By.xpath("//*[@id='cform']/div[1]/div[10]/select")));
        children.selectByValue("2");
        Thread.sleep(500);

        WebElement text = webDriver.findElement(By.xpath("//*[@id='cform']/div[1]/div[11]/textarea"));
        js.executeScript("arguments[0].scrollIntoView(true);", text);
        text.sendKeys("Test");
        Thread.sleep(500);

        WebElement total = webDriver.findElement(By.xpath("//*[@id='cform']/div[2]/div[2]/div/div[2]/div[4]/span"));
        js.executeScript("arguments[0].scrollIntoView(true);", total);

        assertEquals("208 €", total.getText(), "Price mismatch.");

    }
}
