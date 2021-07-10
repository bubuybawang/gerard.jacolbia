package planit.gjacolbia.framework;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.opera.OperaDriver;

import java.util.Optional;

public final class DriverManager {

    private static final ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();

    private DriverManager() {
    }

    public static void init() {
        switch (Configuration.get("browser").toLowerCase()) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                threadLocalDriver.set(new FirefoxDriver());
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                threadLocalDriver.set(new EdgeDriver());
                break;
            case "opera":
                WebDriverManager.operadriver().setup();
                threadLocalDriver.set(new OperaDriver());
                break;
            default:
                WebDriverManager.chromedriver().setup();
                threadLocalDriver.set(new ChromeDriver());
        }
    }

    public static WebDriver getWebDriver() {
        Optional<WebDriver> driver = Optional.ofNullable(threadLocalDriver.get());
        if (driver.isPresent()) {
            return driver.get();
        } else {
            init();
            return threadLocalDriver.get();
        }
    }

    public static void reset() {
        threadLocalDriver.remove();
    }
}
