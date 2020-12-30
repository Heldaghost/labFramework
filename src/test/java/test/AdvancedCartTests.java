package test;
import org.testng.Assert;
import org.testng.annotations.Test;
import util.AssertAccumulator;
import page.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AdvancedCartTests extends CommonConditions {
    @Test
    public void freeShippingTest()
    {
        AssertAccumulator assertAccumulator = new AssertAccumulator();
        CartPage cartPage = new ProductPage(driver,"bottoms/mens-ua-showdown-golf-shorts/1309547.html")
                .openPage()
                .changeSiteCountryToUS()
                .selectSize(30)
                .addToBag()
                .goToCartPage();
        String expectedPrice = cartPage.getShippingCost();
        int expectedSubtotalCost = cartPage.getSubtotalCost();
        assertAccumulator.accumulate(()->assertThat(expectedPrice, is(equalTo("Free"))));
        assertAccumulator.accumulate(()->assertThat(expectedSubtotalCost,is(greaterThanOrEqualTo(60))));
        assertAccumulator.release();
    }

    @Test
    public void enterWrongPromoTest(){
        String expectedErrorPromoCodeMessage = new ProductPage(driver,"bottoms/mens-ua-showdown-golf-shorts/1309547.html")
                .openPage()
                .changeSiteCountryToUS()
                .selectSize(30)
                .addToBag()
                .goToCartPage()
                .enterPromoCode("PACKSACK")
                .getPromoCodeErrorMessage();
        assertThat(expectedErrorPromoCodeMessage,is(equalTo("Promo code cannot be added to your bag")));
    }
}
