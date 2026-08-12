package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BookingFormPage {
    Page page;
    private final static String FULL_NAME_LABEL = "Full Name";
    private final static String CUSTOMER_EMAIL_ID = "#customer-email";
    private final static String PHONE_PLACEHOLDER = "+91 98765 43210";
    private final static String CONFIRM_BUTTON = "Confirm Booking";

    public BookingFormPage(Page page) {
        this.page = page;
    }

    public void fillAndConfirm(String fullName, String email, String phone) {
        page.getByLabel(FULL_NAME_LABEL).fill(fullName);
        //page.getByLabel("Email").fill("hego67@gmail.com");
        page.locator(CUSTOMER_EMAIL_ID).fill(email);
        //page.getByLabel("Phone Number").fill("+3169999999999");
        page.getByPlaceholder(PHONE_PLACEHOLDER).fill(phone);

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(CONFIRM_BUTTON)).click();
        //page.getByText("Confirm Booking").click();
        assertThat(page.getByText("Your tickets are reserved.")).isVisible();
    }
}
