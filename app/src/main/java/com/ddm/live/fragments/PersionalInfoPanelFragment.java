package com.ddm.live.fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ddm.live.AppApplication;
import com.ddm.live.R;
import com.ddm.live.R2;
import com.ddm.live.activities.GagListActivity;
import com.ddm.live.activities.UserInfoHomeActivity;
import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.inject.components.DaggerNetworkingComponent;
import com.ddm.live.inject.components.DaggerPresenterComponent;
import com.ddm.live.inject.components.NetworkingComponent;
import com.ddm.live.inject.components.PresenterComponent;
import com.ddm.live.inject.modules.PresenterModule;
import com.ddm.live.models.bean.common.userbeans.SpecUserInfoBean;
import com.ddm.live.models.bean.user.AppUser;
import com.ddm.live.presenter.UserCenterPresenter;
import com.ddm.live.ui.ActionSheetDialog;
import com.ddm.live.ui.widget.BottomView;
import com.ddm.live.ui.widget.CustomDialog;
import com.ddm.live.utils.DrawableSettingUtils;
import com.ddm.live.views.iface.IOnTouchView;
import com.ddm.live.views.iface.IUserCenterView;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cxx on 2016/10/14.
 */
public class PersionalInfoPanelFragment extends DialogFragment implements IUserCenterView {

    private IOnTouchView iOnTouchView;

    public void attachFramgent(IOnTouchView iOnTouchView) {
        this.iOnTouchView = iOnTouchView;
    }

    private SpecUserInfoBean userInfoBean;
    private Integer uID;
    private Integer[] ids;
    private boolean isFollowed;
    private AppUser appUser;
    private BottomView bottomView;
    private ListView lvBottomList;
    private TextView cancel;
    private ArrayList<String> menus;
    protected CustomDialog gagDialog;
    protected CustomDialog.Builder gagBuilder;
    @Inject
    UserCenterPresenter userCenterPresenter;
    @BindView(R2.id.headImage)
    ImageView headImage;
    @BindView(R2.id.user_name)
    TextView userName;
    @BindView(R2.id.focus_number)
    TextView focusNumText;
    @BindView(R2.id.fans_number)
    TextView fansNumText;
    @BindView(R2.id.focus_cancel_btn)
    TextView focusBtn;
    @BindView(R2.id.level)
    ImageView levelImage;
    @BindView(R2.id.sex)
    ImageView sex;
    @BindView(R2.id.le_id)
    TextView leID;
    @BindView(R2.id.sign)
    TextView sign;
    @BindView(R2.id.user_info_level_tag)
    ImageView userLevelImage;
    @BindView(R2.id.bottom_pannel)
    LinearLayout bottomPannel;

    @OnClick(R2.id.headImage)
    public void onHeadImageClick() {
        if (!this.getTag().equals("LiveMasterPersionalInfoPanel")) {
            PersionalInfoPanelFragment.this.dismiss();
            Intent intent = new Intent(getActivity(), UserInfoHomeActivity.class);
            try {
                intent.putExtra("uID", userInfoBean.getId());
                intent.putExtra("userImg", userInfoBean.getAvatarSmall());
                intent.putExtra("username", userInfoBean.getName());
                intent.putExtra("gender", userInfoBean.getGender());
                intent.putExtra("level", userInfoBean.getLevel());
                intent.putExtra("userNumber", userInfoBean.getUserNumber());
                intent.putExtra("sign", userInfoBean.getSign());
                intent.putExtra("fanCount", userInfoBean.getFansNumber());
                intent.putExtra("masterCount", userInfoBean.getFansNumber());
                intent.putExtra("isFollowed", isFollowed);
            } catch (NullPointerException e) {

            }

            startActivity(intent);
        }
    }

