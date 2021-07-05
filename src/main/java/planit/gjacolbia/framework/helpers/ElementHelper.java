package planit.gjacolbia.framework.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public final class ElementHelper {
    private ElementHelper() {}

    public static boolean isElementByLocationPresent(WebDriver driver, By location) {
        return !driver.findElements(location).isEmpty();
    }
}
