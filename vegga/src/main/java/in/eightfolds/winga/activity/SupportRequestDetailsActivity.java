package in.eightfolds.winga.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import in.eightfolds.winga.R;
import in.eightfolds.winga.model.SupportRequest;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.ConstantsManager;
import in.eightfolds.winga.utils.Utils;

public class SupportRequestDetailsActivity  extends BaseActivity {
    private static final String TAG = NotificationDetailsActivity.class.getSimpleName();

    private SupportRequest supportRequest;
    private TextView tokenTv, messageTv,statusTv,remarkTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_supportrequest_details);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.DATA)) {
                supportRequest = (SupportRequest) bundle.get(Constants.DATA);
            }


        }
        initialize();
    }

    private void initialize() {
        setHeader(getString(R.string.support));

        tokenTv = findViewById(R.id.tokenTv);
        messageTv = findViewById(R.id.messageTv);
        statusTv = findViewById(R.id.statusTv);
        remarkTv = findViewById(R.id.remarkTv);

        findViewById(R.id.backIv).setOnClickListener(this);

        if(!TextUtils.isEmpty(supportRequest.getSupportNumber())){
            tokenTv.setText(supportRequest.getSupportNumber());
        }

        if(!TextUtils.isEmpty(supportRequest.getMessage())){
            messageTv.setText(supportRequest.getMessage());
        }
        statusTv.setText(ConstantsManager.getSupportState(this,supportRequest.getState()));

        if(!TextUtils.isEmpty(supportRequest.getRemark())){
            remarkTv.setText(supportRequest.getRemark());
        }else{
            remarkTv.setText("- -");
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.backIv ) {
                onBackPressed();

        }
    }
}