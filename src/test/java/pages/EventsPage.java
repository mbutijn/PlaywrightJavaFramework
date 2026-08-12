package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.LocatorAssertions;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class EventsPage {
    Page page;

    public EventsPage(Page page) {
        this.page = page;
    }

    public void goTo() {
        page.locator("#nav-events").click();
    }

    public Locator waitForEventsToLoad() {
        Locator eventCards = page.getByTestId("event-card");
        //Locator eventCards = findEventCards("event-card");
        assertThat(eventCards.first()).isVisible();
        System.out.println("number of events: " + eventCards.count());
        return eventCards;
    }

    public Locator findEventCards(String titleCard) {
        Locator eventCards = waitForEventsToLoad();
        Locator targetCard =  eventCards.filter(new Locator.FilterOptions().setHasText(titleCard)).first();
        assertThat(targetCard).isVisible();
        return targetCard;
    }

    public int getSeatsCount(Locator targetCard) {
        //assertThat(targetCard).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(10000));
//        Locator targetCard = findEventCards(titleCard);
        String seatsText = targetCard.getByText("seats").innerText();
        System.out.println(seatsText);
        return Integer.parseInt(seatsText.split(" ")[0]);
    }

    public BookingFormPage proceedToBookingEvent(Locator targetCard) {
        //Locator targetCard = findEventCards(titleCard);
        targetCard.getByTestId("book-now-btn").click();
        return new BookingFormPage(page);
    }
}
