package page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class WishlistPage extends AbstractPage implements IWaitable{
    private final Logger logger = LogManager.getRootLogger();
    private final String BASE_URL = "https://www.underarmour.com/en-us/saved-items";


    private By productElementInWishListLocator = By.xpath("//a[@class='b-tile-name']");

    public WishlistPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(this.driver,this);
    }

    @Override
    protected AbstractPage openPage() {
        driver.navigate().to(BASE_URL);
        return null;
    }

    public List<String> getProductsInWishList(){
        try {
            List<WebElement> productElementsList = driver.findElements(productElementInWishListLocator);
            List<String> productsNameList = new ArrayList<String>();
            for (WebElement productElement : productElementsList) {
                productsNameList.add(productElement.getText());
            }
            logger.info("Products from wish list found");
            return productsNameList;
        }catch (Exception e) {
            logger.info("Cart is empty");
            return new ArrayList<>();
        }
    }
}
