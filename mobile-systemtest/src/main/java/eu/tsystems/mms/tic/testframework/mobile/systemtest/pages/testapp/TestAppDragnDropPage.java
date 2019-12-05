package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp;

import eu.tsystems.mms.tic.testframework.mobile.By;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.NativeMobileGuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.Check;
import org.testng.Assert;

/**
 * Created by nkfa on 31.01.2017.
 */
public abstract class TestAppDragnDropPage extends AbstractTestAppPage {

    @Check
    private final NativeMobileGuiElement mapImage = new NativeMobileGuiElement(By.id("mapImage"));
    private final NativeMobileGuiElement mailImage = new NativeMobileGuiElement(By.id("mailImage"));
    private final NativeMobileGuiElement reminderImage = new NativeMobileGuiElement(By.id("reminderImage"));
    protected NativeMobileGuiElement resetButton;

    public void verifyDragnDrop() {

        NativeMobileGuiElement[] icons = new NativeMobileGuiElement[3];
        int x[] = new int[3];
        int y[] = new int[3];
        icons[0] = mapImage;
        icons[1] = mailImage;
        icons[2] = reminderImage;

        for (int i = 0; i < 3; i++) {
            x[i] = icons[i].getLocation().getX();
            y[i] = icons[i].getLocation().getY();
        }

        icons[0].drag(300, 300);
        Assert.assertTrue(x[0] < icons[0].getLocation().getX() && y[0] < icons[0].getLocation().getY(), "Map Icon can be dragged");
        icons[1].drag(0, 300);
        Assert.assertTrue(y[1] < icons[1].getLocation().getY(), "Mail Icon can be dragged");
        icons[2].drag(300, 0);
        Assert.assertTrue(x[2] < icons[2].getLocation().getX(), "Reminder Icon can be dragged");
    }

    public void resetActivity() {

        resetButton.click();
    }

}
