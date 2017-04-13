package com.ddm.live.models.bean.common.commonbeans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cxx on 2016/8/11.
 */
public class Links {
    @Expose
    @SerializedName("previous")
    private String previous;
    @Expose
    @SerializedName("next")
    private String next;

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "Links{" +
                "previous='" + previous + '\'' +
                ", next='" + next + '\'' +
                '}';
    }
}
