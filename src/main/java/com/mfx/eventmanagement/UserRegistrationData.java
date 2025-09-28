package com.mfx.eventmanagement;

/**
 * Static class to temporarily hold user registration data (First Name, Last Name,
 * Hashed Password, Email) between the Registration and Verification steps.
 * This ensures full database insertion happens only after email verification.
 */
public class UserRegistrationData {

    private static String firstName;
    private static String lastName;
    private static String email;
    private static String passwordHash;

    /**
     * Stores the registration details temporarily.
     */
    public static void storeData(String fName, String lName, String mail, String hash) {
        firstName = fName;
        lastName = lName;
        email = mail;
        passwordHash = hash;
    }

    /**
     * Retrieves and clears the stored data once registration is finalized.
     * This method is intended to be called exactly once during the final database
     * insertion step (e.g., in VerifyNController).
     * @return An array of strings: [firstName, lastName, email, passwordHash]
     */
    public static String[] retrieveAndClearData() {
        String[] data = new String[] {firstName, lastName, email, passwordHash};
        // Clear data immediately after retrieval to prevent stale information
        firstName = null;
        lastName = null;
        email = null;
        passwordHash = null;
        return data;
    }

    /**
     * Retrieves the stored registration details without clearing them.
     * This method is added to resolve the "retrieveData not found" error,
     * but you should typically use retrieveAndClearData() when you are ready to finalize the registration.
     * @return An array of strings: [firstName, lastName, email, passwordHash]
     */
    public static String[] retrieveData() {
        return new String[] {firstName, lastName, email, passwordHash};
    }

    /**
     * Get the email for verification and display purposes without clearing.
     */
    public static String getEmail() {
        return email;
    }

    /**
     * Helper method to generate a random 6-digit OTP code.
     */
    public static String generateOtp() {
        // Generates a random number between 0 and 999999 (inclusive) and formats it as 6 digits, padding with leading zeros.
        return String.format("%06d", (int) (Math.random() * 1000000));
    }
}
