package in.eightfolds.winga.activity;

import androidx.appcompat.app.AppCompatActivity;
import in.eightfolds.winga.R;
import android.os.Bundle;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        System.out.println("TestActivity:: ");
    }
}
