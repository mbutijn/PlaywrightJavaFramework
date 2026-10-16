package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BasicsTest {

    // email: hego95bts@gmail.com
    // password: W...67@
    Page page;

    @BeforeMethod
    public void setup() {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(false));
        //Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
        //Browser browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
        page.setDefaultTimeout(5000);
        page.navigate("https://eventhub.rahulshettyacademy.com/login");
    }

    @Test(description = "Create, book and verify an event")
    public void BrowserTest() {
        assertThat(page).hasTitle("EventHub — Discover & Book Events");

//        page.getByLabel("Email").fill("hego95bts@gmail.com");
        page.getByPlaceholder("you@email.com").fill("hego95bts@gmail.com");
        page.getByLabel("Password").fill("Wachtwoord67@");

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign in")).click();

        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();

        // create event
        page.navigate("https://eventhub.rahulshettyacademy.com/admin/events");
        page.locator("#event-title-input").fill("Bertus party", new Locator.FillOptions().setTimeout(10000));
        page.locator("#admin-event-form textarea").fill("Bertus description");
        page.getByLabel("Category").selectOption("Concert");
        page.getByLabel("City").fill("Valkenburg");
        page.getByLabel("Venue").fill("Valkenburg Limburg");
        page.getByLabel("Event Date & Time").fill("2026-10-31T00:00");
        page.getByLabel("Price ($)").fill("75");
        page.getByLabel("Total Seats").fill("5000");
        page.locator("#add-event-btn").click(new Locator.ClickOptions().setTimeout(12000));

        assertThat(page.getByText("Event created!")).isVisible();

        // find created event in events space
        page.locator("#nav-events").click();
        Locator eventCards = page.getByTestId("event-card");
        System.out.println("number of events: " + eventCards.count());
        Locator targetCard = eventCards.filter(new Locator.FilterOptions().setHasText("Bertus party"));
        assertThat(targetCard).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(10000));

        String seatsText = targetCard.getByText("seats").innerText();
        System.out.println(seatsText);
        int numberSeatsBeforeBooking = Integer.parseInt(seatsText.split(" ")[0]);

        targetCard.getByTestId("book-now-btn").click();

        page.getByLabel("Full Name").fill("Meneer");
        //page.getByLabel("Email").fill("hego67@gmail.com");
        page.locator("#customer-email").fill("hego67@gmail.com");
        //page.getByLabel("Phone Number").fill("+3169999999999");
        page.getByPlaceholder("+91 98765 43210").fill("+3169999999999");

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirm Booking")).click();
        //page.getByText("Confirm Booking").click();

        assertThat(page.getByText("Your tickets are reserved.")).isVisible();
        String bookingRef = page.locator(".booking-ref").innerText();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("View My Bookings")).click();

        // verify in booking history
        Locator bookingCards = page.locator("#booking-card");
        Locator targetBookingCard = bookingCards.filter(new Locator.FilterOptions().setHasText(bookingRef));

        assertThat(targetBookingCard).isVisible();

        page.locator("#nav-events").click();
        page.waitForTimeout(1000);

        Locator eventCardsAfterBooking = page.getByTestId("event-card");
        Locator targetCardAfterBooking = eventCardsAfterBooking.filter(new Locator.FilterOptions().setHasText("Bertus party"));
        String seatsTextAfterBooking = targetCardAfterBooking.getByText("seats").innerText();
        System.out.println(seatsTextAfterBooking);
        int numberSeatsAfterBooking = Integer.parseInt(seatsTextAfterBooking.split(" ")[0]);

        Assert.assertTrue(numberSeatsBeforeBooking > numberSeatsAfterBooking);

    }

    @AfterMethod
    public void close() {

    }

}
