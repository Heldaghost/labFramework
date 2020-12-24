package page;

import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MainPage extends AbstractPage implements IWaitable {
    private final Logger logger = LogManager.getRootLogger();
    private final String BASE_URL = "https://www.underarmour.com/en-us/";

    @FindBy(xpath = "//img[@alt='US']")
    private WebElement changeCountryButton;

    private By buttonMyAccount = By.xpath("//a[@aria-label='My Account']");
    private By buttonAccountToogle = By.xpath("//button[@class='b-header_utility-toggle collapsed'][@aria-label='My Account']");
    private By buttonRegisterLocator = By.xpath("//a[@class='b-header_account-link js-register']");
    private By buttonLogInLocator = By.xpath("//a[@class='b-header_account-link js-login']");
    private By inputLogInEmailLocator = By.xpath("//*[@id='login-form-email']");
    private By inputLogInPasswordLocator = By.xpath("//*[@id='login-form-password']");
    private By inputRegistrationEmailLocator = By.xpath("//*[@id='registration-form-email']");
    private By inputRegistrationPasswordLocator = By.xpath("//*[@id='registration-form-password']");
    private By registerErrorMessageLocator = By.xpath("//span[@class='b-registration-error-span']");
    private By registerButtonLocator = By.xpath("//button[@name='dwfrm_profile_create']");
    private By logInButtonLocator = By.xpath("//button[@class='btn btn-block g-button_primary--black g-button_base b-checkout-login_button js-login-button']");

    public MainPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(this.driver,this);
    }

    @Override
    public MainPage openPage() {
        driver.navigate().to(BASE_URL);
        return this;
    }

    public MainPage changeSiteCountry(){
        changeCountryButton.click();
        logger.info("Country has been changed");
        return this;
    }

    public MainPage openRegisterModalWindow(){
        waitForElementLocatedBy(driver,buttonRegisterLocator)
                .click();
        logger.info("Register window opened");
        return this;
    }

    public MainPage openLogInModalWindow(){
        waitForElementLocatedBy(driver,buttonLogInLocator)
                .click();
        return this;
    }

    public MainPage registerUser(User user){
        waitForElementLocatedBy(driver,inputRegistrationEmailLocator)
                .sendKeys(user.getEmail());
        waitForElementLocatedBy(driver,inputRegistrationPasswordLocator)
                .sendKeys(user.getPassword());
        waitForElementLocatedBy(driver,registerButtonLocator)
                .click();
        logger.info("Registration performed");
        return this;
    }

    public String getRegisterMessage(){
       return new WebDriverWait(driver,WAIT_TIMEOUT_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(registerErrorMessageLocator))
                    .getText();
    }

    public MainPage logIn(User user){
        waitForElementLocatedBy(driver,inputLogInEmailLocator)
                .sendKeys(user.getEmail());
        waitForElementLocatedBy(driver,inputLogInPasswordLocator)
                .sendKeys(user.getPassword());
        waitForElementLocatedBy(driver,logInButtonLocator)
                .click();
        logger.info("Login performed");
        return this;
    }

    public ProfilePage goToProfilePage() {
        waitForElementLocatedBy(driver,buttonAccountToogle)
                .click();
        waitForElementLocatedBy(driver,buttonMyAccount)
                .click();
        return new ProfilePage(driver);
    }

}
