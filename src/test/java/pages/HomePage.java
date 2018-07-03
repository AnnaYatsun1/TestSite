package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import properties.BaseTest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

public class HomePage extends BasePage {
    private final By headerCurrencyButton = By.cssSelector(".expand-more._gray-darker");
    private final By eurCurrencyType = By.xpath("//a[.='EUR €']");
    private final By uahCurrencyType = By.xpath("//a[.='UAH ₴']");
    private final By usdCurrencyType = By.xpath("//a[.='USD $']");
    private final By productPrice = By.className("price");
    private final By searchField = By.className("ui-autocomplete-input");



    public void changeCurrencyType(Currency currency) {
        waitUntilElementIsClickable(headerCurrencyButton);
        driver.findElement(headerCurrencyButton).click();
        switch (currency) {
            case EUR:
                waitForElementAndClick(eurCurrencyType);
                break;
            case UAH:
                waitForElementAndClick(uahCurrencyType);
                break;
            case USD:
                waitForElementAndClick(usdCurrencyType);
                break;
            default:
                throw new IllegalArgumentException("Please, enter the correct currency name: EUR, UAH or USD");
        }
    }

    public boolean checkCurrency(Currency currency) {
        waitUntilElementIsClickable(productPrice);
        List<WebElement> productPrices = driver.findElements(productPrice);
        BaseTest.log("Verifying product's price currency type ...");
        switch (currency) {
            case EUR:
                for (WebElement value : productPrices) {
                    Assert.assertEquals(value.getText().contains("€"), "€", "is displayed in EUR currency. Verified.");
                    Assert.assertEquals(value.getText().contains("EUR"), "EUR", "is displayed in EUR currency. Verified.");
               }

            case UAH:
             for (WebElement value : productPrices) {
                 Assert.assertEquals(value.getText().contains("₴"), "₴", "is displayed in UAH currency. Verified.");
                 Assert.assertEquals(value.getText().contains("UAH"), "UAH", "is displayed in UAH currency. Verified.");

                }

            case USD:
                for (WebElement value : productPrices) {
                    Assert.assertEquals(value.getText().contains("$"), "$", "is displayed in USD currency. Verified.");
                    Assert.assertEquals(value.getText().contains("USD"), "USD", "is displayed in USD currency. Verified.");
}

            default:
                throw new IllegalArgumentException("Please, enter the correct currency name: EUR, UAH or USD");
        }
    }

    public SearchResultsPage searchProduct(String name) {
        fillInputField(searchField, name);
        return new SearchResultsPage();
    }
}

