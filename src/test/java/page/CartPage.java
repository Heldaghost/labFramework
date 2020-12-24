package page;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Function;

import static java.util.concurrent.TimeUnit.SECONDS;

public class CartPage extends AbstractPage implements IWaitable{
    private final Logger logger = LogManager.getRootLogger();
    private final String BASE_URL = "https://www.underarmour.com/en-us/cart";

    @FindBy(xpath = "//button[@class='btn btn-primary btn-block promo-code-btn t-apply_code bfx-coupon-form-submit']")
    private WebElement applyPromoCodeButton;

    @FindBy(xpath = "//*[@id='couponCode']")
    private WebElement inputPromoCode;

    @FindBy(xpath = "//div[@data-name=\"Boys' UA Showdown Pants\"]//option[@selected]")
    private WebElement quantityOfProductInCart;

    @FindBy(xpath = "//div[@class='b-cart_empty_basket_outer']/p")
    private WebElement emptyCartMessage;

    @FindBy(xpath ="//span[@class='text-right shipping-cost bfx-price bfx-total-shipping']")
    private WebElement shippingCostField;

    @FindBy(xpath="//span[@class='text-right sub-total bfx-price bfx-total-subtotal']")
    private WebElement subtotalCostField;

    @FindBy(xpath = "//div[@class='b-cartlineitem_attributes']/p[2]")
    private WebElement colorNameField;

    @FindBy(xpath = "//a[@class='b-lineitem_itemname bfx-product-name']")
    private WebElement productNameField;

    private final By closeBannerButtonLocator = By.xpath("//div[@class='g-email-pop-modal-close g-modal-close-button']");
    private final By promoCodeErrorMessageLocator = By.xpath("//*[@id='invalidCouponCode']");
    private final By greyColorButtonLocator = By.xpath("//a[@alt='Gray']");
    private final By addToBagButtonLocator = By.xpath("//button[@data-addto-bag]");
    private final By editButtonLocator = By.xpath("//a[@data-cmp='editBasketProduct']");


    public CartPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(this.driver,this);
    }

    @Override
    public CartPage openPage() {
        driver.navigate().to(BASE_URL);
        return this;
    }

    public CartPage closeBanner() {
        new WebDriverWait(driver,WAIT_TIMEOUT_SECONDS).until(ExpectedConditions.elementToBeClickable(closeBannerButtonLocator))
                .click();
        return this;
    }

    public String getQuantityOfProduct() {
        return quantityOfProductInCart.getText();
    }

    public String getEmptyCartMessage(){
        return emptyCartMessage.getText();
    }

    public CartPage changeColorOfProductInCart(){
        new WebDriverWait(driver,WAIT_TIMEOUT_SECONDS).until(ExpectedConditions.elementToBeClickable(editButtonLocator)).click();
        WebElement changeColorToGrayButton = wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return driver.findElement(greyColorButtonLocator);
            }
        });
        changeColorToGrayButton.click();
        waitForElementLocatedBy(driver,addToBagButtonLocator)
                .click();
        logger.info("Color has been changed!");
        return this;
    }


    public boolean isColorGray(){
        return new WebDriverWait(driver,WAIT_TIMEOUT_SECONDS)
                .until(ExpectedConditions.textToBePresentInElement(colorNameField, "Color: Gray"));
    }

    public String getShippingCost(){
        return shippingCostField.getText();
    }

    public int getSubtotalCost(){
        int result = (int)Float.parseFloat(subtotalCostField.getText().substring(1));
        return result;
    }

    public CartPage enterPromoCode(){
        inputPromoCode.sendKeys("testdata.promocode");
        applyPromoCodeButton.click();
        logger.info("Promocode entered!");
        return this;
    }

    public String getPromoCodeErrorMessage(){
        return new WebDriverWait(driver,WAIT_TIMEOUT_SECONDS)
                .until(ExpectedConditions.visibilityOfElementLocated(promoCodeErrorMessageLocator))
                .getText();
    }

    public String getNameOfProductInCart(){
        return productNameField.getText();
    }

    Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
            .withTimeout(30,SECONDS)
            .pollingEvery(5, SECONDS)
            .ignoring(NoSuchElementException.class);
}
