package in.eightfolds.winga.utils;

import in.eightfolds.winga.BuildConfig;

/**
 * Created by Swapnika on 19-Apr-18.
 */

public class Constants {


    public static boolean isAppInFg = false;
    public static boolean isScrInFg = false;
    public static boolean isChangeScrFg = false;
    static boolean showAd = true;


    //SHA-1 MAC D4:FB:7A:21:CE:E6:FE:80:78:E4:59:8B:02:7C:9F:7B:3F:12:F8:72

    public static final boolean isForTestingVegga2 = false;
    //static final
    public static final boolean DontShowLogs = !BuildConfig.IS_DEBUG;

    public static final boolean WRITELOGS_TO_TEXTFILE = BuildConfig.IS_DEBUG;


    public static final String DEFAULT_COUNTRYCODE = "IN";

    static final String TOPIC_NAME_WINGA = "winga";
    public static final String TOPIC_NAME_ALERTS = "winga_alerts";
    public static final String TOPIC_NAME_PROMOTIONS = "winga_promotions";
    public static final String TOPIC_NAME_UPDATES = "winga_updates";

    static final boolean showQuickTour = true;

    public static final String fromGoogleAdVideo = "from_google_ad_page";
    public static final String earnAmountFromAdd = "earn_amount_from_add";
    public static final boolean captureDurationDetails = true;

    private static final String BASE_URL = BuildConfig.BASE_URL;


    static final String DYNAMIC_LINK_DOMAIN = BuildConfig.DYNAMIC_LINK_DOMAIN;
    static final String VEGGA_WEB_SITE_URL = "https://veggaappsol.com/mobileapp";

    public static final int NOEVENT_CODE = 1016;
    public static final int SESSION_COMPLETED_CODE = 1015;
    public static final int LUCKY_DRAW_NOT_ANNOUNCED_CODE = 1014;
    public static final int PROMOTION_SESSION_NOT_STARTED = 1000;
    public static final int SPIN_AND_WIN_PENDING = 1003;

    public static final int IMAGE_CONTENT_ID = 3;
    public static final int DEACTIVATED_CODE = 44011;
    public static final int DEACTIVATED_CODE_FROM_BACKEND = 44010;


    public static final int PAGE_SIZE = Integer.MAX_VALUE;
    public static final int NOTIFICATION_PAGE_SIZE = 50;


    public static final String HOME_REFRESH_ACTION = "HOME_REFRESH_ACTION";
    public static final String NOTIFICATION_RECEIVED_ACTION = "NOTIFICATION_RECEIVED_ACTION";
    public static final String HOME_LANGUAGE_REFRESH_ACTION = "HOME_LANGUAGE_REFRESH_ACTION";
    public static final String PROFILE_LANGUAGE_REFRESH_ACTION = "PROFILE_LANGUAGE_REFRESH_ACTION";
    public static final String PROFILE_REFRESH_ACTION = "PROFILE_REFRESH_ACTION";


    public static final String RECEIVED_OTP_ACTION = "RECEIVED_OTP_ACTION";
    public static final String REFRESH_BANNER_ACTION = "REFRESH_BANNER_ACTION";


    public static final String LOGIN_URL =  BuildConfig.BASE_URL_V2+ "/auth/user/login";
    public static final String GET_SETUP = BASE_URL + "/api/setup?langId={langId}";
    public static final String GET_SETUP_SECURE = BASE_URL + "/api/secure/setup?langId={langId}";


   // public static final String GET_HOME_PAGE_RESPONSE = BASE_URL + "/api/secure/user/home/page?langId={langId}";


    public static final String GET_CATEGORIES_LIST = BASE_URL + "/api/v1/secure/user/categories?langId={langId}";

    public static final String POST_GOOGLE_ADD = BASE_URL + "/api/v1/secure/user/game/submit/google/admob/{cgsId}";

    public static final String GENERATE_OTP_URL = BASE_URL + "/api/generate/otp?mobile={mobile}";
    public static final String VERIFY_OTP_URL = BASE_URL + "/api/verify/otp?mobile={mobile}&otp={otp}";

    public static final String REGISTRATION_URL = BASE_URL + "/api/user/registration?otpToken={otpToken}&langId={langId}";
    public static final String FB_VALIDATE_URL = BASE_URL + "/api/user/fb/validate/accesstoken"; //added langid as query paramter
    public static final String GOOGLE_VALIDATE_URL = BASE_URL + "/api/user/google/validate/accesstoken"; //added langid as query paramter

