package com.ddm.live.presenter;

import com.ddm.live.AppApplication;
import com.ddm.live.models.bean.basebean.baseinterface.ResponseBaseInterface;
import com.ddm.live.models.bean.common.commonbeans.ErrorResponseBean;
import com.ddm.live.models.bean.fans.FansCancelFocusedMasterByIdRequest;
import com.ddm.live.models.bean.fans.FansFocusOnMasterByIDRequest;
import com.ddm.live.models.bean.fans.FansGetFansListRequest;
import com.ddm.live.models.bean.fans.FansGetFansListResponse;
import com.ddm.live.models.bean.fans.FansGetFocusedMastersListRequest;
import com.ddm.live.models.bean.fans.FansGetFocusedMastersListResponse;
import com.ddm.live.models.bean.file.QiniuUploadTokenRequest;
import com.ddm.live.models.bean.file.QiniuUploadTokenResponse;
import com.ddm.live.models.bean.user.AppUser;
import com.ddm.live.models.bean.users.GetSpecUserInfoRequest;
import com.ddm.live.models.bean.users.GetSpecUserInfoResponse;
import com.ddm.live.models.bean.users.ModifySpecUserGenderRequest;
import com.ddm.live.models.bean.users.ModifySpecUserHeaderRequest;
import com.ddm.live.models.bean.users.ModifySpecUserInfoResponse;
import com.ddm.live.models.bean.users.ModifySpecUserNameRequest;
import com.ddm.live.models.bean.users.ModifySpecUserSignRequest;
import com.ddm.live.models.layerinterfaces.RequestInterface;
import com.ddm.live.utils.GetErrorResponseBody;
import com.ddm.live.views.iface.IUserCenterView;
import com.ddm.live.views.iface.IUserFansView;
import com.ddm.live.views.iface.IUserLoginOutView;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wsheng on 16/1/28.
 */
public class UserCenterPresenter extends BasePresenter {

    private IUserCenterView iView;
    private IUserLoginOutView iLoginoutView;
    private IUserFansView iUserFansView;
    private AppApplication appApplication;

    public void attachView(IUserCenterView iUserCenterView) {
        this.iView = iUserCenterView;
    }

    public void attachView(IUserLoginOutView iUserLoginOutView) {
        this.iLoginoutView = iUserLoginOutView;
    }

    public void attachView(IUserFansView iUserFansView) {
        this.iUserFansView = iUserFansView;
    }

    /**
     * 获取七牛上传图片token
     */

