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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CallBackTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
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
    void sendForm() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Василий Алибабаевич");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79501112233");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.className("button")).click();

        WebElement successText = driver.findElement(By.cssSelector("[data-test-id='order-success']"));
        assertTrue(successText.isDisplayed());
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", successText.getText().trim());

    }

    @Test
    void sendFormInvalidName() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Vasiliy Alibabaevich");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79501112233");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.className("button")).click();

        WebElement invalidName = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", invalidName.getText().trim());
    }

    @Test
    void sendEmptyName() {
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79501112233");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.className("button")).click();

        WebElement invalidName = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));
        assertEquals("Поле обязательно для заполнения", invalidName.getText().trim());
    }

    @Test
    void sendInvalidPhone() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Василий Алибабаевич");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("89501112233");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.className("button")).click();

        WebElement invalidName = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", invalidName.getText().trim());
    }

    @Test
    void sendEmptyPhone() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Василий Алибабаевич");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.className("button")).click();

        WebElement invalidName = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        assertEquals("Поле обязательно для заполнения", invalidName.getText().trim());
    }

    @Test
    void shouldNotSendWithoutCheckbox() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Василий Алибабаевич");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79501112233");
        driver.findElement(By.className("button")).click();

        WebElement invalidName = driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid .checkbox__text"));
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй", invalidName.getText().trim());
    }

}
