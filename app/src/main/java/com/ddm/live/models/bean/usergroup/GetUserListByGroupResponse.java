package com.ddm.live.models.bean.usergroup;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cxx on 2016/8/10.
 */
public class GetUserListByGroupResponse implements ResponseBaseInterface {
    @Expose
    @SerializedName("data")
    private List<Datas> data;

    public void setData(List<Datas> data) {
        this.data = data;
    }
    public List<Datas> getData() {
        return data;
    }

    public class Datas {
        @Expose
        @SerializedName("id")
        private int id;

        @Expose
        @SerializedName("tag")
        private String tag;

        @Expose
        @SerializedName("users")
        private List<Users> users;

        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
        public String getTag() {
            return tag;
        }

        public void setUsers(List<Users> users) {
            this.users = users;
        }
        public List<Users> getUsers() {
            return users;
        }

        public class Users {
            @Expose
            @SerializedName("id")
            private int id;

            @Expose
            @SerializedName("user_group_id")
            private int userGroupId;

            @Expose
            @SerializedName("name")
            private String name;

            @Expose
            @SerializedName("email")
            private String email;

            @Expose
            @SerializedName("phone_number")
            private String phoneNumber;

            @Expose
            @SerializedName("is_password")
            private int isPassword;

            @Expose
            @SerializedName("description")
            private String description;

            @Expose
            @SerializedName("gender")
            private int gender;

            @Expose
            @SerializedName("province")
            private String province;

            @Expose
            @SerializedName("city")
            private String city;

            @Expose
            @SerializedName("sign")
            private String sign;

            @Expose
            @SerializedName("birthday")
            private String birthday;

            @Expose
            @SerializedName("avatar")
            private String avatar;

            @Expose
            @SerializedName("user_number")
            private int userNumber;

            @Expose
            @SerializedName("watched_number")
            private int watchedNumber;

            @Expose
            @SerializedName("fans_number")
            private int fansNumber;

            @Expose
            @SerializedName("favours_number")
            private int favoursNumber;

            @Expose
            @SerializedName("experience_number")
            private int experienceNumber;

            @Expose
            @SerializedName("created_at")
            private String createdAt;

            @Expose
            @SerializedName("updated_at")
            private String updatedAt;

            @Expose
            @SerializedName("deleted_at")
            private String deletedAt;

            public void setId(int id) {
                this.id = id;
            }
            public int getId() {
                return id;
            }

            public void setUserGroupId(int userGroupId) {
                this.userGroupId = userGroupId;
            }
            public int getUserGroupId() {
                return userGroupId;
            }

            public void setName(String name) {
                this.name = name;
            }
            public String getName() {
                return name;
            }

            public void setEmail(String email) {
                this.email = email;
            }
            public String getEmail() {
                return email;
            }

            public void setPhoneNumber(String phoneNumber) {
                this.phoneNumber = phoneNumber;
            }
            public String getPhoneNumber() {
                return phoneNumber;
            }

            public void setIsPassword(int isPassword) {
                this.isPassword = isPassword;
            }
            public int getIsPassword() {
                return isPassword;
            }

            public void setDescription(String description) {
                this.description = description;
            }
            public String getDescription() {
                return description;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }
            public int getGender() {
                return gender;
            }

            public void setProvince(String province) {
                this.province = province;
            }
            public String getProvince() {
                return province;
            }

            public void setCity(String city) {
                this.city = city;
            }
            public String getCity() {
                return city;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }
            public String getSign() {
                return sign;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }
            public String getBirthday() {
                return birthday;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }
            public String getAvatar() {
                return avatar;
            }

            public void setUserNumber(int userNumber) {
                this.userNumber = userNumber;
            }
            public int getUserNumber() {
                return userNumber;
            }

            public void setWatchedNumber(int watchedNumber) {
                this.watchedNumber = watchedNumber;
            }
            public int getWatchedNumber() {
                return watchedNumber;
            }

            public void setFansNumber(int fansNumber) {
                this.fansNumber = fansNumber;
            }
            public int getFansNumber() {
                return fansNumber;
            }

            public void setFavoursNumber(int favoursNumber) {
                this.favoursNumber = favoursNumber;
            }
            public int getFavoursNumber() {
                return favoursNumber;
            }

            public void setExperienceNumber(int experienceNumber) {
                this.experienceNumber = experienceNumber;
            }
            public int getExperienceNumber() {
                return experienceNumber;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }
            public String getCreatedAt() {
                return createdAt;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }
            public String getUpdatedAt() {
                return updatedAt;
            }

            public void setDeletedAt(String deletedAt) {
                this.deletedAt = deletedAt;
            }
            public String getDeletedAt() {
                return deletedAt;
            }

        }
    }



}