    public void getHeaderImgUploadToken() {
        appApplication = AppApplication.getInstance();
        final QiniuUploadTokenRequest request = new QiniuUploadTokenRequest();
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
                            String errorContent = errorResponseBean.getErrors().getName()[0];
//                            Integer errorStatusCode = errorResponseBean.getStatusCode();
//                            print("修改指定用户信息失败:" + errorMessage + ":" + errorStatusCode);
                            iView.onfailed(errorContent);

                        } else {
                            iView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        try {
                            print("业务层:" + response.getClass().getName() + ":" + gson.toJson(response));
                        } catch (NullPointerException e) {
                            print("无返回信息，或检查接口url是否更改");
                        } catch (Exception e) {
                            print(e.toString());
                        }
                        QiniuUploadTokenResponse uploadTokenResponse = (QiniuUploadTokenResponse) response;
                        String token = uploadTokenResponse.getToken();
                        String host = uploadTokenResponse.getHost();
                        iView.getUploadToken(token, host);
                    }
                });
    }

    /**
     * 修改用户头像
     *
     * @param headerUrl 修改用户头像
     */
    public void changeUserHeader(final String headerUrl) {
        appApplication = AppApplication.getInstance();
        final AppUser appUser = appApplication.getAppUser();
        final ModifySpecUserHeaderRequest request = new ModifySpecUserHeaderRequest(headerUrl);
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
                            String errorContent = errorResponseBean.getErrors().getName()[0];
                            iView.onfailed(errorContent);
                        } else {
                            iView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        try {
                            print("业务层:" + response.getClass().getName() + ":" + gson.toJson(response));
                        } catch (NullPointerException e) {
                            print("无返回信息，或检查接口url是否更改");
                        } catch (Exception e) {
                            print(e.toString());
                        }
                        ModifySpecUserInfoResponse userInfoResponse = (ModifySpecUserInfoResponse) response;
                        String headImgUrl = userInfoResponse.getData().getAvatar();
                        String smallHeadImgUrl = userInfoResponse.getData().getAvatarSmall();

                        appUser.setHeadimgurl(headImgUrl);
                        appUser.setSmallHeadimgurl(smallHeadImgUrl);
                        iView.changeResult();
                    }
                });
    }

    /**
     * 修改用户名称
     *
     * @param uname 修改用户名
     */
    public void changeUserName(final String uname) {
        appApplication = AppApplication.getInstance();
        final AppUser appUser = appApplication.getAppUser();
        final ModifySpecUserNameRequest request = new ModifySpecUserNameRequest(uname);
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
                            String errorContent = errorResponseBean.getErrors().getName()[0];
//                            Integer errorStatusCode = errorResponseBean.getStatusCode();
//                            print("修改指定用户信息失败:" + errorMessage + ":" + errorStatusCode);
                            iView.onfailed(errorContent);
                        } else {
                            iView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        try {
                            print("业务层:" + response.getClass().getName() + ":" + gson.toJson(response));
                        } catch (NullPointerException e) {
                            print("无返回信息，或检查接口url是否更改");
                        } catch (Exception e) {
                            print(e.toString());
                        }
//                        ModifySpecUserInfoResponse userInfoResponse = (ModifySpecUserInfoResponse) response;
                        appUser.setNickname(uname);
                        iView.changeResult();
                    }
                });
    }

    /**
     * 修改用户性别
     *
     * @param gender 修改用户性别
     */
    public void changeUserGender(final int gender) {
        appApplication = AppApplication.getInstance();
        final AppUser appUser = appApplication.getAppUser();
        final ModifySpecUserGenderRequest request = new ModifySpecUserGenderRequest(gender);
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
                            String errorContent = errorResponseBean.getErrors().getName()[0];
//                            Integer errorStatusCode = errorResponseBean.getStatusCode();
//                            print("修改指定用户信息失败:" + errorMessage + ":" + errorStatusCode);
                            iView.onfailed(errorContent);
                        } else {
                            iView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        try {
                            print("业务层:" + response.getClass().getName() + ":" + gson.toJson(response));
                        } catch (NullPointerException e) {
                            print("无返回信息，或检查接口url是否更改");
                        } catch (Exception e) {
                            print(e.toString());
                        }
//                        ModifySpecUserInfoResponse userInfoResponse = (ModifySpecUserInfoResponse) response;
                        appUser.setGender(gender);
                        iView.changeResult();
                    }
                });
    }

    /**
     * 修改用户介绍
     *
     * @param sign 修改介绍
     */
    public void changeUserSign(final String sign) {
        appApplication = AppApplication.getInstance();
        final AppUser appUser = appApplication.getAppUser();
        final ModifySpecUserSignRequest request = new ModifySpecUserSignRequest(sign);
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
                            String errorContent = errorResponseBean.getErrors().getName()[0];
//                            Integer errorStatusCode = errorResponseBean.getStatusCode();
//                            print("修改指定用户信息失败:" + errorMessage + ":" + errorStatusCode);
                            iView.onfailed(errorContent);
                        } else {

                            iView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        try {
                            print("业务层:" + response.getClass().getName() + ":" + gson.toJson(response));
                        } catch (NullPointerException e) {
                            print("无返回信息，或检查接口url是否更改");
                        } catch (Exception e) {
                            print(e.toString());
                        }
//                        ModifySpecUserInfoResponse userInfoResponse = (ModifySpecUserInfoResponse) response;
                        appUser.setSign(sign);
                        iView.changeResult();
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
                            print("加载个人中心视频列表失败:" + errorMessage + ":" + errorStatusCode);
                            iView.onfailed(errorMessage);
                        } else {
                            iView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        try {
                            print("业务层:" + response.getClass().getName() + ":" + gson.toJson(response));
                        } catch (NullPointerException e) {
                            print("无返回信息，或检查接口url是否更改");
                        } catch (Exception e) {
                            print(e.toString());
                        }
                        GetSpecUserInfoResponse specUserInfoResponse = (GetSpecUserInfoResponse) response;
                        iView.getUserInfo(specUserInfoResponse.getData().get(0));
                    }
                });
    }

    /**
     * 获取关注/粉丝用户信息
     *
     * @param ids 查询用户ID
     * @return
     */
    public void getUserFocusOrFansInfo(Integer[] ids) {
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
                            print("加载个人中心失败:" + errorMessage + ":" + errorStatusCode);
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        try {
                            print("业务层:" + response.getClass().getName() + ":" + gson.toJson(response));
                        } catch (NullPointerException e) {
                            print("无返回信息，或检查接口url是否更改");
                        } catch (Exception e) {
                            print(e.toString());
                        }
                        GetSpecUserInfoResponse specUserInfoResponse = (GetSpecUserInfoResponse) response;
//                        int fansNum = specUserInfoResponse.getData().get(0).getFans().getData().getFansCount();
//                        int masterNum = specUserInfoResponse.getData().get(0).getFans().getData().getMastersCount();
//                        iLoginoutView.getFocusOrFansInfo(fansNum, masterNum);
                        iLoginoutView.getFocusOrFansInfo(specUserInfoResponse.getData().get(0));
                    }
                });
    }

    /**
     * 获取关注用户列表信息
     *
     * @param id 用户id
     */
    public void getFocusUserInfo(Integer id) {
        final FansGetFocusedMastersListRequest request = new FansGetFocusedMastersListRequest(id);
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
//                            print("获取关注用户信息:" + errorMessage + ":" + errorStatusCode);
                            iUserFansView.onfailed(errorMessage);
                        } else {
                            iUserFansView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        try {
                            print("业务层:" + response.getClass().getName() + ":" + gson.toJson(response));
                        } catch (NullPointerException e) {
                            print("无返回信息，或检查接口url是否更改");
                        } catch (Exception e) {
                            print(e.toString());
                        }
                        FansGetFocusedMastersListResponse fansGetFocusedMastersListResponse = (FansGetFocusedMastersListResponse) response;
                        iUserFansView.getFocusUserInfo(fansGetFocusedMastersListResponse.getData());
                    }
                });
    }

    /**
     * 获取粉丝信息列表
     *
     * @param id 用户ID
     */
    public void getFansUserInfo(Integer id) {
        final FansGetFansListRequest request = new FansGetFansListRequest(id);
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
//                            print("获取粉丝用户信息:" + errorMessage + ":" + errorStatusCode);
                            iUserFansView.onfailed(errorMessage);
                        } else {
                            iUserFansView.onfailed("当前网络状况不佳");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        try {
                            print("业务层:" + response.getClass().getName() + ":" + gson.toJson(response));
                        } catch (NullPointerException e) {
                            print("无返回信息，或检查接口url是否更改");
                        } catch (Exception e) {
                            print(e.toString());
                        }
                        FansGetFansListResponse fansGetFansListResponse = (FansGetFansListResponse) response;
                        iUserFansView.getFansUserInfo(fansGetFansListResponse.getData());
                    }
                });
    }

    /**
     * 取消关注
     *
     * @param aid 被关注主播id
     */
    public void userCancelFocusMaster(Integer aid) {
        RequestInterface requestInterface = new RequestInterface();
        final FansCancelFocusedMasterByIdRequest request = new FansCancelFocusedMasterByIdRequest(aid);
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
//                            print("取消关注失败:" + errorMessage + ":" + errorStatusCode);
                            iView.onfailed(errorMessage);
                            iView.cancelFocusResult(false, "关注失败，请稍后再试");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        try {
//                            print("取消关注成功:" + response.getClass().getName() + ":" + gson.toJson(response));
                            iView.cancelFocusResult(true, "取消关注");
                        } catch (NullPointerException e) {
//                            print("取消关注无返回信息，或检查接口url是否更改");
                        } catch (Exception e) {
                            print(e.toString());
                        }

                    }
                });
    }

    /**
     * 关注
     *
     * @param ids 目标ID
     */
    public void userFocusMaster(Integer[] ids) {
        RequestInterface requestInterface = new RequestInterface();
        FansFocusOnMasterByIDRequest request = new FansFocusOnMasterByIDRequest(ids);
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
                            print("关注主播失败:" + errorMessage + ":" + errorStatusCode);
                            iView.onfailed(errorMessage);
                            iView.addFocusResult(false, "关注失败，请稍后再试");
                        }
                    }

                    @Override
                    public void onNext(ResponseBaseInterface response) {
                        try {
                            print("关注主播成功:" + response.getClass().getName() + ":" + gson.toJson(response));
                            iView.addFocusResult(true, "关注成功");
                        } catch (NullPointerException e) {
                            print("关注主播无返回信息，或检查接口url是否更改");
                            iView.addFocusResult(true, "关注成功");
                        } catch (Exception e) {
                            print(e.toString());
                        }
                    }
                });
    }

}
