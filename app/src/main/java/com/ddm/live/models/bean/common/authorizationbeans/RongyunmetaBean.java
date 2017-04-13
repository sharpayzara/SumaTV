package com.ddm.live.models.bean.common.authorizationbeans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/15.
 */
public class RongyunmetaBean {
    @Expose
    @SerializedName("rongyun_token")
    private RongyunToken tongyunToken;

    public RongyunToken getTongyunToken() {
        return tongyunToken;
    }

    public void setTongyunToken(RongyunToken tongyunToken) {
        this.tongyunToken = tongyunToken;
    }

    public class RongyunToken {
        @Expose
        @SerializedName("user_id")
        private Integer userID;
        @Expose
        @SerializedName("token")
        private String token;

        public Integer getUserID() {
            return userID;
        }

        public void setUserID(Integer userID) {
            this.userID = userID;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        @Override
        public String toString() {
            return "RongyunToken{" +
                    "userID='" + userID + '\'' +
                    ", token='" + token + '\'' +
                    '}';
        }
    }

}
