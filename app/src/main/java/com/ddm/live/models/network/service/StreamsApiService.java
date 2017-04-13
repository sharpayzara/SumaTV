package com.ddm.live.models.network.service;

import com.ddm.live.models.bean.streams.CreateStreamsRequest;
import com.ddm.live.models.bean.streams.CreateStreamsResponse;
import com.ddm.live.models.bean.streams.DeleteStreamRequest;
import com.ddm.live.models.bean.streams.DeleteStreamResponse;
import com.ddm.live.models.bean.streams.GetMasterLiveListRequest;
import com.ddm.live.models.bean.streams.GetMasterLiveListResponse;
import com.ddm.live.models.bean.streams.GetSpecStreamInfoRequest;
import com.ddm.live.models.bean.streams.GetSpecStreamInfoResponse;
import com.ddm.live.models.bean.streams.GetStreamsByUserIDRequest;
import com.ddm.live.models.bean.streams.GetStreamsByUserIDResponse;
import com.ddm.live.models.bean.streams.GetStreamsListRequest;
import com.ddm.live.models.bean.streams.GetStreamsListResponse;
import com.ddm.live.models.bean.streams.ModifyStreamStateAsEndLiveRequest;
import com.ddm.live.models.bean.streams.ModifyStreamStateAsEndLiveResponse;
import com.ddm.live.models.bean.streams.ModifyStreamStateAsLiveRequest;
import com.ddm.live.models.bean.streams.ModifyStreamStateAsLiveResponse;
import com.ddm.live.models.bean.streams.ModifyStreamWatchedNumberRequest;
import com.ddm.live.models.bean.streams.ModifyStreamWatchedNumberResponse;
import com.ddm.live.models.layerinterfaces.RetrofitApiManager;
import com.ddm.live.models.network.api.StreamsApi;

import rx.Observable;

/**
 * Created by cxx on 2016/8/11.
 */
public class StreamsApiService extends BaseService {
    StreamsApi streamsApi;
    RetrofitApiManager retrofitApiManager;

    public StreamsApiService() {
        retrofitApiManager = new RetrofitApiManager();
        streamsApi = retrofitApiManager.provideWithHeaderApi().create(StreamsApi.class);
    }

    /**
     * 1.Streams_创建
     */
    public Observable<CreateStreamsResponse> creatStreams(CreateStreamsRequest request) {
        Observable<CreateStreamsResponse> response = streamsApi.creatStreams(request);
        return response;
    }

    /**
     * 2.Streams_获取流列表
     */
    public Observable<GetStreamsListResponse> getStreamsList(GetStreamsListRequest request) {
        Observable<GetStreamsListResponse> response = streamsApi.getStreamsList(request.getQueryMap());
        return response;
    }

    /**
     * 3.Streams_根据用户获取流
     */
    public Observable<GetStreamsByUserIDResponse> getStreamsByUserId(GetStreamsByUserIDRequest request) {
        Observable<GetStreamsByUserIDResponse> response = streamsApi.getStreamsByUserId(request,request.getPageMap());
        return response;
    }

    /**
     * 4.Streams_获取指定的流信息
     */
    public Observable<GetSpecStreamInfoResponse> getSpecMainInfo(GetSpecStreamInfoRequest request) {
        String qiniuId = request.getQiniuId();
        Observable<GetSpecStreamInfoResponse> response = streamsApi.getSpecMainInfo(qiniuId);
        return response;
    }

    /**
     * 5.Streams_更改流状态为结束直播
     */
    public Observable<ModifyStreamStateAsEndLiveResponse> modifyStreamStateAsEndLive(ModifyStreamStateAsEndLiveRequest request) {
        String qiniuId = request.getQiniuId();
        Observable<ModifyStreamStateAsEndLiveResponse> response = streamsApi.modifyStreamStateAsEndLive(qiniuId, request);
        return response;
    }

    /**
     * 6.Streams_删除指定的流信息
     */
    public Observable<DeleteStreamResponse> deleteStream(DeleteStreamRequest request) {
        String id = request.getId();
        Observable<DeleteStreamResponse> response = streamsApi.deleteStream(id);
        return response;
    }

    /**
     * 7.Streams_获取关注主播直播列表
     */
    public Observable<GetMasterLiveListResponse> getMasterLiveList(GetMasterLiveListRequest request) {
        Observable<GetMasterLiveListResponse> response = streamsApi.getMasterLiveList(request.getPageMap());
        return response;
    }

    /**
     * 8.Streams_修改观看流的观看人数
     */
    public Observable<ModifyStreamWatchedNumberResponse> modifyStreamWatchedNumber(ModifyStreamWatchedNumberRequest request) {
        Observable<ModifyStreamWatchedNumberResponse> response = streamsApi.modifyStreamWatchedNumber(request);
        return response;
    }
    /**
     * 9.Streams_修改流状态为直播状态
     */
    public Observable<ModifyStreamStateAsLiveResponse> modifyStreamStateAsLive(ModifyStreamStateAsLiveRequest request) {
        String qnId=request.getQnId();
        Observable<ModifyStreamStateAsLiveResponse> response = streamsApi.modifyStreamStateAsLive(qnId);
        return response;
    }

}
