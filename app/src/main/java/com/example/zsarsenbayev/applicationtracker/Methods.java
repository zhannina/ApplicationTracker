package com.example.zsarsenbayev.applicationtracker;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

import java.util.List;

/**
 * Created by zsarsenbayev on 3/8/18.
 */

public class Methods {

    public static final String TAG = "Zhanna";

    public static boolean isAccessibilityServiceEnabled(Context context, Class<?> accessibilityService) {
        ComponentName expectedComponentName = new ComponentName(context, accessibilityService);

        String enabledServicesSetting = Settings.Secure.getString(context.getContentResolver(),  Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        if (enabledServicesSetting == null)
            return false;

        TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter(':');
        colonSplitter.setString(enabledServicesSetting);

        while (colonSplitter.hasNext()) {
            String componentNameString = colonSplitter.next();
            ComponentName enabledService = ComponentName.unflattenFromString(componentNameString);

            if (enabledService != null && enabledService.equals(expectedComponentName))
                return true;
        }

        return false;
    }

//    public static void logInstalledAccessiblityServices(Context context) {
//
//        AccessibilityManager am = (AccessibilityManager) context
//                .getSystemService(Context.ACCESSIBILITY_SERVICE);
//
//        List<AccessibilityServiceInfo> runningServices = am
//                .getInstalledAccessibilityServiceList();
//        for (AccessibilityServiceInfo service : runningServices) {
//            Log.i(TAG, service.getResolveInfo().resolvePackageName +"");
//        }
//    }

}
