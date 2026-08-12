package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import pages.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class FrameworkBuildTest extends TestBase {

    @Test(description = "Create, book and verify an event")
    public void DemoTest() {
        String eventTitle = "Bertus party";

        LoginPage loginPage = new LoginPage(page, base_url);
        DashboardPage dashboardPage = loginPage.loginToApplication();
        dashboardPage.waitForEventsToLoad();

        // step 1 create event
        AdminEventsPage adminEventsPage = new AdminEventsPage(page);
        adminEventsPage.goTo();
        adminEventsPage.createEvent(eventTitle, "Bertus description", "Concert", "Valkenburg",
                "Valkenburg Limburg", "2026-08-31T00:00", "450", "5000");

        // step 2 find created event in events space
        EventsPage eventsPage = new EventsPage(page);
        eventsPage.goTo();
        Locator targetCard = eventsPage.findEventCards(eventTitle);
        int numberSeatsBeforeBooking = eventsPage.getSeatsCount(targetCard);

        BookingFormPage bookingFormPage = eventsPage.proceedToBookingEvent(targetCard);
        bookingFormPage.fillAndConfirm("Meneer Hegobuur", "hego67@gmail.com", "+3169999999999");

        String bookingRef = page.locator(".booking-ref").innerText();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("View My Bookings")).click();

        // verify in booking history
        Locator bookingCards = page.locator("#booking-card");
        Locator targetBookingCard = bookingCards.filter(new Locator.FilterOptions().setHasText(bookingRef));

        assertThat(targetBookingCard).isVisible();

        page.locator("#nav-events").click();
        page.waitForTimeout(1000);

        Locator eventCardsAfterBooking = page.getByTestId("event-card");
        Locator targetCardAfterBooking = eventCardsAfterBooking.filter(new Locator.FilterOptions().setHasText("Bertus party")).first();
        String seatsTextAfterBooking = targetCardAfterBooking.getByText("seats").innerText();
        System.out.println(seatsTextAfterBooking);
        int numberSeatsAfterBooking = Integer.parseInt(seatsTextAfterBooking.split(" ")[0]);

        Assert.assertTrue(numberSeatsBeforeBooking > numberSeatsAfterBooking);

    }

    @AfterMethod
    public void close() {

    }

}
