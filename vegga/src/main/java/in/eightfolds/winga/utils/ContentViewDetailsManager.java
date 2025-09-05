package in.eightfolds.winga.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import in.eightfolds.rest.CommonResponse;
import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.Api;
import in.eightfolds.utils.DateTime;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.activity.HomeActivity;
import in.eightfolds.winga.database.AppDataBase;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.ContentViewDetail;
import in.eightfolds.winga.model.DurationDetails;
import in.eightfolds.winga.model.DurationDetailsServer;
import in.eightfolds.winga.model.FirstGameResultResponse;
import in.eightfolds.winga.model.GameResultResponse;
import in.eightfolds.winga.model.HomePageAdViewDetail;
import in.eightfolds.winga.model.HomePageAdViewDetailResponse;
import in.eightfolds.winga.model.HomePageResponse;
import in.eightfolds.winga.model.NewGameSubmitRequest;

public class ContentViewDetailsManager {

    private static final String TAG = ContentViewDetailsManager.class.getSimpleName();

    public static ArrayList<ContentViewDetail> calculateDurationDetails(long gameId, AppDataBase appDataBase) {

        Logg.v(TAG,"*calculateDurationDetails");
        ArrayList<ContentViewDetail> contentViewDetails = new ArrayList<>();


        List<Long> allContentIds = appDataBase.getDurationDetailsDao().getUniqueContentIdsInGame(gameId);

        for (Long contentId : allContentIds) {
            Logg.v(TAG,"*calculateDurationDetails > "+contentId);

            ContentViewDetail contentViewDetail = new ContentViewDetail();


            DurationDetailsServer contentViewDuration = appDataBase.getDurationDetailsDao().getContentDurationDetails(gameId, contentId);
            Logg.v(TAG,"*contentViewDuration > "+contentViewDuration);

            if (contentViewDuration.getActiveMills() > 0) {
                List<Long> uniqueWatchDetails = appDataBase.getDurationDetailsDao().getUniqueVideoWatchDetails(gameId, contentId);

                Collections.sort(uniqueWatchDetails, new Comparator<Long>() {
                    @Override
                    public int compare(Long lhs, Long rhs) {
                        return rhs.compareTo(lhs);
                    }
                });

                contentViewDetail.setContentId(contentId);
                contentViewDetail.setCategoryGameId(gameId);
                contentViewDetail.setPlayDuration(contentViewDuration.getActiveMills());
                contentViewDetail.setTimeTakenToAnswer(contentViewDuration.getQuestionsActiveMills());
                contentViewDetail.setTotalEngagementTime(contentViewDuration.getActiveMills() + contentViewDuration.getQuestionsActiveMills());
                if (uniqueWatchDetails.size() > 0) {
                    contentViewDetail.setUniquePlayDuration(uniqueWatchDetails.get(0));
                    contentViewDetail.setNoOfTimesVideoPlayed((long) uniqueWatchDetails.size());
                } else {
                    contentViewDetail.setUniquePlayDuration((long) 0);
                    contentViewDetail.setNoOfTimesVideoPlayed((long) 0);
                }

                //Just for checking in the json format if all are inserted properly or not.
                ArrayList<DurationDetails> dbDurationDetails = (ArrayList<DurationDetails>) appDataBase.getDurationDetailsDao().getAll(0);//(gameId);
                if (dbDurationDetails != null && dbDurationDetails.size() > 0) {
                    Logg.v(TAG, "dbdurationDetails size >> " + dbDurationDetails.size());

                    String durationJson = null;
                    try {
                        durationJson = in.eightfolds.utils.Api.toJson(dbDurationDetails);
                        Logg.v(TAG, "**DB durationJson>> " + durationJson);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                contentViewDetails.add(contentViewDetail);
            }
        }
        List<DurationDetails> durationDetailsList = appDataBase.getDurationDetailsDao().getAll();
        Log.v(TAG,"*durationDetailsList > "+durationDetailsList);
        Log.v(TAG,"*contentViewDetails > "+contentViewDetails);

        return contentViewDetails;
    }

    public static ArrayList<HomePageAdViewDetail> calculateDurationDetailsForAdId(long aduniqueid, AppDataBase appDataBase) {

        ArrayList<HomePageAdViewDetail> contentViewDetails = new ArrayList<>();


        HomePageAdViewDetail contentViewDetail = new HomePageAdViewDetail();


        DurationDetailsServer contentViewDuration = appDataBase.getDurationDetailsDao().getHomeAdDurationDetails(aduniqueid);
        if (contentViewDuration.getActiveMills() > 0) {
            //List<Long> uniqueWatchDetails = appDataBase.getDurationDetailsDao().getUniqueAdWatchDetails(aduniqueid, homepageaddid);
               /* Collections.sort(uniqueWatchDetails, new Comparator<Long>() {
                    @Override
                    public int compare(Long lhs, Long rhs) {
                        return rhs.compareTo(lhs);
                    }
                });*/

            contentViewDetail.setHomePageAddId(contentViewDuration.getContentId());
            contentViewDetail.setPlayDuration(contentViewDuration.getActiveMills());

            contentViewDetail.setTotalEngagementTime(contentViewDuration.getActiveMills() + contentViewDuration.getQuestionsActiveMills());

            contentViewDetail.setUniquePlayDuration(contentViewDuration.getActiveMills());
            contentViewDetail.setNoOfTimesVideoPlayed((long) 0);


            //Just for checking in the json format if all are inserted properly or not.
            ArrayList<DurationDetails> dbDurationDetails = (ArrayList<DurationDetails>) appDataBase.getDurationDetailsDao().getAll(1);//(gameId);
            if (dbDurationDetails != null && dbDurationDetails.size() > 0) {
                Logg.v(TAG, "Ad dbdurationDetails size >> " + dbDurationDetails.size());

                String durationJson = null;
                try {
                    durationJson = in.eightfolds.utils.Api.toJson(dbDurationDetails);
                    Logg.v(TAG, "**Ad DB durationJson>> " + durationJson);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            contentViewDetails.add(contentViewDetail);
        }

        return contentViewDetails;
    }

    public static void deleteTheSubmittedDetails(long gameId, AppDataBase appDataBase) {
        Logg.v(TAG,"*deleteTheSubmittedDetails");

        appDataBase.getDurationDetailsDao().deleteSubmittedgameQueries(gameId);

        List<DurationDetails> durationDetails = appDataBase.getDurationDetailsDao().getAll(0);
        String durationJson = null;
        try {
            durationJson = in.eightfolds.utils.Api.toJson(durationDetails);
            Logg.v(TAG, "**DB durationJson After deletion>> " + durationJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void pushAdContentViewDetailsToServer(Context context, Long userId, final AppDataBase appDataBase) {
        if (Constants.showAd) {
            String imei = EightfoldsUtils.getInstance().getDeviceIMEI((Activity) context);
            ArrayList<HomePageAdViewDetail> homePageAdViewDetails = new ArrayList<>();
            final List<Long> adUniqueIds = appDataBase.getDurationDetailsDao().getUniqueAdIdsInDB();
            for (long addId : adUniqueIds) {
                ArrayList<HomePageAdViewDetail> contentViewDetailsForGame = calculateDurationDetailsForAdId(addId, appDataBase);
                homePageAdViewDetails.addAll(contentViewDetailsForGame);
            }

            for (HomePageAdViewDetail model : homePageAdViewDetails) {
                model.setPlatformId(Constants.ANDROID_PLATFORM_ID);
                model.setImei(imei);
                model.setNoOfTimesVideoPlayed((long) 1);
                model.setUserId(userId);
                String date = DateTime.getNowInUTC();
                Logg.v(TAG,"date Now UTC>>"+date);

                model.setAppViewDateTime(date);

            }

            if (homePageAdViewDetails.size() > 0) {
                HomePageAdViewDetailResponse newGameSubmitRequest = new HomePageAdViewDetailResponse();
                newGameSubmitRequest.setAdvertisementViewDetails(homePageAdViewDetails);
                String url = Constants.SUBMIT_HOME_AD_DURATION_DETAILS;

                EightfoldsVolley.getInstance().makingJsonRequest(new VolleyResultCallBack() {
                    @Override
                    public void onVolleyResultListener(Object object, String requestType) {
                        for (long addId : adUniqueIds) {
                            appDataBase.getDurationDetailsDao().deleteSubmittedQueries(addId, 1);
                        }
                    }

                    @Override
                    public void onVolleyErrorListener(Object object) {

                        if (object instanceof CommonResponse) {
                            CommonResponse commonResponse = (CommonResponse) object;
                            try {
                                String error = Api.toJson(commonResponse);
                                Logg.v(TAG, "error>> " + error);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else if (object instanceof String) {
                            Logg.v(TAG, "error>> " + object.toString());
                        }
                    }
                }, CommonResponse.class, Request.Method.POST, url, newGameSubmitRequest);
            }
        }
    }

    public static void pushContentViewDetailsToServer(final AppDataBase appDataBase) {
        ArrayList<ContentViewDetail> contentViewDetails = new ArrayList<>();
        final List<Long> gameIds = appDataBase.getDurationDetailsDao().getUniqueGameIdsInDB();
        for (long game : gameIds) {
            ArrayList<ContentViewDetail> contentViewDetailsForGame = calculateDurationDetails(game, appDataBase);
            contentViewDetails.addAll(contentViewDetailsForGame);
        }

        if (contentViewDetails.size() > 0) {
            NewGameSubmitRequest newGameSubmitRequest = new NewGameSubmitRequest();
            newGameSubmitRequest.setContentViewDetails(contentViewDetails);
            String url = Constants.SUBMIT_DURATION_DETAILS;

            EightfoldsVolley.getInstance().makingJsonRequest(new VolleyResultCallBack() {
                @Override
                public void onVolleyResultListener(Object object, String requestType) {
                    for (long game : gameIds) {
                        appDataBase.getDurationDetailsDao().deleteSubmittedgameQueries(game);
                    }
                }

                @Override
                public void onVolleyErrorListener(Object object) {

                    if (object instanceof CommonResponse) {
                        CommonResponse commonResponse = (CommonResponse) object;
                        try {
                            String error = Api.toJson(commonResponse);
                            Logg.v(TAG, "error>> " + error);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (object instanceof String) {
                        Logg.v(TAG, "error>> " + object.toString());
                    }
                }
            }, CommonResponse.class, Request.Method.POST, url, newGameSubmitRequest);
        }
    }
}
