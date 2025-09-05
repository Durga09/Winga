package in.eightfolds.winga.adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import in.eightfolds.winga.R;
import in.eightfolds.winga.fragment.ShareCodeFragment;
import in.eightfolds.winga.fragment.InvitesFragment;

/**
 * Created by Swapnika on 26-Apr-18.
 */

public class CodeInvitesPagerAdapter extends FragmentStatePagerAdapter {
    private Fragment currentFragment;
    private int numOfTabs;
    private Context context;

    public CodeInvitesPagerAdapter(Context context, FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.context = context;
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        if (position == 0) {
            fragment = new ShareCodeFragment();
        } else if (position == 1) {
            fragment = new InvitesFragment();
        }
        return fragment;
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

    public Fragment getCurrentFragment() {
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
    public CharSequence getPageTitle(int position) {

        if (position == 0) {
            return context.getString(R.string.share);
        } else if (position == 1) {
            return context.getString(R.string.invites);
        }
        return null;
    }

}