package planit.gjacolbia.tests.components;

import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import planit.gjacolbia.framework.Configuration;

import java.util.Arrays;
import java.util.Optional;

@Log4j2
public class ContactForm extends LoadableComponent<ContactForm> {
    public enum Field {
        FORENAME,
        SURNAME,
        EMAIL,
        TELEPHONE,
        MESSAGE
    }

    private static final By PAGE_LOADED_INDICATOR = By.xpath("//a[contains(@class, 'btn-contact') and contains(@ui-event,'onSubmit') and text()='Submit']");

    WebDriver driver;
    LoadableComponent<?> parent;

    @FindBy(xpath = "//a[contains(@class, 'btn-contact') and contains(@ui-event,'onSubmit') and text()='Submit']")
    WebElement submitButton;
    // Forename
    @FindBy(xpath = "//input[@id='forename']/ancestor::div[contains(@class, 'control-group')]")
    WebElement forenameControlGroup;
    @FindBy(id = "forename")
    WebElement forenameField;
    @FindBy(id = "forename-err")
    WebElement forenameErrorMessage;
    // Surname
    @FindBy(xpath = "//input[@id='surname']/ancestor::div[contains(@class, 'control-group')]")
    WebElement surnameControlGroup;
    @FindBy(id = "surname")
    WebElement surnameField;
    @FindBy(id = "surname-err")
    WebElement surnameErrorMessage;
    // Email
    @FindBy(xpath = "//input[@id='email']/ancestor::div[contains(@class, 'control-group')]")
    WebElement emailControlGroup;
    @FindBy(id = "email")
    WebElement emailField;
    @FindBy(id = "email-err")
    WebElement emailErrorMessage;
    // Telephone
    @FindBy(xpath = "//input[@id='telephone']/ancestor::div[contains(@class, 'control-group')]")
    WebElement telephoneControlGroup;
    @FindBy(id = "telephone")
    WebElement telephoneField;
    @FindBy(id = "telephone-err")
    WebElement telephoneErrorMessage;
    // Message
    @FindBy(xpath = "//textarea[@id='message']/ancestor::div[contains(@class, 'control-group')]")
    WebElement messageControlGroup;
    @FindBy(id = "message")
    WebElement messageField;
    @FindBy(id = "message-err")
    WebElement messageErrorMessage;


    public ContactForm(WebDriver driver, LoadableComponent<?> parent) {
        this.driver = driver;
        this.parent = parent;
        PageFactory.initElements(driver, this);
    }

    public boolean isFieldInvalid(Field field) {
        // TODO have a field getter, then throw if not found
        var classFields = ContactForm.class.getDeclaredFields();
        Optional<WebElement> controlGroupElement = Arrays.stream(classFields).filter(classField -> isControlGroupElement(classField, field)).map(this::mapToWebElement).findFirst();
        Optional<WebElement> formFieldElement = Arrays.stream(classFields).filter(classField -> isFormFieldElement(classField, field)).map(this::mapToWebElement).findFirst();

        if (controlGroupElement.isPresent() && formFieldElement.isPresent()) {
            return controlGroupElement.get().getAttribute("class").contains("error") && formFieldElement.get().getAttribute("class").contains("ng-invalid");
        } else {
            log.error(field + " does not have a ControlGroup element or a FieldElement in the page object.");
        }

        return false;
    }

    public String getFieldErrorMessage(Field field) {
        if (isFieldInvalid(field)) {
            // TODO have a field getter, then throw if not found
            var classFields = ContactForm.class.getDeclaredFields();
            Optional<WebElement> errorMessageElement = Arrays.stream(classFields).filter(classField -> isFormErrorElement(classField, field)).map(this::mapToWebElement).findFirst();
            if (errorMessageElement.isPresent()) {
                return errorMessageElement.get().getText();
            } else {
                log.error(field + " does not have a FieldElement in the page object.");
            }
        }
        return "";
    }

    private boolean isControlGroupElement(java.lang.reflect.Field classField, Field formField) {
        return isWebElementOfPrefixSuffix(classField, formField.name().toLowerCase(), "ControlGroup");
    }

    private boolean isFormFieldElement(java.lang.reflect.Field classField, Field formField) {
        return isWebElementOfPrefixSuffix(classField, formField.name().toLowerCase(), "Field");
    }

    private boolean isFormErrorElement(java.lang.reflect.Field classField, Field formField) {
        return isWebElementOfPrefixSuffix(classField, formField.name().toLowerCase(), "ErrorMessage");
    }

    private boolean isWebElementOfPrefixSuffix(java.lang.reflect.Field classField, String prefix, String suffix) {
        String classFieldName = classField.getName();
        return classField.getType().equals(WebElement.class)
                && classFieldName.startsWith(prefix)
                && classFieldName.endsWith(suffix);
    }

    private WebElement mapToWebElement(java.lang.reflect.Field classField) {
        try {
            return (WebElement) classField.get(this);
        } catch (IllegalAccessException e) {
            throw new Error("classField should be of type WebElement before being passed to Mapper. Ensure that this has been filtered.");
        }
    }

    public void fill(Field field, String text) {
        var classFields = ContactForm.class.getDeclaredFields();
        Optional<WebElement> formFieldElement = Arrays.stream(classFields).filter(classField -> isFormFieldElement(classField, field)).map(this::mapToWebElement).findFirst();
        if (formFieldElement.isPresent()) {
            formFieldElement.get().click();
            formFieldElement.get().clear();
            formFieldElement.get().sendKeys(text);
        } else {
            log.error(field + " does not have a FieldElement in the page object.");
        }
    }

    public void clickSubmit() {
        this.submitButton.click();
    }

    @Override
    protected void load() {
        parent.get();
        var wait = new WebDriverWait(driver, Configuration.timeout());
        wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_LOADED_INDICATOR));
    }

    @Override
    protected void isLoaded() throws Error {
        // TODO Use util for elementNotPresent
        Assert.assertFalse(driver.findElements(PAGE_LOADED_INDICATOR).isEmpty(), "Contact form is not loaded.");
    }
}
