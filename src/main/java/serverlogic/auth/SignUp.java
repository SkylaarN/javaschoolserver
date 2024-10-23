package serverlogic.auth;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


import java.util.HashMap;
import java.util.Map;

public class SignUp {
    private static final String API_KEY = "AIzaSyD-TzJ8VMZ0LBUQawLu5Kt7-eqkcjJQEKM";
    private static final String SIGN_UP_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + API_KEY;
    private static final HttpClient client = HttpClient.newHttpClient();


    public static void signUp(Context ctx) throws Exception {
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
                    .uri(URI.create(SIGN_UP_URL))
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
                String uid = (String) responseData.get("localId"); // Firebase uid (localId)

                System.out.println("User signed up successfully. ID Token: " + idToken);
                System.out.println("User UID: " + uid);

                // Send email verification
//                new FirebaseEmailVerification(uid);
                serverlogic.validate.FirebaseEmailVerification emailVerification = new serverlogic.validate.FirebaseEmailVerification(uid);
                System.out.println("break");
                serverlogic.validate.EmailService.sendEmail(email, "Email Verification", "Please verify your email by clicking on this link: " + emailVerification.getVerificationLink());

            } else {
                System.out.println("Failed to sign up: " + response.body());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
