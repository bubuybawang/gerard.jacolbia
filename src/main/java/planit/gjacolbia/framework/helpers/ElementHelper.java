package planit.gjacolbia.framework.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public final class ElementHelper {
    private ElementHelper() {
    }

    /**
     * Returns true if the element is present in the DOM and is visible.
     * @param driver WebDriver instance to use in finding the element
     * @param location By locator
     * @return Returns true if the element is present in the DOM and is visible.

     */
    public static boolean isElementByLocationPresent(WebDriver driver, By location) {
        try {
            return !driver.findElements(location).isEmpty() && driver.findElement(location).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Returns true if the element is present in the DOM and is visible.
     * @param element WebElement instance to check if present
     * @return Returns true if the element is present in the DOM and is visible.
     */
    public static boolean isElementPresent(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
