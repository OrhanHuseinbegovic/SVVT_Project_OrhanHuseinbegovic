import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SecurityProtocolsCookiesHandlingTest {
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

    @Test
    void testEnforcingHTTPS() throws InterruptedException {
        webDriver.get("http://www.apartmanija.hr/kontakt");
        Thread.sleep(500);

        assertEquals(webDriver.getCurrentUrl(), "https://www.apartmanija.hr/kontakt");
    }

    @Test
    void checkCookieSecurity() throws InterruptedException {
        webDriver.get("https://www.apartmanija.hr");

        Thread.sleep(2000);

        // Retrieve cookies
        Set<Cookie> cookies = webDriver.manage().getCookies();
        // Assert that cookies exist
        assert !cookies.isEmpty() : "No cookies were found. Ensure the site sets cookies.";

        // Check each cookie for security attributes
        for (Cookie cookie : cookies) {
            System.out.println("Cookie Name: " + cookie.getName());
            System.out.println("Cookie Value: " + cookie.getValue());
            System.out.println("Domain: " + cookie.getDomain());
            System.out.println("Path: " + cookie.getPath());
            System.out.println("Is Secure: " + cookie.isSecure());
            System.out.println("Is HttpOnly: " + cookie.isHttpOnly());
            System.out.println();

            // Perform attribute validation
            if (!cookie.isSecure()) {
                System.out.println("WARNING: Cookie '" + cookie.getName() + "' is not marked Secure.");
            }
            if (!cookie.isHttpOnly()) {
                System.out.println("WARNING: Cookie '" + cookie.getName() + "' is not marked HttpOnly.");
            }
            // Validate domain
            if (!cookie.getDomain().contains("apartmanija.hr")) {
                System.out.println("WARNING: Cookie '" + cookie.getName() + "' domain is invalid: " + cookie.getDomain());
            }

            System.out.println();
        }
    }

}


