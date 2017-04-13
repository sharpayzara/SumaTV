package com.ddm.live.models.network.api;

import com.ddm.live.models.bean.streams.CreateStreamsRequest;
import com.ddm.live.models.bean.streams.CreateStreamsResponse;
import com.ddm.live.models.bean.streams.DeleteStreamResponse;
import com.ddm.live.models.bean.streams.GetMasterLiveListResponse;
import com.ddm.live.models.bean.streams.GetSpecStreamInfoResponse;
import com.ddm.live.models.bean.streams.GetStreamsByUserIDRequest;
import com.ddm.live.models.bean.streams.GetStreamsByUserIDResponse;
import com.ddm.live.models.bean.streams.GetStreamsListResponse;
import com.ddm.live.models.bean.streams.ModifyStreamStateAsEndLiveRequest;
import com.ddm.live.models.bean.streams.ModifyStreamStateAsEndLiveResponse;
import com.ddm.live.models.bean.streams.ModifyStreamStateAsLiveResponse;
import com.ddm.live.models.bean.streams.ModifyStreamWatchedNumberRequest;
import com.ddm.live.models.bean.streams.ModifyStreamWatchedNumberResponse;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by cxx on 2016/8/11.
 */
public interface StreamsApi {

    /**
     * 1.Streams_创建
     */
    @POST("streams")
    Observable<CreateStreamsResponse> creatStreams(@Body CreateStreamsRequest body);

    /**
     * SDK打包时:关闭此处代码
     * 2.Streams_获取视频流列表
     */
    @GET("streams")
    Observable<GetStreamsListResponse> getStreamsList(@QueryMap Map<String,Integer> page);

    /**
     * SDK打包时:打开此处代码
     * 2.Streams_获取视频流列表(SDK版本中不需要token)
     */
//    @GET("streams/list")
//    Observable<GetStreamsListResponse> getStreamsList();


    /**
     * 3.Streams_根据用户ID获取流
     */
    @POST("streams/userid")
    Observable<GetStreamsByUserIDResponse> getStreamsByUserId(@Body GetStreamsByUserIDRequest body,@QueryMap Map<String,Integer> pageMap);

    /**
     * 4.Streams_获取指定的流信息
     */
    @GET("streams/{qnId}")
    Observable<GetSpecStreamInfoResponse> getSpecMainInfo(@Path("qnId") String qnId);

    /**
     * 5.Streams_更改流状态为结束直播
     */
    @PUT("streams/{qnId}")
    Observable<ModifyStreamStateAsEndLiveResponse> modifyStreamStateAsEndLive(@Path("qnId") String qnId,
                                                                              @Body ModifyStreamStateAsEndLiveRequest body);

    /**
     * 6.Streams_删除指定的流信息
     */
    @GET("delete_stream/{id}")
    Observable<DeleteStreamResponse> deleteStream(@Path("id") String id);

    /**
     * 7.Streams_获取关注主播直播列表
     */
    @GET("streams/get_master_streams")
    Observable<GetMasterLiveListResponse> getMasterLiveList(@QueryMap Map<String,Integer> pageMap);

    /**
     * 8.Streams_修改观看流的观看人数
     */
    @PUT("streams/update_watched_number")
    Observable<ModifyStreamWatchedNumberResponse> modifyStreamWatchedNumber(@Body ModifyStreamWatchedNumberRequest body);

    /**
     * 9.Streams_更改流状态为直播状态
     */
    @PUT("streams/start/{qnId}")
    Observable<ModifyStreamStateAsLiveResponse> modifyStreamStateAsLive(@Path("qnId") String qnId);
}
