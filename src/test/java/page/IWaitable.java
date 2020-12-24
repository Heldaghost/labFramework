package page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

interface IWaitable {
      default WebElement waitForElementLocatedBy(WebDriver driver, By by){
          return new WebDriverWait(driver,3)
                .until(ExpectedConditions.presenceOfElementLocated(by));
    }
}
