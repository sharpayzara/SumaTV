package com.ddm.live.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.ddm.live.AppApplication;
import com.ddm.live.R;
import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.inject.components.DaggerNetworkingComponent;
import com.ddm.live.inject.components.DaggerPresenterComponent;
import com.ddm.live.inject.components.NetworkingComponent;
import com.ddm.live.inject.components.PresenterComponent;
import com.ddm.live.inject.modules.PresenterModule;
import com.ddm.live.models.bean.common.commonbeans.UserInfoBean;
import com.ddm.live.models.bean.common.userbeans.FansContributionInfoBean;
import com.ddm.live.models.bean.common.userbeans.SpecUserInfoBean;
import com.ddm.live.presenter.UserInfoPresenter;
import com.ddm.live.utils.DrawableSettingUtils;
import com.ddm.live.views.iface.IUserInfoBaseView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * 横向listview的数据适配器，也就是观众列表，后居者可以直接根据自己的需求在这里改功能以及布局文件就ok
 * <p/>
 * Success is the sum of small efforts, repeated day in and day out.
 * 成功就是日复一日那一点点小小努力的积累。
 * AndroidGroup：158423375
 * Author：Johnny
 * AuthorQQ：956595454
 * AuthorWX：Qiang_it
 * AuthorPhone：nothing
 * Created by 2016/9/21.
 */
public class AudienceAdapter extends BaseAdapter implements IUserInfoBaseView {

    private List<SpecUserInfoBean> data = new ArrayList<>();
    private Context mContext;
    private UserInfoPresenter userInfoPresenter;
    private float DownX;


    public AudienceAdapter(List<SpecUserInfoBean> data,Context mContext) {
        this.data = data;
        this.mContext = mContext;
        setupFragmentComponent();
    }

    public void setData(List<SpecUserInfoBean> data) {
        this.data = data;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        String avatarSmall = data.get(position).getAvatarSmall();
        Integer level = data.get(position).getLevel();

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_audienceadapter, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.crvheadimage);
            holder.levelImageView = (ImageView) convertView.findViewById(R.id.user_head_list_user_level_tag);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (avatarSmall.isEmpty()) {
            holder.imageView.setImageResource(R.mipmap.user1);

        } else Picasso.with(mContext)
                .load(avatarSmall)
                .placeholder(R.mipmap.user1)
//                .resize(DensityUtils.dip2px(mContext, 60), DensityUtils.dip2px(mContext, 60))
//                .centerCrop()
                .error(R.mipmap.user1)
                .into(holder.imageView);

        int levelTag = (level + 16 - 1) / 16;
        String tagName = "resource_v_10" + levelTag;
        int tagId = DrawableSettingUtils.getDrawResourceID(tagName);
        holder.levelImageView.setImageResource(tagId);
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        ImageView levelImageView;
//        String id;
    }

    public void setupFragmentComponent() {
        AppComponent appComponent = AppApplication.get(mContext).getAppComponent();

        NetworkingComponent networkingComponent = DaggerNetworkingComponent.create();

        PresenterComponent presenterComponent = DaggerPresenterComponent.builder().appComponent(appComponent).networkingComponent(networkingComponent).presenterModule(new PresenterModule()).build();

        userInfoPresenter = presenterComponent.getUserInfoPresenter();

        userInfoPresenter.attachView(this);
    }

    @Override
    public void getFansContributionInfoResult(List<FansContributionInfoBean> fansContributionInfoBeanList, String total) {

    }

    @Override
    public void getUserInfoResult(List<UserInfoBean> userInfoBeen) {

    }

    @Override
    public void getSpecUserInfo(SpecUserInfoBean specUserInfoBean) {
    }

    @Override
    public void onfailed(String message) {
        if (!message.equals("The refresh token is invalid.") && !message.equals("The resource owner or authorization server denied the request.")) {//刷新token不成功，重新登录
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
    }
}