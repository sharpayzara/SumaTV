package com.ddm.live.models.bean.basebean.request;

/**
 * Created by wsheng on 16/1/12.
 */
public class RequestData {
    private RequestHeader header;

    private RequestBody body;


    public RequestData(String code,RequestBody body) {
        header = new RequestHeader(code,String.valueOf(System.currentTimeMillis()));
        this.body = body;
        /*
        Gson gson = new Gson();
        System.out.println("发送的数据:");
        System.out.println(gson.toJson(this));
        返回的数据如果是json则打印出来,注意不能直接用下面的语句,如果不是对象,则会报错
        Log.e("zz",gson.toJson(this));
         */



    }

    public RequestData(RequestHeader header, RequestBody body) {
        this.header = header;
        this.body = body;
    }


    public RequestHeader getHeader() {
        return header;
    }

    public void setHeader(RequestHeader header) {
        this.header = header;
    }

    public RequestBody getBody() {
        return body;
    }

    public void setBody(RequestBody body) {
        this.body = body;
    }

}
