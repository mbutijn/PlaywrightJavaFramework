package tests;

import com.jayway.jsonpath.JsonPath;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

public class APITest {
    @Test
    public void e2eAPITest() {
        // login
        HashMap<Object, Object> loginPayload = new HashMap<>();
        loginPayload.put("email", "hego95bts@gmail.com");
        loginPayload.put("password", "Wachtwoord67@");

        Playwright playwright = Playwright.create();
        APIRequestContext apiRequest = playwright.request().newContext();
        APIResponse loginResponse = apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/auth/login",
                RequestOptions.create().setData(loginPayload));

        Assert.assertTrue(loginResponse.ok());
        System.out.println(loginResponse.text());

        String token = JsonPath.read(loginResponse.text(), "$.token");
        System.out.println("Login success, token: " + token);

        // create event
        String eventTitle = "Playwright API Testing";
        HashMap<Object, Object> createEventPayload = new HashMap<>();
        createEventPayload.put("title", eventTitle);

        createEventPayload.put("description", "partying");
        createEventPayload.put("category", "Festival");
        createEventPayload.put("venue", "Limburg");
        createEventPayload.put("city", "Maastricht");
        createEventPayload.put("eventDate", "2026-10-04T08:41:00.000Z");
        createEventPayload.put("price", 300);
        createEventPayload.put("totalSeats", 600);

        APIResponse eventResponse = apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create().setHeader("Authorization", "Bearer " + token)
                .setData(createEventPayload));

        Assert.assertTrue(eventResponse.ok(), "Create Event API should succeed");
        int eventId = JsonPath.read(eventResponse.text(), "$.data.id");
        System.out.println("Event created and its ID is " + eventId);

        // Get event
        APIResponse retrieveEvents = apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create().setQueryParam("page", "1").setQueryParam("limit", "12")
                        .setHeader("Authorization", "Bearer " + token));

        Assert.assertTrue(retrieveEvents.ok(), "Event Retrieval API should succeed");
        System.out.println(retrieveEvents.text());

        List<Integer> allEventIds = JsonPath.read(retrieveEvents.text(), "$.data[*].id");
        Assert.assertTrue(allEventIds.contains(eventId), "Created event should appear in events list");

        // Delete
        APIResponse deleteResponse = apiRequest.delete("https://api.eventhub.rahulshettyacademy.com/api/events/" + eventId,
                RequestOptions.create().setHeader("Authorization", "Bearer " + token));

        Assert.assertTrue(deleteResponse.ok(), "Event Deletion API should succeed");

        APIResponse retrieveEventsAfterDeletion = apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create().setQueryParam("page", "1").setQueryParam("limit", "12")
                        .setHeader("Authorization", "Bearer " + token));

        List<Integer> allEventIdsAfterDeletion = JsonPath.read(retrieveEventsAfterDeletion.text(), "$.data[*].id");
        Assert.assertFalse(allEventIdsAfterDeletion.contains(eventId), "Created event should not appear in events list");
    }

}
