package page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WishlistPage extends AbstractPage implements IWaitable{
    private final Logger logger = LogManager.getRootLogger();
    private final String BASE_URL = "https://www.underarmour.com/en-us/saved-items";


    private By pantsCardInWishListLocator = By.xpath("//a[@class='b-tile-name']");

    public WishlistPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(this.driver,this);
    }

    @Override
    protected AbstractPage openPage() {
        driver.navigate().to(BASE_URL);
        return null;
    }

    public String getTextFromCard() {
        return waitForElementLocatedBy(driver,pantsCardInWishListLocator)
                .getText();
    }
}
