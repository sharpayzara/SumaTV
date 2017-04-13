package com.ddm.live.models.bean.msgtype;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * Created by cxx on 2016/11/23.
 * type:0 普通聊天消息，1 进入房间消息，2 退出房间消息，3 关注消息，4，弹幕消息，5点亮
 */
@MessageTag(value = "LiveUserMessage", flag = MessageTag.STATUS)
public class LiveUserMessage extends MessageContent {
    private String content;
    private int type;
    private MsgUserInfoBean user;

    public LiveUserMessage(String content, int type, MsgUserInfoBean user) {
        this.content = content;
        this.type = type;
        this.user = user;
    }

    public String getConent() {
        return content;
    }

    public void setConent(String conent) {
        this.content = conent;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public MsgUserInfoBean getUser() {
        return user;
    }

    public void setUser(MsgUserInfoBean user) {
        this.user = user;
    }

    public LiveUserMessage(byte[] data) {
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
        out.writeParcelable(getUser(), 0);
    }

    public static final Creator<LiveUserMessage> CREATOR = new Creator<LiveUserMessage>() {
        @Override
        public LiveUserMessage createFromParcel(Parcel source) {
            return new LiveUserMessage(source);
        }

        @Override
        public LiveUserMessage[] newArray(int size) {
            return new LiveUserMessage[0];
        }
    };

    protected LiveUserMessage(Parcel in) {
        content = in.readString();
        type = in.readInt();
        setUser((MsgUserInfoBean) in.readParcelable((MsgUserInfoBean.class).getClassLoader()));
    }


}
