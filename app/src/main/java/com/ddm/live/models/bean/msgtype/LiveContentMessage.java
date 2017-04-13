package com.ddm.live.models.bean.msgtype;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * Created by cxx on 2016/11/23.
 * 内容消息
 */
@MessageTag(value = "LiveContentMessage", flag = MessageTag.NONE)
public class LiveContentMessage extends MessageContent {
    private String content;
    private int type;

    public LiveContentMessage(String content,int type) {
        this.content = content;
        this.type=type;
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

    public LiveContentMessage(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonObject=new JSONObject(jsonStr);
            if(jsonObject.has("content")){
                content=jsonObject.optString("content");
            }
            if(jsonObject.has("type")){
                type=jsonObject.optInt("type");
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj=new JSONObject();
        try {
            jsonObj.put("content",content);
            jsonObj.put("type",type);
        }catch (JSONException e){
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
    }

    public static final Creator<LiveContentMessage> CREATOR = new Creator<LiveContentMessage>() {
        public LiveContentMessage createFromParcel(Parcel in) {
            return new LiveContentMessage(in);
        }

        public LiveContentMessage[] newArray(int size) {
            return new LiveContentMessage[size];
        }
    };

    private LiveContentMessage(Parcel in) {
        content = in.readString();
        type = in.readInt();
    }
}
