package com.ddm.live.presenter;

import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.ddm.live.models.bean.common.commonbeans.ErrorResponseBean;
import com.ddm.live.models.bean.users.GetRandomUsersRequest;
import com.ddm.live.models.bean.users.GetRandomUsersResponse;
import com.ddm.live.models.bean.users.GetSpecUserInfoRequest;
import com.ddm.live.models.bean.users.GetSpecUserInfoResponse;
import com.ddm.live.models.bean.users.SearchFansContributionListRequest;
import com.ddm.live.models.bean.users.SearchFansContributionListResponse;
import com.ddm.live.models.bean.users.SearchUsersByKeywordRequest;
import com.ddm.live.models.bean.users.SearchUsersByKeywordResponse;
import com.ddm.live.models.layerinterfaces.RequestInterface;
import com.ddm.live.utils.GetErrorResponseBody;
import com.ddm.live.views.iface.IUserInfoBaseView;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cxx on 2016/10/23.
 */
public class UserInfoPresenter extends BasePresenter {
    IUserInfoBaseView iUserInfoBaseView;

    public void attachView(IUserInfoBaseView iUserInfoBaseView) {
        this.iUserInfoBaseView = iUserInfoBaseView;
    }

    //随机获取一批用户测试
    public void getRandomUsers() {
        RequestInterface requestInterface = new RequestInterface();
        GetRandomUsersRequest request = new GetRandomUsersRequest();
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
//                            print("业务层:" + errorMessage + ":" + errorStatusCode);
                            iUserInfoBaseView.onfailed(errorMessage);
                        } else {
                            iUserInfoBaseView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        GetRandomUsersResponse userInfoResponse = (GetRandomUsersResponse) response;
                        iUserInfoBaseView.getUserInfoResult(userInfoResponse.getData());
//                        try {
//                            print("业务层:" + response.getClass().getName() + ":" + gson.toJson(response));
//                        } catch (NullPointerException e) {
//                            print("无返回信息，或检查接口url是否更改");
//                        } catch (Exception e) {
//                            print(e.toString());
//                        }
                    }
                });
    }


    /**
     * 查询用户的粉丝贡献榜单
     *
     * @param id
     */
    public void searchFansContributionList(Integer id) {
        SearchFansContributionListRequest request = new SearchFansContributionListRequest(id);
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
//                            print("查询用户粉丝贡献榜单失败:" + errorMessage + ":" + errorStatusCode);
                            iUserInfoBaseView.onfailed(errorMessage);
                        } else {
                            iUserInfoBaseView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        SearchFansContributionListResponse fansContributionListResponse = (SearchFansContributionListResponse) response;
                        iUserInfoBaseView.getFansContributionInfoResult(fansContributionListResponse.getData(), fansContributionListResponse.getMeta().getTotal());
                    }
                });
    }


    /**
     * 获取用户信息
     *
     * @param ids 查询用户ID
     * @return
     */
    public void getUserInfo(Integer[] ids) {
        final GetSpecUserInfoRequest request = new GetSpecUserInfoRequest(ids);
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
                            iUserInfoBaseView.onfailed(errorMessage);
                        } else {
                            iUserInfoBaseView.onfailed("当前网络状况不佳");
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
                        GetSpecUserInfoResponse specUserInfoResponse = (GetSpecUserInfoResponse) response;
                        iUserInfoBaseView.getSpecUserInfo(specUserInfoResponse.getData().get(0));
                    }
                });
    }

    /**
     * 根据关键字搜索用户
     */
    public void searchUsersByKeywords(String keyword) {
        SearchUsersByKeywordRequest request = new SearchUsersByKeywordRequest(keyword);
        final RequestInterface requestInterface = new RequestInterface();
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
                            iUserInfoBaseView.onfailed(errorMessage);
                        } else {
                            iUserInfoBaseView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface responseBaseInterface) {
                        SearchUsersByKeywordResponse response = (SearchUsersByKeywordResponse) responseBaseInterface;
                        iUserInfoBaseView.getUserInfoResult(response.getData());
                    }
                });
    }
}
