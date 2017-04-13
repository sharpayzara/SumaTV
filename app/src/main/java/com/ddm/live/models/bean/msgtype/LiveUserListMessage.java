package com.ddm.live.models.bean.msgtype;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * Created by cxx on 2017/2/28.
 */
@MessageTag(value = "LiveUserListMessage", flag = MessageTag.NONE)
public class LiveUserListMessage extends MessageContent {
    private String content;
    private int type;
    private String userListJsonString;

    public LiveUserListMessage(String content, int type, String userListJsonString) {
        this.content = content;
        this.type = type;
        this.userListJsonString=userListJsonString;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserListJsonString() {
        return userListJsonString;
    }

    public void setUserListJsonString(String userListJsonString) {
        this.userListJsonString = userListJsonString;
    }

    public LiveUserListMessage(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj.has("content")) {
                content = jsonObj.optString("content");

            }
            if (jsonObj.has("type")) {
                type = jsonObj.optInt("type");
            }
            if (jsonObj.has("userListJsonString")) {
                userListJsonString = jsonObj.optString("userListJsonString");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("content", content);
            jsonObj.put("type", type);
            jsonObj.put("userListJsonString", userListJsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(content);
        out.writeInt(type);
        out.writeString(userListJsonString);
    }

    public static final Creator<LiveUserListMessage> CREATOR = new Creator<LiveUserListMessage>() {
        @Override
        public LiveUserListMessage createFromParcel(Parcel source) {

            return new LiveUserListMessage(source);
        }

        @Override
        public LiveUserListMessage[] newArray(int size) {
            return new LiveUserListMessage[0];
        }
    };

    protected LiveUserListMessage(Parcel in) {
        content = in.readString();
        type = in.readInt();
        userListJsonString = in.readString();
    }

}
