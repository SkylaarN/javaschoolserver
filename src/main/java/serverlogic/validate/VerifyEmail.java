package serverlogic.validate;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class VerifyEmail {

    // Method to check if the user's email is verified using Firebase Admin API
    public static boolean checkEmailVerified(String idToken, String API_KEY, HttpClient client) {
        try {
            // Firebase API URL to get the user info
            String getUserInfoUrl = "https://identitytoolkit.googleapis.com/v1/accounts:lookup?key=" + API_KEY;

            // Create the payload to get the user's info using the idToken
            Map<String, Object> payload = new HashMap<>();
            payload.put("idToken", idToken);

            Gson gson = new Gson();
            String jsonPayload = gson.toJson(payload);

            // Create the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(getUserInfoUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload, StandardCharsets.UTF_8))
                    .build();

            // Send the request to Firebase to get user info
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Parse the response to get email verification status
                Map<String, Object> responseData = gson.fromJson(response.body(), Map.class);
                Map<String, Object> user = ((Map<String, Object>) ((java.util.List<?>) responseData.get("users")).get(0));

                // Return the emailVerified property
                return (boolean) user.get("emailVerified");
            } else {
                System.out.println("Failed to retrieve user info: " + response.body());
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
