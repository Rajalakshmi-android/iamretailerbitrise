package com.iamretailer.Common;

public class Appconstatants {

    public static final String LOG_ID ="2abd0c80-6a04-40af-ba88-ce08a48e4e61";

    public static String key="x-oc-merchant-id";
    public static String value="APP_DOMAIN_KEY";
    //public static String value="nuhgMM40tVc2alX3gmXlVJLdzPIc0AeL";//"cXPSUy2UUXFQqeKnHg44abn7ewySi3pz";///"nuhgMM40tVc2alX3gmXlVJLdzPIc0AeL";//"jgEtw1UZQHvvPhOVEuZt2Yn3WG63pHaa";//"jgEtw1UZQHvvPhOVEuZt2Yn3WG63pHaa";//"nuhgMM40tVc2alX3gmXlVJLdzPIc0AeL";//"cDXtRf72fHTPDz0Mp5OCFysHna10Ssjf";//"nuhgMM40tVc2alX3gmXlVJLdzPIc0AeL";

    public static String key1="X-Oc-Session";
    public static String domain ="APP_DOMAIN_NAME";
    // public static String domain ="http://shopzen.stutzen.in/";//"https://shopzenstage.stutzen.in/";//"https://krstore.iamretailer.com/";//"http://shopzen.stutzen.in/";//"https://krstore.iamretailer.com/";//http://shopzenstage.stutzen.in/";//"http://shopzenstage.stutzen.in/";

    public static String sessiondata ="5050f4ca981c9fc266f6b1e0b7";

    public static String SESSION_API =domain+"index.php?route=feed/rest_api/session";

    public static String CAT_LIST=domain+"index.php?route=feed/rest_api/categories";

    public static String PRODUCT_LIST=domain+"index.php?route=feed/rest_api/products&id=";

    public static String REGISTER=domain+"index.php?route=rest/register/register";

    public static String cart_api=domain+"index.php?route=rest/cart/cart";

    public static String Wallet_api=domain+"index.php?route=rest/wallet/save";

    public static String login_api=domain+"index.php?route=rest/login/login";


    public static String guest_api=domain+"index.php?route=rest/guest/guest";

    public static String payment_list=domain+"index.php?route=rest/payment_method/payments";

    public static String payment_method_api=domain+"index.php?route=rest/payment_method/payments";

    public static String country_list_api=domain+"index.php?route=feed/rest_api/countries";

    public static String shipping_save_list_api=domain+"index.php?route=rest/shipping_method/shippingmethods";

    public static String myorder_api=domain+"index.php?route=rest/order/orders";

    public static String addres_list=domain+"index.php?route=rest/payment_address/paymentaddress";

    public static String address_save=domain+"index.php?route=rest/shipping_address/shippingaddress";

    public static String bill_address_save=domain+"index.php?route=rest/payment_address/paymentaddress";

    public static String cart_update_api=domain+"index.php?route=rest/cart/cart";

    public static String guest_shipping_api=domain+"index.php?route=rest/guest_shipping/guestshipping";

    public static String BANNER_IMAGEa= domain+"index.php?route=feed/rest_api/banners";

    public static String DELIVERYMETHOD_LIST=domain+"index.php?route=rest/shipping_method/shippingmethods";

    public static String Confirm_Order = domain+"index.php?route=rest/simple_confirm/confirm";

    public static String Place_Order =domain+"index.php?route=rest/simple_confirm/confirm";

    public static String LOGOUT_URL=domain+"index.php?route=rest/logout/logout";

    public static String SEARCH=domain+"index.php?route=feed/rest_api/products&id=&search=";

    public static String SEARCH1=domain+"index.php?route=feed/rest_api/products&search=";

    public static String WALLET=domain+"index.php?route=feed/rest_api/products&topUp=1";

    public static String Wallet_Amount=domain+"index.php?route=rest/wallet/getCustomerWalletAmount";

    public static  String CATEGORY =domain+"index.php?route=feed/rest_api/categories" ;
    public static  String CATEGORY_PRODUCT =domain+"index.php?route=rest/category_special_product&category_id=" ;
    public static  String WishList_Add = domain+"index.php?route=rest/wishlist/wishlist&id=";

    public static  String Wishlist_Get =domain+ "index.php?route=rest/wishlist/wishlist" ;

