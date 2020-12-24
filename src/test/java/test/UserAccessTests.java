package test;
import model.User;
import org.testng.annotations.Test;
import page.*;
import util.UserCreator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserAccessTests extends CommonConditions {
    @Test
    public void registerExistentUserTest(){
        User testUser = UserCreator.withCredentialsFromProperty();
        String expectedErrorMessage = new MainPage(driver)
                .openPage()
                .changeSiteCountry()
                .openRegisterModalWindow()
                .registerUser(testUser)
                .getRegisterMessage();
        assertThat(expectedErrorMessage,is(equalTo("Uh-oh, we've either hit a technical error or you might already have an account set up on UA.com, UA MapMyFitness, or UA MyFitnessPal using this email address. Try")));
    }

    @Test
    public void canLogInTest() {
        User testUser = UserCreator.withCredentialsFromProperty();
        String expectedUserEmail = new MainPage(driver)
                .openPage()
                .changeSiteCountry()
                .openLogInModalWindow()
                .logIn(testUser)
                .goToProfilePage()
                .getUserEmail();
        assertThat(expectedUserEmail,is(equalTo(testUser.getEmail())));
    }
}
