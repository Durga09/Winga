package in.eightfolds.winga.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import in.eightfolds.rest.EightfoldsVolley;

import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.adapter.LanguagesGridAdapter;
import in.eightfolds.winga.model.Language;
import in.eightfolds.winga.model.Setup;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.MyGridView;
import in.eightfolds.winga.utils.Utils;

/**
 * Created by Swapnika on 01-May-18.
 */

public class LanguageActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private MyGridView languagesGridView;
    private Button chooseLangBtn;
    private LanguagesGridAdapter languagesGridAdapter;
    private ArrayList<Language> languages;
    private Language selectedLanguage;
    private ImageView backIv;
    private boolean isfromsplash;
    private RelativeLayout adRL;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_language);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("isfromsplash")) {
                isfromsplash = bundle.getBoolean("isfromsplash");
            }
        }

        overridePendingTransition(R.anim.slide_in_long, R.anim.slide_out_long);
        initialize();
    }

    private void initialize() {
        languagesGridView = findViewById(R.id.languagesGridView);
        adRL = findViewById(R.id.adRL);
        chooseLangBtn = findViewById(R.id.chooseLangBtn);
        backIv = findViewById(R.id.backIv);
        Setup setup = (Setup) EightfoldsUtils.getInstance().getObjectFromSharedPreference(this, Constants.SET_UP_DETAILS, Setup.class);
        languages = new ArrayList<>(setup.getLanguages());


        if (isfromsplash) {
            backIv.setVisibility(View.INVISIBLE);
            adRL.setVisibility(View.GONE);
        }


        String selectedlLangId = EightfoldsUtils.getInstance().getFromSharedPreference(this, Constants.SELECTED_LANGUAGE_ID);

        if (TextUtils.isEmpty(selectedlLangId)) {
            for (Language language : languages
                    ) {
                if (language.isDefault()) {
                    selectedLanguage = language;
                    language.setSelected(true);
                } else {
                    language.setSelected(false);
                }
            }
        } else {
            for (Language language : languages
                    ) {
                if (language.getLangId().toString().equalsIgnoreCase(selectedlLangId)) {
                    selectedLanguage = language;
                    language.setSelected(true);

                } else {
                    language.setSelected(false);
                }
            }
        }

        if(!TextUtils.isEmpty(selectedLanguage.getContinueTxt())) {
            chooseLangBtn.setText(selectedLanguage.getContinueTxt());
        }
       /* if(!TextUtils.isEmpty(selectedLanguage.getDispTitle())) {
            chooseLangBtn.setText(String.format("%s %s", getString(R.string.to_continue_in), selectedLanguage.getDispTitle()));
        }*/
        languagesGridAdapter = new LanguagesGridAdapter(this, languages);
        languagesGridView.setAdapter(languagesGridAdapter);
        chooseLangBtn.setOnClickListener(this);
        languagesGridView.setOnItemClickListener(this);
        backIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.chooseLangBtn) {
            EightfoldsUtils.getInstance().saveToSharedPreference(this, Constants.SELECTED_LANGUAGE_ID, selectedLanguage.getLangId() + "");
            EightfoldsUtils.getInstance().saveToSharedPreference(this, Constants.SELECTED_LANGUAGE_LOCALE, selectedLanguage.getLocale() + "");
            EightfoldsUtils.getInstance().saveToSharedPreference(this, Constants.SELECTED_LANGUAGE_ISO, selectedLanguage.getIsoCode() + "");
            Utils.setAppLanguage(this);

            String accessToken = EightfoldsUtils.getInstance().getFromSharedPreference(this, EightfoldsVolley.ACCESS_TOKEN);
            String firstGamePlayed = EightfoldsUtils.getInstance().getFromSharedPreference(this, Constants.FIRST_GAME_PLAYED);


            if (!TextUtils.isEmpty(accessToken)) {

                Intent homeRefreshIntent = new Intent();
                homeRefreshIntent.setAction(Constants.HOME_LANGUAGE_REFRESH_ACTION);
                homeRefreshIntent.setAction(Constants.PROFILE_LANGUAGE_REFRESH_ACTION);
                sendBroadcast(homeRefreshIntent);

                finish();
               // Intent intent = new Intent();
                //intent.putExtra(Constants.DATA, "language");
                setResult(RESULT_OK);

               Intent intent = new Intent(this, V2HomeFeatureActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        } else if (v.getId() == R.id.backIv) {
            finish();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        for (int i = 0; i < languages.size(); i++) {
            if (i == position) {
                selectedLanguage = languages.get(i);
                languages.get(i).setSelected(true);

            } else {
                languages.get(i).setSelected(false);
            }
             //String.format("%s %s", getString(R.string.to_continue_in), selectedLanguage.getDispTitle()));


        }
        chooseLangBtn.setText(selectedLanguage.getContinueTxt());
        languagesGridAdapter = new LanguagesGridAdapter(LanguageActivity.this, languages);
        languagesGridAdapter.notifyDataSetChanged();
        languagesGridView.setAdapter(languagesGridAdapter);
    }
}
