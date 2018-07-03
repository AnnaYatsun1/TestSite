package testcases;

import org.testng.annotations.*;
import pages.BasePage;
import pages.HomePage;
import pages.SearchResultsPage;
import properties.BaseTest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TrialTestCase extends BaseTest {



    @Test
    public void trialTestCase() {
        HomePage homePage = new HomePage();
        homePage.changeCurrencyType(BasePage.Currency.EUR);
        assertTrue(homePage.checkCurrency(BasePage.Currency.EUR), "The currency type of the products doesn't match the selected EUR currency in header");
        homePage.changeCurrencyType(BasePage.Currency.USD);
        assertTrue(homePage.checkCurrency(BasePage.Currency.USD), "The currency type of the products doesn't match the selected USD currency in header");
        homePage.changeCurrencyType(BasePage.Currency.UAH);
        assertTrue(homePage.checkCurrency(BasePage.Currency.UAH), "The currency type of the products doesn't match the selected UAH currency in header");
        homePage.changeCurrencyType(BasePage.Currency.USD);
        SearchResultsPage searchResultsPage = homePage.searchProduct("dress");
        BaseTest.log("Verifying that the \"Товаров\" label is displayed and shows the correct quantity of products ...");
        assertEquals(searchResultsPage.getFoundProductsLabel(), "Товаров: " + searchResultsPage.getAllFoundProductsSize() + ".");
        BaseTest.log("The label: \"Товаров: " + searchResultsPage.getAllFoundProductsSize() + "\" is displayed and correct. Verified.");
        assertTrue(searchResultsPage.checkTheCurrencyOfFoundProducts(BasePage.Currency.USD), "The currency type of one or more of the found products doesn't match the selected USD currency in header");
        searchResultsPage.changeSortingMethod(BasePage.SortingMethod.PRICE_FROM_HIGH_TO_LOW);
        assertTrue(searchResultsPage.checkHighToLowSorting(), "The products are not sorted correctly");
        assertTrue(searchResultsPage.checkDiscountProductPriceAndLabel(), "Some of the products doesn't have discount label, actual or regular price.");
        assertTrue(searchResultsPage.checkDiscountCalculation(), "Some of the products has incorrect discount calculation");
    }
}
