package com.mfx.eventmanagement;

/**
 * Static class to manage the current user session after successful login.
 * This holds the user's ID and email for use throughout the application lifecycle.
 */
public class SessionManager {

    // Unique identifier for the authenticated user
    private static int currentUserId = -1;
    // Email of the authenticated user
    private static String currentUserEmail = null;

    /**
     * Sets the data for the currently logged-in user.
     * @param userId The unique ID of the user from the database.
     * @param email The email of the user.
     */
    public static void setLoggedInUser(int userId, String email) {
        currentUserId = userId;
        currentUserEmail = email;
        System.out.println("Session started: User ID " + userId + ", Email: " + email);
    }

    /**
     * Clears the session data (logs the user out).
     */
    public static void clearSession() {
        currentUserId = -1;
        currentUserEmail = null;
        System.out.println("Session cleared: User logged out.");
    }

    /**
     * @return The ID of the currently logged-in user, or -1 if no user is logged in.
     */
    public static int getCurrentUserId() {
        return currentUserId;
    }

    /**
     * @return The email of the currently logged-in user, or null if no user is logged in.
     */
    public static String getCurrentUserEmail() {
        return currentUserEmail;
    }

    /**
     * @return true if a user is currently logged in, false otherwise.
     */
    public static boolean isLoggedIn() {
        return currentUserId != -1;
    }
}
