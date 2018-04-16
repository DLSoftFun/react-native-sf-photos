package com.sf.sfphotopicker.imagepicker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.sf.sfphotopicker.multi_image_selector.MultiImageSelectorActivity;
import com.sf.sfphotopicker.multi_image_selector.data.Image;
import com.sf.sfphotopicker.sflib.utils.CheckPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2018-03-30.
 */

public class PickerModule extends ReactContextBaseJavaModule implements ActivityEventListener {
    private Promise promise;
    private final int CAMERA = 200;
    private static final String E_ACTIVITY_DOES_NOT_EXIST = "E_ACTIVITY_DOES_NOT_EXIST";
    private List<Image> data_list = new ArrayList<>();

    public PickerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addActivityEventListener(this);
    }

    @Override
    public String getName() {
        return "SFImagePicker";
    }

    @ReactMethod
    public void select(final ReadableMap readableMap, final Promise promise) {
        this.promise = promise;
        final Activity activity = getCurrentActivity();
        final PickerModule module = this;

        if (activity == null) {
            promise.reject(E_ACTIVITY_DOES_NOT_EXIST, "Activity doesn't exist");
            return;
        }

        CheckPermission.CheckPermission(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, new CheckPermission.CheckPermissionCallback() {
            @Override
            public void onResult() {
                int type = 2;
                int number = 9;
                boolean isSingle = false;
                boolean isCrop = false;
                if(readableMap!=null){
                    type = readableMap.getInt("type");
                    number = readableMap.getInt("number");
                    isSingle = readableMap.getBoolean("isSingle");
                    isCrop = readableMap.getBoolean("isCrop");
                    if(!isSingle){
                        isCrop = false;
                    }
                }
                final Intent intent = new Intent(activity, MultiImageSelectorActivity.class);
                intent.putExtra("data", (Serializable) data_list);
                intent.putExtra("type",type);
                intent.putExtra("number",number);
                intent.putExtra("isSingle",isSingle);
                intent.putExtra("isCrop",isCrop);
                activity.startActivityForResult(intent, CAMERA);
            }
        });
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA && resultCode == RESULT_OK) {
            int type = data.getExtras().getInt("type");
            if(type == 0){
                ArrayList<Image> list = (ArrayList<Image>) data.getExtras().getSerializable("data");
//                JSONArray jsonArray = new JSONArray();
//                Object[] s = new Object[list.size()];
                WritableMap map = new WritableNativeMap();
                WritableArray array = new WritableNativeArray();
                for (int i = 0; i < list.size(); i++) {
//                    jsonArray.put(Uri.fromFile(new File(list.get(i).getPath())).toString());
                    array.pushString(Uri.fromFile(new File(list.get(i).getPath())).toString());
//                    s[i] = Uri.fromFile(new File(list.get(i).getPath())).toString();
                }
                map.putArray("allList",array);
                this.promise.resolve(map);
            }else if(type == 1){
                WritableMap map = new WritableNativeMap();
                WritableArray array = new WritableNativeArray();
                Uri uri = data.getExtras().getParcelable("data");
                array.pushString(uri.toString());
                map.putArray("allList",array);
                this.promise.resolve(map);
            }

        }
    }

    @Override
    public void onNewIntent(Intent intent) {

    }
}
