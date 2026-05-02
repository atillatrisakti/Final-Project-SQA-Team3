package com.hadirapp.drivers.strategies;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.util.HashMap;
import java.util.Map;

public class Chrome implements DriverStrategy {

    @Override
    public WebDriver setStrategy(String browser) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-session-crashed-bubble");
        
        // Auto-grant permissions and fake video feed (if not blocked)
        String cameraPermission = System.getProperty("camera.permission", "1"); // 1: Allow (Default), 2: Block
        
        if (cameraPermission.equals("1")) {
            options.addArguments("--use-fake-ui-for-media-stream"); // Auto-grant
            options.addArguments("--use-fake-device-for-media-stream"); // Use fake feed
        }
        
        // Auto-grant location and configure camera based on system property
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.geolocation", 1); // 1: Allow
        if (cameraPermission.equals("2")) {
            prefs.put("profile.default_content_setting_values.media_stream_camera", 2); // 2: Block
            prefs.put("profile.default_content_setting_values.media_stream_mic", 2); // 2: Block
        }
        options.setExperimentalOption("prefs", prefs);

        options.setExperimentalOption("excludeSwitches",
                new java.util.ArrayList<>(java.util.Arrays.asList("enable-automation")));

        if (browser.contains("headless")) {
            options.addArguments("--headless=new");
        }

        return new org.openqa.selenium.chrome.ChromeDriver(options);
    }

    @Override
    public WebDriver setStrategy() {
        return setStrategy("chrome");
    }
}
