package com.github.kevinejohn.keyevent;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.bridge.WritableMap;

import android.view.KeyEvent;

/**
 * Created by Kevin Johnson on 8/15/16.
 */
public class KeyEventModule extends ReactContextBaseJavaModule {
    private ReactContext mReactContext;
    private DeviceEventManagerModule.RCTDeviceEventEmitter mJSModule = null;

    private static KeyEventModule instance = null;

    public static KeyEventModule initKeyEventModule(ReactApplicationContext reactContext) {
        instance = new KeyEventModule(reactContext);
        return instance;
    }

    public static KeyEventModule getInstance() {
        return instance;
    }

    public void onKeyDownEvent(int keyCode, KeyEvent keyEvent) {
        if (!mReactContext.hasActiveCatalystInstance()) {
            return;
        }

        if (mJSModule == null) {
            mJSModule = mReactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class);
        }
        mJSModule.emit("onKeyDown", getJsEventParams(keyCode, keyEvent, null));
    };

    public void onKeyUpEvent(int keyCode, KeyEvent keyEvent) {
        if (!mReactContext.hasActiveCatalystInstance()) {
            return;
        }

        if (mJSModule == null) {
            mJSModule = mReactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class);
        }
        mJSModule.emit("onKeyUp", getJsEventParams(keyCode, keyEvent, null));
    };

    public void onKeyMultipleEvent(int keyCode, int repeatCount, KeyEvent keyEvent) {
        if (!mReactContext.hasActiveCatalystInstance()) {
            return;
        }
        
        if (mJSModule == null) {
            mJSModule = mReactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class);
        }


        mJSModule.emit("onKeyMultiple", getJsEventParams(keyCode, keyEvent, repeatCount));
    };

    protected KeyEventModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mReactContext = reactContext;
    }

    private WritableMap getJsEventParams(int keyCode, KeyEvent keyEvent, Integer repeatCount) {
        WritableMap params = new WritableNativeMap();
        
        int action = keyEvent.getAction();
        char pressedKey = (char) keyEvent.getUnicodeChar();
        boolean modifiers = keyEvent.isModifierKey(keyCode);

        if (keyEvent.getAction() == KeyEvent.ACTION_MULTIPLE && keyCode == KeyEvent.KEYCODE_UNKNOWN) {
            String chars = keyEvent.getCharacters();
            if (chars != null) {
                params.putString("characters", chars);
            }
        }

        if(keyCode == KeyEvent.KEYCODE_TAB) {
            params.putBoolean("isTabsPressed", true);
        }

        params.putString("displayLabel", String.valueOf(keyEvent.getDisplayLabel()));
        params.putInt("scanCode", keyEvent.getScanCode());
        params.putInt("unicodeChar", keyEvent.getUnicodeChar());
        params.putInt("getModifiers", keyEvent.getModifiers());
        params.putString("Number", String.valueOf(keyEvent.getNumber()));
        params.putBoolean("isShiftPressed", keyEvent.isShiftPressed());
        params.putBoolean("isLongPress", keyEvent.isLongPress());
        params.putBoolean("isCapsLockOn", keyEvent.isCapsLockOn());
        params.putBoolean("isCtrlPressed", keyEvent.isCtrlPressed());
        params.putInt("keyCode", keyCode);
        params.putInt("action", action);
        params.putBoolean("isModifierKey", modifiers);
        params.putString("pressedKey", String.valueOf(pressedKey));

        return params;
    }

    @Override
    public String getName() {
        return "KeyEventModule";
    }
}
