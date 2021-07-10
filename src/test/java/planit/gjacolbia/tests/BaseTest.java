package planit.gjacolbia.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import planit.gjacolbia.framework.Configuration;
import planit.gjacolbia.framework.helpers.PageHelper;

import java.io.IOException;

public abstract class BaseTest {
    // TODO Have a driver manager class and call it from here.
    private ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    @BeforeMethod
    public void setUp() {
        switch (Configuration.get("browser").toLowerCase()) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver.set(new FirefoxDriver());
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver.set(new EdgeDriver());
                break;
            case "opera":
                WebDriverManager.operadriver().setup();
                driver.set(new OperaDriver());
            default:
                WebDriverManager.chromedriver().setup();
                driver.set(new ChromeDriver());
        }
    }

    @AfterMethod
    public void tearDown() throws IOException {
        PageHelper.takeScreenshot(getDriver());
        getDriver().quit();
    }

    protected WebDriver getDriver() {
        return driver.get();
    }

}
