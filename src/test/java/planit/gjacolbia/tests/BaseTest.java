package planit.gjacolbia.tests;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import planit.gjacolbia.framework.DriverManager;
import planit.gjacolbia.framework.helpers.PageHelper;

import java.io.IOException;

public abstract class BaseTest {

    @BeforeMethod
    public void setUp() {
        DriverManager.init();
    }

    @AfterMethod
    public void tearDown() throws IOException {
        PageHelper.takeScreenshot(getDriver());
        getDriver().quit();
        DriverManager.reset();
    }

    protected WebDriver getDriver() {
        return DriverManager.getWebDriver();
    }

}