    public static String forget_pass=domain+"index.php?route=rest/forgotten/forgotten";

    public static String Feature_api=domain+"index.php?route=feed/rest_api/featured";


    public static String Best_Sell=domain+"index.php?route=feed/rest_api/bestsellers";

    public static String Deal_List=domain+"index.php?route=feed/rest_api/specials";

    public static String POSTREVIEW=domain+"index.php?route=feed/rest_api/reviews&id=";

    public static String CHANGE_PWD=domain+"index.php?route=rest/account/password";

    public static String Review_LIST=domain+"index.php?route=feed/rest_api/reviews&product_id=";

    public static String CURRENCY=domain+"index.php?route=feed/rest_api/getCurrency";

    public static String MY_PROFILE=domain+"index.php?route=rest/account/account";

    public static String COUPON_API=domain+"index.php?route=rest/cart/coupon";

    public static String Gift_COUPON_API=domain+"index.php?route=rest/cart/voucher";

    public static String UPDATE_ADD_API=domain+"index.php?route=rest/account/address&id=";

    public static String SAVE_ADD_API=domain+"index.php?route=rest/account/address";


    public static String ADDRESS_DEL=domain+"index.php?route=rest/account/address&id=";

    public static String SOCIAL_LOGIN=domain+"index.php?route=rest/login/sociallogin";


    public static String ORDER_CUSTOMISE=domain+"index.php?route=rest/order_config/orderconfig";

    public static String CONTACT_API=domain+"index.php?route=rest/contact/send";

    public static String RELATED_API=domain+"index.php?route=feed/rest_api/related&id=";

    public  static String LANG_API=domain+"index.php?route=feed/rest_api/languages";

    public static String Lang="";

    public static String ABOUT=domain+"index.php?route=feed/rest_api/information";

    public static String ABOUT_IN=domain+"index.php?route=feed/rest_api/information&id=";


    public static String CAL_SHIP=domain+"index.php?route=rest/cart/shippingquotes";

    public static String CUR_LIST=domain+"index.php?route=feed/rest_api/productclasses";

    public static String CUR="";
    public static String Mobile_Otp="0";

    public static String PAYMENT=domain+"index.php?route=rest/confirm/confirm&page=pay";

    public static String BANNER_LINK=domain+"index.php?route=feed/rest_api/bannerProducts&id=";

    // public static String CATEGORY_FILTER=domain+"index.php?route=feed/rest_api/manufacturers";
    public static String CATEGORY_FILTER=domain+"index.php?route=rest/filter/filteroption";


    public  static  String LICENSE_KEY="APP_LICENSE_KEY";
    public static int need_brand_product=1;
    public static String store_locator="STORE_LOCATOR";
    public static String returns_menu="ORDER_RETURN_NEED";
    public static String whatsapp_mode="WHATSAPP_MODE";
    public static String fblogin="FB_LOGIN_NEED";
    public static int view_detail_call=1;
    public static int return_view_detail_call=1;
    public static String whatsapp_number="WHATSAPP_NUMBER";
    public static String mobile_number="MOBILENO";
    public static String playstore_url="https://play.google.com/store/apps/details?id=com.iamretailer/";

    public static String[] lat = {"9.448540", "10.3673", "9.925201", "13.082680", "8.713913", "11.016844", "10.2381","12.120000"};
    public static String[] lng = {"77.799435", "77.9803", "78.119775", "80.270718", "77.756652", "76.955832", "77.4892","76.680000"};

    public static String Payment_Success =domain+"index.php?route=rest/razorpay";

    public static String razorpay=domain+"index.php?route=rest/razorpay/order";

    public static String Filter_Api=domain+"index.php?route=rest/filter/filteroption&path=";

    public static String RETURN_SAVE=domain+"index.php?route=rest/return/returns";
    public static String REASON=domain+"index.php?route=rest/return/returnReason";
    public static String Return_List=domain+"index.php?route=rest/return/returns";
    public static String Return_Detail=domain+"index.php?route=rest/return/returns&id=";

    public static String Store_list=domain+"index.php?route=feed/rest_api/stores";
    public static String Store_Detail=domain+"index.php?route=feed/rest_api/stores&id=";
    public static String COUPON=domain+"index.php?route=rest/coupon/getCoupon";

}
