package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.nio.file.Paths;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MockWebTest extends TestBase {

//    Page page;
//    Browser browser;
//    Playwright playwright;
//    @BeforeMethod
//    public void setup() {
//        playwright = Playwright.create();
//        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(false));
        //Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
        //Browser browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
//        page = browser.newPage();
//        page.setDefaultTimeout(5000);
//        page.navigate("https://eventhub.rahulshettyacademy.com/login");
//    }

    @Test(description = "sandbox banner is shown when 6 events are returned")
    public void DemoTest() {
        page.navigate(base_url);
        page.getByPlaceholder("you@email.com").fill("hego95bts@gmail.com");
        page.getByLabel("Password").fill("Wachtwoord67@");

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign in")).click();

        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();
        page.route("**/api/events**", route -> route.fulfill(
                new Route.FulfillOptions().setPath(Paths.get("src/test/resources/events_6.json"))
        ));
        page.navigate("https://eventhub.rahulshettyacademy.com/events");

        Locator eventCards = page.getByTestId("event-card");
        assertThat(eventCards.first()).isVisible();

        Assert.assertEquals(eventCards.count(), 6);
        assertThat(page.locator(".mx-1").first()).isVisible();


        // 4 events
        page.route("**/api/events**", route -> route.fulfill(
                new Route.FulfillOptions().setPath(Paths.get("src/test/resources/events_4.json"))
        ));
        page.navigate("https://eventhub.rahulshettyacademy.com/events");

        Locator eventCards_4 = page.getByTestId("event-card");
        assertThat(eventCards_4.first()).isVisible();

        Assert.assertEquals(eventCards_4.count(), 4);
        assertThat(page.locator(".mx-1").first()).isHidden();
    }

    @Test
    public void routeResumeTest() {
        page.getByPlaceholder("you@email.com").fill("hego95bts@gmail.com");
        page.getByLabel("Password").fill("Wachtwoord67@");

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign in")).click();

        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();


        page.getByTestId("nav-bookings").click();

        page.route("**/api/bookings**", route -> route.resume(
                new Route.ResumeOptions().setUrl("https://api.eventhub.rahulshettyacademy.com/api/bookings/109871")
        ));

        page.waitForTimeout(7000);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View Details")).first().click();

//        assertThat(page.getByText("Access Denied")).isVisible();
    }
}
