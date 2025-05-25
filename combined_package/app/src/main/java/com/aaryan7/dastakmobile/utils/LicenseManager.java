package com.aaryan7.dastakmobile.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Utility class to manage trial period and activation status
 */
public class LicenseManager {
    private static final String PREFS_NAME = "DastakMobileLicense";
    private static final String KEY_INSTALL_DATE = "install_date";
    private static final String KEY_ACTIVATED = "activated";
    private static final String DEV_USERNAME = "aaryan77";
    private static final String DEV_PASSWORD = "aaryan7781@@";
    private static final int TRIAL_DAYS = 5;
    
    private final Context context;
    private final SharedPreferences prefs;
    private final String deviceId;
    
    public LicenseManager(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.deviceId = getUniqueDeviceId();
        
        // Initialize install date if not set
        if (!prefs.contains(KEY_INSTALL_DATE)) {
            setInstallDate();
        }
    }
    
    /**
     * Check if the app is in trial period or activated
     * @return true if app can be used, false if trial expired and not activated
     */
    public boolean isAppUsable() {
        // If app is activated, always return true
        if (isActivated()) {
            return true;
        }
        
        // Otherwise, check if trial period is still valid
        return isInTrialPeriod();
    }
    
    /**
     * Check if the app is in trial period
     * @return true if in trial period, false otherwise
     */
    public boolean isInTrialPeriod() {
        long installTime = getDecryptedLong(KEY_INSTALL_DATE);
        if (installTime == 0) {
            return false;
        }
        
        Calendar installDate = Calendar.getInstance();
        installDate.setTimeInMillis(installTime);
        
        Calendar expiryDate = Calendar.getInstance();
        expiryDate.setTimeInMillis(installTime);
        expiryDate.add(Calendar.DAY_OF_MONTH, TRIAL_DAYS);
        
        Calendar now = Calendar.getInstance();
        
        return now.before(expiryDate);
    }
    
    /**
     * Get days remaining in trial period
     * @return days remaining, 0 if expired
     */
    public int getDaysRemaining() {
        if (isActivated()) {
            return -1; // -1 indicates activated
        }
        
        long installTime = getDecryptedLong(KEY_INSTALL_DATE);
        if (installTime == 0) {
            return 0;
        }
        
        Calendar installDate = Calendar.getInstance();
        installDate.setTimeInMillis(installTime);
        
        Calendar expiryDate = Calendar.getInstance();
        expiryDate.setTimeInMillis(installTime);
        expiryDate.add(Calendar.DAY_OF_MONTH, TRIAL_DAYS);
        
        Calendar now = Calendar.getInstance();
        
        if (now.after(expiryDate)) {
            return 0;
        }
        
        long diffMillis = expiryDate.getTimeInMillis() - now.getTimeInMillis();
        return (int) (diffMillis / (24 * 60 * 60 * 1000)) + 1;
    }
    
    /**
     * Check if the app is activated for lifetime
     * @return true if activated, false otherwise
     */
    public boolean isActivated() {
        return getDecryptedBoolean(KEY_ACTIVATED);
    }
    
    /**
     * Activate the app for lifetime
     */
    public void activateApp() {
        encryptAndStore(KEY_ACTIVATED, true);
    }
    
    /**
     * Verify developer credentials
     * @param username entered username
     * @param password entered password
     * @return true if credentials are valid, false otherwise
     */
    public boolean verifyDeveloperCredentials(String username, String password) {
        return DEV_USERNAME.equals(username) && DEV_PASSWORD.equals(password);
    }
    
    /**
     * Set the installation date to current time
     */
    private void setInstallDate() {
        long now = System.currentTimeMillis();
        encryptAndStore(KEY_INSTALL_DATE, now);
    }
    
    /**
     * Get a unique device ID that persists across app reinstalls
     * @return unique device ID
     */
    private String getUniqueDeviceId() {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
    
    /**
     * Encrypt and store a long value
     * @param key preference key
     * @param value long value to store
     */
    private void encryptAndStore(String key, long value) {
        try {
            String encrypted = encrypt(String.valueOf(value));
            prefs.edit().putString(key, encrypted).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Encrypt and store a boolean value
     * @param key preference key
     * @param value boolean value to store
     */
    private void encryptAndStore(String key, boolean value) {
        try {
            String encrypted = encrypt(String.valueOf(value));
            prefs.edit().putString(key, encrypted).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Get decrypted long value
     * @param key preference key
     * @return decrypted long value, 0 if error
     */
    private long getDecryptedLong(String key) {
        try {
            String encrypted = prefs.getString(key, null);
            if (encrypted == null) {
                return 0;
            }
            String decrypted = decrypt(encrypted);
            return Long.parseLong(decrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     * Get decrypted boolean value
     * @param key preference key
     * @return decrypted boolean value, false if error
     */
    private boolean getDecryptedBoolean(String key) {
        try {
            String encrypted = prefs.getString(key, null);
            if (encrypted == null) {
                return false;
            }
            String decrypted = decrypt(encrypted);
            return Boolean.parseBoolean(decrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Encrypt a string using AES
     * @param input string to encrypt
     * @return encrypted string
     */
    private String encrypt(String input) throws Exception {
        SecretKeySpec keySpec = generateKey();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encrypted = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeToString(encrypted, Base64.DEFAULT);
    }
    
    /**
     * Decrypt a string using AES
     * @param input encrypted string
     * @return decrypted string
     */
    private String decrypt(String input) throws Exception {
        SecretKeySpec keySpec = generateKey();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decoded = Base64.decode(input, Base64.DEFAULT);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted, StandardCharsets.UTF_8);
    }
    
    /**
     * Generate encryption key based on device ID
     * @return SecretKeySpec for AES encryption
     */
    private SecretKeySpec generateKey() throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = deviceId.getBytes(StandardCharsets.UTF_8);
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        return new SecretKeySpec(key, "AES");
    }
}
