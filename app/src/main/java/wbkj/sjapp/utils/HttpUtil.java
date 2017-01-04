package wbkj.sjapp.utils;

/**
 * Created by jianghan on 16/7/23.
 */
public class HttpUtil {

    /**
     * 服务器地址
     * 服务器图片地址
     */
//    public static String URL  = "http://hysj.zzwbkj.com:8888/";
//    public static String IMGURL = "http://hysj.zzwbkj.com:8888/";

    public static String URL  = "http://120.55.161.215/";
    public static String IMGURL = "http://120.55.161.215/";

//    public static String URL  = "http://192.168.1.117/";

//    public static String URL  = "http://192.168.1.126/";
//    public static String IMGURL = "http://192.168.1.126/";


    /**
     * 注册
     */
    public static String regUrl = URL + "shangjie/Addvipuserexd.do";

    /**
     * 登陆
     */
    public static String loginUrl = URL + "shangjie/Loginvipuserexd.do";

    /**
     * 修改昵称
     */
    public static String modNickUrl = URL + "shangjie/Modifyvipuserbyvipid.do";

    /**
     * 修改性别
     */
    public static String modSexUrl = URL + "shangjie/Modifyvipuserbyvipid.do";

    /**
     * 上传头像
     */
    public static String upImgUrl = URL + "shangjie/Modifyvipuserbyvipid.do";

    /**
     * 修改密码
     */
    public static String modPwdUrl = URL + "shangjie/Updatepasswordbyoldpassword.do";

    /**
     * 找回密码
     */
    public static String findPwdUrl = URL + "shangjie/findpassword.do";

    /**
     * 意见反馈
     */
    public static String feedbackUrl = URL + "shangjie/Addcomplain.do";

    /**
     * 广告图片接口
     */
    public static String getImgUrl = URL + "shangjie/QueryadvertbyType.do";

    /**
     * 获取帖子列表
     */
    public static String getMsgUrl = URL + "shangjie/QuerytdetailsListbyttype.do";

    /**
     * 回复帖子
     */
    public static String addCmtUrl = URL + "shangjie/Addcomment.do";

    /**
     * 发布帖子
     */
    public static String addMsgUrl = URL + "shangjie/Addtdetails.do";

    /**
     * 限时优惠产品
     */
    public static String getDisUrl = URL + "shangjie/QuerywareandwdetailsbyWsubclass.do";

    /**
     * 缴费记录
     */
    public static String payNotesUrl = URL + "shangjie/Querytradebyuserid.do";

    /**
     * 收藏列表
     */
    public static String favUrl = URL + "shangjie/QuerycollectbyUserid.do";

//    /**
//     * 商盟类型数据
//     */
//    public static String proTypeUrl = URL + "shangjie/QueryAllWsubclass.do";
//
//    /**
//     * 商盟类型下的小分类和商品
//     */
//    public static String sTypeProUrl = URL + "shangjie/QueryAllWarebyWsubclass.do";
//
//    /**
//     * 商盟小分类下的商品
//     */
//    public static String sProUrl = URL + "shangjie/QuerywareandwdetailsbyWsubclass.do";

    /**
     * 推荐商品
     */
    public static String recProUrl = URL + "shangjie/QueryWarebysize.do";

    /**
     * 查询收藏状态
     */
    public static String favStateUrl = URL + "shangjie/QueryIsCollect.do";

    /**
     * 添加收藏
     */
    public static String addFavUrl = URL + "shangjie/Addcollect.do";

    /**
     * 取消收藏
     */
    public static String calFavUrl = URL + "shangjie/DelcollectbyClid.do";

    /**
     * 添加购物车
     */
    public static String addCart = URL + "shangjie/Addshoppingcar.do";

    /**
     * 购物车列表
     */
    public static String cartUrl = URL + "shangjie/Queryshoppingcarbyuserid.do";

    /**
     * 删除购物车
     */
    public static String delCart = URL + "shangjie/Delshoppingcar.do";

    /**
     * 签到状态
     */
    public static String signStatus = URL + "shangjie/IsSignin.do";

    /**
     * 签到
     */
    public static String signUrl = URL + "shangjie/Addsignin.do";

    /**
     * 个人小区列表
     */
    public static String adsUrl = URL + "shangjie/QueryuserdataByVipid.do";

    /**
     * 添加小区信息
     */
    public static String addAdsUrl = URL + "shangjie/Adduserdata.do";

    /**
     * 修改/添加小区信息
     */
    public static String modAdsUrl = URL + "shangjie/Modifyuserdata.do";

    /**
     * 物业列表
     */
    public static String ppUrl = URL + "shangjie/QueryWuyeList.do";

    /**
     * 通知列表
     */
    public static String noticeUrl = URL + "shangjie/QueryCdetailsList.do";

    /**
     * 订单列表
     */
    public static String orderUrl = URL + "shangjie/QueryordersByuseridandState.do";

    /**
     * 添加订单
     */
    public static String addOrder = URL + "shangjie/Addordersbycar.do";

    /**
     * 删除订单
     */
    public static String delOrder = URL + "shangjie/Delordersbyoid.do";

    /**
     * 微信预支付
     */
    public static String wxUrl = URL + "shangjie/Wxpay.do";

    /**
     * 版本更新
     */
    public static String appUrl = URL + "shangjie/AppUpdate.do";

    /**
     * 积分兑换商品
     */
    public static String proExc = URL + "shangjie/Integralexchange.do";

    /**
     * 店铺列表
     */
    public static String shopUrl = URL + "shangjie/QueryShopbyType.do";

    /**
     * 店铺详情
     */
    public static String shopDetail = URL + "shangjie/QueryShopbyID.do";

    /**
     * 二手商品列表
     */
    public static String sProUrl = URL + "shangjie/QuerywareandwdetailsbyWsubclass.do";

    /**
     * 发布二手商品
     */
    public static String editsProurl = URL + "shangjie/AddGoods.do";

    /**
     * 商圈店铺列表
     */
    public static String nearByshopUrl = URL + "shangjie/QueryNearbyshop.do";

    /**
     * 添加评价列表
     */
    public static String addCmt = URL + "shangjie/AddcommentInFront.do";

    /**
     * 积分查询
     */
    public static String jifenUrl = URL + "shangjie/QueryIntergration.do";

    /**
     * 搜索店铺
     */
    public static String searchShops = URL + "shangjie/QueryShopByname.do";

    /**
     * 搜索店铺产品
     */
    public static String searchPros = URL + "shangjie/QueryWareBynameAndShopid.do";

    /**
     * 评论列表
     */
    public static String cmtUrl = URL + "shangjie/QueryWareAndWdetailsbyWid.do";

    /**
     * 店铺列表
     */
    public static String shopProductUrl = URL + "shangjie/QueryShopbySort.do";

    /**
     * 大管家超市
     */
    public static String shopingUrl = URL + "QueryAllGoods.do";

    /**
     * 发送短信
     */
    public static String smsUrl = URL + "shangjie/Addorder.do";

    /**
     * 大管家超市全部商品
     */
    public static String dgjUrl = URL + "QueryWare.do";

    /**
     * 获取粉丝量
     */
    public static String fslUrl = URL + "queryAllCount.do";

}
