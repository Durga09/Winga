package in.eightfolds.winga.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import in.eightfolds.commons.AnimateFirstDisplayListener;
import in.eightfolds.commons.EightfoldsImage;
import in.eightfolds.winga.R;
import in.eightfolds.winga.activity.QuickTourActivity;
import in.eightfolds.winga.model.QuickTourModel;
import in.eightfolds.winga.utils.Logg;


public class QuickTourFragment extends Fragment {

    private static final String TAG = QuickTourActivity.class.getSimpleName();
    private ArrayList<QuickTourModel> imageList;
    private int position;
    private ImageView bgIV, zoomInbgIV;
    private TextView descTv;
    private AnimateFirstDisplayListener animateFirstListener;
    private DisplayImageOptions options;
    private boolean isAnimationPerformed = false;

    private Activity myContext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Logg.e(TAG, "onCreateView() >> " + position);
        View rootView = inflater.inflate(R.layout.quick_tour_bg, container, false);
        animateFirstListener = new AnimateFirstDisplayListener();
        options = EightfoldsImage.getInstance().getDisplayImageOption(myContext, 0, null, null);

        bgIV = rootView.findViewById(R.id.bgIV);
        zoomInbgIV = rootView.findViewById(R.id.zoomInbgIV);
        descTv = rootView.findViewById(R.id.descTv);
        if (imageList != null) {
            Glide.with(this)
                .load( imageList.get(position).getImageId())
                .into(bgIV);

            Glide.with(this)
                    .load( imageList.get(position).getOverlayImageId())
                    .into(zoomInbgIV);

            //bgIV.setImageDrawable(getResources().getDrawable( imageList.get(position).getImageId()));
           // zoomInbgIV.setImageDrawable(getResources().getDrawable( imageList.get(position).getOverlayImageId()));
           // ImageLoader.getInstance().displayImage("drawable://" + imageList.get(position).getImageId(), bgIV, options, animateFirstListener);
            descTv.setText(imageList.get(position).getDescription());
           // ImageLoader.getInstance().displayImage("drawable://" + imageList.get(position).getOverlayImageId(), zoomInbgIV, options, animateFirstListener);

            if (position == 0 && !isAnimationPerformed) {
                performAnimation();
            }
        }
        return rootView;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
       // Logg.e(TAG, "onCreate() >> ");
        super.onCreate(savedInstanceState);
        imageList = (ArrayList<QuickTourModel>) getArguments().getSerializable("imageList");
        position = getArguments().getInt("position");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (this.isVisible()) {
            // If we are becoming invisible, then...
            if (!isVisibleToUser) {
                //Reset your animation, as fragment is being scrolled out of view
                invisibleView();
            }
        }
    }

    public void invisibleView() {
        if (zoomInbgIV != null) {
           // Logg.e(TAG, "*%* invisibleView() >> " + position);
            isAnimationPerformed = false;
            zoomInbgIV.setVisibility(View.INVISIBLE);
        }
    }

    public void performAnimation() {
        try {
            isAnimationPerformed = true;
            Logg.e(TAG, "*%* performAnimation() >> " + position);
            //zoomInbgIV.setAnimation(AnimationUtils.loadAnimation(myContext, R.anim.zoom_in));

            Animation animationZoomIn = AnimationUtils.loadAnimation(getActivity(), R.anim.zoom_in);
            animationZoomIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                 //   Logg.e(TAG, "*%* visibleView() >> " + position);
                    zoomInbgIV.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            zoomInbgIV.startAnimation(animationZoomIn);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onResume() {
       // Logg.e(TAG, "onResume() >> " + position);
        super.onResume();

    }

    @Override
    public void onPause() {
       // Logg.e(TAG, "onPause() >> " + position);
       // zoomInbgIV.setVisibility(View.GONE);
        super.onPause();
    }



    @Override
    public void onDestroy() {
      //  Logg.e(TAG, "onDestroy() >> " + position);
        super.onDestroy();
    }


}

