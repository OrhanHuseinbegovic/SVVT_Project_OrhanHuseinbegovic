import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.Set;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginForm {

    private static WebDriver webDriver;
    private static String baseUrl;
    private static HashMap<String, Object> vars = new HashMap<>();


    @BeforeAll
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", "/Users/orhanhuseinbegovic/Downloads/chromedriver-mac-arm64/chromedriver"); // specify the path to chromedriver
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        webDriver = new ChromeDriver(options);
        baseUrl = "https://www.korisnici.apartmanija.hr/login";
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
    void testEmptyLoginForm() {
        webDriver.get(baseUrl);
        WebElement submitButton = webDriver.findElement(By.cssSelector("button[type='submit']"));

        submitButton.click();

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='loginForm']/div/ul/li")));

        assertEquals("Neispravna e-mail adresa!", errorMessage.getText(), "Error message did not match the expected text.");
    }

    @Test
    public void testLoginTitle() throws InterruptedException {
        webDriver.get(baseUrl);
        String actualTitle = webDriver.getTitle();
        System.out.println("Actual title: " + actualTitle);
        assertEquals("Korisničko sučelje - Apartmanija.hr", actualTitle, "Title does not match");
        Thread.sleep(1000);
    }

    @Test
    public void testInputWrongCredentials() {
        webDriver.get(baseUrl);
        webDriver.manage().window().setSize(new Dimension(1470, 822));

        webDriver.findElement(By.name("LoginForm[username]")).sendKeys("someemail@gmail.com");
        webDriver.findElement(By.name("LoginForm[password]")).sendKeys("somepassword");


        webDriver.findElement(By.cssSelector("button")).click();

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li")));

        assertEquals("Neispravni korisnički podaci!", errorMessage.getText());
    }

    @Test
    public void testForgotPassword() throws InterruptedException {
        webDriver.get(baseUrl);
        webDriver.manage().window().setSize(new Dimension(1470, 822));


        // Click the "Forgot password?" link
        webDriver.findElement(By.linkText("Zaboravljena lozinka?")).click();
        Thread.sleep(1000);

        String forgotPasswordURL = webDriver.getCurrentUrl();
        Thread.sleep(2000);

        WebElement cookiesSubmit = webDriver.findElement(By.cssSelector(".btn_ok"));
        Thread.sleep(1000);

        cookiesSubmit.click();

        // IF fails, change email address
        WebElement emailField = webDriver.findElement(By.id("email_passrec"));
        emailField.click();
        emailField.sendKeys("test@gmail.com");

        String email = "test@gmail.com";

        // Click the submit button
        webDriver.findElement(By.id("submit")).click();
        Thread.sleep(3000);

        String loginURL = webDriver.getCurrentUrl();
        Thread.sleep(2000);

        assertNotEquals(forgotPasswordURL, loginURL);
    }


}
