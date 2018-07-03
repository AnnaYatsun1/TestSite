package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import properties.BaseTest;

import java.text.DecimalFormat;
import java.util.List;

public class SearchResultsPage extends HomePage {
    private final By productTile = By.className("product-title");
    private final By foundProductsLabel = By.className("total-products");
    private final By productPrice = By.className("price");
    private final By productOnlyRegularPrice = By.xpath("//div[@class='product-price-and-shipping']/span[1]");
    private final By productsWithDiscount = By.className("discount");
    private final By labelOfDiscount = By.className("discount-percentage");
    private final By actualPriceOfDiscountProducts = By.xpath("//span[@class='discount-percentage']/following-sibling::span");
    private final By regularPriceOfDiscountProducts = By.xpath("//span[@class='discount-percentage']/preceding-sibling::span");
    private final By sortingMethodsField = By.className("select-title");
    private final By sortingMethods = By.className("select-list");
    private final int priceLength = 4;

    public int getAllFoundProductsSize() {
        waitUntilElementIsClickable(productTile);
        return driver.findElements(productTile).size();
    }

    public String getFoundProductsLabel() {
        return  getText(foundProductsLabel);
    }

    public boolean checkTheCurrencyOfFoundProducts(Currency currency) {
        waitUntilElementIsClickable(productPrice);
        List<WebElement> prices = driver.findElements(productPrice);
        BaseTest.log("Verifying currency of product prices ...");
        switch (currency) {
            case EUR:
                for (WebElement value : prices) {
                    Assert.assertEquals(value.getText().contains("€"), "€", "All products prices are displayed in EUR currency. Verified.");
                    Assert.assertEquals(value.getText().contains("EUR"), "EUR", "All products prices are displayed in EUR currency. Verified.");
                    }

            case UAH:
                for (WebElement value : prices) {
                    Assert.assertEquals(value.getText().contains("₴"), "₴", "All products prices are displayed in UAH currency. Verified.");
                    Assert.assertEquals(value.getText().contains("UAH"), "UAH", "All products prices are displayed in UAH currency. Verified.");
                }

            case USD: // TODO: а если не USD? - done
                for (WebElement value : prices) {
                    Assert.assertEquals(value.getText().contains("$"), "$", "All products prices are displayed in USD currency. Verified.");
                    Assert.assertEquals(value.getText().contains("USD"), "USD", "All products prices are displayed in USD currency. Verified.");
                }

            default:
                throw new IllegalArgumentException("Please, enter the correct currency name: EUR, UAH or USD");
        }
    }

    public boolean checkHighToLowSorting() {
        wait.until((ExpectedConditions.urlContains("search?controller=search&order=product.price.desc&s=")));
        List<WebElement> regularPrices = driver.findElements(productOnlyRegularPrice);
        if (regularPrices.size() >= 2) {
            BaseTest.log("Verifying the High to Low price sorting method...");
            for (int i = 0; i + 1 < regularPrices.size(); i++) {
                float price = Float.parseFloat(regularPrices.get(i).getText().substring(0, priceLength).replace(",", ".")); // TODO: опять магические числа. обьяви переменную для 4, чтоб по названию было понятно - done
                float nextPrice = Float.parseFloat(regularPrices.get(i + 1).getText().substring(0, priceLength).replace(",", "."));
                if (price < nextPrice) { //
                    BaseTest.log(price + " is less than " + nextPrice + " Failed.");
                    return false;
                }
                BaseTest.log(price + " is more than or equals " + nextPrice + " Verified.");
            }
            return true;
        } else {
            throw new IllegalStateException("Impossible to check sorting method, there are less than 2 products on page");
        }
    }

