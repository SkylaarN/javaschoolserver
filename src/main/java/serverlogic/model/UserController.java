package serverlogic.model;

import io.javalin.http.Context;
import java.util.Map;
import java.util.HashMap;

import serverlogic.model.User;

public class UserController {

    private static Map<String, User> userDB = new HashMap<>();

    static {
        userDB.put("user123", new User("user123", "John Doe", "john.doe@example.com"));
        userDB.put("user2", new User("user2", "Alice", "alice@example.com"));
        userDB.put("user3", new User("user3", "Bob", "bob@example.com"));
        userDB.put("user4", new User("user4", "Charlie", "charlie@example.com"));
        userDB.put("user5", new User("user5", "David", "david@asans.com"));
    }

    public static void getProfile(Context ctx) {
        String uid = ctx.attribute("uid"); // Retrieved from validated token

        // Fetch user from the mock database using the UID
        User user = userDB.get(uid);
        System.out.println("getProfile here");
        System.out.println(uid);

        if (user != null) {
            ctx.json(user); // Return user profile as JSON
        } else {
            ctx.status(404).result("User not found");
        }
    }
}