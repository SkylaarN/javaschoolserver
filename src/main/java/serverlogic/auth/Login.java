package serverlogic.auth;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

import static serverlogic.validate.VerifyEmail.checkEmailVerified;
//import emailVerification.EmailService;

public class Login {
    // Firebase project API key from your Firebase console
    private static final String API_KEY = "AIzaSyD-TzJ8VMZ0LBUQawLu5Kt7-eqkcjJQEKM";

    // Sign-in URL
    private static final String SIGN_IN_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + API_KEY;

    // HTTP client for making requests
    private static final HttpClient client = HttpClient.newHttpClient();

    public static void signIn(Context ctx) throws Exception {
        try {

            // Parse JSON body into a HashMap
            Map<String, String> userSignupData = ctx.bodyAsClass(HashMap.class);

            String email = userSignupData.get("email");
            String password = userSignupData.get("password");

            // Create the payload (body of the request)
            Map<String, Object> payload = new HashMap<>();
            payload.put("email", email);
            payload.put("password", password);
            payload.put("returnSecureToken", true);

            // Convert the payload to JSON
            Gson gson = new Gson();
            String jsonPayload = gson.toJson(payload);

            // Create the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SIGN_IN_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload, StandardCharsets.UTF_8))
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check the response status code
            if (response.statusCode() == 200) {
                // Parse the response body
                Map<String, Object> responseData = gson.fromJson(response.body(), Map.class);
                String idToken = (String) responseData.get("idToken");

                System.out.println("User signed in successfully. ID Token: " + idToken);

                // Check if the user's email is verified
                boolean isEmailVerified = checkEmailVerified(idToken, API_KEY, client);
//                boolean isEmailVerified = false;
                if (isEmailVerified) {
                    System.out.println("Email is verified.");
                } else {
                    System.out.println("Email is not verified. Sending verification email...");
//                    EmailService.sendVerificationEmail(email);
                }

            } else {
                System.out.println("Failed to sign in: " + response.body());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

