package in.eightfolds.winga.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
/*import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;*/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import in.eightfolds.rest.EightfoldsVolley;
import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.activity.HomeActivity;
import in.eightfolds.winga.activity.ResultsActivity;
import in.eightfolds.winga.activity.VideoActivity;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.Form;
import in.eightfolds.winga.model.FormDetail;
import in.eightfolds.winga.model.FormDetails;
import in.eightfolds.winga.model.FormResponse;
import in.eightfolds.winga.model.GameResultResponse;
import in.eightfolds.winga.model.NewGameResponse;
import in.eightfolds.winga.model.NewGameSubmitRequest;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.Utils;

import static com.facebook.FacebookSdk.getApplicationContext;

public class FormFillingFragment extends Fragment implements View.OnClickListener, OnEventListener, VolleyResultCallBack {
    private boolean firstgame;
    private NewGameResponse newGameResponse;
    Bundle bundle;
    LinearLayout linearLayout;
    FormResponse form;

    List<EditText> allEds = new ArrayList<EditText>();

    List<String> strings = new ArrayList<String>();
    List<RadioGroup> allRadioGroups=new ArrayList<RadioGroup>();
    TextView textView;
    Button button;
    int fillForm = 0;
    List<Spinner> spinnerList=new ArrayList<Spinner>();

    List<CheckBox> checkBoxesLIst=new ArrayList<CheckBox>();
    private static final String AD_UNIT_ID = "ca-app-pub-3530294870311813/1954091489";
//    private RewardedAd rewardedAd;
    boolean isLoading;
    boolean isUserEarnedReward = false;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        try {
            if (v.getId() == R.id.submit) {

                    for(int i=0; i < allEds.size(); i++){

                    }
                    if(fillForm==0){
                        setFields();

                    }else{
                        showDailog();
                    }


            }
            }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onEventListener() {

    }

    @Override
    public void onEventListener(int type) {

    }

