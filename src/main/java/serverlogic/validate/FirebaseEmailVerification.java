package serverlogic.validate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

public class FirebaseEmailVerification {
    private String verificationLink;

    public FirebaseEmailVerification(String uid) throws FirebaseAuthException {
        sendVerificationEmail(uid);
    }

    public void sendVerificationEmail(String uid) throws FirebaseAuthException {
        // Get the user by their uid
        UserRecord user = FirebaseAuth.getInstance().getUser(uid);

        if (!user.isEmailVerified()) {
            // Generate email verification link
            verificationLink = FirebaseAuth.getInstance().generateEmailVerificationLink(user.getEmail());
            // You can now send the link via email to the user using your preferred email service
            System.out.println("Verification email link: " + verificationLink);
        } else {
            System.out.println("Email already verified.");
        }
    }

    public String getVerificationLink() {
        return verificationLink;
    }
}
