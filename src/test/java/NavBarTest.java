import java.time.Duration;
import java.util.List;
import java.util.Set;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NavBarTest {
    private static WebDriver webDriver;
    WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

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


    //Reload if it fails xD
    @Test
    void testCategoryDropdown(){
        String [] categoryLinks = {"/apartmani", "/vile", "/robinzonski-turizam", "/seoski-turizam"};
        WebElement categoryDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("catDropdown")));
        categoryDropdown.click();

        List<WebElement> categoryOptions = webDriver.findElements(By.xpath("//li[contains(.,'Apartmani')]/ul/li"));
        assertEquals(categoryLinks.length, categoryOptions.size(), "Category options count mismatch!");

        for (int i = 0; i < categoryLinks.length; i++) {
            categoryOptions.get(i).click();

            wait.until(ExpectedConditions.urlContains(categoryLinks[i]));
            System.out.println("Verified category: " + categoryLinks[i]);
            assertTrue(webDriver.getCurrentUrl().contains(categoryLinks[i]), "URL does not match the expected category!");

            webDriver.navigate().back();
            wait.until(ExpectedConditions.elementToBeClickable(By.id("catDropdown"))).click(); // Reopen dropdown

        }
    }

    @Test
    void testSaveButton(){
        //url contains moji-apartmani
        WebElement bookmarks = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".nav-link > .fa-light")));
        bookmarks.click();

        wait.until(ExpectedConditions.urlContains("moji-apartmani"));
        assertTrue(webDriver.getCurrentUrl().contains("moji-apartmani"), "Bookmarks URL mismatch!");
        System.out.println("Verified bookmarks page: " + webDriver.getCurrentUrl());

        webDriver.navigate().back();

    }

    @Test
    void testLoginButton(){
        WebElement profile = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".img-no-usr")));

        // Capture the current window handle (main window)
        String originalWindow = webDriver.getWindowHandle();

        profile.click();
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));

        // Get all the window handles after clicking
        Set<String> allWindows = webDriver.getWindowHandles();
        assertEquals(2, allWindows.size(), "Profile new tab not opened!");

        // Switch to the newly opened tab
        for (String windowHandle : allWindows) {
            if (!windowHandle.equals(originalWindow)) {
                webDriver.switchTo().window(windowHandle);
                break;
            }
        }

        // Verify the new tab URL
        String profileUrl = webDriver.getCurrentUrl();
        System.out.println("Verified profile login URL: " + profileUrl);
        assertTrue(profileUrl.contains("/login"), "Login URL mismatch!");

        // Close the new tab and switch back to the original window
        webDriver.close();
        webDriver.switchTo().window(originalWindow);

    }

    @Test
    void testOglasiSeButton() throws InterruptedException {
        WebElement register = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Oglasi se!")));
        register.click();

        wait.until(ExpectedConditions.urlContains("oglasavanje-smjestaja"));
        assertTrue(webDriver.getCurrentUrl().contains("oglasavanje-smjestaja"), "URL mismatch!");
        System.out.println("Verified 'Oglasavanje' page: " + webDriver.getCurrentUrl());

        webDriver.navigate().back();
    }

}
