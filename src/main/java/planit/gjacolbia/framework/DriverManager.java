package planit.gjacolbia.framework;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import planit.gjacolbia.framework.configuration.Browser;
import planit.gjacolbia.framework.configuration.Configuration;
import planit.gjacolbia.framework.configuration.RunMode;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

@Log4j2
public final class DriverManager {

    private static final ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();
    private static final RunMode RUN_MODE = RunMode.valueOf(Configuration.get("runMode").toUpperCase());
    private static final Browser BROWSER = Browser.valueOf(Configuration.get("browser").toUpperCase());

    private DriverManager() {
    }

    public static void init() {
        switch (BROWSER) {
            case FIREFOX:
                var firefoxOptions = new FirefoxOptions();
                setThreadLocalDriver(firefoxOptions, RUN_MODE);
                break;
            case EDGE:
                var edgeOptions = new EdgeOptions();
                setThreadLocalDriver(edgeOptions, RUN_MODE);
                break;
            case OPERA:
                var operaOptions = new OperaOptions();
                setThreadLocalDriver(operaOptions, RUN_MODE);
                break;
            default:
                var chromeOptions = new ChromeOptions();
                setThreadLocalDriver(chromeOptions, RUN_MODE);
        }
    }

    private static void setThreadLocalDriver(Capabilities capabilities, RunMode runMode) {
        if (runMode == RunMode.LOCAL) {
            setThreadLocalDriverLocalRunMode(capabilities);
        } else if (runMode == RunMode.GRID) {
            setThreadLocalDriverGridRunMode(capabilities);
        }
    }

    private static void setThreadLocalDriverLocalRunMode(Capabilities capabilities) {
        switch (BROWSER) {
            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                threadLocalDriver.set(new FirefoxDriver((FirefoxOptions) capabilities));
                break;
            case EDGE:
                WebDriverManager.edgedriver().setup();
                threadLocalDriver.set(new EdgeDriver((EdgeOptions) capabilities));
                break;
            case OPERA:
                WebDriverManager.operadriver().setup();
                threadLocalDriver.set(new OperaDriver((OperaOptions) capabilities));
                break;
            default:
                WebDriverManager.chromedriver().setup();
                threadLocalDriver.set(new ChromeDriver((ChromeOptions) capabilities));
        }
    }

    @SneakyThrows
    public static void setThreadLocalDriverGridRunMode(Capabilities capabilities) {
        URL serverUrl;
        String gridUrl = Configuration.get("gridUrl");
        // NOTE: As per selenoid, there is a bug in operadriver hence the below is needed when using selenoid.
        if (capabilities.getBrowserName().contains("opera")) {
            var operaDesiredCapability = new DesiredCapabilities();
            operaDesiredCapability.setCapability("browserName", "opera");
            ((OperaOptions) capabilities).setBinary("/usr/bin/opera");
            capabilities.merge(operaDesiredCapability);
        }
        try {
            serverUrl = new URL(gridUrl);
        } catch (MalformedURLException e) {
            log.error(String.format("Unable to create a URL from gridUrl config with value: %s", gridUrl));
            throw e;
        }
        threadLocalDriver.set(new RemoteWebDriver(serverUrl, capabilities));
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
