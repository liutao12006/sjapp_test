package wbkj.sjapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import wbkj.sjapp.MainActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.SharedPreferencesUtil;
import wbkj.sjapp.activity.AdsListActivity;
import wbkj.sjapp.activity.CartActivity;
import wbkj.sjapp.activity.LoginActivity;
import wbkj.sjapp.activity.MyFavActivity;
import wbkj.sjapp.activity.NoticeActivity;
import wbkj.sjapp.activity.OrdersActivity;
import wbkj.sjapp.activity.PayNotesActivity;
import wbkj.sjapp.activity.PointsShopActivity;
import wbkj.sjapp.activity.SetupActivity;
import wbkj.sjapp.activity.UserInfoActivity;
import wbkj.sjapp.models.Address;
import wbkj.sjapp.models.JsonModel;
import wbkj.sjapp.models.JsonModel2;
import wbkj.sjapp.models.User;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.OkHttpClientManager;
import wbkj.sjapp.utils.StringUtils;
import wbkj.sjapp.views.CircleImageView;

public class MineFragment extends Fragment {

	private MainActivity baseActivity;
    private View myView;

    private LinearLayout lyInfo, lyAds;
    private TextView tvFav, tvOrders, tvCart, tvNotes, tvNotice, tvPoints, tvName, tvSex, tvPoint, tvShare, tvGrade,tvUserCount;
    private TextView tvAds;
    private ImageView imgSet, imgSign, imgGrade;
    private CircleImageView imgHead;

