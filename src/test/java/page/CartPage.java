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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.util.concurrent.TimeUnit.SECONDS;

public class CartPage extends AbstractPage implements IWaitable{
    private final Logger logger = LogManager.getRootLogger();
    private final String BASE_URL = "https://www.underarmour.com/en-us/cart";

    Wait<WebDriver> wait;
    
    @FindBy(xpath = "//button[@class='btn btn-primary btn-block promo-code-btn t-apply_code bfx-coupon-form-submit']")
    private WebElement applyPromoCodeButton;

    @FindBy(xpath = "//*[@id='couponCode']")
    private WebElement inputPromoCode;

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

    @FindBy(xpath = "//div[@class='g-email-pop-modal-close g-modal-close-button']")
    private WebElement closeBannerButton;

    private final By editButtonLocator = By.xpath("//a[@data-cmp='editBasketProduct']");
    private final By quantityOfProductInCartLocator = By.xpath("//select[@class='b-quantity-select custom-select form-control js-quantity-select bfx-product-qty']/option[@selected]");
    private final By promoCodeErrorMessageLocator = By.xpath("//*[@id='invalidCouponCode']");
    private final By addToBagButtonLocator = By.xpath("//button[@data-addto-bag]");
    private final By productElementInCartLocator = By.xpath("//div[@data-cmp='cartTile'][@data-name]");
    private String colorComponentOfChangeColorButton = "//div[@class='b-product_attribute m-color']//a[@alt='%s']";

    public CartPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(this.driver,this);
        wait = new FluentWait<WebDriver>(driver)
                .withTimeout(30,SECONDS)
                .pollingEvery(5, SECONDS)
                .ignoring(NoSuchElementException.class);
    }

    @Override
    public CartPage openPage() {
        driver.navigate().to(BASE_URL);
        logger.info("Cart page opened");
        return this;
    }

    private List<WebElement> getProductElementsInCart(){
        try {
            return driver.findElements(productElementInCartLocator);
        }catch (Exception e) {
            logger.info("Cart is empty");
            return new ArrayList<>();
        }
    }

    public List<String> getNamesOfProductsInCart(){
        List<String> productsNameList = new ArrayList<String>();
        if(getProductElementsInCart().size()!=0) {
            for (WebElement productElement : getProductElementsInCart()) {
                productsNameList.add(productElement.getAttribute("data-name"));
            }
            logger.info("Product names list was formed");
            return productsNameList;
        }
        else{
            return new ArrayList<>();
        }
    }

    public CartPage closeBanner() {
       closeBannerButton.click();
       logger.info("Banner closed");
        return this;
    }

    public String getEmptyCartMessage(){
        return emptyCartMessage.getText();
    }

    public CartPage editProductItemInCart(){
        waitForElementLocatedBy(driver,editButtonLocator).click();
        logger.info("Edit product button clicked");
        return this;
    }

    public boolean changeColorOfProduct(String color){
        By changeColorButtonLocator = By.xpath(String.format(colorComponentOfChangeColorButton,color));
        WebElement changeColorButton = wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return driver.findElement(changeColorButtonLocator);
            }
        });
        changeColorButton.click();
        new WebDriverWait(driver,WAIT_TIMEOUT_SECONDS).until(ExpectedConditions.elementToBeClickable(addToBagButtonLocator))
                .click();
        logger.info("Color has been changed!");
        return new WebDriverWait(driver,WAIT_TIMEOUT_SECONDS)
                .until(ExpectedConditions.textToBePresentInElement(colorNameField, String.format("Color: %s",color)));
    }

    public String getQuantityOfProductInCart(){
        if(getProductElementsInCart().size() == 1) {
            return getProductElementsInCart().get(0).findElement(quantityOfProductInCartLocator).getText();
        }
        else return null;
    }

    public String getShippingCost(){
        logger.info("Shipping cost was updated");
        return shippingCostField.getText();
    }

    public int getSubtotalCost(){
        logger.info("Subtotal cost was updated");
        return (int)Float.parseFloat(subtotalCostField.getText().substring(1));
    }

    public CartPage enterPromoCode(String promoCode){
        inputPromoCode.sendKeys(promoCode);
        applyPromoCodeButton.click();
        logger.info("Promocode entered!");
        return this;
    }

    public String getPromoCodeErrorMessage(){
        logger.info("Promo code was updated");
        return new WebDriverWait(driver,WAIT_TIMEOUT_SECONDS)
                .until(ExpectedConditions.visibilityOfElementLocated(promoCodeErrorMessageLocator))
                .getText();
    }
}
