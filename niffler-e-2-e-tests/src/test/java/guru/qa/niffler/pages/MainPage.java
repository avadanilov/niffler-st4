package guru.qa.niffler.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    @Getter
    private final SelenideElement spendingTable = $(".spendings-table tbody");
    private final String spendingTableRowLocator = "tr";
    private final String spendingTableColumnLocator = "td";
    @Getter
    private final ElementsCollection spendingTableRows = spendingTable.$$(spendingTableRowLocator);
    @Getter
    private final SelenideElement deleteSpendingButton = $(byText("Delete selected"));

    public SelenideElement getSpendingRowByDescription(String description) {
        return spendingTable
                .$$(spendingTableRowLocator)
                .find(text(description))
                .$$(spendingTableColumnLocator)
                .first();
    }

}