    public static final String FORGOT_PASSWORD_GET_OTP_URL = BASE_URL + "/api/user/forgot/password?username={username}&confirm={confirm}";
    public static final String FORGOT_PASSWORD_VERIFY_OTP_URL = BASE_URL + "/api/user/forgot/password/verification?username={username}&verificationCode={verificationCode}&langId={langId}";
    public static final String FORGOT_PASSWORD_CHANGE_URL = BASE_URL + "/api/secure/user/forget/password/change?password={password}&langId={langId}";

    public static final String SCRATCH_URL = BASE_URL + "/api/v1/secure/scratch/card/{scratchCardId}";
    public static final String CHANGE_PASSWORD_URL = BASE_URL + "/api/secure/user/password/change?password={password}&oldPassword={oldPassword}&langId={langId}";

    public static final String GET_NEW_GAME_URL = BASE_URL + "/api/v1/secure/user/new/game?langId={langId}&categoryId={categoryId}";
    public static final String SUBMIT_GAME_URL = BASE_URL + "/api/v1/secure/user/game/submit?langId={langId}";

    public static final String GET_CATEGORY_HOMEPAGE = BASE_URL + "/api/v1/secure/user/category/home/page?langId={langId}&categoryId={categoryId}";


    public static final String NEW_SUBMIT_GAME_URL = BASE_URL + "/api/v1/secure/user/game/submit?langId={langId}";

    public static final String SUBMIT_FORM_GAME_URL = BASE_URL + "/api/v1/secure/user/game/submit/form?langId={langId}";

    public static final String SUBMIT_FIRST_GAME_URL = BASE_URL + "/api/v1/secure/user/first/game/submit?langId={langId}";
    public static final String GET_PLAY_AND_WIN = BASE_URL + "/api/secure/user/spinwin";

    public static final String GET_PLAY_SPIN_AND_WIN_NEW = BASE_URL + "/api/v1/secure/user/spinwin?categoryId={categoryId}&langId={langId}";


    static final String SUBMIT_DURATION_DETAILS = BASE_URL + "/api/v1/secure/content/view/details";
    static final String SUBMIT_HOME_AD_DURATION_DETAILS = BASE_URL + "/api/secure/advertisement/view/details";
    public static final String GET_ADD_BANNER_URL = BASE_URL + "/api/footer/ad/{random}?exclude={exclude}";
    public static final String GET_FOOTER_IMAGE_URL = BASE_URL + "/api/footer/ad/image/{footerAddId}";
    public static final String UPDATE_PREFERRED_CONTENT_TYPE_URL = BASE_URL + "/api/secure/user/update/preferred/content/type/{type}?langId={langId}";
    // type - 0 for both, 1- video, 2 -image

    public static final String UPLOAD_FILE_URL = BASE_URL + "/api/media/secure/file/upload";
    public static final String DEACTIVATE_ACCOUNT = BASE_URL + "/api/secure/user/deactivate";
    public static final String FILE_URL = BASE_URL + "/api/media/file/";
    public static final String VOUCHER_FILE_URL = BASE_URL + "/api/voucher/logo/image/";

    public static final String GET_USER_PROFILE_URL = BASE_URL + "/api/secure/user/profile";
    public static final String EDIT_USER_PROFILE_URL = BASE_URL + "/api/secure/user/update?langId={langId}";
    public static final String GET_USER_ADDRESSES_URL = BASE_URL + "/api/secure/user/address";
    public static final String ADD_USER_ADDRESS_URL = BASE_URL + "/api/secure/user/address/add";
    public static final String UPDATE_USER_ADDRESS_URL = BASE_URL + "/api/secure/user/address/update";
    public static final String DELETE_USER_ADDRESS_URL = BASE_URL + "/api/secure/user/address/{addressId}/delete";
    public static final String UPDATE_PRIMARY_USER_ADDRESS_URL = BASE_URL + "/api/secure/mark/user/address/{addressId}/{addressFlag}";

    public static final String GET_WINNERS_LIST_URL = BASE_URL + "/api/v1/secure/winners?selectedDate={selectedDate}&langId={langId}" +
            "&search={search}&page={page}&pageSize={pageSize}&categoryId={categoryId}";
    public static final String SESSION_WISE_WINNERS_LIST = BASE_URL + "/api/v1/secure/game/session/{cgsId}/winners?tz={tz}&page={page}&pageSize={pageSize}&langId={langId}";


    public static final String GET_HISTORY_URL = BASE_URL + "/api/secure/user/prize/history/v1?selectedDate={selectedDate}&langId={langId}" +
            "&state={state}&search={search}&page={page}&pageSize={pageSize}";


