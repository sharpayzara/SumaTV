package com.ddm.live.presenter;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.ddm.live.models.bean.common.commonbeans.ErrorResponseBean;
import com.ddm.live.models.bean.streams.DeleteStreamRequest;
import com.ddm.live.models.bean.streams.GetMasterLiveListRequest;
import com.ddm.live.models.bean.streams.GetMasterLiveListResponse;
import com.ddm.live.models.bean.streams.GetSpecStreamInfoRequest;
import com.ddm.live.models.bean.streams.GetSpecStreamInfoResponse;
import com.ddm.live.models.bean.streams.GetStreamsByUserIDRequest;
import com.ddm.live.models.bean.streams.GetStreamsByUserIDResponse;
import com.ddm.live.models.bean.streams.GetStreamsListRequest;
import com.ddm.live.models.bean.streams.GetStreamsListResponse;
import com.ddm.live.models.layerinterfaces.RequestInterface;
import com.ddm.live.models.network.service.LocationApiService;
import com.ddm.live.utils.GetErrorResponseBody;
import com.ddm.live.views.iface.IBaseView;
import com.ddm.live.views.iface.IListLiveView;
import com.ddm.live.views.iface.IUserVideoListView;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wsheng on 16/1/18.
 */
public class LiveListPresenter extends BasePresenter {

    private LocationApiService locationApiService;
    private IListLiveView iView;
    private IUserVideoListView iUserView;


    public void attachView(IBaseView view) {
        this.iView = (IListLiveView) view;
    }

    public void attachIView(IListLiveView iView) {
        this.iView = iView;
    }

    public void attachIView(IUserVideoListView iView) {
        this.iUserView = iView;
    }

    public LiveListPresenter(LocationApiService locationApiService) {
        this.locationApiService = locationApiService;
    }

    public void loadList(final int start, int limit) {

    }