    @Override
    public void onEventListener(int type, Object object) {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onVolleyResultListener(Object object, String requestType) {
        String url = Constants.GET_NEW_GAME_URL;
        String id= EightfoldsUtils.getInstance().getFromSharedPreference(getContext(),Constants.SELECTED_CATEGORY);
        String langId = EightfoldsUtils.getInstance().getFromSharedPreference(getContext(), Constants.SELECTED_LANGUAGE_ID);
        url = url.replace("{langId}", langId);
        url=url.replace("{categoryId}",id);
        if (requestType.equalsIgnoreCase(url)) {
            if (object instanceof NewGameResponse) {
                NewGameResponse newGameResponse = (NewGameResponse) object;
                if (newGameResponse.getGameSessionMessage().getCode() == Constants.SESSION_COMPLETED_CODE) {
                    onVolleyErrorListener(getString(R.string.session_closed));
                    Intent intent = new Intent(getContext(), HomeActivity.class);

                    startActivity(intent);



                } else {
                    if(newGameResponse.getContents()!=null){
                        NewGameResponse filteredLangGameResp = Utils.getSelectedLangContents(getContext(), newGameResponse);
                        Intent intent = new Intent(getContext(), VideoActivity.class);
                        intent.putExtra(Constants.DATA, filteredLangGameResp);
                        startActivity(intent);

                    }else{
                        if(newGameResponse.getGoogleAdMobViewCount()>=newGameResponse.getGoogleAdMobMaxViewCount()){
                            Intent intent = new Intent(getContext(), HomeActivity.class);
                            startActivity(intent);
                        }else{

                            EightfoldsVolley.getInstance().showProgress(getContext());

//                            loadRewardedAd();
                        }

                    }



                }
            }
        }else{
            fillForm=1;
            if(this.newGameResponse.getForm().getAlert2()!=null){
                showDailog();
            }else{
                Intent intent = new Intent(getContext(), HomeActivity.class);
                startActivity(intent);
            }

        }
    }
    private void getNewGameResponse() {
        if (EightfoldsUtils.getInstance().isNetworkAvailable(getContext())) {

            EightfoldsVolley.getInstance().showProgress(getContext());
            String url = Constants.GET_NEW_GAME_URL;
            String id= EightfoldsUtils.getInstance().getFromSharedPreference(getContext(),Constants.SELECTED_CATEGORY);
            String langId = EightfoldsUtils.getInstance().getFromSharedPreference(getContext(), Constants.SELECTED_LANGUAGE_ID);
            url = url.replace("{langId}", langId);
            url=url.replace("{categoryId}",id);

            EightfoldsVolley.getInstance().makingStringRequest(this, NewGameResponse.class, Request.Method.GET, url);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showDailog() {

        MyDialog.showButtonDialog(getContext(), new OnEventListener() {
            @Override
            public void onEventListener() {

            }

            @Override
            public void onEventListener(int type) {
                if (type == R.id.yesTv) {
                    getNewGameResponse();
                }
                if(type == R.id.noText){
                    Intent intent = new Intent(getContext(), HomeActivity.class);
                    startActivity(intent);

                }

            }

            @Override
            public void onEventListener(int type, Object object) {

            }
        },"", newGameResponse.getForm().getAlert2(),"Watch more","Exit");
    }

 /*   private void loadRewardedAd() {
        if (rewardedAd == null || !rewardedAd.isLoaded()) {
            isLoading = true;
            rewardedAd = new RewardedAd(getContext(), AD_UNIT_ID);

            rewardedAd.loadAd(
                    new PublisherAdRequest.Builder().build(),
                    new RewardedAdLoadCallback() {
                        @Override
                        public void onRewardedAdLoaded() {
                            // Ad successfully loaded.
                           isLoading = false;



                            showRewardedVideo();

                        }

                        @Override
                        public void onRewardedAdFailedToLoad(LoadAdError loadAdError) {
                            // Ad failed to load.

                            EightfoldsVolley.getInstance().dismissProgress();

                            isLoading = false;

                        }
                    });
        }
    }

    private void showRewardedVideo() {

        if (rewardedAd.isLoaded()) {
            EightfoldsVolley.getInstance().dismissProgress();
            RewardedAdCallback adCallback =
                    new RewardedAdCallback() {
                        @Override
                        public void onRewardedAdOpened() {
                            // Ad opened.

                        }

                        @Override
                        public void onRewardedAdClosed() {
                            // Ad closed.


                            if (isUserEarnedReward) {
                                Intent intent = new Intent(getContext(), ResultsActivity.class);
                                intent.putExtra(Constants.fromGoogleAdVideo, true);
                                intent.putExtra(Constants.earnAmountFromAdd, newGameResponse.getGoogleAdMobPerViewPoints());
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else{
                                getNewGameResponse();
                            }


                        }

                        @Override
                        public void onUserEarnedReward(RewardItem rewardItem) {
                            // User earned reward.
                            isUserEarnedReward = true;

                            // addCoins(rewardItem.getAmount());
                        }

                        @Override
                        public void onRewardedAdFailedToShow(int errorCode) {
                            // Ad failed to display
                            Toast.makeText(getContext(), "onRewardedAdFailedToShow", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    };
            rewardedAd.show(getActivity(), adCallback);
        }
    }
*/
    @Override
    public void onVolleyErrorListener(Object object) {
        if(object!=null){
            Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootview = inflater.inflate(R.layout.fragment_form_filling, container, false);

/*
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });*/
        bundle = getArguments();
        if (bundle != null) {
            newGameResponse = (NewGameResponse) bundle.get(Constants.DATA);

            if (bundle.containsKey("firstgame")) {
                firstgame = bundle.getBoolean("firstgame");
            }
        }
        initBinding(rootview);
        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linearLayout = view.findViewById(R.id.lLayout);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        form = newGameResponse.getForm();
        for (int i = 0; i < form.getFormdetails().size(); i++) {
            TextView textView = new TextView(getContext());
            textView.setText(form.getFormdetails().get(i).getLableName());
            params.setMargins(10,10,10,10);
            textView.setLayoutParams(params);

            linearLayout.addView(textView);

           if(form.getFormdetails().get(i).getFftId()==1||form.getFormdetails().get(i).getFftId()==2 ||form.getFormdetails().get(i).getFftId()==4 ){
               linearLayout.addView(getViewFromObject(form.getFormdetails().get(i)));
           }
           else{
               String[] array = form.getFormdetails().get(i).getLableValue().split(",");
               for (int j=0;j<array.length;j++){
                  FormDetail formDetail= form.getFormdetails().get(i);
                  formDetail.setLableValue(array[j]);
                  linearLayout.addView(getViewFromObject(formDetail));

               }
           }


        }

        for(int i=0;i<allRadioGroups.size();i++){
            final int finalI = i;

            allRadioGroups.get(i).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                   for(int j=0;j<allRadioGroups.get(finalI).getChildCount();j++){
                       RadioButton radioButton = (RadioButton)allRadioGroups.get(finalI).getChildAt(j) ;
                       if(radioButton.getId()==checkedId){
                           strings.remove(finalI);
                          strings.add(finalI,radioButton.getText().toString());
                           return;
                       }
                   }


                }

            });

        }
    }

    private void initBinding(View rootview) {

      button=rootview.findViewById(R.id.submit);
      button.setOnClickListener(this);
      textView=rootview.findViewById(R.id.title);
      textView.setText(newGameResponse.getForm().getFormName());


    }

    private void setFields() {

        NewGameSubmitRequest submitRequest = new NewGameSubmitRequest();
        submitRequest.setFirstGame(false);
        submitRequest.setCgsId(newGameResponse.getCgsId());
        submitRequest.setCategoryGameId(newGameResponse.getCategoryGameId());
        submitRequest.setUserId(newGameResponse.getUserId());
        FormResponse newForm = new FormResponse();
        newForm.setContentId(form.getContentId());
        newForm.setFcId(form.getFcId());
        List<FormDetail> listOfDetails = new ArrayList<FormDetail>();
        for (int i = 0, j=0, k = 0,m=0; i < form.getFormdetails().size(); i++) {
            FormDetail fd = new FormDetail();
            if (form.getFormdetails().get(i).getFftId() == 1) {

              if(getFileDetails(form.getFormdetails().get(i),j)!=null){
                  listOfDetails.add(getFileDetails(form.getFormdetails().get(i),j));
              }else{
                  return;
              }
              j+=1;


            }else if(form.getFormdetails().get(i).getFftId() == 2){
                if(getRadioButtonText(form.getFormdetails().get(i),k)!=null){
                    listOfDetails.add(getRadioButtonText(form.getFormdetails().get(i),k));
                }else{
                    return;
                }
              k+=1;

            }else if(form.getFormdetails().get(i).getFftId() == 3){
                fd.setFclId(form.getFormdetails().get(i).getFclId());
                fd.setFftId(form.getFormdetails().get(i).getFftId());
                String value="";
                for(int l=0;l<checkBoxesLIst.size();l++){

                    if(checkBoxesLIst.get(l).isChecked()){
                        if(l==checkBoxesLIst.size()-1){
                            value+=checkBoxesLIst.get(l).getText();
                        }else{
                            value+=checkBoxesLIst.get(l).getText()+",";
                        }

                    }else{
                        continue;
                    }
                }
                if(value.equals("")){
                    String enter="Please select a value from "+form.getFormdetails().get(i).getLableName();
                    Toast.makeText(getContext(),enter,Toast.LENGTH_LONG).show();
                    return;
                }else{
                    fd.setLableValue(value);
                    listOfDetails.add(fd);
                }

            }else if(form.getFormdetails().get(i).getFftId() == 4){
                fd.setFclId(form.getFormdetails().get(i).getFclId());
                fd.setFftId(form.getFormdetails().get(i).getFftId());
                fd.setLableValue(spinnerList.get(m).getSelectedItem().toString());
                listOfDetails.add(fd);
                m+=1;
            }
        }
        newForm.setFormdetails(listOfDetails);
        submitRequest.setForm(newForm);
        sendGamingRequest(submitRequest);
    }

    private FormDetail getRadioButtonText(FormDetail formDetails, int editCount) {
        FormDetail fd=new FormDetail();
        fd.setFclId(formDetails.getFclId());
        fd.setFftId(formDetails.getFftId());
        if(strings.get(editCount).equals("")){
            String value="Please select a value from "+formDetails.getLableName();
            Toast.makeText(getContext(),value,Toast.LENGTH_LONG).show();

            return null;
        }else{
            fd.setLableValue(strings.get(editCount));
        }
        return fd;
    }

    private FormDetail getFileDetails(FormDetail formDetails, int editCount) {
        FormDetail fd=new FormDetail();
        fd.setFclId(formDetails.getFclId());
        fd.setFftId(formDetails.getFftId());
        if(allEds.get(editCount).getText().toString().equals("")){
            String value="Please fill "+formDetails.getLableName();
            Toast.makeText(getContext(),value,Toast.LENGTH_LONG).show();
            return null;
        }else{
            if(formDetails.getRegex()!=null && !formDetails.getRegex().isEmpty()){
                if(Pattern.matches(formDetails.getRegex(),allEds.get(editCount).getText().toString())){
                    fd.setLableValue(allEds.get(editCount).getText().toString());
                }else{
                    String value="Please enter proper "+formDetails.getLableName();
                    Toast.makeText(getContext(),value,Toast.LENGTH_LONG).show();
                    return null;
                }
            }else{
                fd.setLableValue(allEds.get(editCount).getText().toString());
            }

        }
        return fd;
    }

    private void sendGamingRequest(NewGameSubmitRequest submitRequest) {

        EightfoldsVolley.getInstance().showProgress(getContext());
        String url = Constants.SUBMIT_FORM_GAME_URL;
        String langId = EightfoldsUtils.getInstance().getFromSharedPreference(getContext(), Constants.SELECTED_LANGUAGE_ID);
        url = url.replace("{langId}", langId);

        EightfoldsVolley.getInstance().makingJsonRequest(this, GameResultResponse.class, Request.Method.POST, url, submitRequest);
    }

    private View getViewFromObject(FormDetail formDetails) {

        if (formDetails.getFftId() == 1) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            EditText editText = new EditText(getContext());
            params.setMargins(10,10,10,10);
            if(formDetails.getLableName().equals("Mobile Number")){

                editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
                editText.setInputType(InputType.TYPE_CLASS_PHONE);
            }else {
                editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(30) });;
            }
            editText.setSingleLine();
            editText.setLayoutParams(params);

