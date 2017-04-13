package com.ddm.live.utils;

import com.ddm.live.models.bean.common.commonbeans.ErrorResponseBean;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.exceptions.CompositeException;


/**
 * Created by cxx on 2016/8/8.
 * 访问异常返回bean
 */
public class GetErrorResponseBody {

    //获取返回的异常bean
    public static ErrorResponseBean getErrorResponseBody(Throwable e) {
        ErrorResponseBean errorResponseBody = null;

        try {
            if (e instanceof HttpException) {
                ResponseBody body = ((HttpException) e).response().errorBody();

                String errorString = body.string();
                Gson gson = new Gson();
                errorResponseBody = gson.fromJson(errorString, ErrorResponseBean.class);
            } else if (e instanceof CompositeException) {
                List<Throwable> exceptions = ((CompositeException) e).getExceptions();
                for (Throwable throwable : exceptions) {
                    System.out.println("捕获异常信息1:" + throwable.toString());
                }
            } else {
                System.out.println("捕获异常信息2:" + e.toString());
            }

        } catch (IOException IOe) {
            System.out.println("捕获异常信息3:" + IOe.toString());
        }

        return errorResponseBody;
    }

    //打印异常信息
    public static void printErrorInfos(String errorMessage, String errorStatusCode, Throwable e) {
        ErrorResponseBean errorResponseBody = GetErrorResponseBody.getErrorResponseBody(e);
        if (null != errorMessage) {
            System.out.println(errorMessage + errorResponseBody.getMessage());
            System.out.println(errorStatusCode + errorResponseBody.getStatusCode());
            System.out.println("**********************************************************************");
        } else {
            System.out.println("************非HttpException,获取不到errorResponseBody***************");
        }

    }
}
