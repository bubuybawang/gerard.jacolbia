package planit.gjacolbia.framework.helpers;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import planit.gjacolbia.framework.Configuration;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

@Log4j2
public final class PageHelper {
    private PageHelper() {
    }

    public static void takeScreenshot(WebDriver driver) throws IOException {
        String filename = Configuration.get("workDir") + "Screenshot_" + Instant.now().getEpochSecond() + ".png";
        takeScreenshot(driver, filename);
    }

    public static void takeScreenshot(WebDriver driver, String destination) throws IOException {
        WebDriver augmentedDriver = new Augmenter().augment(driver);
        var srcFile = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
        var output = new File(destination);
        FileUtils.copyFile(srcFile, output);
    }

    public static WebElement getElementViaReflection(Object obj, String elementName) {
        // TODO Review as this breaks encapsulation of page objects. Might have to bring back to individual page objects instead
        try {
            var field = obj.getClass().getDeclaredField(elementName);
            field.setAccessible(true);
            if (field.getType().equals(WebElement.class)) {
                return (WebElement) field.get(obj);
            } else {
                var errorMessage = String.format("Provided elementName '%s' should be of type WebElement.", elementName);
                log.error(errorMessage);
                throw new IncorrectTypeForSupposedWebElementProperty(errorMessage);
            }
        } catch (NoSuchFieldException e) {
            var errorMessage = String.format("WebElement with name '%s' not found in %s", elementName, obj.getClass());
            log.error(errorMessage);
            throw new WebElementPropertyNotFound(errorMessage);
        } catch (IllegalAccessException e) {
            var errorMessage = String.format("WebElement property with name '%s' could not be accessed even when setAccesible is true", elementName);
            log.error(errorMessage);
            throw new WebElementPropertyNotFound(errorMessage);
        }
    }

    private static class WebElementPropertyNotFound extends Error {
        WebElementPropertyNotFound(String message) {
            super(message);
        }
    }

    private static class IncorrectTypeForSupposedWebElementProperty extends Error {
        IncorrectTypeForSupposedWebElementProperty(String message) {
            super(message);
        }
    }
}
