package page;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.function.Function;

import static java.util.concurrent.TimeUnit.SECONDS;

public class ProductListPage extends AbstractPage {
    private final Logger logger = LogManager.getRootLogger();
    private final String BASE_URL = "https://www.underarmour.com/en-us/c/mens/clothing/bottoms/";

    @FindBy(xpath = "//div[@data-target='#refinement-color']")
    private WebElement colorToogle;

    @FindBy(xpath = "//img[@alt='BY']")
    private WebElement closeModalButton;

    private final By imageOfFilteredProductLocator = By.xpath("//img[@class='b-tile-image b-tile-hover_image js-tile-hover_image'][@alt=\"Men's Project Rock Charged Cotton® Fleece Shorts\"]");
    private By pinkColorButtonLocator = By.xpath("//a[@data-analytics-plp-filter-value='Pink']");

    public ProductListPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(this.driver,this);
    }

    @Override
    public ProductListPage openPage() {
        driver.navigate().to(BASE_URL);
        closeModalButton.click();
        return this;
    }

    public ProductListPage filterByPinkColor() throws InterruptedException {
        colorToogle.click();
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();"
                ,colorToogle);
        new WebDriverWait(driver,WAIT_TIMEOUT_SECONDS).until(ExpectedConditions.elementToBeClickable(pinkColorButtonLocator)).click();
        logger.info("Products were filtered");
        return this;
    }

    public String getColorOfFilteredProduct(){
        WebElement imageOfFilteredProduct = wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return driver.findElement(imageOfFilteredProductLocator);
            }
        });
        return imageOfFilteredProduct.getAttribute("Title");
    }

    Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
            .withTimeout(30,SECONDS)
            .pollingEvery(5, SECONDS)
            .ignoring(NoSuchElementException.class);

}
