package in.eightfolds.winga.adapter;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import in.eightfolds.winga.fragment.CropImageFragment;
import in.eightfolds.winga.fragment.ImageFragment;
import in.eightfolds.winga.utils.Constants;

/**
 * Created by Swapnika on 07-Jun-18.
 */

public class ImageAdapter extends FragmentPagerAdapter {
    @SuppressWarnings("unused")
    private Context context;
    private ArrayList<String> images;
    private Fragment currentFragment;
    private boolean isBitMap;
    private boolean isToCrop;

    public ImageAdapter(FragmentManager fm, Context context,
                        List<String> list, boolean isBitMap) {
        super(fm);
        this.context = context;
        this.isBitMap = isBitMap;
        this.images = (ArrayList<String>) list;
    }

    public boolean isToCrop() {
        return isToCrop;
    }

    public void setToCrop(boolean toCrop) {
        isToCrop = toCrop;
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        args.putSerializable(Constants.DATA, images.get(position));
        args.putBoolean(Constants.IS_LOCAL_IMAGE, isBitMap);
        args.putInt(Constants.POSITION, position);
        if (isToCrop) {
            currentFragment = new CropImageFragment();
            currentFragment.setArguments(args);
        } else {
            currentFragment = new ImageFragment();
            currentFragment.setArguments(args);
        }
        return currentFragment;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            currentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public int getCount() {
        int count = 0;
        if (images != null) {

            count = images.size();
        }
        return count;
    }

}
