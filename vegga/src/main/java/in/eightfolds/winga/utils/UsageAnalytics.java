package in.eightfolds.winga.utils;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.firebase.analytics.FirebaseAnalytics;

import in.eightfolds.WingaApplication;
import in.eightfolds.winga.model.ContentResponse;
import in.eightfolds.winga.model.NewGameResponse;
import in.eightfolds.winga.model.QuestionResponse;

public class UsageAnalytics {

    public UsageAnalytics() {

    }

    private boolean isActive() {
        return true;
    }


    public void trackPage(String page) {
        if (isActive() && WingaApplication.analytics != null) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, page);
            WingaApplication.analytics.logEvent("VIEW_PAGE", bundle);
        }
    }

    public void trackLogin(String user) {
        if (isActive() && WingaApplication.analytics != null) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, user);
            WingaApplication.analytics.logEvent("LOGIN", bundle);
        }
    }

    public void trackPlayGame(String page, NewGameResponse game) {
        if (isActive() && WingaApplication.analytics != null) {
            Bundle bundle = new Bundle();
            if (game != null && game.getCategoryGameId() != null) {
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, game.getCategoryGameId().toString());
                //bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, story.getType());
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, game.getCategoryGameId().toString());
                WingaApplication.analytics.logEvent("START_GAME", bundle);
            }
        }
    }

    public void trackWatchVideo(String page, ContentResponse content) {
        if (isActive() && WingaApplication.analytics != null) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, content.getContentId().toString());
            //bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, story.getType());
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, content.getContentId().toString());
            WingaApplication.analytics.logEvent("Watch_Video", bundle);
        }
    }

    public void trackAnswerQuestion(String page, QuestionResponse question) {
        if (isActive() && WingaApplication.analytics != null) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, question.getqId().toString());
            //bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, story.getType());
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, question.getqId().toString());
            WingaApplication.analytics.logEvent("QUESTION", bundle);
        }
    }

    public void trackYoutubeError(String page, YouTubePlayer.ErrorReason errorReason) {
        if (isActive() && WingaApplication.analytics != null) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, errorReason.name());
            WingaApplication.analytics.logEvent("YOUTUBE_ERROR", bundle);
        }
    }
}