            allEds.add(editText);
            return editText;
        } else if (formDetails.getFftId() == 3) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
                CheckBox checkBox = new CheckBox(getContext());
                checkBox.setText(formDetails.getLableValue());

                checkBox.setLayoutParams(params);
                checkBoxesLIst.add(checkBox);
                return checkBox;



        } else if (formDetails.getFftId() == 2) {
            strings.add("");
            String[] array =formDetails.getLableValue().split(",");
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            RadioGroup rg=new RadioGroup(getContext());
            int selectId = -1;
            for (int j=0;j<array.length;j++) {

                RadioButton radioButton = new RadioButton(getContext());
                radioButton.setText(array[j]);
                radioButton.setLayoutParams(params);
                radioButton.setId(j);
                rg.addView(radioButton);

             if(formDetails.getDefaultValue()==array[j]){
                 selectId=j;
             }
            }
            if(selectId>-1){
                rg.check(selectId);
            }

            rg.setOrientation(RadioGroup.HORIZONTAL);

            allRadioGroups.add(rg);
            return rg;


            }else if(formDetails.getFftId() == 4){

            String[] array =formDetails.getLableValue().split(",");
            Spinner spinner=new Spinner(getContext());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,array);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
          //  spinner.setLayoutParams(params);
            spinner.setAdapter(adapter);
            spinnerList.add(spinner);
            return spinner;

        }


        return null;


    }


}
