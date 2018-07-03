package properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public abstract class BaseTest {
    private static EventFiringWebDriver driver;
    private final static short IMPLICIT_TIME_OUT = 10;


    @BeforeTest
    @Parameters("browser")
    public void setUp(String browser) {
        driver = getConfiguredDriver(browser);
        String homePageUrl = "http://prestashop-automation.qatestlab.com.ua/ru/";
        driver.get(homePageUrl);
    }

    @AfterTest
    @Parameters("browser")
    public void tearDown(String browser) {
        if (driver != null) {
            driver.quit();
        }
        writeLogs(browser);
    }

    public static void log(String message) {
        Reporter.log(message);
        System.out.println(message);
    }

    public static WebDriver getDriver() {
        return driver;
    }

    private void writeLogs(String browserName) {
        File logsFile = new File(browserName + "Logs.txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(logsFile))) {
            bw.write(EventHandler.sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        EventHandler.sb = new StringBuilder();
    }

    private static EventFiringWebDriver getConfiguredDriver(String browserName) {
        WebDriver driver = getDriver(browserName);
        driver.manage().window().maximize();
        EventFiringWebDriver eventFiringWebDriver = new EventFiringWebDriver(driver);
        eventFiringWebDriver.register(new EventHandler());
        eventFiringWebDriver.manage().timeouts().implicitlyWait(IMPLICIT_TIME_OUT, TimeUnit.SECONDS);
        return eventFiringWebDriver;
    }

    private static WebDriver getDriver(String browserName) {
        switch (browserName) {
            case "chrome":
                System.setProperty("webdriver.chrome.driver", new File(BaseTest.class.getResource("//E:/chromedriver.exe").getFile()).getPath());
                return new ChromeDriver();
            case "firefox":
                System.setProperty("webdriver.gecko.driver", new File(BaseTest.class.getResource("//E:/geckodriver.exe").getFile()).getPath());
                return new FirefoxDriver();
            case "ie":
                System.setProperty("webdriver.ie.driver", new File(BaseTest.class.getResource("//E:/IEDriverServer.exe").getFile()).getPath());
                return new InternetExplorerDriver();
            default:
                throw new IllegalArgumentException("Unsupported browser name: " + browserName);
        }
    }
}
