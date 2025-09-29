package com.mfx.eventmanagement;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class VerificationManager {
    private static VerificationManager instance;

    private final Map<String, String> pendingCodes = new HashMap<>();
    private final Timer timer = new Timer(true); // Daemon timer for expiration

    private VerificationManager() {}

    public static synchronized VerificationManager getInstance() {
        if (instance == null) {
            instance = new VerificationManager();
        }
        return instance;
    }

    public void addCode(String email, String code) {
        pendingCodes.put(email, code);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                pendingCodes.remove(email);
            }
        }, 10 * 60 * 1000); // 10 minutes
    }

    public String getCode(String email) {
        return pendingCodes.get(email);
    }

    public void removeCode(String email) {
        pendingCodes.remove(email);
    }
}