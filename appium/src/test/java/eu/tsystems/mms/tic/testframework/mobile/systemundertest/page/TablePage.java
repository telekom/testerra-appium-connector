/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Eric Kubenka
 */

package eu.tsystems.mms.tic.testframework.mobile.systemundertest.page;

import eu.tsystems.mms.tic.testframework.pageobjects.Check;
import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.Locate;
import eu.tsystems.mms.tic.testframework.pageobjects.Page;
import eu.tsystems.mms.tic.testframework.pageobjects.factory.PageFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents https://the-internet.herokuapp.com/tables
 * <p>
 * Date: 14.05.2020
 * Time: 12:58
 *
 * @author Eric Kubenka
 */
public class TablePage extends Page {

    @Check
    private GuiElement tableOne = new GuiElement(this.getWebDriver(), Locate.by(By.id("table1")));

    private List<String> availableColumns = new LinkedList<>();

    public TablePage(WebDriver driver) {

        super(driver);
    }

    /**
     * @return
     */
    public List<String> getAvailAbleColumnNames(boolean forceUpdate) {

        if (availableColumns.isEmpty() || forceUpdate) {
            tableOne.getSubElement(Locate.by(By.className("header"))).getList().forEach(headerElement -> this.availableColumns.add(headerElement.getText()));
        }

        return availableColumns;
    }

    public TablePage doSortTableByColumn(String columnHeaderValue) {

        GuiElement tableOneHeader = tableOne.getSubElement(Locate.by(By.className("header")).filter(e -> e.getText().equalsIgnoreCase(columnHeaderValue)));

        tableOneHeader.scrollIntoView();
        tableOneHeader.click();
        return PageFactory.create(TablePage.class, this.getWebDriver());
    }

    public boolean isUserShown(String userLastName, String userFirstName) {

        int indexLastName = getIndexOfColumn("Last Name");
        int indexFirstName = getIndexOfColumn("First Name");
        final GuiElement filteredDataRow = tableOne.getSubElement(
                Locate.by(By.xpath(".//tr" +
                        "//td[" + indexLastName + "][text()='" + userLastName + "']/.." +
                        "//td[" + indexFirstName + "][text()='" + userFirstName + "']/..")));

        if (filteredDataRow.isPresent()) {
            filteredDataRow.scrollIntoView();
        }
        return filteredDataRow.isDisplayed();
    }

    private int getIndexOfColumn(final String column) {

        final List<String> availAbleColumnNames = this.getAvailAbleColumnNames(false);
        for (int i = 0; i < availAbleColumnNames.size(); i++) {
            if (availAbleColumnNames.get(i).equals(column)) {
                return i + 1;
            }
        }

        return 0;
    }
}
