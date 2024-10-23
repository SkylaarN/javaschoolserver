package server;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import io.javalin.Javalin;
import serverlogic.auth.Login;
import serverlogic.auth.SignUp;
import serverlogic.model.UserController;
import serverlogic.validate.ValidateToken;

import java.io.FileInputStream;
import java.util.HashMap;

public class Server {


    public static void main(String[] args) {

        Javalin app = Javalin.create().start(7000);

        initializeFirebase(); // Initialize Firebase
        validateRoutes(app);
        routes(app);

        //post methods
    }

    public static void initializeFirebase() {
        try {
            FileInputStream serviceAccount =
                    new FileInputStream("src/main/java/server/testinging-cd9e6-firebase-adminsdk-jo75e-ebee9ae4d3.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://<your-project-id>.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void validateRoutes(Javalin app){
        //token verification
//        app.before("/login",  ValidateToken::jwtMiddleware);
        app.before("/profile",  ValidateToken::jwtMiddleware);
        app.before("/topics",  ValidateToken::jwtMiddleware);
        app.before("/rating",  ValidateToken::jwtMiddleware);
        app.before("/notes",  ValidateToken::jwtMiddleware);
    }

    public static void routes(Javalin app){
        //get methods
        app.get("/profile", UserController::getProfile);
//        app.get("/topics", UserController::topics);
        app.get("/rating", UserController::getProfile);
        app.get("/notes", UserController::getProfile);
//        app.get("/signup", SignUp::tryOut);
        app.post("/signup", ctx -> new SignUp().signUp(ctx));
        app.post("/login", ctx -> new Login().signIn(ctx));
    }
}

