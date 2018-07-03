package properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;

import java.util.Arrays;

class EventHandler implements WebDriverEventListener {
    static StringBuilder sb = new StringBuilder();

    @Override
    public void beforeAlertAccept(WebDriver driver) {
        improvedEventLogs("Trying to accept Alert ...");
    }

    @Override
    public void afterAlertAccept(WebDriver driver) {
        improvedEventLogs("Alert is accepted.");
    }

    @Override
    public void afterAlertDismiss(WebDriver driver) {
        improvedEventLogs("Alert is canceled.");
    }

    @Override
    public void beforeAlertDismiss(WebDriver driver) {
        improvedEventLogs("Trying to cancel Alert ...");
    }

    @Override
    public void beforeNavigateTo(String url, WebDriver driver) {
        improvedEventLogs("Trying to open the " + url);
    }

    @Override
    public void afterNavigateTo(String url, WebDriver driver) {
        improvedEventLogs("Navigated to " + url);
    }

    @Override
    public void beforeNavigateBack(WebDriver driver) {
        improvedEventLogs("Trying to navigate back ...");
    }

    @Override
    public void afterNavigateBack(WebDriver driver) {
        improvedEventLogs("Navigated back.");
    }

    @Override
    public void beforeNavigateForward(WebDriver driver) {
        improvedEventLogs("Trying to navigate forward ...");
    }

    @Override
    public void afterNavigateForward(WebDriver driver) {
        improvedEventLogs("Navigated forward.");
    }

    @Override
    public void beforeNavigateRefresh(WebDriver driver) {
        improvedEventLogs("Trying to refresh the page ...");
    }

    @Override
    public void afterNavigateRefresh(WebDriver driver) {
        improvedEventLogs("The page is refreshed.");
    }

    @Override
    public void beforeFindBy(By by, WebElement element, WebDriver driver) {
        improvedEventLogs("Trying to find: " + readElement(element) + " by: " + by.toString()); // by.toString()
    }

    @Override
    public void afterFindBy(By by, WebElement element, WebDriver driver) {
        improvedEventLogs("Successfully found.");
    }

    @Override
    public void beforeClickOn(WebElement element, WebDriver driver) {
        improvedEventLogs("Trying to click on " + readElement(element)); // TODO: Сейчас выводит что-то вроде: Trying to click on [[FirefoxDriver: firefox on XP (44634ae0-3991-4952-b06d-c9f363a426a6)] -> xpath: //a[.='EUR €']] - done
    }

    @Override
    public void afterClickOn(WebElement element, WebDriver driver) {
        improvedEventLogs("Successfully clicked.");
    }

    @Override
    public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
        improvedEventLogs("Trying to change value of " + readElement(element) + " on " +
                Arrays.toString(keysToSend));
    }

    @Override
    public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
        improvedEventLogs("Successfully changed the value of " + readElement(element) + " on" + Arrays.toString(keysToSend)); // TODO: отформатируй вывод - done
    }

    @Override
    public void beforeScript(String script, WebDriver driver) {
        improvedEventLogs("Trying to execute script " + script);
    }

    @Override
    public void afterScript(String script, WebDriver driver) {
        improvedEventLogs("Successfully executed script " + script);
    }

    @Override
    public void onException(Throwable throwable, WebDriver driver) {
        improvedEventLogs("The next exception has been occurred " + throwable);
    }

    private void improvedEventLogs(String event) {
        System.out.println(event);
        sb.append(event).append("\n");
    }

    private String readElement(WebElement element) {
        if (element != null) {
            if (element.getText() != null) {
                return element.getText();
            } else if (element.getTagName() != null) {
                return element.getTagName();
            } else {
                return null;
            }
        } else {
            return null;
        }

    }
}