    public static final String GET_LOYALTYPOINTS_HISTORY = BASE_URL + "/api/secure/user/point/history/v1?tz={tz}&page={page}&pageSize={pageSize}";
    public static final String GET_GAMEPLAY_HISTORY = BASE_URL + "/api/secure/user/game/history?tz={tz}&page={page}&pageSize={pageSize}&winFlag={winFlag}";
    public static final String GET_REFERRED_HISTORY = BASE_URL + "/api/secure/user/refered/history?tz={tz}&page={page}&pageSize={pageSize}";
    public static final String GET_NOTIFICATIONS = BASE_URL + "/api/secure/user/notifications?tz={tz}&page={page}&pageSize={pageSize}";
    public static final String GET_NOTIFICATION_DETAIL = BASE_URL + "/api/secure/notification/{notificationId}";
    public static final String GET_SUPPORT_TICKETS_HISTORY = BASE_URL + "/api/secure/user/support/requests?tz={tz}&page={page}&pageSize={pageSize}";


    public static final String GET_LOYALITY_POINTS_REDEEM_TOKEN = BASE_URL + "/api/secure/redeem/loyality/points/{points}?type={type}";


    public static final String REDEEM_LOYALITY_POINTS = BASE_URL + "/api/secure/redeem/loyality/points/{points}/{mobile}?type={type}&redeemToken={redeemToken}";  //type -1 for paytm

    public static final String RETRY_REDEEM_AMOUNT_TO_PAYTM = BASE_URL + "/api/secure/redeem/rewards/{refType}/{refId}/{mobile}";
    public static final String RETRY_REDEEM_USING_VOUCHER = BASE_URL + "/api/secure/retry/voucher/redeem/{refType}/{refId}";


    public static final String UPDATE_PREFERRED_STATE = BASE_URL + "/api/secure/user/update/preferred/state/{stateId}?langId={langId}";




    public static final String GET_VOUCHERS_URL = BASE_URL + "/api/vouchers";
    public static final String REDEEM_VOUCHERS_URL = BASE_URL + "/api/secure/redeem/voucher?redeemToken={redeemToken}";
    public static final String BUY_CONTENT_URL = BASE_URL + "/api/user/{userId}/view/content/{contentId}/buylink";
    public static final String BUY_FOOTER_CONTENT_URL = BASE_URL + "/api/user/{userId}/view/footer/ad/{footerAddId}/redirect";
    public static final String BUY_HOME_CONTENT_URL = BASE_URL + "/api/user/{userId}/view/home/ad/{homePageAddId}/redirect";
    public static final String UPDATE_DEVICE_INFO = BASE_URL + "/api/secure/user/logged-in/device";

    public static final String USER_DEVICE_DETAILS_URL = BASE_URL + "/api/v1/secure/userDeviceDetail";


    public static final String ADD_SUPPORT_REQUEST_URL = BASE_URL + "/api/secure/support/request";
    static final String POST_DEVICE_INFO = BASE_URL + "/api/secure/user/device";
    static final String LOG_OUT = BASE_URL + "/api/secure/user/logout?imei={imei}";
    public static final String WEGGA_WEBSITE_URL = "https://s3.ap-south-1.amazonaws.com/winga-static/wing_about_us.html";
    public static final String TERMS_CONDITIONS_URL = "http://winga-static.s3-website.ap-south-1.amazonaws.com/winga_tc.html";
    public static final String PRIVACY_POLICY_URL = "https://winga-static.s3.ap-south-1.amazonaws.com/winga_privacy-policy.html";
    public static final String FAQ_URL = "https://s3.ap-south-1.amazonaws.com/winga-static/faqs/faq_<LANG>.html";

    public static final String SHARE_LINK_CONTENT_URL=BASE_URL+"/api/user/{userId}/view/content/{contentId}/sharelink";

    public static final  String HTML_CONTENT_GAMES_LINK_URL=BASE_URL+"/api/v1/secure/category/content/games/{categoryId}/{langId}";
    public static final int ANDROID_PLATFORM_ID = 1;
    public static final int PAYTM_ID = 1;
    public static final int VOUCHER_ID = 2;
    public static final int SUCCESS = 2000;
    public static final int TODAY_GAME_LIMIT_REACHED = 4443;
    public static final int GAME_ALREADY_SUBMITTED = 4442;
    public static final int SESSION_END_CODE = 4421;
    public static final int ALREADY_REGISTERED = 4004;
    public static final int MOBILE_NOT_REGISTERED = 44000;
    public static final int INVALID_USER = 4002;
    public static final int OTP_EXPIRED = 5020;
    public static final int INVALID_PINCODE = 4420;
    public static final int INVALID_REFERRAL_CODE = 4008;