    //获取流列表
    public void loadList2(final int page) {
        RequestInterface requestInterface = new RequestInterface();
        final GetStreamsListRequest request = new GetStreamsListRequest(page);
        requestInterface.sendRequest2(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseBaseInterface>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ErrorResponseBean errorResponseBean = GetErrorResponseBody.getErrorResponseBody(e);
                        if (null != errorResponseBean) {
                            String errorMessage = errorResponseBean.getMessage();
                            Integer errorStatusCode = errorResponseBean.getStatusCode();
//                            print("加载精选视频列表失败:" + errorMessage + ":" + errorStatusCode);
                            iView.onfailed(errorMessage);

                        } else {
                            iView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        GetStreamsListResponse streamsListResponse = (GetStreamsListResponse) response;
                        String next = streamsListResponse.getMeta().getPagination().getNext();
                        String previous = streamsListResponse.getMeta().getPagination().getPrevious();
                        int currentPage = streamsListResponse.getMeta().getPagination().getCurrentPage();
                        streamsListResponse.getMeta().getPagination().getTotalPages();
                        iView.setInfo2List(streamsListResponse.getData(), currentPage, (!previous.equals("0")), (!next.equals("0")));
                    }
                });
    }

    public void loadMasterList(final int page) {
        RequestInterface requestInterface = new RequestInterface();
        final GetMasterLiveListRequest request = new GetMasterLiveListRequest(page);
        requestInterface.sendRequest2(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseBaseInterface>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ErrorResponseBean errorResponseBean = GetErrorResponseBody.getErrorResponseBody(e);
                        if (null != errorResponseBean) {
                            String errorMessage = errorResponseBean.getMessage();
                            Integer errorStatusCode = errorResponseBean.getStatusCode();
//                            print("加载关注主播视频列表失败:" + errorMessage + ":" + errorStatusCode);
                            iView.onfailed(errorMessage);

                        } else {
                            iView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        GetMasterLiveListResponse getMasterLiveListResponse = (GetMasterLiveListResponse) response;
                        String next = getMasterLiveListResponse.getMeta().getPagination().getNext();
                        String previous = getMasterLiveListResponse.getMeta().getPagination().getPrevious();
                        int currentPage = getMasterLiveListResponse.getMeta().getPagination().getCurrentPage();
                        getMasterLiveListResponse.getMeta().getPagination().getTotalPages();
                        iView.setInfo2MasterList(getMasterLiveListResponse.getData(), currentPage, (!previous.equals("0")), (!next.equals("0")));
                    }
                });
    }


    //加载个人中心视频列表
    public void loadUserList2(final int page, final Integer userId) {
        GetStreamsByUserIDRequest request = new GetStreamsByUserIDRequest(userId, page);
        RequestInterface requestInterface = new RequestInterface();
        requestInterface.sendRequest2(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseBaseInterface>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ErrorResponseBean errorResponseBean = GetErrorResponseBody.getErrorResponseBody(e);
                        if (null != errorResponseBean) {
                            String errorMessage = errorResponseBean.getMessage();
                            Integer errorStatusCode = errorResponseBean.getStatusCode();
//                            print("加载个人中心视频列表失败:" + errorMessage + ":" + errorStatusCode);
                            iView.onfailed(errorMessage);

                        } else {
                            iView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
//                        try {
//                            print("业务层:" + response.getClass().getName() + ":" + gson.toJson(response));
//                        } catch (NullPointerException e) {
//                            print("无返回信息，或检查接口url是否更改");
//                        } catch (Exception e) {
//                            print(e.toString());
//                        }
                        GetStreamsByUserIDResponse streamsByUserIDResponse = (GetStreamsByUserIDResponse) response;
                        String next = streamsByUserIDResponse.getMeta().getPagination().getNext();
                        String previous = streamsByUserIDResponse.getMeta().getPagination().getPrevious();
                        int currentPage = streamsByUserIDResponse.getMeta().getPagination().getCurrentPage();
                        iView.setInfo2List(streamsByUserIDResponse.getData(), currentPage, (!previous.equals("0")), (!next.equals("0")));
                    }
                });
    }


    public void play2(final String qnId) {
        GetSpecStreamInfoRequest request = new GetSpecStreamInfoRequest(qnId);
        RequestInterface requestInterface = new RequestInterface();
        requestInterface.sendRequest2(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseBaseInterface>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ErrorResponseBean errorResponseBean = GetErrorResponseBody.getErrorResponseBody(e);
                        if (null != errorResponseBean) {
                            String errorMessage = errorResponseBean.getMessage();
                            Integer errorStatusCode = errorResponseBean.getStatusCode();
//                            print("业务层:" + errorMessage + ":" + errorStatusCode + ":视频资源异常");
                            iView.onfailed("视频资源异常");
                        } else {
                            iView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {

//                        try {
//                            print("业务层:" + response.getClass().getName() + ":" + gson.toJson(response));
//                        } catch (NullPointerException e) {
//                            print("无返回信息，或检查接口url是否更改");
//                        } catch (Exception e) {
//                            print(e.toString());
//                        }

                        GetSpecStreamInfoResponse specStreamInfoResponse = (GetSpecStreamInfoResponse) response;
                        iView.intentPlayerActivity(specStreamInfoResponse.getData());
                    }
                });
    }


    /**
     * 删除流切换流状态
     *
     * @param id 流id
     */
    public void deleteStream2(String id) {
        RequestInterface requestInterface = new RequestInterface();
        DeleteStreamRequest request = new DeleteStreamRequest(id);
        requestInterface.sendRequest2(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseBaseInterface>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ErrorResponseBean errorResponseBean = GetErrorResponseBody.getErrorResponseBody(e);
                        if (null != errorResponseBean) {
                            String errorMessage = errorResponseBean.getMessage();
                            Integer errorStatusCode = errorResponseBean.getStatusCode();
                            iView.onfailed(errorMessage);
                        } else {
                            iView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        try {
                            print("成功:" + response.getClass().getName() + ":" + gson.toJson(response));
                        } catch (NullPointerException e) {
//                            Log.d("zz", "删除成功，无返回值");
                            iView.deleteStreamResult("true");
                        } catch (Exception e) {
//                            print(e.toString());
                        }

                    }
                });
    }

}
