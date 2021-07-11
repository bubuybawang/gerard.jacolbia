package planit.gjacolbia.tests.pages;

import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.SlowLoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import planit.gjacolbia.framework.configuration.Configuration;
import planit.gjacolbia.framework.helpers.ElementHelper;

import java.time.Clock;

@Log4j2
public class ContactFormSubmittedPage extends SlowLoadableComponent<ContactFormSubmittedPage> {
    WebDriver driver;

    @FindBy(xpath = "//div[contains(@class, 'popup modal')]")
    WebElement sendingFeedbackModal;
    @FindBy(xpath = "//div[@class='container-fluid']/div[@ui-if='contactValidSubmit']/div[contains(@class, 'alert-success')]")
    WebElement submitSuccessBanner;

    public ContactFormSubmittedPage(WebDriver driver) {
        super(Clock.systemDefaultZone(), Configuration.timeout() * 3);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getSuccessBannerText() {
        return submitSuccessBanner.getText();
    }

    @Override
    protected void load() {
        var wait = new WebDriverWait(driver, Configuration.timeout());
        try {
            wait.until(ExpectedConditions.invisibilityOf(sendingFeedbackModal));
        } catch (TimeoutException e) {
            log.info("Waited for " + Configuration.timeout() + "s and would check if page already loaded.");
        }
    }

    @Override
    protected void isLoaded() throws Error {
        By contactActiveButton = By.xpath("//li[@id='nav-contact' and @class='active']");
        Assert.assertTrue(ElementHelper.isElementByLocationPresent(driver, contactActiveButton), "Contact navbar button is not active.");
        Assert.assertFalse(driver.findElement(By.tagName("body")).getAttribute("class").contains("modal-open"), "Body page still has a modal opened.");
    }
}
