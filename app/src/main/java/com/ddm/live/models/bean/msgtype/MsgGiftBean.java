package com.ddm.live.models.bean.msgtype;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cxx on 2016/11/23.
 */

public class MsgGiftBean implements Parcelable {
    private int id;
    private String name;
    private int mode;
    private int onebyone;
    private String pic_url;

    public MsgGiftBean(int id, String name, int mode, int onebyone, String pic_url) {
        this.id = id;
        this.name = name;
        this.mode = mode;
        this.onebyone = onebyone;
        this.pic_url = pic_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getOnebyone() {
        return onebyone;
    }

    public void setOnebyone(int onebyone) {
        this.onebyone = onebyone;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public static final Creator<MsgGiftBean> CREATOR = new Creator<MsgGiftBean>() {
        @Override
        public MsgGiftBean createFromParcel(Parcel source) {
            return new MsgGiftBean(source);
        }

        @Override
        public MsgGiftBean[] newArray(int size) {
            return new MsgGiftBean[size];
        }
    };

    protected MsgGiftBean(Parcel in) {
        id=in.readInt();
        name=in.readString();
        mode=in.readInt();
        onebyone=in.readInt();
        pic_url=in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(mode);
        dest.writeInt(onebyone);
        dest.writeString(pic_url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static JSONObject getJSONMsgGiftBean(MsgGiftBean giftBean){
        if(giftBean!=null){
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("id",giftBean.getId());
                jsonObject.put("name",giftBean.getName());
                jsonObject.put("mode",giftBean.getMode());
                jsonObject.put("onebyone",giftBean.getOnebyone());
                jsonObject.put("pic_url",giftBean.getPic_url());
            }catch (JSONException e){
                e.printStackTrace();
            }

            return jsonObject;
        }
        return null;
    }

    public static MsgGiftBean parsJsonToMsgGiftBean(JSONObject jsonObj) {

        int id = jsonObj.optInt("id");
        String name = jsonObj.optString("name");
        int mode = jsonObj.optInt("mode");
        int onebyone = jsonObj.optInt("onebyone");
        String pic_url = jsonObj.optString("pic_url");

        MsgGiftBean giftBean = new MsgGiftBean(id,name,mode,onebyone,pic_url);
        return giftBean;
    }

}
