package fun.observe.touchy;

import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import java.lang.reflect.Field;


class DecorViewInfiltrator {

    private static Class classDecorView;
    private static Class classPopupDecorView;
    private static Class classPhoneWindow;

    static {
        try {
            classDecorView = Class.forName("com.android.internal.policy.DecorView");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            classPopupDecorView = Class.forName("android.widget.PopupWindow$PopupDecorView");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            classPhoneWindow = Class.forName("com.android.internal.policy.PhoneWindow$DecorView");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void useWindowCallbackWrapper(Object decorViewObject) throws NoSuchFieldException, IllegalAccessException {

        Field mWindowField = classDecorView.getDeclaredField("mWindow");
        mWindowField.setAccessible(true);
        Window mWindowObject = (Window) mWindowField.get(decorViewObject);
        Window.Callback callback = mWindowObject.getCallback();
        Window.Callback windowCallbackWrapper = new WindowCallbackWrapper(callback);
        mWindowObject.setCallback(windowCallbackWrapper);
    }

    private static void useTouchListenerWrapper(Object decorViewObject) throws Exception {

        Field this$0Field = classPopupDecorView.getDeclaredField("this$0");
        this$0Field.setAccessible(true);
        Object popupWindowObject = this$0Field.get(decorViewObject);
        Class classPopupWindow = PopupWindow.class;
        Field mTouchInterceptorField = classPopupWindow.getDeclaredField("mTouchInterceptor");
        mTouchInterceptorField.setAccessible(true);
        View.OnTouchListener mTouchInterceptorObject = (View.OnTouchListener) mTouchInterceptorField.get(popupWindowObject);
        View.OnTouchListener onTouchListenerWrapper = new OnTouchListenerWrapper(mTouchInterceptorObject);
        mTouchInterceptorField.set(popupWindowObject, onTouchListenerWrapper);
    }

    private static void useTouchListenerWrapperForPhoneDecor(Object decorViewObject) throws Exception {

        Field this$0Field = classPhoneWindow.getDeclaredField("this$0");
        this$0Field.setAccessible(true);
        Window mWindowObject = (Window) this$0Field.get(decorViewObject);
        Window.Callback callback = mWindowObject.getCallback();
        Window.Callback windowCallbackWrapper = new WindowCallbackWrapper(callback);
        mWindowObject.setCallback(windowCallbackWrapper);
    }

    static void infiltrateFor(Object objectDecorView) {

        try {
            if (classDecorView != null && classDecorView.isInstance(objectDecorView)) {
                useWindowCallbackWrapper(objectDecorView);
            } else if (classPhoneWindow != null && classPhoneWindow.isInstance(objectDecorView)) {
                useTouchListenerWrapperForPhoneDecor(objectDecorView);
            } else if (classPopupDecorView.isInstance(objectDecorView)) {
                useTouchListenerWrapper(objectDecorView);
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (Exception e) {//?
            e.printStackTrace();
        }
    }
}
