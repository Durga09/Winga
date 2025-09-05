package in.eightfolds.winga.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.WindowManager;
import android.widget.ImageView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
//import com.bumptech.glide.integration.webp.decoder.WebpDrawable;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.NewGameResponse;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Logg;
import in.eightfolds.winga.utils.UsageAnalytics;
import in.eightfolds.winga.utils.Utils;

/**
 * Created by Swapnika on 20-Jun-18.
 */
public class StartGameActivity extends BaseActivity implements VolleyResultCallBack {

    private static final String TAG = StartGameActivity.class.getSimpleName();
     NewGameResponse filteredLangGameResp;
     boolean gameAPICallFinished = false;
     boolean gifLoadfinished = false;
    private ProgressDialog progressDialog;
     boolean firstgame;
     boolean onBackPressed = false;
    private ImageView startGifIv;
//    private WebpDrawable gifDrawable = null;
    private boolean onExited = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_startgame);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.DATA)) {
                filteredLangGameResp = (NewGameResponse) bundle.get(Constants.DATA);
            }
            if (bundle.containsKey("firstgame")) {
                firstgame = bundle.getBoolean("firstgame");
            }
        }
        initialize();
    }

    @Override
    protected void onStop() {
        Logg.v(TAG, "onStop()");
        super.onStop();
       /* if (gifDrawable != null && gifDrawable.isRunning()) {
            gifDrawable.stop();
            gifDrawable.setCallback(null);
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onExited = true;
        Glide.get(this).clearMemory();
    }

    private void initialize() {
        startGifIv = findViewById(R.id.startGifIv);
//        gifDrawable = null;

        if (filteredLangGameResp != null && filteredLangGameResp.getCategoryGameId() != null) {
            gameAPICallFinished = true;
        } else {

            getNewGameResponse();
        }


        Glide.with(this)
                .load(R.drawable.start_game_webp)

                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Logg.v(TAG, "**onResourceReady**");

              /*          if (resource instanceof WebpDrawable) { //GifDrawable
                            gifDrawable = (WebpDrawable) resource;
                            gifDrawable.startFromFirstFrame();
                            gifDrawable.setLoopCount(1);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {

                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    while (!onExited) {
                                        if (!gifDrawable.isRunning() && !onBackPressed) {
                                            gifLoadfinished = true;
                                            onGifFinished(); //do your stuff
                                            break;
                                        }
                                    }
                                }
                            }).start();
                        }*/
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(startGifIv);
    }

    private void onGifFinished() {

        if (gifLoadfinished && gameAPICallFinished) {

            String activityName = EightfoldsUtils.getInstance().getTopmostActivity(this);
            if ((activityName.equals("in.eightfolds.winga.activity.StartGameActivity"))) {
                Intent intent = new Intent(StartGameActivity.this, VideoActivity.class);
                intent.putExtra(Constants.DATA, filteredLangGameResp);
                intent.putExtra("firstgame", firstgame);
                startActivity(intent);
                finish();
            }


        } else if (!gameAPICallFinished && gifLoadfinished) {
          //  progressDialog = MyDialog.showProgress(this);
            getNewGameResponse();
        }
    }

    private void getNewGameResponse() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(this)) {

            // EightfoldsVolley.getInstance().showProgress(this);
            String url = Constants.GET_NEW_GAME_URL;
            String id= EightfoldsUtils.getInstance().getFromSharedPreference(getApplicationContext(),Constants.SELECTED_CATEGORY);
            String langId = EightfoldsUtils.getInstance().getFromSharedPreference(this, Constants.SELECTED_LANGUAGE_ID);
            url = url.replace("{langId}", langId);
            url=url.replace("{categoryId}",id);

            EightfoldsVolley.getInstance().makingStringRequest(this, NewGameResponse.class, Request.Method.GET, url);

        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        gameAPICallFinished = false;
        gifLoadfinished = false;
        onBackPressed = true;
        super.onBackPressed();
    }

    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        if (object instanceof NewGameResponse) {
            NewGameResponse newGameResponse = (NewGameResponse) object;
            if (newGameResponse.getGameSessionMessage().getCode() == Constants.LUCKY_DRAW_NOT_ANNOUNCED_CODE) {

                String id= EightfoldsUtils.getInstance().getFromSharedPreference(getApplicationContext(),Constants.SELECTED_CATEGORY);

                    onVolleyErrorListener("No more  games available");



                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);
                finish();
            } else {
                (new UsageAnalytics()).trackPlayGame("", newGameResponse);
                Logg.v("dash", "items count >> " + newGameResponse.getContents().size());
                filteredLangGameResp = Utils.getSelectedLangContents(this, newGameResponse);
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                gameAPICallFinished = true;

                if(!onExited) {
                    onGifFinished();
                }
            }
        }
    }

    @Override
    public void onVolleyErrorListener(Object object) {
        Utils.handleCommonErrors(this, object);

        finish();
    }
}
