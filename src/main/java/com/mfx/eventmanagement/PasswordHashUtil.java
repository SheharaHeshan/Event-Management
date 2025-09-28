package com.mfx.eventmanagement;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for securely hashing and verifying user passwords using the BCrypt algorithm.
 * * NOTE: This class relies on the jBCrypt library being included in your project dependencies.
 * If you are using Maven, add this dependency:
 * <dependency>
 * <groupId>org.mindrot</groupId>
 * <artifactId>jbcrypt</artifactId>
 * <version>0.4</version>
 * </dependency>
 */
public class PasswordHashUtil {

    // Define the BCrypt strength level (higher is slower and more secure)
    private static final int HASH_STRENGTH = 12;

    /**
     * Hashes a plain text password for secure storage.
     * This method should be used during user registration.
     * * @param password The raw, plain text password.
     * @return The securely hashed password string.
     */
    public static String hashPassword(String password) {
        // Automatically handles salt generation, hashing, and storing the salt in the resulting hash.
        return BCrypt.hashpw(password, BCrypt.gensalt(HASH_STRENGTH));
    }

    /**
     * Verifies a raw password against a stored, hashed password.
     * This method should be used during user login.
     * * @param plainPassword The raw password entered by the user.
     * @param storedHash The hashed password retrieved from the database.
     * @return true if the passwords match, false otherwise.
     */
    public static boolean verifyPassword(String plainPassword, String storedHash) {
        if (storedHash == null || storedHash.isEmpty() || storedHash.length() < 50) {
            System.err.println("SECURITY ERROR: Stored hash is null, empty, or too short. Likely an unregistered or compromised account.");
            return false;
        }

        try {
            // BCrypt handles the re-hashing of the plain password and comparison with the stored hash.
            return BCrypt.checkpw(plainPassword, storedHash);
        } catch (IllegalArgumentException e) {
            // This is the error you are seeing. It means the hash format is incompatible (old data).
            if (e.getMessage().contains("Invalid salt version")) {
                System.err.println("SECURITY WARNING: Failed password verification due to OLD/INVALID HASH FORMAT.");
                System.err.println("This often happens when testing with users registered *before* BCrypt implementation.");
                System.err.println("ACTION REQUIRED: Delete this user from the 'users' table and re-register them.");
            } else {
                System.err.println("Error during password verification: " + e.getMessage());
            }
            return false;
        }
    }
}
