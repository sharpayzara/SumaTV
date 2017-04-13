package com.ddm.live.models.bean.msgtype;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * Created by cxx on 2016/11/23.
 */
@MessageTag(value = "LiveGiftMessage", flag = MessageTag.NONE)
public class LiveGiftMessage extends MessageContent {
    String content;
    int type;
    MsgUserInfoBean user;
    MsgGiftBean gift;

    public LiveGiftMessage(String content, int type, MsgUserInfoBean user, MsgGiftBean gift) {
        this.type = type;
        this.content = content;
        this.user = user;
        this.gift = gift;
    }

    public LiveGiftMessage(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj.has("content"))
                content = jsonObj.optString("content");
            if (jsonObj.has("type"))
                type = jsonObj.optInt("type");
            if (jsonObj.has("user"))
                user = MsgUserInfoBean.parsJsonToMsgUserInfoBean(jsonObj.optJSONObject("user"));
            if (jsonObj.has("gift"))
                gift = MsgGiftBean.parsJsonToMsgGiftBean(jsonObj.optJSONObject("gift"));

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
            if (MsgUserInfoBean.getJSONMsgUserInfo(getUser()) != null) {
                jsonObj.putOpt("user", MsgUserInfoBean.getJSONMsgUserInfo(getUser()));
            }
            if (MsgGiftBean.getJSONMsgGiftBean(getGift()) != null) {
                jsonObj.putOpt("gift", MsgGiftBean.getJSONMsgGiftBean(getGift()));
            }
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeInt(type);
        dest.writeParcelable(getUser(),2);
        dest.writeParcelable(getGift(), 3);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MsgUserInfoBean getUser() {
        return user;
    }

    public void setUser(MsgUserInfoBean user) {
        this.user = user;
    }

    public MsgGiftBean getGift() {
        return gift;
    }

    public void setGift(MsgGiftBean gift) {
        this.gift = gift;
    }

    public static final Creator<LiveGiftMessage> CREATOR = new Creator<LiveGiftMessage>() {
        public LiveGiftMessage createFromParcel(Parcel in) {
            return new LiveGiftMessage(in);
        }

        public LiveGiftMessage[] newArray(int size) {
            return new LiveGiftMessage[size];
        }
    };

    private LiveGiftMessage(Parcel in) {
        content = in.readString();
        type = in.readInt();
        setUser((MsgUserInfoBean) in.readParcelable((MsgUserInfoBean.class).getClassLoader()));
        setGift((MsgGiftBean)in.readParcelable(MsgGiftBean.class.getClassLoader()));
    }
}
