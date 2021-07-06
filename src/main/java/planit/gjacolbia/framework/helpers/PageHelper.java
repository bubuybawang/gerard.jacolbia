package planit.gjacolbia.framework.helpers;

import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebElement;

@Log4j2
public final class PageHelper {
    private PageHelper() {
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
