package test;

import org.testng.annotations.Test;
import page.ProductListPage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class FilterTests extends CommonConditions {
    @Test
    public void colorFilterTest(){
        boolean isFiltered = new ProductListPage(driver).openPage()
                .FilterByColor("Navy");
        assertThat(isFiltered,is(equalTo(true)));
    }
}
