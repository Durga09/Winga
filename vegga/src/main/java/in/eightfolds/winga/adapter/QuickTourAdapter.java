package in.eightfolds.winga.adapter;


import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;

import in.eightfolds.winga.fragment.QuickTourFragment;
import in.eightfolds.winga.model.QuickTourModel;


public class QuickTourAdapter extends FragmentStatePagerAdapter {
    @SuppressWarnings("unused")
    private Context context;
    private ArrayList<QuickTourModel> integerList = new ArrayList<>();
    private Fragment currentFragment;
    //private QuickTourFragment fragment;

    private Fragment one, two, three, four, five, six, seven, eight, nine;

    public QuickTourAdapter(FragmentManager fm, Context context,
                            ArrayList<QuickTourModel> list) {
        super(fm);
        this.context = context;
        this.integerList = list;

    }


    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (one == null) {
                    one = new QuickTourFragment();
                    Bundle args = new Bundle();
                    args.putInt("position", position);
                    args.putSerializable("imageList", (Serializable) integerList);
                    one.setArguments(args);
                }
                return one;
            case 1:
                if (two == null) {
                    two = new QuickTourFragment();
                    Bundle args = new Bundle();
                    args.putInt("position", position);
                    args.putSerializable("imageList", (Serializable) integerList);
                    two.setArguments(args);
                }
                return two;
            case 2:

                if (three == null) {
                    three = new QuickTourFragment();
                    Bundle args = new Bundle();
                    args.putInt("position", position);
                    args.putSerializable("imageList", (Serializable) integerList);
                    three.setArguments(args);
                }
                return three;
            case 3:
                if (four == null) {
                    four = new QuickTourFragment();
                    Bundle args = new Bundle();
                    args.putInt("position", position);
                    args.putSerializable("imageList", (Serializable) integerList);
                    four.setArguments(args);
                }
                return four;
            case 4:

                if (five == null) {
                    five = new QuickTourFragment();
                    Bundle args = new Bundle();
                    args.putInt("position", position);
                    args.putSerializable("imageList", (Serializable) integerList);
                    five.setArguments(args);
                }
                return five;

            case 5:

                if (six == null) {
                    six = new QuickTourFragment();
                    Bundle args = new Bundle();
                    args.putInt("position", position);
                    args.putSerializable("imageList", (Serializable) integerList);
                    six.setArguments(args);
                }
                return six;

            case 6:

                if (seven == null) {
                    seven = new QuickTourFragment();
                    Bundle args = new Bundle();
                    args.putInt("position", position);
                    args.putSerializable("imageList", (Serializable) integerList);
                    seven.setArguments(args);
                }
                return seven;

            case 7:

                if (eight == null) {
                    eight = new QuickTourFragment();
                    Bundle args = new Bundle();
                    args.putInt("position", position);
                    args.putSerializable("imageList", (Serializable) integerList);
                    eight.setArguments(args);
                }
                return eight;

            case 8:

                if (nine == null) {
                    nine = new QuickTourFragment();
                    Bundle args = new Bundle();
                    args.putInt("position", position);
                    args.putSerializable("imageList", (Serializable) integerList);
                    nine.setArguments(args);
                }
                return nine;


            default:
                QuickTourFragment fragment = new QuickTourFragment();
                Bundle args = new Bundle();
                args.putInt("position", position);
                args.putSerializable("imageList", (Serializable) integerList);
                fragment.setArguments(args);
                return fragment;
        }
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
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
        if (integerList != null) {
            count = integerList.size();
        }
        return count;
    }


    @Override
    public void finishUpdate(ViewGroup container) {
        try {
            super.finishUpdate(container);
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        }
    }
}
