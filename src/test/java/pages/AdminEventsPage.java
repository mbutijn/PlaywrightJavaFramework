package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AdminEventsPage {
    Page page;
    private static final String EVENT_TITLE_INPUT = "#event-title-input";
    private static final String DESCRIPTION = "#admin-event-form textarea";
    private static final String CATEGORY = "Category";
    private static final String CITY = "City";
    private static final String VENUE = "Venue";
    private static final String EVENT_DATE_AND_TIME = "Event Date & Time";
    private static final String PRICE = "Price ($)";
    private static final String TOTAL_SEATS = "Total Seats";
    private static final String ADD_EVENT_BUTTON = "#add-event-btn";
    private static final String EVENT_CREATED = "Event created!";

    public AdminEventsPage(Page page){
        this.page = page;
    }

    public void goTo() {
        page.navigate("https://eventhub.rahulshettyacademy.com/admin/events");
    }

    public void createEvent(String title, String description, String category, String city,
                            String venue, String dateTime, String price, String totalSeats) {
        page.locator(EVENT_TITLE_INPUT).fill(title, new Locator.FillOptions().setTimeout(10000));
        page.locator(DESCRIPTION).fill(description);
        page.getByLabel(CATEGORY).selectOption(category);
        page.getByLabel(CITY).fill(city);
        page.getByLabel(VENUE).fill(venue);
        page.getByLabel(EVENT_DATE_AND_TIME).fill(dateTime);
        page.getByLabel(PRICE).fill(price);
        page.getByLabel(TOTAL_SEATS).fill(totalSeats);
        page.locator(ADD_EVENT_BUTTON).click(new Locator.ClickOptions().setTimeout(12000));

        assertThat(page.getByText(EVENT_CREATED)).isVisible();
    }
}
