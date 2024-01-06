package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$;

@Getter
public class WelcomePage {
    private final SelenideElement loginButton = $("a[href*='redirect']");
    private final SelenideElement registerButton = $("a[href*='register']");
}
