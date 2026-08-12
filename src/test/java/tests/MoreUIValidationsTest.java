package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

public class MoreUIValidationsTest extends TestBase {
    BrowserContext context;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        context = browser.newContext();
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));

        page = context.newPage();
        page.navigate("https://rahulshettyacademy.com/loginpagePractise/");
    }

    @AfterMethod
    public void tearDown() {
        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace.zip")));
    }

    @Test
    public void ChildWindowHandle() {
        Locator blinkingTexts = page.locator(".blinkingText");
        Page newPage = context.waitForPage(() -> blinkingTexts.first().click());
        newPage.waitForLoadState();
        String childText = newPage.locator(".red").textContent();
        String emailId = childText.split("at ")[1].split(" ")[0];
        //System.out.println(emailId);
        page.getByLabel("Username:").fill(emailId);
        //page.waitForTimeout(3000);
        System.out.println(page.getByLabel("Username:").inputValue());
        page.waitForTimeout(3000);
    }

    @Test(groups = {"smoke"})
    public void uiControls() {
        Locator userRdBtn = page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("User"));
        userRdBtn.click();

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Okay")).click();
        Assert.assertTrue(userRdBtn.isChecked());

        Locator checkBoxTerms = page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("I agree to the terms and conditions"));
        checkBoxTerms.check();
        Assert.assertTrue(checkBoxTerms.isChecked());

        page.getByRole(AriaRole.COMBOBOX).selectOption("Teacher");
        page.waitForTimeout(3000);
    }
}
