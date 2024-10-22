package serverlogic.validate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import io.javalin.http.Context;

public class ValidateToken {

    public static void jwtMiddleware(Context ctx) {
        String authHeader = ctx.header("Authorization");
        System.out.println();
        System.out.println(authHeader);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Extract token from header
            System.out.println(token);

            try {
                // Validate the Firebase token
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
                String uid = decodedToken.getUid(); // Get user ID from token


                System.out.println(uid);

                // Attach user ID to context for use in request lifecycle
                ctx.attribute("uid", uid);

            } catch (Exception e) {
                ctx.status(401).result("Invalid or expired token");
                return;
            }
        } else {
            ctx.status(401).result("Authorization header missing or invalid");
            return;
        }
    }
}
