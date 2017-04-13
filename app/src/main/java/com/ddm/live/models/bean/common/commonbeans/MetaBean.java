package com.ddm.live.models.bean.common.commonbeans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;

/**
 * Created by cxx on 2016/8/10.
 */
public class MetaBean {
    @Expose
    @SerializedName("pagination")
    private Pagination pagination;

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public class Pagination {
        @Expose
        @SerializedName("total")
        private int total;

        @Expose
        @SerializedName("count")
        private int count;

        @Expose
        @SerializedName("per_page")
        private int perPage;

        @Expose
        @SerializedName("current_page")
        private int currentPage;

        @Expose
        @SerializedName("total_pages")
        private int totalPages;

        @Expose
        @SerializedName("links")
        private Object links;

        private String previous;
        private String next;

        public Object getLinks() {
            return links;
        }


      public String getPrevious() {

          //对返回结果的类型进行判断，以确定是否有分页
          if (links instanceof ArrayList) {
              setPrevious("0");
          } else if (links instanceof LinkedTreeMap) {

              if (null != ((LinkedTreeMap) links).get("previous")) {
                  String previousInfo = ((LinkedTreeMap) links).get("previous").toString();
                  setPrevious(previousInfo);
              }else{
                  setPrevious("0");
              }
          }

          return previous;
        }

        public void setPrevious(String previous) {
            this.previous = previous;
        }

        public String getNext() {


            //对返回结果的类型进行判断，以确定是否有分页
            if (links instanceof ArrayList) {
                setNext("0");

            } else if (links instanceof LinkedTreeMap) {
                if (null != ((LinkedTreeMap) links).get("next")) {

                    String nextInfo = ((LinkedTreeMap) links).get("next").toString();
                    setNext(nextInfo);
                }else{
                    setNext("0");
                }
            }
            return next;
        }

        public void setNext(String next) {
            this.next = next;
        }

        public void setLinks(Object links) {
            this.links = links;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getTotal() {
            return total;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }

        public void setPerPage(int perPage) {
            this.perPage = perPage;
        }

        public int getPerPage() {
            return perPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public int getTotalPages() {
            return totalPages;
        }

    }

}