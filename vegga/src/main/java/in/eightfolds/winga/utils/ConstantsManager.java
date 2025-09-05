package in.eightfolds.winga.utils;

import android.content.Context;

import java.util.ArrayList;

import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.model.AddressType;
import in.eightfolds.winga.model.QuickTourModel;
import in.eightfolds.winga.model.Setup;
import in.eightfolds.winga.model.SupportType;

public class ConstantsManager {

    public static ArrayList<SupportType> getSupportTypes(Context context) {
        ArrayList<SupportType> supportTypes = new ArrayList<>();

        SupportType supportTypeService = new SupportType();
        supportTypeService.setTitle(context.getString(R.string.support_service));
        supportTypeService.setSupportTypeId((long) 1);
        supportTypes.add(supportTypeService);

        SupportType supportTypeDelivery = new SupportType();
        supportTypeDelivery.setTitle(context.getString(R.string.support_delivery));
        supportTypeDelivery.setSupportTypeId((long) 2);
        supportTypes.add(supportTypeDelivery);

        SupportType supportTypeOther = new SupportType();
        supportTypeOther.setTitle(context.getString(R.string.other));
        supportTypeOther.setSupportTypeId((long) 3);
        supportTypes.add(supportTypeOther);

        return supportTypes;
    }

    public static String getSupportTypeFromId(Context context, Long supportType) {
       for (SupportType supportType1 :getSupportTypes(context)) {
            if ((supportType1.getSupportTypeId() + "").equalsIgnoreCase(supportType + "")) {
                return supportType1.getTitle();
            }
        }
        return "";
    }

    public static ArrayList<AddressType> getAddressTypes(Context context) {
        ArrayList<AddressType> addressTypes = new ArrayList<>();

        AddressType addressTypeHome = new AddressType();
        addressTypeHome.setTitle(context.getString(R.string.address_home));
        addressTypeHome.setAdreTypeId((long) 1);
        addressTypes.add(addressTypeHome);

        AddressType addressTypeOffice = new AddressType();
        addressTypeOffice.setTitle(context.getString(R.string.office));
        addressTypeOffice.setAdreTypeId((long) 2);
        addressTypes.add(addressTypeOffice);

        AddressType addressTypeOther = new AddressType();
        addressTypeOther.setTitle(context.getString(R.string.other));
        addressTypeOther.setAdreTypeId((long) 3);
        addressTypes.add(addressTypeOther);

        return addressTypes;
    }

    public static String getAddressTypeFromId(Context context, long addressTypeId) {

        for (AddressType addressType : getAddressTypes(context)) {
            if (addressType.getAdreTypeId() == addressTypeId) {
                return addressType.getTitle();
            }
        }
        return "";
    }

    public static ArrayList<QuickTourModel> getQuickTourData(Context context) {

        ArrayList<QuickTourModel> quickTourModels = new ArrayList<>();


        QuickTourModel quickTourModel1 = new QuickTourModel();
        quickTourModel1.setImageId(R.drawable.tutorial_a);
        quickTourModel1.setDescription(context.getString(R.string.tutorial_watch_videos));
        quickTourModel1.setOverlayImageId(R.drawable.tutorial_aa);
        quickTourModels.add(quickTourModel1);

        QuickTourModel quickTourModel2 = new QuickTourModel();
        quickTourModel2.setImageId(R.drawable.tutorial_b);
        quickTourModel2.setDescription(context.getString(R.string.tutorial_watch_video));
        quickTourModel2.setOverlayImageId(R.drawable.tutorial_ba);
        quickTourModels.add(quickTourModel2);

        QuickTourModel quickTourModel3 = new QuickTourModel();
        quickTourModel3.setImageId(R.drawable.tutorial_c);
        quickTourModel3.setDescription(context.getString(R.string.tutorial_answer_simple_questions));
        quickTourModel3.setOverlayImageId(R.drawable.tutorial_ca);
        quickTourModels.add(quickTourModel3);

        QuickTourModel quickTourModel4 = new QuickTourModel();
        quickTourModel4.setImageId(R.drawable.tutorial_d);
        quickTourModel4.setDescription(context.getString(R.string.tutorial_scratch_card));
        quickTourModel4.setOverlayImageId(R.drawable.tutorial_da);
        quickTourModels.add(quickTourModel4);

        QuickTourModel quickTourModel5 = new QuickTourModel();
        quickTourModel5.setImageId(R.drawable.tutorial_e);
        quickTourModel5.setDescription(context.getString(R.string.tutorial_winner));
        quickTourModel5.setOverlayImageId(R.drawable.tutorial_ea);
        quickTourModels.add(quickTourModel5);

        QuickTourModel quickTourModel6 = new QuickTourModel();
        quickTourModel6.setImageId(R.drawable.tutorial_f);
        quickTourModel6.setDescription(context.getString(R.string.tutorial_earn_rewards_loyalty_points));
        quickTourModel6.setOverlayImageId(R.drawable.tutorial_fa);
        quickTourModels.add(quickTourModel6);


        QuickTourModel quickTourModel7 = new QuickTourModel();
        quickTourModel7.setImageId(R.drawable.tutorial_g);
        quickTourModel7.setDescription(context.getString(R.string.tutorial_get_lucky_to_stand_close));
        quickTourModel7.setOverlayImageId(R.drawable.tutorial_ga);
        quickTourModels.add(quickTourModel7);

        QuickTourModel quickTourModel8 = new QuickTourModel();
        quickTourModel8.setImageId(R.drawable.tutorial_h);
        quickTourModel8.setDescription(context.getString(R.string.tutorial_redeem_to_paytm));
        quickTourModel8.setOverlayImageId(R.drawable.tutorial_ha);
        quickTourModels.add(quickTourModel8);

        QuickTourModel quickTourModel9 = new QuickTourModel();
        quickTourModel9.setImageId(R.drawable.tutorial_i);
        quickTourModel9.setDescription(context.getString(R.string.tutorial_vouchers));
        quickTourModel9.setOverlayImageId(R.drawable.tutorial_ia);
        quickTourModels.add(quickTourModel9);

        return quickTourModels;
    }

    public static String getSupportState(Context context, int supportStateId) {
        switch (supportStateId) {
            case 0:
                return context.getString(R.string.pending);

            case 1:
                return context.getString(R.string.resolved);

            case -1:
                return context.getString(R.string.closed);
        }
        return "";
    }
}