    @OnClick(R2.id.report_btn)
    public void report() {
        //举报
        Toast.makeText(getContext(), "已举报该用户", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R2.id.focus_cancel_btn)
    public void focusOrCancelFocus() {
        //添加关注或取消关注
        if (uID != appUser.getId()) {
            if (isFollowed) {
                userCenterPresenter.userCancelFocusMaster(uID);
            } else {
                userCenterPresenter.userFocusMaster(ids);
            }
        }
    }

    @OnClick(R2.id.home_page_btn)
    public void homePage() {
        if (!this.getTag().equals("LiveMasterPersionalInfoPanel")) {
            //跳转到个人主页
            PersionalInfoPanelFragment.this.dismiss();
            Intent intent = new Intent(getActivity(), UserInfoHomeActivity.class);
            try {
                intent.putExtra("uID", userInfoBean.getId());
                intent.putExtra("userImg", userInfoBean.getAvatarSmall());
                intent.putExtra("username", userInfoBean.getName());
                intent.putExtra("gender", userInfoBean.getGender());
                intent.putExtra("level", userInfoBean.getLevel());
                intent.putExtra("userNumber", userInfoBean.getUserNumber());
                intent.putExtra("sign", userInfoBean.getSign());
                intent.putExtra("fanCount", userInfoBean.getFansNumber());
                intent.putExtra("masterCount", userInfoBean.getFansNumber());
                intent.putExtra("isFollowed", isFollowed);
            } catch (NullPointerException e) {

            }
            startActivity(intent);
        }
    }

    @BindView(R2.id.manage_btn)
    TextView manageBtn;
    @BindView(R2.id.horizontal_divideline)
    View horizontalDivideline;

    @OnClick(R2.id.manage_btn)
    public void manageBtn() {
        new ActionSheetDialog(getActivity())
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("禁言", ActionSheetDialog.SheetItemColor.Black,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                showDialog();
                            }
                        })
                .addSheetItem("禁言列表", ActionSheetDialog.SheetItemColor.Black,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                startActivity(new Intent(getActivity(), GagListActivity.class));
                            }
                        }).show();

    }

    //    @OnClick(R2.id.manage_btn)
//    public void manageBtn() {
//        if (menus == null) {
//            menus = new ArrayList<>();
//            menus.add(getResources().getString(R.string.gag));
//            menus.add(getResources().getString(R.string.gag_list));
//        }
//        bottomView = new BottomView(getActivity(),
//                R.style.BottomViewTheme_Defalut, R.layout.bottom_view);
//        bottomView.setAnimation(R.style.BottomToTopAnim);
//
//        bottomView.showBottomView(false);
//        lvBottomList = (ListView) bottomView.getView().findViewById(
//                R.id.lv_list);
//        cancel=(TextView) bottomView.getView().findViewById(R.id.cancel);
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bottomView.dismissBottomView();
//            }
//        });
//        BVAdapter adapter = new BVAdapter(getActivity(), menus);
//        lvBottomList.setAdapter(adapter);
//        lvBottomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1,
//                                    int arg2, long arg3) {
//                bottomView.dismissBottomView();
//            }
//        });
//    }

    @OnClick(R2.id.at_user_btn)
    public void atUser() {
        if(onAtUserListener != null) {
            onAtUserListener.onAtUser("@"+userInfoBean.getName()+" ");
        }
    }

    @BindView(R2.id.at_user_btn)
    TextView atUserBtn;

    @BindView(R2.id.home_page_btn)
    TextView homePageBtn;

    @BindView(R2.id.divide_line)
    TextView divedLine;

    @BindView(R2.id.report_btn)
    TextView reportBtn;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.persion_info_fragment, container, false);
        ButterKnife.bind(this, view);
        AppApplication appApplication = AppApplication.getInstance();
        appUser = appApplication.getAppUser();
        this.getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    PersionalInfoPanelFragment.this.dismiss();
                }
                return true;
            }
        });

        setupFragmentComponent();
        userCenterPresenter.attachView(this);

        Serializable obj = getArguments().getSerializable("userInfo");
        Bundle bundle = getArguments();
        boolean isLive = bundle.getBoolean("isLive");
        if (obj != null && obj instanceof SpecUserInfoBean) {
            userInfoBean = (SpecUserInfoBean) obj;
            uID = userInfoBean.getId();
            ids = new Integer[]{uID};
            initView();
            userCenterPresenter.getUserInfo(ids);
        }
        if (appUser.getId().equals(uID)) {
            reportBtn.setVisibility(View.INVISIBLE);
        }
        if (!appUser.getId().equals(bundle.getInt("anchorId", 0))) {
            manageBtn.setVisibility(View.GONE);
        }
        if (this.getTag().equals("LiveMasterPersionalInfoPanel")) {
            if (appUser.getId().equals(uID)) {
                bottomPannel.setVisibility(View.GONE);
                horizontalDivideline.setVisibility(View.GONE);
                manageBtn.setVisibility(View.GONE);
            } else {
//                divedLine.setVisibility(View.GONE);
//                homePageBtn.setVisibility(View.GONE);
            }
        }