    public boolean checkDiscountProductPriceAndLabel() {
        waitUntilElementIsClickable(productTile);
        selectFromList(productsWithDiscount);
        List<WebElement> productsWithDiscount = driver.findElements(this.productsWithDiscount);
        List<WebElement> labelsOfDiscount = driver.findElements(this.labelOfDiscount);
        List<WebElement> regularPriceOfDiscountProducts = driver.findElements(this.regularPriceOfDiscountProducts);
        List<WebElement> actualPriceOfDiscountProducts = driver.findElements(this.actualPriceOfDiscountProducts);
        BaseTest.log("Verifying each On-Sale product has its own discount label, actual and regular price ...");
        BaseTest.log("Found on page: " + productsWithDiscount.size() + " products On-Sale.");
        BaseTest.log("Found on page: " + labelsOfDiscount.size() + " labels of products On-Sale.");
        BaseTest.log("Found on page: " + regularPriceOfDiscountProducts.size() + " regular prices of products On-Sale.");
        BaseTest.log("Found on page: " + actualPriceOfDiscountProducts.size() + " actual prices of products On-Sale.");
        BaseTest.log("Verifying the price is displayed in percentages ...");
        for (WebElement label : labelsOfDiscount) {
            if (label.getText().contains("%")) {
                BaseTest.log("Label: " + label.getText() + " Verified.");
            }
            else {
                BaseTest.log("Label: " + label.getText() + " Failed.");
                return false;
            }

        }
        if (productsWithDiscount.size() == labelsOfDiscount.size() && productsWithDiscount.size() == regularPriceOfDiscountProducts.size() && productsWithDiscount.size() == actualPriceOfDiscountProducts.size()) {
            BaseTest.log("Quantity of On-Sale products: " + productsWithDiscount.size() + " equals the quantity of discount labels: " + labelsOfDiscount.size() + " quantity of regular prices: " + regularPriceOfDiscountProducts.size() + " quantity of actual prices: " + actualPriceOfDiscountProducts.size() + ". Verified.");
            return true;
        } else {
            BaseTest.log("Quantity of On-Sale products: " + productsWithDiscount.size() + " IS NOT equal the quantity of discount labels: " + labelsOfDiscount.size() + " quantity of regular prices: " + regularPriceOfDiscountProducts.size() + " quantity of actual prices: " + actualPriceOfDiscountProducts.size() + ". Verified.");
            return false;
        }
    }

    public boolean checkDiscountCalculation() {
        waitUntilElementIsClickable(productTile);
        List<WebElement> productsWithDiscount = driver.findElements(this.productsWithDiscount);
        List<WebElement> labelsOfDiscount = driver.findElements(this.labelOfDiscount);
        List<WebElement> regularPriceOfDiscountProducts = driver.findElements(this.regularPriceOfDiscountProducts);
        List<WebElement> actualPriceOfDiscountProducts = driver.findElements(this.actualPriceOfDiscountProducts);
        DecimalFormat f = new DecimalFormat("##.00");
        BaseTest.log("Verifying the discount calculation ...");
        for (int i = 0; i < productsWithDiscount.size(); i++) {
            int discountPercentage = Integer.parseInt(labelsOfDiscount.get(i).getText().replace("%", ""));
            float regularPrice = Float.parseFloat(regularPriceOfDiscountProducts.get(i).getText().substring(0, priceLength).replace(",", "."));
            float actualPrice = Float.parseFloat(actualPriceOfDiscountProducts.get(i).getText().substring(0, priceLength).replace(",", "."));
            float discountAmount = Float.parseFloat(f.format(((regularPrice * (Math.abs(discountPercentage))) / 100)).replace(",", "."));
            if ((regularPrice - discountAmount) != actualPrice) {
                System.out.println(productsWithDiscount.get(i).getTagName() + " product has " + labelsOfDiscount.get(i).getText() + " . Actual price from " + regularPrice + " is " + actualPrice + " Failed.");
                return false;
            }
            BaseTest.log("Product #" + (i + 1) + " has " + labelsOfDiscount.get(i).getText() + "($" + discountAmount + ")" + " discount amount. Actual price from " + regularPrice + " is " + actualPrice + " Verified.");
        }
        return true;

    }

    public void changeSortingMethod(SortingMethod sortingMethod) {
        waitUntilElementIsClickable(this.sortingMethodsField);
        driver.findElement(this.sortingMethodsField).click();
        for (WebElement value : driver.findElements(sortingMethods)) {
            if (value.getText().trim().equals(sortingMethod.getDescription())) {
                waitUntilElementIsClickable(sortingMethods);
                value.click();
            }
        }

    }
}