    public static final String ADDRESS = "ADDRESS";
    public static final int PASSWORD_LENGTH = 8;
    public static final String FIRE_BASE_TOKEN = "FIRE_BASE_TOKEN";
    public static final String IMEI = "IMEI";
    static final String READ_NOTIFICATION_IDS = "READ_NOTIFICATION_IDS";

    public static String NAVIGATE_TAB_POSITION = "NAVIGATE_TAB_POSITION";
    public static final String OTHER_DATA = "OTHER_DATA";

    public static final String DATA = "DATA";
    public static final String ADD_DETAILS = "ADD_DETAILS";
    public static final String FROM_NOTIFICATION = "FROM_NOTIFICATION";
    public static final String FROM_SPLASH = "FROM_SPLASH";
    public static final String IS_VOUCHER_TRANSFER = "IS_VOUCHER_TRANSFER";
    public static final String IS_RETRY = "IS_RETRY";
    public static final String TITLE = "TITLE";

    public static final String HOME_DATA = "HOME_DATA";

    public static final String CATEGORY_TRANSLATION = "CATEGORY_TRANSLATION";

    public static final String SELECTED_CATEGORY = "SELECTED_CATEGORY";
    public static final String MOBILE_NUMBER = "MOBILE_NUMBER";
    public static final String IS_LOCAL_IMAGE = "IS_LOCAL_IMAGE";
    public static final String POSITION = "POSITION";

    public static final String IS_TO_CROP = "IS_TO_CROP";
    public static final int CROP = 100;
    public static final String PATH = "PATH";


    static String LATO_BOLD = "lato_bold.otf";
    static String AIRSTREAM_REGULAR = "airstream_regular.otf";
    public static String COLOR_TUBE = "colortube.otf";
    static String BRUSH_SCRIPT_STANDARD = "brush_script_std.otf";
    public static String BERNHC = "bernhc.otf";

    public static final String CLIENT_ID = BuildConfig.GOOGLE_CLIENT_ID;

    public static final String LOGIN_TYPE = "LOGIN_TYPE";
    public static String FB_LOGIN = "FB_LOGIN";
    public static String GOOGLE_LOGIN = "GOOGLE_LOGIN";
    public static String FACEBOOK = "facebook";
    public static String GOOGLE = "google";

    static final String IS_TUTORIAL_SKIPPED = "IS_TUTORIAL_SKIPPED";
    public static final String IS_HOME_TUTORIAL_SHOW_IN_NO_SESSION = "IS_HOME_TUTORIAL_SHOW_IN_NO_SESSION";
    public static final String INVITED_BY_REF_CODE = "INVITED_BY_REF_CODE";

    public static final String FIRST_GAME_PLAYED = "FIRST_GAME_PLAYED";
    public static final String LOGIN_DATA = "LOGIN_DATA";
    public static final String DEVICE_INFO_LAST_UPDATED_TIME = "DEVICE_INFO_LAST_UPDATED_TIME";
    public static final String USER_DETAILS = "USER_DETAILS";

    public static final String LOYALITY_POINTS = "LOYALITY_POINTS";

    public static final String CATEGORY_GAMESESSION_ID="Category_Game_Session_Id";
    static final String APP_FIRST_LOGGEDIN_DATE = "APP_FIRST_LOGGEDIN_DATE";
    public static final String SET_UP_DETAILS = "SET_UP_DETAILS";

    public static final String GOOGLE_PLACES_API = "AIzaSyAqA_sofwPipIQRcc_f13O1OfmaApdNoqY";

    public static final String SELECTED_LANGUAGE_ID = "SELECTED_LANGUAGE_ID";
    public static final String SELECTED_LANGUAGE_LOCALE = "SELECTED_LANGUAGE_LOCALE";
    public static final String SELECTED_LANGUAGE_ISO = "SELECTED_LANGUAGE_ISO";
    public static final String DEFAULT_ISO = "eng";
    static final String DEFAULT_LOCALE = "en";
    public static final String TELUGU_LOCALE = "te";
    static final String DEFAULT_ENGLISH_LANG_ID = "1";

    public static final String OPTION_PAYTM_TITLE = "PAYTM";
    public static final String OPTION_VOUCHERS_TITLE = "XOXODAY";


    public static String MY_SUPER_SECRET_PASSWORD = "Pass@word12";

    public static float viewAlpha = 1;
    public static long imageViewSec = 10;

    static String Youtube_Package_Name = "com.google.android.youtube";


    public static String GOLIVE_MESSAGE = "GOLIVE_MESSAGE";
    public static String GOLIVE_DATE = "GOLIVE_DATE";
    public static String CURRENT_SERVER_TIME = "CURRENT_SERVER_TIME";
}
