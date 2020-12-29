package test;
import org.testng.Assert;
import org.testng.annotations.Test;
import util.AssertAccumulator;
import page.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ProductTests extends CommonConditions {

    @Test
    public void addProductToCart() {
        AssertAccumulator assertAccumulator = new AssertAccumulator();
        List<String> products = new ProductPage(driver,"bottoms/mens-ua-showdown-golf-shorts/1309547.html")
                .openPage()
                .closeAdds()
                .selectSize(30)
                .addToBag()
                .goToCartPage()
                .getNamesOfProductsInCart();
        assertAccumulator.accumulate(()->assertThat(products.size(), is(equalTo(1))));
        assertAccumulator.accumulate(()->assertThat(products.get(0),is(equalTo("Men's UA Showdown Golf Shorts"))));
        assertAccumulator.release();
    }

   @Test
    public void addManyProductsToCart() {
       String expectedQuantity = new ProductPage(driver,"bottoms/mens-ua-showdown-golf-shorts/1309547.html")
                .openPage()
                .closeAdds()
                .selectSize(30)
                .selectQuantity("10")
                .addToBag()
                .goToCartPage()
                .getQuantityOfProductInCart();
        Assert.assertEquals(expectedQuantity,"10");
    }

    @Test
    public void addToWishList() {
        AssertAccumulator assertAccumulator = new AssertAccumulator();
        List<String> productsInWishList = new ProductPage(driver,"bottoms/boys-ua-showdown-pants/193444360967.html")
                .openPage()
                .closeAdds()
                .addToWishList()
                .openWishListPage()
                .getProductsInWishList();
        assertAccumulator.accumulate(()->assertThat(productsInWishList.size(),is(equalTo(1))));
        assertAccumulator.accumulate(()->assertThat(productsInWishList.get(0),is(equalTo("Boys' UA Showdown Pants"))));
    }

    @Test
    public void emptyCart(){
        AssertAccumulator assertAccumulator = new AssertAccumulator();
        CartPage cartPage= new CartPage(driver)
                .openPage();
        assertAccumulator.accumulate(()->assertThat(cartPage.getNamesOfProductsInCart().size(), is(equalTo(0))));
        assertAccumulator.accumulate(()->assertThat(cartPage.getEmptyCartMessage(),is(equalTo("You have no items in your bag."))));
        assertAccumulator.release();
    }

    @Test
    public void changeColorOfProductInCart() {
        boolean isColorChanged = new ProductPage(driver,"bottoms/boys-ua-showdown-pants/193444360967.html")
                .openPage()
                .closeAdds()
                .selectSize(8)
                .addToBag()
                .goToCartPage()
                .closeBanner()
                .editProductItemInCart()
                .changeColorOfProduct("Gray");
        assertThat(isColorChanged, is(equalTo(true)));
    }

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
    public void colorFilterTest(){
        boolean isFiltered = new ProductListPage(driver).openPage()
                .FilterByColor("Navy");
        assertThat(isFiltered,is(equalTo(true)));
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
