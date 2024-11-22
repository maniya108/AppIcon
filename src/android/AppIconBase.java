package com.cordova.appicon;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AppIconBase {

    private Activity activity;
    private Context context;
    private String packageName;
    private PackageManager pm;

    public AppIconBase(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;

        this.packageName = context.getPackageName();
        this.pm = context.getPackageManager();
    }

    public Boolean isSupported() {
        return true; // Assume all Android devices support icon switching for simplicity
    }

    public String getName() {
//        try {
//            ComponentName componentName = pm.getLaunchIntentForPackage(packageName).getComponent();
//            int status = pm.getComponentEnabledSetting(componentName);
//
//            if (status == PackageManager.COMPONENT_ENABLED_STATE_ENABLED ||
//                status == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT) {
//                String name = componentName.getShortClassName();
//                if (Objects.equals(name, ".MainActivity")) {
//                    return null; // Default icon
//                }
//                return name.substring(1); // Return the short class name
//            }
//        } catch (Exception e) {
//            Log.e("AppIconBase", "Error retrieving current icon name: " + e.getMessage());
//        }
//        return null;

        ComponentName componentName = new ComponentName(this.activity, this.activity.getClass());
        int status = pm.getComponentEnabledSetting(componentName);
        if (status == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            // The component is currently enabled
            String name = componentName.getShortClassName();
            if (Objects.equals(name, ".MainActivity")) {
                return null;
            }
            return name.substring(1);
        } else {
            // The component is currently disabled
            return null;
        }
    }

    public void change(String enableName, JSONArray disableNames) throws JSONException {
//        List<String> disableList = toList(disableNames);
//
//        // Enable the new icon
//        pm.setComponentEnabledSetting(
//            new ComponentName(packageName, packageName + "." + enableName),
//            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//            PackageManager.DONT_KILL_APP
//        );
//
//        // Disable the specified icons
//        for (String name : disableList) {
//            pm.setComponentEnabledSetting(
//                new ComponentName(packageName, packageName + "." + name),
//                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                PackageManager.DONT_KILL_APP
//            );
//        }
//
//        // Always disable the default MainActivity icon
//        pm.setComponentEnabledSetting(
//            new ComponentName(packageName, packageName + ".MainActivity"),
//            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//            PackageManager.DONT_KILL_APP
//        );

        try {
            List<String> newList = toList(disableNames);

            pm.setComponentEnabledSetting(
                    new ComponentName(this.packageName, this.packageName + "." + enableName),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
            );

            for (String value : newList) {
                Log.i("AppIconBase", this.packageName + "." + value);
                pm.setComponentEnabledSetting(
                        new ComponentName(this.packageName, this.packageName + "." + value),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP
                );
            }

            // Always disable main app icon
            pm.setComponentEnabledSetting(
                    new ComponentName(this.packageName, this.packageName + ".MainActivity"),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP
            );
        } catch (JSONException ignore) {
            // do nothing
        }
    }

    public void reset(JSONArray disableNames) throws JSONException {
//        List<String> disableList = toList(disableNames);
//
//        // Re-enable the default MainActivity icon
//        pm.setComponentEnabledSetting(
//            new ComponentName(packageName, packageName + ".MainActivity"),
//            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//            PackageManager.DONT_KILL_APP
//        );
//
//        // Disable the specified icons
//        for (String name : disableList) {
//            pm.setComponentEnabledSetting(
//                new ComponentName(packageName, packageName + "." + name),
//                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                PackageManager.DONT_KILL_APP
//            );
//        }

        try {
            List<String> newList = toList(disableNames);
            // Reset the icon to the default icon
            pm.setComponentEnabledSetting(
                    new ComponentName(packageName, packageName + ".MainActivity"),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
            );
            for (String value : newList) {
                Log.i("AppIconBaseReset", this.packageName + "." + value);
                pm.setComponentEnabledSetting(
                        new ComponentName(this.packageName, this.packageName + "." + value),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP
                );
            }
        } catch (JSONException ignore) {
            // do nothing
        }
    }

    private List<String> toList(JSONArray jsonArray) throws JSONException {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getString(i));
        }
        return list;
    }
}
