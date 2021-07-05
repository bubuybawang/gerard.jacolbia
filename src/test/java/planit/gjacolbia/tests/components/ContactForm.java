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

@Log4j2
public class ContactForm extends LoadableComponent<ContactForm> {
    public enum Field {
        FORENAME,
        SURNAME,
        EMAIL,
        TELEPHONE,
        MESSAGE
    }

    enum FieldType {
        CONTROL_GROUP("ControlGroup"),
        FIELD("Field"),
        ERROR_MESSAGE("ErrorMessage");

        String text;

        FieldType(String textValue) {
            text = textValue;
        }
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
        var controlGroupElement = getWebElementReflection(field, FieldType.CONTROL_GROUP);
        var formFieldElement = getWebElementReflection(field, FieldType.FIELD);
        return controlGroupElement.getAttribute("class").contains("error") && formFieldElement.getAttribute("class").contains("ng-invalid");
    }

    public String getFieldErrorMessage(Field field) {
        if (isFieldInvalid(field)) {
            WebElement errorMessageElement = getWebElementReflection(field, FieldType.ERROR_MESSAGE);
            return errorMessageElement.getText();
        }
        return "";
    }

    private WebElement getWebElementReflection(Field formField, FieldType formFieldType) {
        var classFields = ContactForm.class.getDeclaredFields();
        String webElementName = formField.name().toLowerCase() + formFieldType.text;
        return Arrays.stream(classFields).filter(classField -> classField.getName().equals(webElementName)).map(this::mapToWebElement).findFirst().orElseThrow(() -> new Error("WebElement field with name " + webElementName + " not found in class."));
    }

    private WebElement mapToWebElement(java.lang.reflect.Field classField) {
        try {
            return (WebElement) classField.get(this);
        } catch (IllegalAccessException e) {
            throw new Error("classField should be of type WebElement before being passed to Mapper. Ensure that this has been filtered.");
        }
    }

    public void fill(Field field, String text) {
        WebElement formFieldElement = getWebElementReflection(field, FieldType.FIELD);
        formFieldElement.click();
        formFieldElement.clear();
        formFieldElement.sendKeys(text);
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
