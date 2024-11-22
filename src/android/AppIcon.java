package com.cordova.appicon;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AppIcon extends CordovaPlugin {

    private AppIconBase appIconBase;

    @Override
    protected void pluginInitialize() {
        appIconBase = new AppIconBase(cordova.getActivity(), cordova.getContext());
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        switch (action) {
            case "isSupported":
                isSupported(callbackContext);
                return true;

            case "getName":
                getName(callbackContext);
                return true;

            case "change":
                change(args, callbackContext);
                return true;

            case "reset":
                reset(args, callbackContext);
                return true;

            default:
                callbackContext.error("Invalid action");
                return false;
        }
    }

    private void isSupported(CallbackContext callbackContext) {
        boolean supported = appIconBase.isSupported();
        JSONObject result = new JSONObject();
        try {
            result.put("value", supported);
            callbackContext.success(result);
        } catch (JSONException e) {
            callbackContext.error("Error: " + e.getMessage());
        }
    }

    private void getName(CallbackContext callbackContext) {
        String name = appIconBase.getName();
        JSONObject result = new JSONObject();
        try {
            result.put("value", name);
            callbackContext.success(result);
        } catch (JSONException e) {
            callbackContext.error("Error: " + e.getMessage());
        }
    }

    private void change(JSONArray args, CallbackContext callbackContext) {
        try {
            JSONObject options = args.getJSONObject(0);
            if(!options.has("name")) {
                callbackContext.error("Must provide an icon name");
                return;
            }
            if(!options.has("disable")) {
                callbackContext.error("Must provide an array of icon names to disable");
                return;
            }
            String enableName = options.getString("name");
            JSONArray disableNames = options.getJSONArray("disable");

            appIconBase.change(enableName, disableNames);
            callbackContext.success();
        } catch (Exception e) {
            callbackContext.error("Error: " + e.getMessage());
        }
    }

    private void reset(JSONArray args, CallbackContext callbackContext) {
        try {
            JSONObject options = args.getJSONObject(0);
            if(!options.has("disable")) {
                callbackContext.error("Must provide an array of icon names to disable");
                return;
            }
            JSONArray disableNames = options.getJSONArray("disable");

            appIconBase.reset(disableNames);
            callbackContext.success();
        } catch (Exception e) {
            callbackContext.error("Error: " + e.getMessage());
        }
    }
}
