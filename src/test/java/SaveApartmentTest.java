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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SaveApartmentTest {
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
    void testSaveApartments() throws InterruptedException {
        WebElement regionCard = webDriver.findElement(By.xpath("//*[@id='reg0']/a"));
        js.executeScript("arguments[0].scrollIntoView(true);", regionCard);
        Thread.sleep(500);
        js.executeScript("arguments[0].click();", regionCard);

        assertEquals("https://www.apartmanija.hr/hrvatska/otoci", webDriver.getCurrentUrl());

        WebElement apartment1Name = webDriver.findElement(By.xpath("//*[@id='ads_items']/div[1]/a/div[2]/span"));
        js.executeScript("arguments[0].scrollIntoView(true);", apartment1Name);
        Thread.sleep(800);
        String apartment1NameText = apartment1Name.getText();
        WebElement saveApartment1 = webDriver.findElement(By.xpath("//*[@id='ads_items']/div[1]/a/div[2]/i"));
        js.executeScript("arguments[0].click();", saveApartment1);
        Thread.sleep(800);


        WebElement saveApartment3 = webDriver.findElement(By.xpath("//*[@id='ads_items']/div[3]/a/div[2]/i"));
        saveApartment3.click();
        Thread.sleep(800);

        WebElement navBarElement = webDriver.findElement(By.xpath("//*[@id='offcanvasNavbar']/div[2]/ul/li[5]/a"));
        js.executeScript("arguments[0].scrollIntoView(true);", navBarElement);
        Thread.sleep(800);
        navBarElement.click();

        WebElement pageTitle = webDriver.findElement(By.xpath("/html/body/div[6]/div[1]/div/h1"));
        assertEquals("Odabrani smještaj", pageTitle.getText());
        Thread.sleep(800);

        WebElement numberOfSavedApartments = webDriver.findElement(By.xpath("/html/body/div[6]/div[6]/div/div[1]/span"));
        String numberOfSavedApartmentsText = numberOfSavedApartments.getText();

        WebElement apartment1NameSaved = webDriver.findElement(By.xpath("//*[@id='ads_items']/div[1]/a/div[2]/span"));
        assertEquals(apartment1NameText, apartment1NameSaved.getText());

        WebElement apartment1saveButton = webDriver.findElement(By.xpath("//*[@id='ads_items']/div[1]/a/div[2]/i"));
        js.executeScript("arguments[0].scrollIntoView(true);", apartment1saveButton);
        js.executeScript("arguments[0].click();", apartment1saveButton);
        Thread.sleep(800);

        WebElement navBar = webDriver.findElement(By.xpath("//*[@id='offcanvasNavbar']/div[2]/ul/li[5]/a"));
        js.executeScript("arguments[0].scrollIntoView(true);", navBar);
        Thread.sleep(800);
        navBar.click();

        WebElement newNumberOfSavedApartments = webDriver.findElement(By.xpath("/html/body/div[6]/div[6]/div/div[1]/span"));
        assertNotEquals(numberOfSavedApartmentsText, newNumberOfSavedApartments.getText());

        webDriver.close();

    }

    @Test
    void testSaveIconAndRemoveAll () throws InterruptedException {
        WebElement regionCard = webDriver.findElement(By.xpath("//*[@id='reg0']/a"));
        js.executeScript("arguments[0].scrollIntoView(true);", regionCard);
        Thread.sleep(500);
        js.executeScript("arguments[0].click();", regionCard);

        assertEquals("https://www.apartmanija.hr/hrvatska/otoci", webDriver.getCurrentUrl());

        WebElement apartment1Name = webDriver.findElement(By.xpath("//*[@id='ads_items']/div[1]/a/div[2]/span"));
        js.executeScript("arguments[0].scrollIntoView(true);", apartment1Name);
        Thread.sleep(800);
        WebElement saveApartment1 = webDriver.findElement(By.xpath("//*[@id='ads_items']/div[1]/a/div[2]/i"));
        js.executeScript("arguments[0].click();", saveApartment1);
        Thread.sleep(800);


        WebElement saveApartment3 = webDriver.findElement(By.xpath("//*[@id='ads_items']/div[3]/a/div[2]/i"));
        saveApartment3.click();
        Thread.sleep(800);

        WebElement floatingIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[4]/a")));
        //WebElement shortcutIcon = webDriver.findElement(By.xpath("/html/body/div[4]/a"));
        js.executeScript("arguments[0].scrollIntoView(true);", floatingIcon);
        js.executeScript("arguments[0].click();", floatingIcon);


        WebElement pageTitle = webDriver.findElement(By.xpath("/html/body/div[6]/div[1]/div/h1"));
        assertEquals("Odabrani smještaj", pageTitle.getText());
        Thread.sleep(800);

        WebElement numberOfSavedApartments = webDriver.findElement(By.xpath("/html/body/div[6]/div[6]/div/div[1]/span"));
        String numberOfSavedApartmentsText = numberOfSavedApartments.getText();

        WebElement removeAllButton = webDriver.findElement(By.xpath("/html/body/div[6]/div[8]/a"));
        js.executeScript("arguments[0].scrollIntoView(true);", removeAllButton);
        js.executeScript("arguments[0].click();", removeAllButton);
        Thread.sleep(800);

        WebElement navBar = webDriver.findElement(By.xpath("//*[@id='offcanvasNavbar']/div[2]/ul/li[5]/a"));
        js.executeScript("arguments[0].scrollIntoView(true);", navBar);
        Thread.sleep(800);
        navBar.click();

        WebElement newNumberOfSavedApartments = webDriver.findElement(By.xpath("/html/body/div[6]/div[6]/div/div[1]/span"));
        assertNotEquals(numberOfSavedApartmentsText, newNumberOfSavedApartments.getText());

        webDriver.close();

    }
}
