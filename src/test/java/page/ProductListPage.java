package page;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;



public class ProductListPage extends AbstractPage {
    private final Logger logger = LogManager.getRootLogger();
    private final String BASE_URL = "https://www.underarmour.com/en-us/c/mens/clothing/bottoms/";

    @FindBy(xpath = "//div[@data-target='#refinement-color']")
    private WebElement colorToogle;

    @FindBy(xpath = "//img[@alt='BY']")
    private WebElement closeModalButton;

    private String colorFilterButtonLocator = "//a[@data-analytics-plp-filter-value='%s']";
    private final By filteredElementLocator = By.xpath("//div[@class='b-tile-info']");
    private String colorComponentOfFilteredElement = "//a[@alt='%s']";

    public ProductListPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(this.driver,this);
    }

    @Override
    public ProductListPage openPage() {
        driver.navigate().to(BASE_URL);
        closeModalButton.click();
        logger.info("Product list page opened. Adds closed");
        return this;
    }

    public boolean FilterByColor(String color){
        boolean isFiltered = false;
        colorToogle.click();
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();"
                ,colorToogle);
        new WebDriverWait(driver,WAIT_TIMEOUT_SECONDS).until(ExpectedConditions.elementToBeClickable(By.xpath(String.format(colorFilterButtonLocator,color))))
                .click();
        try {
            List<WebElement> filteredProductElementsList = driver.findElements(filteredElementLocator);
            for (WebElement productElement : filteredProductElementsList) {
                if(productElement.findElement(By.xpath(String.format(colorComponentOfFilteredElement,color)))!= null){
                    isFiltered = true;
                }
                else{
                    isFiltered=false;
                }
            }
            logger.info("Products were filtered");
            return isFiltered;
        }catch (Exception e) {
            logger.info("Error in filter");
            return isFiltered;
        }
    }
}