    private List<Address> adsList = new ArrayList<>();
    private SharedPreferencesUtil sp;
    private boolean isSign = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity = (MainActivity) getActivity();
        sp = new SharedPreferencesUtil(baseActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        myView = inflater.inflate(R.layout.fragment_mine, null, false);
        initView(myView);
        getSignMethod();
        getDataMethod();
        return myView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        Logger.e(hidden+"");
        if (!hidden) {
            getVip();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getVip();
        if (StringUtils.isNotBlank(sp.getUser().getImg())) {
            if (sp.getUser().getThirdfrom() == 0) {//这里得到的是 0
                Picasso.with(baseActivity).load(HttpUtil.IMGURL+sp.getUser().getImg()).into(imgHead);
            } else {
                Picasso.with(baseActivity).load(sp.getUser().getImg()).into(imgHead);
            }
        }
        if (sp.getUser().getSex() == 0) {
            tvSex.setText("女");
        } else {
            tvSex.setText("男");
        }
    }

    private void initView(final View v) {
        MyClick click = new MyClick();
        imgSet = (ImageView) v.findViewById(R.id.user_setting);
        imgSet.setOnClickListener(click);
        imgHead = (CircleImageView) v.findViewById(R.id.user_head);
        imgHead.setOnClickListener(click);
        imgSign = (ImageView) v.findViewById(R.id.user_signin);
        imgSign.setOnClickListener(click);
        lyInfo = (LinearLayout) v.findViewById(R.id.user_info);
        lyInfo.setOnClickListener(click);
        lyAds = (LinearLayout) v.findViewById(R.id.user_set_address);
        lyAds.setOnClickListener(click);
        tvOrders = (TextView) v.findViewById(R.id.user_myorder);
        tvOrders.setOnClickListener(click);
        tvCart = (TextView) v.findViewById(R.id.user_shop_car);
        tvCart.setOnClickListener(click);
        tvNotes = (TextView) v.findViewById(R.id.user_pay_record);
        tvNotes.setOnClickListener(click);
        tvFav = (TextView) v.findViewById(R.id.user_my_collect);
        tvFav.setOnClickListener(click);
        tvNotice = (TextView) v.findViewById(R.id.user_notification);
        tvNotice.setOnClickListener(click);
        tvPoints = (TextView) v.findViewById(R.id.user_count_macket);
        tvPoints.setOnClickListener(click);
        tvPoint = (TextView) v.findViewById(R.id.user_count);
        tvPoint.setText(sp.getUser().getIntergration()+"积分");
        tvShare = (TextView) v.findViewById(R.id.user_my_share);
        tvShare.setOnClickListener(click);
        tvAds = (TextView) v.findViewById(R.id.user_address);

        tvName = (TextView) v.findViewById(R.id.user_name);
        tvName.setText(sp.getUser().getNickname());
        tvSex = (TextView) v.findViewById(R.id.user_sex);

        tvUserCount = (TextView) v.findViewById(R.id.user_info_count);
        if(IndexFragment.data!=null){
            tvUserCount.setText("当前会员人数:"+IndexFragment.data[1].substring(0,IndexFragment.data[1].length()-1)+",您的大管家尊敬的第101位会员");
        }
        tvGrade = (TextView) v.findViewById(R.id.user_grade);
        imgGrade = (ImageView) v.findViewById(R.id.img_grade);
        if (sp.getUser().getGrade() == 0) {
            imgGrade.setImageResource(R.mipmap.user_1x);
            tvGrade.setText("普通会员");
        } else if (sp.getUser().getGrade() == 1) {
            imgGrade.setImageResource(R.mipmap.user_2x);
            tvGrade.setText("铜牌会员");
        } else if (sp.getUser().getGrade() == 2) {
            imgGrade.setImageResource(R.mipmap.user_3x);
            tvGrade.setText("银牌会员");
        } else if (sp.getUser().getGrade() == 3) {
            imgGrade.setImageResource(R.mipmap.user_4x);
            tvGrade.setText("金牌会员");
        }

    }

    class MyClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.user_setting:
                    baseActivity.skipActivity(baseActivity, SetupActivity.class, null);
                    break;
                case R.id.user_signin:
                    if (isSign) signMethod();
                    break;
                case R.id.user_info:
                    baseActivity.skipActivity(baseActivity, UserInfoActivity.class, null);
                    break;
                case R.id.user_set_address:
                    baseActivity.skipActivity(baseActivity, AdsListActivity.class, null);
                    break;
                case R.id.user_myorder:
                    baseActivity.skipActivity(baseActivity, OrdersActivity.class, null);
                    break;
                case R.id.user_shop_car:
                    baseActivity.skipActivity(baseActivity, CartActivity.class, null);
                    break;
                case R.id.user_pay_record:
                    baseActivity.skipActivity(baseActivity, PayNotesActivity.class, null);
                    break;
                case R.id.user_my_collect:
                    baseActivity.skipActivity(baseActivity, MyFavActivity.class, null);
                    break;
                case R.id.user_notification:
                    baseActivity.skipActivity(baseActivity, NoticeActivity.class, null);
                    break;
                case R.id.user_count_macket:
                    baseActivity.skipActivity(baseActivity, PointsShopActivity.class, null);
                    break;
                case R.id.user_head:
                    String img_uri = sp.getUser().getImg();//image/vipuser/86photo.jpg
                    int from = sp.getUser().getThirdfrom();//from = 0
                    baseActivity.skipActivity(baseActivity, UserInfoActivity.class, null);
                    break;
                case R.id.user_my_share:
                    showShare();
                    break;
                default:break;
            }
        }
    }

    /**
     * 签到
     */
    private void signMethod() {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{
                new OkHttpClientManager.Param("userid", String.valueOf(sp.getUser().getVipid()))
        };
        OkHttpClientManager.postAsyn(HttpUtil.signUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(baseActivity, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                JsonModel json = new Gson().fromJson(response, JsonModel.class);
                if (json.result == 1) {
                    Toast.makeText(baseActivity, "签到成功", Toast.LENGTH_SHORT).show();
                    isSign = false;
                    imgSign.setImageResource(R.mipmap.user_issign);
                    User user = sp.getUser();
                    user.setIntergration(json.intergration);
                    sp.onLoginSuccess(user);
                    tvPoint.setText(json.intergration+"积分");
                } else {
                    Toast.makeText(baseActivity, json.msg, Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }

    /**
     * 签到状态
     */
    private void getSignMethod() {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{
                new OkHttpClientManager.Param("userid", String.valueOf(sp.getUser().getVipid()))
        };
        OkHttpClientManager.postAsyn(HttpUtil.signStatus, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(baseActivity, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                JsonModel json = new Gson().fromJson(response, JsonModel.class);
                if (json.result == 0) {
                    isSign = true;
                    imgSign.setImageResource(R.mipmap.user_sign);
                } else {
                    isSign = false;
                    imgSign.setImageResource(R.mipmap.user_issign);
                    imgSign.setClickable(false);
                }
            }
        }, null);
    }

    private void getVip() {
        if (!sp.isLogin()) baseActivity.skipActivity(baseActivity, LoginActivity.class, null);
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{
                new OkHttpClientManager.Param("vipid", String.valueOf(sp.getUser().getVipid()))
        };
        OkHttpClientManager.postAsyn(HttpUtil.jifenUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(baseActivity, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                JsonModel json = new Gson().fromJson(response, JsonModel.class);
                if (json.result == 1) {
                    User user = sp.getUser();
                    user.setGrade(json.grade);
                    user.setIntergration(json.intergration);
                    sp.onLoginSuccess(user);
                }
            }
        }, null);
    }

    private void showShare() {
        ShareSDK.initSDK(baseActivity);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.app_name));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://hysj.zzwbkj.com:8888/apk/sjgj.apk");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("上街大管家，上街地区你最棒的选择!");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://hysj.zzwbkj.com:8888/image/logo/logo.png");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://hysj.zzwbkj.com:8888/apk/sjgj.apk");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("上街大管家，你的选择");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://hysj.zzwbkj.com:8888/apk/sjgj.apk");

        // 启动分享GUI
        oks.show(baseActivity);
    }

    private void getDataMethod() {
        adsList.clear();
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[] {
                new OkHttpClientManager.Param("vipid", String.valueOf(sp.getUser().getVipid()))
        };
        OkHttpClientManager.postAsyn(HttpUtil.adsUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(baseActivity, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override

//            public void onResponse(JsonElement response) {
//                JsonModel2 json = new Gson().fromJson(response, new TypeToken<JsonModel2<Address>>(){}.getType());
//                if (json.result == 1) {
//                    adsList.addAll(json.data);
//                    if (adsList.size() > 0) {
//                        Address ads = adsList.get(0);
//                        tvAds.setText(ads.getPlotnickname());
//
//                    }
//                }
//            }

            public void onResponse(JsonElement response) {
                JsonModel2 json = new Gson().fromJson(response, new TypeToken<JsonModel2<Address>>(){}.getType());
                if (json.result == 1) {
                    adsList.addAll(json.data);
                    if (adsList.size() > 0) {
                        Address ads = adsList.get(0);
                        tvAds.setText(ads.getPlotnickname());

                    }
                }
            }
        }, null);
    }

}
