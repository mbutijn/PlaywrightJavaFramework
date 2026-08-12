package tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;

public class DataProviderDemoTest {

    @DataProvider(name = "basicData") //Multi Dimensional array of objects
    public Object[][] basicData() {
        return new Object[][]{{"b@hotmail", "pw1"}, {"b@gmail", "pw2"}};
    }

    @Test(dataProvider = "basicData")
    public void testFillForm(String email, String password) {
        System.out.println(email);
        System.out.println(password);
    }

    @DataProvider(name = "hashMapData") //Multi Dimensional array of objects
    public Object[][] hashMapData() {
        HashMap<String, String> user1 = new HashMap<>();
        user1.put("email", "b2@hotmail");
        user1.put("password", "pw1");

        HashMap<String, String> user2 = new HashMap<>();
        user2.put("email", "b2@gmail");
        user2.put("password", "pw2");

        return new Object[][] {{user1}, {user2}};
    }

    @Test(dataProvider = "hashMapData")
    public void testHashMapData(HashMap<String, String> user) {
        System.out.println(user.get("email"));
        System.out.println(user.get("password"));
    }
}
