Feature: test for Schedule event
  @verifyLoginToWebsite
  Scenario: Verify that logging in with an existing user account is possible
    When I go to the website
    And I fill in the username hego95bts@gmail.com
    And I fill in the password Wachtwoord67@
    And I click on Sign in
    Then I should see Browse Events →

  @verifyCreateEvent
  Scenario: Make event
    Given I am logged in with hego95bts@gmail.com and Wachtwoord67@
    When I create an event
    And I fill in title Bertus Party
    And I fill in the following data Bertus is having a party, Concert, Valkenburg, Valkenburg Limburg, 2026-10-31T00:00, 75, 5000
    And I click on Create Event
    Then the message Event created! should be visible
    And the event Bertus Party is found in the events space

  @verifyBookEvent
  Scenario: Book Event
    Given I have created an event
    When I click on Book now
    And I fill in the booking with the following data Bertje, hego955@gmail.com, +3169999999999
    And I click on Confirm now
    Then a success message: Your tickets are reserved. should appear

