package page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProductPage extends AbstractPage implements IWaitable {
    private static String PRODUCT_URL;
    private final Logger logger = LogManager.getRootLogger();

    @FindBy(xpath = "//button[@class='close']")
    private WebElement closeBannerButton;

    @FindBy(xpath = "//span[@class='b-header_minicart-icon']")
    private WebElement goToCartButton;

    @FindBy(xpath = "//div[@class='b-product_attrs']//button[@data-addto-bag]")
    private WebElement addToBagButton;

    @FindBy(xpath = "//select[@id='quantity-1']")
    private WebElement quantityList;

    @FindBy(xpath = "//img[@alt='US']")
    private WebElement changeSiteCountryToUSButton;

    @FindBy(xpath = "//img[@alt='BY']")
    private WebElement closeModalButton;

    @FindBy(xpath = "//button[@class='b-product_name-fav_defultButton add-to-wish-list product']")
    private WebElement addToWishListButton;

    @FindBy(xpath = "//div[@class='b-header-wishlist b-wishlist']")
    private WebElement goToWishListButton;

    private final By selectedAddedToWishListButtonLocator = By.xpath("//div[@class='js-whislist-icon product-added b-product_name-fav_selectButton']");
    private String buttonSizeLocator = "//a[@data-size-attr=%s]";
    private String quantitySelectionLabelLocator = "//select[@id='quantity-1']/option[@value=%s]";
    private final By activeSizeButtonLocator = By.xpath("//a[@class='js-size-select selectable m-active selected']");
    private final By notEmptyCartButtonLocator = By.xpath("//span[@class='b-header_minicart-icon']/span[@style='display: block;']");

    public ProductPage(WebDriver driver, String partOfProductURL)
    {
        super(driver);
        PageFactory.initElements(this.driver,this);
        PRODUCT_URL = String.format("https://www.underarmour.com/en-us/p/%s", partOfProductURL);
    }

    @Override
    public ProductPage openPage() {
        driver.get(PRODUCT_URL);
        logger.info("Product page was opened");
        return this;
    }

    public ProductPage changeSiteCountryToUS(){
        changeSiteCountryToUSButton.click();
        logger.info("Site country changed to US");
        openPage();
        return this;
    }

    public ProductPage closeAdds() {
        closeModalButton.click();
        closeBannerButton.click();
        logger.info("Adds closed");
        return this;
    }
    public ProductPage addToWishList() {
        addToWishListButton.click();
        logger.info("Product added to wish list");
        return this;
    }

    public WishlistPage openWishListPage() {
        waitForElementLocatedBy(driver,selectedAddedToWishListButtonLocator);
        goToWishListButton.click();
        logger.info("Wish list page opened");
        return new WishlistPage(driver);
    }

    public ProductPage selectSize(int neededSize){
        new WebDriverWait(driver,WAIT_TIMEOUT_SECONDS).until(ExpectedConditions.elementToBeClickable(By.xpath(String.format(buttonSizeLocator,neededSize))))
                .click();
        logger.info("Size selected");
        return this;
    }

    public ProductPage selectQuantity(String quantity) {
        waitForElementLocatedBy(driver,activeSizeButtonLocator);
        quantityList.click();
        waitForElementLocatedBy(driver,By.xpath(String.format(quantitySelectionLabelLocator, quantity)))
                .click();
        logger.info("Quantity selected!");
        return this;
    }

    public ProductPage addToBag(){
        waitForElementLocatedBy(driver,activeSizeButtonLocator);
        addToBagButton.click();
        logger.info("Product added to bag");
        return this;
    }

    public CartPage goToCartPage(){
        waitForElementLocatedBy(driver,notEmptyCartButtonLocator);
        goToCartButton.click();
        logger.info("Cart page opened");
        return new CartPage(driver);
    }
}
