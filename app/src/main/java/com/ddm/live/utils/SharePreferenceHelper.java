package com.ddm.live.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Map;

//封装SharedPreferences类

public class SharePreferenceHelper {
    private Context context;
    private String filename = "live_private";
    SharedPreferences preferences;

    public SharePreferenceHelper(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(filename,
                Context.MODE_PRIVATE);
    }


    public SharedPreferences getPreferences() {
        return preferences;
    }

    public boolean saveSharePreference(Map<String, Object> map) {
        boolean flag = false;
        Editor editor = preferences.edit();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object object = entry.getValue();
            if (object instanceof Boolean) {
                boolean b = (boolean) object;
                editor.putBoolean(key, b);
            }
            if (object instanceof String) {
                String s = (String) object;
                editor.putString(key, s);
            }
            if (object instanceof Integer) {
                Integer i = (Integer) object;
                editor.putInt(key, i);
            }
            if (object instanceof Float) {
                Float f = (Float) object;
                editor.putFloat(key, f);
            }
            if (object instanceof Long) {
                Long l = (Long) object;
                editor.putLong(key, l);
            }
        }
        editor.commit();
        return flag;
    }

    //添加的方法
    public boolean set(String key, Object object) {
        Editor editor = preferences.edit();
        if (object instanceof Boolean) {
            boolean b = (boolean) object;
            editor.putBoolean(key, b);
        }
        if (object instanceof String) {
            String s = (String) object;
            editor.putString(key, s);
        }
        if (object instanceof Integer) {
            Integer i = (Integer) object;
            editor.putInt(key, i);
        }
        if (object instanceof Float) {
            Float f = (Float) object;
            editor.putFloat(key, f);
        }
        if (object instanceof Long) {
            Long l = (Long) object;
            editor.putLong(key, l);
        }
        return editor.commit();
    }

    public Object getValue(String key) {
        Object value = preferences.getString(key, "");
        return value;
    }


}

/*

//主函数中

private void setMsg() {

SharePreferenceHelper sharePreferenceHelper=new SharePreferenceHelper(this);

Map<String,Object>map=new HashMap<String,Object>();

map.put("username", "admin");

map.put("password", "123");

map.put("age", 23);

map.put("id", 1823855324);

map.put("isManager", true);

sharePreferenceHelper.saveSharePreference("sharefile", map);

}

 */