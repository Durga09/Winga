package in.eightfolds.winga.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import in.eightfolds.commons.AnimateFirstDisplayListener;
import in.eightfolds.commons.EightfoldsImage;
import in.eightfolds.winga.R;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Logg;
import in.eightfolds.winga.utils.TouchImageView;

public class ImageFragment extends Fragment {

    public TouchImageView imageView;
    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private Object fileId;
    ImageView star;
    boolean isLocalImage;

    private Activity myContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.image_item, container,
                false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            fileId = bundle.getString(Constants.DATA);
            isLocalImage = bundle.getBoolean(Constants.IS_LOCAL_IMAGE);
        }

        imageView = (TouchImageView) rootView.findViewById(R.id.imageView);
        imageView.setMaxZoom(4f);
        this.options = EightfoldsImage.getInstance().getDisplayImageOption(myContext, com.liuguangqiang.swipeback.R.drawable.ic_launcher, null, null);

        if (fileId != null) {
            setImage(fileId);
        }
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        Logg.e("IMAGE URL ", "URL IS " + fileId);
    }


    public void setImage(Object selectedImagePath) {

        String filePath = "";

        if (isLocalImage) {
            filePath = selectedImagePath.toString();
        } else {
            filePath = Constants.FILE_URL + selectedImagePath.toString();
        }
        Glide.with(this)
                .load(filePath)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(imageView);
    }
}
