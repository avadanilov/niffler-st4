package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.GenerateSpend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.pages.LoginPage;
import guru.qa.niffler.pages.MainPage;
import guru.qa.niffler.pages.WelcomePage;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.size;

public class SpendingTest extends BaseWebTest {

    static {
        Configuration.browserSize = "1980x1024";
    }

    @BeforeEach
    void doLogin() {
        Selenide.open("http://127.0.0.1:3000/main");
        new WelcomePage().getLoginButton().click();
        LoginPage loginPage = new LoginPage();
        loginPage.getUsernameInput().setValue("duck");
        loginPage.getPasswordInput().setValue("123456");
        loginPage.getSubmitButton().click();
    }

    @GenerateSpend(
            username = "duck",
            description = "QA.GURU Advanced 4",
            amount = 72500.00,
            category = "Обучение",
            currency = CurrencyValues.RUB
    )
    @Test
    void spendingShouldBeDeletedByButtonDeleteSpending(SpendJson spend) {
        MainPage mainPage = new MainPage();
        mainPage.getSpendingRowByDescription(spend.description())
                .scrollIntoView(true)
                .click();

        Allure.step("Delete spending", () -> mainPage
                .getDeleteSpendingButton()
                .click());

        Allure.step("Check that spending was deleted", () -> {
            mainPage.getSpendingTableRows()
                    .shouldHave(size(0));
        });
    }

}
