package stepDefinitions;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.options.AriaRole;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class StepDefinitions {
    private static Page page;
    private static Browser browser;
    private Locator targetCard;

    @When("I go to the website")
    public void GoToTheWebsite() {
        if (browser == null) {
            go_to_site();
        }
    }

    @Given("I am logged in with {} and {}")
    public void loggedIn(String userName, String password) {
        if (browser == null) {
            go_to_site();
            fillInUserName(userName);
            fillInPassword(password);

            clickOnSignIn();
        }
    }

    @Given("I have created an event")
    public void IHaveCreatedAnEvent() {
        loggedIn("hego95bts@gmail.com", "Wachtwoord67@");
        NavigateToTheEventCreationPage();
        String eventName = "Bertus Party";
        FillInTitle(eventName);
        FillInEventData("description", "Concert", "city", "venue", "2026-12-31T00:00", "45", "900");
        clickOnCreateEvent();
        verifyEventFoundInEventsSpace(eventName);
    }

    public static void go_to_site() {
        Playwright playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(false));
        page = browser.newPage();
        page.setDefaultTimeout(10000);
        page.navigate("https://eventhub.rahulshettyacademy.com/login");

        assertThat(page).hasTitle("EventHub — Discover & Book Events");
    }

    @When("I fill in the username {}")
    public void fillInUserName(String userName) {
        page.getByPlaceholder("you@email.com").fill(userName);
    }

    @When("I fill in the password {}")
    public void fillInPassword(String password) {
        page.getByLabel("Password").fill(password);
    }

    @When("I click on Sign in")
    public void clickOnSignIn() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign in")).click();
    }

    @When("I create an event")
    public void NavigateToTheEventCreationPage() {
        page.navigate("https://eventhub.rahulshettyacademy.com/admin/events");
    }

    @When("I fill in title {}")
    public void FillInTitle(String title) {
        page.locator("#event-title-input").fill(title, new Locator.FillOptions().setTimeout(10000));
    }

    @When("I fill in the following data {}, {}, {}, {}, {}, {}, {}")
    public void FillInEventData(String description, String category, String city, String venue, String eventDate, String price, String totalSeats) {
        page.locator("#admin-event-form textarea").fill(description);
        page.getByLabel("Category").selectOption(category);
        page.getByLabel("City").fill(city);
        page.getByLabel("Venue").fill(venue);
        page.getByLabel("Event Date & Time").fill(eventDate);
        page.getByLabel("Price ($)").fill(price);
        page.getByLabel("Total Seats").fill(totalSeats);
    }

    @When("I click on Create Event")
    public void clickOnCreateEvent() {
        page.locator("#add-event-btn").click(new Locator.ClickOptions().setTimeout(12000));
    }

    @When("I click on Book now")
    public void clickOnBookEvent() {
        targetCard.getByTestId("book-now-btn").click();
    }


    @When("I fill in the booking with the following data {}, {}, {}")
    public void fillInBookingData(String fullName, String email, String phone) {
        page.getByLabel("Full Name").fill(fullName);
        //page.getByLabel("Email").fill("hego67@gmail.com");
        page.locator("#customer-email").fill(email);
        //page.getByLabel("Phone Number").fill("+3169999999999");
        page.getByPlaceholder("+91 98765 43210").fill(phone);
    }

    @When("I click on Confirm now")
    public void clickOnConfirm() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirm Booking")).click();
    }

    @Then("I should see {}")
    public void verifyBrowseEvents(String browseEvents) {
        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(browseEvents))).isVisible();
    }

    @Then("the message {} should be visible")
    public void verifyEventCreatedMessage(String eventCreatedMessage) {
//        System.out.println("deze methode wordt WEL bereikt!!!!");
        assertThat(page.getByText(eventCreatedMessage)).isVisible();
    }

    @Then("the event {} is found in the events space")
    public void verifyEventFoundInEventsSpace(String eventName) {
        page.locator("#nav-events").click();
        Locator eventCards = page.getByTestId("event-card");
        System.out.println("number of events: " + eventCards.count());
        targetCard = eventCards.filter(new Locator.FilterOptions().setHasText(eventName));
        assertThat(targetCard).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(10000));
    }

    @Then("a success message: {} should appear")
    public void successMessageShouldAppear(String message) {
        System.out.println("hallo");
        assertThat(page.getByText(message)).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(10000));
    }

    public static void quitBrowser() {
        browser.close();
    }

}
