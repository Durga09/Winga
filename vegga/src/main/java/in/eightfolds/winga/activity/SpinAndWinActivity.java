package in.eightfolds.winga.activity;

import android.animation.Animator;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import in.eightfolds.winga.R;
import in.eightfolds.winga.model.SpinAndWinResponse;
import in.eightfolds.winga.utils.Constants;

public class SpinAndWinActivity extends AppCompatActivity implements Animator.AnimatorListener {
    private SpinAndWinResponse spinAndWinResponse;
    private int fileList[] = {R.raw.spin_one, R.raw.spin_two, R.raw.spin_three, R.raw.spin_four, R.raw.spin_five, R.raw.spin_six, R.raw.spin_seven, R.raw.spin_eight, R.raw.spin_nine, R.raw.spin_ten, R.raw.spin_eleven, R.raw.spin_twelve, R.raw.spin_thirteen, R.raw.spin_forteen, R.raw.spin_fifteen, R.raw.spin_sixteen, R.raw.spin_seventeen, R.raw.spin_eighteen, R.raw.spin_ninteen, R.raw.spin_twenty, R.raw.spin_twentyone, R.raw.spin_twentytwo, R.raw.spin_twentythree, R.raw.spin_twentyfour, R.raw.spin_twentyfive};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin_and_win);
        spinAndWinResponse = (SpinAndWinResponse) getIntent().getSerializableExtra(Constants.DATA);
        int animationId = R.raw.spin_one;
        if (spinAndWinResponse != null && spinAndWinResponse.getNoOfGamesPerUser() > 0 && fileList.length >= spinAndWinResponse.getNoOfGamesPerUser()) {
            animationId = fileList[spinAndWinResponse.getNoOfGamesPerUser() - 1];
        }
        final LottieAnimationView view = findViewById(R.id.animation_view);
        view.setAnimation(animationId);
        view.addAnimatorListener(this);

    }


    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animator) {
        finish();
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }
}
