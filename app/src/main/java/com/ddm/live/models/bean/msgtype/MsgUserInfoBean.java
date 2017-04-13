package com.ddm.live.models.bean.msgtype;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cxx on 2016/11/23.
 */
//@MessageTag(value = "MsgUserInfoBean", flag = MessageTag.STATUS)
public class MsgUserInfoBean implements Parcelable {
    private String avatar;
    private String avatar_small;
    private String city;
    private int gender;
    private Integer id;
    private int is_followed;
    private Integer level;
    private String name;
    private int user_number;
    private String type_image;

    public MsgUserInfoBean(String avatar, String avatar_small, String city, int gender, Integer id,
                           int is_followed, Integer level, String name, int user_number, String type_image) {
        this.avatar = avatar;
        this.avatar_small = avatar_small;
        this.city = city;
        this.gender = gender;
        this.id = id;
        this.is_followed = is_followed;
        this.level = level;
        this.name = name;
        this.user_number = user_number;
        this.type_image = type_image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(avatar);
        out.writeString(avatar_small);
        out.writeString(city);
        out.writeInt(gender);
        out.writeInt(id);
        out.writeInt(is_followed);
        out.writeInt(level);
        out.writeString(name);
        out.writeInt(user_number);
        out.writeString(type_image);
    }

    protected MsgUserInfoBean(Parcel in) {
        avatar = in.readString();
        avatar_small = in.readString();
        city = in.readString();
        gender = in.readInt();
        id = in.readInt();
        is_followed = in.readInt();
        level = in.readInt();
        name = in.readString();
        user_number = in.readInt();
        type_image = in.readString();
    }

    public static final Creator<MsgUserInfoBean> CREATOR = new Creator<MsgUserInfoBean>() {
        @Override
        public MsgUserInfoBean createFromParcel(Parcel source) {
            return new MsgUserInfoBean(source);
        }

        @Override
        public MsgUserInfoBean[] newArray(int size) {
            return new MsgUserInfoBean[size];
        }
    };


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar_small() {
        return avatar_small;
    }

    public void setAvatar_small(String avatar_small) {
        this.avatar_small = avatar_small;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getIs_followed() {
        return is_followed;
    }

    public void setIs_followed(int is_followed) {
        this.is_followed = is_followed;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUser_number() {
        return user_number;
    }

    public void setUser_number(int user_number) {
        this.user_number = user_number;
    }

    public String getType_image() {
        return type_image;
    }

    public void setType_image(String type_image) {
        this.type_image = type_image;
    }

    public static JSONObject getJSONMsgUserInfo(MsgUserInfoBean infoBean){
        if(infoBean!=null){
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("avatar",infoBean.getAvatar());
                jsonObject.put("avatar_small",infoBean.getAvatar_small());
                jsonObject.put("city",infoBean.getCity());
                jsonObject.put("gender",infoBean.getGender());
                jsonObject.put("id",infoBean.getId());
                jsonObject.put("is_followed",infoBean.getIs_followed());
                jsonObject.put("level",infoBean.getLevel());
                jsonObject.put("name",infoBean.getName());
                jsonObject.put("user_number",infoBean.getUser_number());
                jsonObject.put("type_image",infoBean.getType_image());
            }catch (JSONException e){
                e.printStackTrace();
            }

            return jsonObject;
        }
        return null;
    }

    public static MsgUserInfoBean parsJsonToMsgUserInfoBean(JSONObject jsonObj) {

        String avatar = jsonObj.optString("avatar");
        String avatar_small = jsonObj.optString("avatar_small");
        String city = jsonObj.optString("city");
        int gender = jsonObj.optInt("gender");
        int id = jsonObj.optInt("id");
        int is_followed = jsonObj.optInt("is_followed");
        Integer level = jsonObj.optInt("level");
        String name = jsonObj.optString("name");
        int user_number = jsonObj.optInt("user_number");
        String type_image = jsonObj.optString("type_image");
        MsgUserInfoBean infoBean = new MsgUserInfoBean(avatar, avatar_small, city, gender, id, is_followed, level, name, user_number, type_image);
        return infoBean;
    }
}
