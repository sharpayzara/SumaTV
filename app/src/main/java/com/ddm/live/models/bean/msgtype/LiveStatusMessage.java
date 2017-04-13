package com.ddm.live.models.bean.msgtype;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * Created by cxx on 2016/11/23.
 * 状态消息：0 主播暂时离开，1 主播回来，2 点赞
 */
@MessageTag(value = "LiveStatusMessage", flag = MessageTag.STATUS)
public class LiveStatusMessage extends MessageContent {
    private int type;

    public LiveStatusMessage(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public LiveStatusMessage(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
       try {
           JSONObject jsonObject=new JSONObject(jsonStr);
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
        dest.writeInt(type);
    }

    public static final Creator<LiveStatusMessage> CREATOR = new Creator<LiveStatusMessage>() {
        public LiveStatusMessage createFromParcel(Parcel in) {
            return new LiveStatusMessage(in);
        }

        public LiveStatusMessage[] newArray(int size) {
            return new LiveStatusMessage[size];
        }
    };

    private LiveStatusMessage(Parcel in) {
        type = in.readInt();
    }
}