//        if(!isLive) {
//            atUserBtn.setVisibility(View.GONE);
//            if (appUser.getId().equals(uID))
//                manageBtn.setVisibility(View.GONE);
//        }

        return view;
    }

    public void initView() {
        String headimgUrl = userInfoBean.getAvatarSmall();
        if (headimgUrl == null || headimgUrl.isEmpty()) {
            headImage.setImageResource(R.mipmap.user1);
        } else {
            Picasso.with(getActivity())
                    .load(headimgUrl)
                    .placeholder(R.mipmap.user1)
                    .error(R.mipmap.user1)
                    .into(headImage);

        }

        userName.setText(userInfoBean.getName());
        if (0 == userInfoBean.getGender()) {
            sex.setImageResource(R.mipmap.ic_profile_female);
        } else {
            sex.setImageResource(R.mipmap.ic_profile_male);
        }
        leID.setText(userInfoBean.getUserNumber() + "");
        if(!TextUtils.isEmpty(userInfoBean.getSign()))
            sign.setText(userInfoBean.getSign());
        Integer level = userInfoBean.getLevel();
        String drawableName = "level_" + level;
        int drawResId = DrawableSettingUtils.getDrawResourceID(drawableName);
        levelImage.setImageResource(drawResId);

        /*主播头像设置星级小图标*/
        int levelTag = (level + 16 - 1) / 16;
        String tagName = "resource_v_10" + levelTag;
        int tagId = DrawableSettingUtils.getDrawResourceID(tagName);
        userLevelImage.setImageResource(tagId);


        if (appUser.getId().equals(uID)) {
//            divedLine.setVisibility(View.GONE);
            focusBtn.setVisibility(View.GONE);
            atUserBtn.setVisibility(View.GONE);
        }
        else {
            atUserBtn.setText("@TA");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersionalInfoPanelFragment.this.dismiss();
                if (iOnTouchView != null && !uID.equals(0) && !uID.equals(appUser.getId())) {
                    iOnTouchView.onTouch();
                }
            }
        });

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.PersionalPanelDialog) {/*设置MainDialogFragment的样式，这里的代码最好还是用我的，大家不要改动*/
            @Override
            public void onBackPressed() {
                super.onBackPressed();
                getActivity().finish();
            }
        };
        return dialog;
    }

    public void showDialog() {
        gagBuilder = new CustomDialog.Builder(getActivity());
        gagBuilder.setMessage(null);
        gagBuilder.setTitle("禁言该用户？");
        gagBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //禁言实现代码
            }
        });
        gagBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        gagDialog = gagBuilder.create(R.layout.gag_dialog_layout);
        gagDialog.show();
    }

    @Override
    public void changeResult() {

    }

    @Override
    public void getUserInfo(SpecUserInfoBean userInfo) {
        userInfoBean = userInfo;
        //判断用户关注状态
        isFollowed = userInfoBean.getFans().getData().getIsFollowed();
        if (isFollowed) {
            focusBtn.setText("已关注");
        } else {
            focusBtn.setText("关注");
        }
        homePageBtn.setText("主页");

        int focusNum = userInfoBean.getFans().getData().getMastersCount();
        int fansNum = userInfoBean.getFans().getData().getFansCount();
        userInfoBean.setFansNumber(fansNum);
        userInfoBean.setFavoursNumber(focusNum);
        String focusCount = focusNum + "";
        String fansCount = fansNum + "";
        focusNumText.setText(focusCount);
        fansNumText.setText(fansCount);
        initView();
    }

    @Override
    public void addFocusResult(boolean result, String msg) {
        if (result) {
            ids = new Integer[]{uID};
            userCenterPresenter.getUserInfo(ids);
            isFollowed = true;
            focusBtn.setText("已关注");
        } else {
            isFollowed = false;
        }
    }

    @Override
    public void cancelFocusResult(boolean result, String msg) {
        if (result) {
            ids = new Integer[]{uID};
            userCenterPresenter.getUserInfo(ids);
            isFollowed = false;
            focusBtn.setText("关注");
        } else {
            isFollowed = true;
        }
    }


    public void setupFragmentComponent() {

        AppComponent appComponent = AppApplication.get(getActivity()).getAppComponent();

        NetworkingComponent networkingComponent = DaggerNetworkingComponent.create();

        PresenterComponent presenterComponent = DaggerPresenterComponent.builder().appComponent(appComponent).networkingComponent(networkingComponent).presenterModule(new PresenterModule()).build();

        presenterComponent.inject(this);

    }

    @Override
    public void onfailed(String message) {
        if (!message.equals("The refresh token is invalid.") && !message.equals("The resource owner or authorization server denied the request.")) {//刷新token不成功，重新登录
            Toast.makeText(AppApplication.getInstance(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getUploadToken(String token, String host) {

    }

    interface OnAtUserListener {
        void onAtUser(String nickName);
    }
    private OnAtUserListener onAtUserListener;
    public void setOnAtUserListener (OnAtUserListener onAtUserListener) {
        this.onAtUserListener = onAtUserListener;
    }
}
