package test;
import org.testng.Assert;
import org.testng.annotations.Test;
import util.AssertAccumulator;
import page.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ProductTests extends CommonConditions {

    @Test
    public void addProductToCart() {
        String expectedProduct = new ProductPage(driver)
                .openPage()
                .closeAdds()
                .selectSize(8)
                .addToBag()
                .goToCartPage()
                .getNameOfProductInCart();
        Assert.assertEquals(expectedProduct,"Boys' UA Showdown Pants");
    }

   @Test
    public void addManyProductsToCart() {
       String expectedQuantity = new ProductPage(driver)
                .openPage()
                .closeAdds()
                .selectSize(8)
                .selectQuantity()
                .addToBag()
                .goToCartPage()
                .getQuantityOfProduct();
        Assert.assertEquals(expectedQuantity,"10");
    }

    @Test
    public void addToWishList() {
        String expectedName = new ProductPage(driver)
                .openPage()
                .closeAdds()
                .addToWishList()
                .openWishListPage()
                .getTextFromCard();
        Assert.assertEquals(expectedName,"Boys' UA Showdown Pants");
    }

    @Test
    public void emptyCart(){
        String expectedMessage = new CartPage(driver)
                .openPage()
                .getEmptyCartMessage();
        assertThat(expectedMessage, is(equalTo("You have no items in your bag.")));
    }

    @Test
    public void changeColorOfProductInCart() {
        boolean isColorGray = new ProductPage(driver)
                .openPage()
                .closeAdds()
                .selectSize(8)
                .addToBag()
                .goToCartPage()
                .closeBanner()
                .changeColorOfProductInCart()
                .isColorGray();
        assertThat(isColorGray, is(equalTo(true)));
    }

    @Test
    public void freeShippingTest()
    {
        AssertAccumulator assertAccumulator = new AssertAccumulator();
        CartPage cartPage = new ProductPage(driver)
                .openPage()
                .changeSiteCountry()
                .selectSize(8)
                .addToBag()
                .goToCartPage();
        String expectedPrice = cartPage.getShippingCost();
        int expectedSubtotalCost = cartPage.getSubtotalCost();
        assertAccumulator.accumulate(()->assertThat(expectedPrice, is(equalTo("Free"))));
        assertAccumulator.accumulate(()->assertThat(expectedSubtotalCost,is(greaterThanOrEqualTo(60))));
        assertAccumulator.release();
    }

    @Test
    public void colorFilterTest() throws InterruptedException {
        String expectedColorOfProduct = new ProductListPage(driver).openPage()
                .filterByPinkColor()
                .getColorOfFilteredProduct();
        assertThat(expectedColorOfProduct,is(equalTo("Men's Project Rock Charged Cotton® Fleece Shorts, Pink")));
    }

    @Test
    public void enterWrongPromoTest(){
        String expectedErrorPromoCodeMessage = new ProductPage(driver)
                .openPage()
                .changeSiteCountry()
                .selectSize(8)
                .addToBag()
                .goToCartPage()
                .enterPromoCode()
                .getPromoCodeErrorMessage();
        assertThat(expectedErrorPromoCodeMessage,is(equalTo("Promo code cannot be added to your bag")));
    }

}
