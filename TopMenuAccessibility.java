package files;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class TopMenuAccessibility {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\WebDriver\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            driver.get("https://www.tendable.com");
            driver.manage().window().maximize();

            clickAndCheckDemoButton(driver, By.id("top-banner"), "Home", wait);
            clickAndCheckDemoButton(driver, By.xpath("//*[@id='main-navigation-new']/ul/li[1]/a"), "Our Story", wait);
            clickAndCheckDemoButton(driver, By.xpath("//*[@id='main-navigation-new']/ul/li[2]/a"), "Our Solution", wait);
            clickAndCheckDemoButton(driver, By.linkText("Why Tendable?"), "Why Tendable", wait);

            driver.findElement(By.linkText("Contact Us")).click();

            fillContactUsForm(driver, wait);

            validateErrorMessage(driver, wait);
            System.out.println("Error message displayed. Test passed!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private static void clickAndCheckDemoButton(WebDriver driver, By locator, String menuName, WebDriverWait wait) {
        WebElement menu = driver.findElement(locator);
        menu.click();

        WebElement demoButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//html/body/header/div/a/img")));

        boolean isClickable = false;
        int attempts = 0;
        while (!isClickable && attempts < 5) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(demoButton));
                scrollToElement(driver, demoButton);
                demoButton.click();
                System.out.println("Request a Demo button is present, visible, enabled, and clickable on " + menuName);
                isClickable = true;
            } catch (Exception e) {
                attempts++;
                System.out.println("Attempt " + attempts + ": Element click intercepted, retrying...");
                wait.until(ExpectedConditions.stalenessOf(demoButton));
                demoButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//html/body/header/div/a/img")));
            }
        }
    }

    private static void scrollToElement(WebDriver driver, WebElement element) {
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'});", element);
    }

    private static void fillContactUsForm(WebDriver driver, WebDriverWait wait) {
        WebElement contactUsButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[4]/div/div/div[2]/div/div/div[1]/div/div[2]/div[2]/button")));
        contactUsButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[5]/div/div[1]/div/div[1]/form/div[3]/div/input"))).sendKeys("Nayan jain");
        driver.findElement(By.xpath("/html/body/div[5]/div/div[1]/div/div[1]/form/div[4]/div/input")).sendKeys("SA Tech");
        driver.findElement(By.xpath("/html/body/div[5]/div/div[1]/div/div[1]/form/div[5]/div[1]/input")).sendKeys("+91 7828023456");
        driver.findElement(By.xpath("/html/body/div[5]/div/div[1]/div/div[1]/form/div[5]/div[2]/input")).sendKeys("Nayan.jain@SATech.com");
        driver.findElement(By.xpath("/html/body/div[5]/div/div[1]/div/div[1]/form/div[8]/div/label[2]/input")).click();
        driver.findElement(By.xpath("//*[@id=\"contactMarketingPipedrive-163701\"]/div[10]/div/button")).click();
    }

    private static void validateErrorMessage(WebDriver driver, WebDriverWait wait) {
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"contactMarketingPipedrive-163701\"]/div[1]/p")));
        Assert.assertTrue(errorMessage.isDisplayed());
    }
}
