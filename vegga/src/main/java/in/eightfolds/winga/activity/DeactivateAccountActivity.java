package in.eightfolds.winga.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;

import in.eightfolds.winga.R;

/**
 * Created by Swapnika on 01-May-18.
 */

public class DeactivateAccountActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deactivateaccount);
        initialize();
    }
    private void initialize(){
        setHeader(getString(R.string.deactivate_acct));
    }
}
