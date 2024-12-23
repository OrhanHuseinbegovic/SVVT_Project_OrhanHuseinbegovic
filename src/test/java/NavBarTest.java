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

    @BeforeAll
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver",
                "C:/Users/rakov/Documents/IBU/Fall 3/svvt/chromedriver-win64/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        webDriver = new ChromeDriver(options);
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
    void navbarTest() throws InterruptedException {
        webDriver.get("https://www.apartmanija.hr");
        webDriver.manage().window().setSize(new Dimension(1552, 832));
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;

        WebElement cookieButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class = 'btn_ok']")));
        cookieButton.click();
        Thread.sleep(800);

        //CATEGORIES
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

        //webDriver.get("https://www.apartmanija.hr");

        //LANGUAGES
        String [] languageLinks = {"https://www.direct-croatia.com/", "https://www.alloggiocroazia.it/",
                "https://www.direkt-kroatien.de/", "https://www.chorvatskeubytovani.cz/", "https://www.apartamentychorwacja.pl/",
                "https://www.obmorju.si/", "https://www.horvatorszagapartmanok.net/","https://www.vacancescroatie.net/", "https://www.alojamientocroacia.es/",
                "https://www.apartamentecroatia.com/", "https://www.directkroatie.nl/"};

        WebElement languageDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("langDropdown")));
        languageDropdown.click();

        List<WebElement> languageOptions = webDriver.findElements(By.xpath("//li[contains(.,'HR')]/ul/li"));
        //take only first 13, because it counts li from nxt dropdown
        List<WebElement> languageItems = languageOptions.subList(1, 12);

        assertEquals(languageLinks.length, languageItems.size(), "Language options count mismatch!");

        for (int i = 0; i < languageLinks.length; i++) {
            // Capture the current window handle (main window)
            String originalWindow = webDriver.getWindowHandle();
            js.executeScript("arguments[0].scrollIntoView(true);", languageItems.get(i)); //scroll to the elemnt
            languageItems.get(i).click();

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
            System.out.println("Verified language URL: " + currentUrl);
            assertEquals(languageLinks[i], currentUrl, "Language URL mismatch!");

            // Close the new tab and switch back to the original window
            webDriver.close();
            webDriver.switchTo().window(originalWindow);

            wait.until(ExpectedConditions.elementToBeClickable(By.id("langDropdown"))).click(); // Reopen dropdown
        }


        //CURRENCY
        String [] currencyText = {"CHF (CHF)", "CZK (Kč)", "DKK (kr)", "GBP (£)", "HRK (kn)",
                "HUF (Ft)", "NOK (kr)","PLN (zł)", "RON (LEI)", "SEK (kr)", "RUB (RUB)", "USD ($)"};

        for (int i = 0; i < currencyText.length; i++) {
            WebElement currencyDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("servicesDropdown")));
            currencyDropdown.click();

            List<WebElement> currencyItems = webDriver.findElements(By.xpath("//li[contains(.,'EUR')]/ul/li"));
            List<WebElement> currencyOptions = currencyItems.subList(1, 13);

            assertEquals(currencyText.length, currencyOptions.size(), "Currency options count mismatch!");

            currencyOptions.get(i).click();

            WebElement selectedCurrency = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("servicesDropdown")));
            System.out.println("Selected currency: " + selectedCurrency.getText());
            assertTrue(selectedCurrency.getText().contains(currencyText[i]), "Currency text mismatch!");

            currencyDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("servicesDropdown")));
            currencyDropdown.click(); // Reopen dropdown
        }


        //FAV
        // contains moji-apartmani
        WebElement bookmarks = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".nav-link > .fa-light")));
        bookmarks.click();

        wait.until(ExpectedConditions.urlContains("moji-apartmani"));
        assertTrue(webDriver.getCurrentUrl().contains("moji-apartmani"), "Bookmarks URL mismatch!");
        System.out.println("Verified bookmarks page: " + webDriver.getCurrentUrl());

        webDriver.navigate().back();
        //Thread.sleep(2000);

        //PRIJAVA
        // li class = nav-item mob_menu_usr -> link vodi na prijavu
        WebElement profile = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".img-no-usr")));

        // Capture the current window handle (main window)
        String originalWindow = webDriver.getWindowHandle();

        profile.click();
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));

        // Get all the window handles after clicking (should include the new tab)
        Set<String> allWindows = webDriver.getWindowHandles();
        assertEquals(2, allWindows.size(), "Profile new tab not opened!"); // Ensure there's a new tab opened

        // Switch to the newly opened tab
        for (String windowHandle : allWindows) {
            if (!windowHandle.equals(originalWindow)) {
                webDriver.switchTo().window(windowHandle);
                break;
            }
        }

        // Verify the new tab URL (update expected URL as needed)
        String profileUrl = webDriver.getCurrentUrl();
        System.out.println("Verified profile login URL: " + profileUrl);
        assertTrue(profileUrl.contains("/login"), "Login URL mismatch!");

        // Close the new tab and switch back to the original window
        webDriver.close();
        webDriver.switchTo().window(originalWindow);


        //OGLASI SE
        // li class = nav-item mob_menu_ftr -> text Oglasi se i vodi na oglasi se page
        WebElement register = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Oglasi se!")));
        register.click();

        wait.until(ExpectedConditions.urlContains("oglasavanje-smjestaja"));
        assertTrue(webDriver.getCurrentUrl().contains("oglasavanje-smjestaja"), "Oglasi se URL mismatch!");
        System.out.println("Verified 'Oglasavanje' page: " + webDriver.getCurrentUrl());

    }

}
