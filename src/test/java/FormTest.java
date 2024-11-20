import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.Color;

import javax.swing.*;
import java.util.List;

import static java.awt.SystemColor.text;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FormTest {
    private WebDriver driver;

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldSendFormWithCorrectValues() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Григорий");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79604578591");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text);
    }

    //невалидное значение в поле имени и фамилии
    @Test
    void shouldGiveErrorIfIncorrectValueInNameField() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Grisha");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79604578591");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text);
    }

    //невалидное значение в поле телефона
    @Test
    void shouldGiveErrorIfIncorrectValueInPhoneField() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Григорий");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("78591");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    //пустое поле имени и фамилии
    @Test
    void shouldGiveErrorIfEmptyValueInNameField() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79604578591");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals("Поле обязательно для заполнения", text);
    }

    //пустое поле телефона
    @Test
    void shouldGiveErrorIfEmptyValueInPhoneField() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Гриша");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals("Поле обязательно для заполнения", text);
    }

    //неотмеченный чекбокс
    @Test
    void shouldGiveErrorIfCheckboxNotChecked() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Гриша");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79604578591");
        driver.findElement(By.cssSelector("button")).click();
        String rgbaColor = driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid")).getCssValue("color");
        String hexColor = Color.fromString(rgbaColor).asHex();
        assertEquals("#ff5c5c", hexColor);
    }
}