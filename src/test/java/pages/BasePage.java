package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import properties.BaseTest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

public abstract class BasePage {
    final WebDriver driver;
    final WebDriverWait wait;

    BasePage() {
        driver = BaseTest.getDriver();
        wait = new WebDriverWait(driver, 10);
    }

    void waitUntilElementIsClickable(By by) {
        wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    void waitUntilElementIsVisible(By by) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }
    public String getText(By locator) {
        return waitUntilClickable(locator).getText();
    }
    List<WebElement> selectFromList(By locator) {
        return driver.findElements(locator);
    }
    public WebElement waitUntilClickable(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.ignoring(NoSuchElementException.class);
        wait.pollingEvery(250, TimeUnit.MILLISECONDS);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void waitForElementAndClick(By locator) {
        waitUntilClickable(locator).click();
    }
    public void fillInputField(By locator, String message) {
        elementWaitVisibility(locator).clear();
        elementWaitVisibility(locator).sendKeys(message);
    }
    public WebElement elementWaitVisibility(By element) {
        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.ignoring(NoSuchElementException.class);
        wait.pollingEvery(250, TimeUnit.MILLISECONDS);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(element));
    }

    public enum Currency {
        EUR,
        UAH,
        USD
    }

    public enum SortingMethod {
        RELEVANCE("Релевантность"),
        NAME_FROM_A_TO_Z("Названию: от А к Я"),
        NAME_FROM_Z_TO_A("Названию: от Я к А"),
        PRICE_FROM_LOW_TO_HIGH("Цене: от низкой к высокой"),
        PRICE_FROM_HIGH_TO_LOW("Цене: от высокой к низкой");

        private final String description;

        SortingMethod(String description) {
            this.description = description;
        }

        String getDescription() {
            return description;
        }
    }
}
