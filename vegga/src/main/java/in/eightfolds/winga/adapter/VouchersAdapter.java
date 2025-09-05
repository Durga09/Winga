package in.eightfolds.winga.adapter;

import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.EventListener;

import in.eightfolds.winga.R;
import in.eightfolds.winga.activity.VouchersActivity;
import in.eightfolds.winga.model.Voucher;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.MyDialog;

public class VouchersAdapter extends RecyclerView.Adapter<VouchersAdapter.VouchersItemViewHolder> {

    private static final String TAG = VouchersAdapter.class.getSimpleName();
    private ArrayList<Voucher> vouchersList;
    private Context context;
    private EventListener eventListener;


    public VouchersAdapter(Context context, ArrayList<Voucher> vochersList) {
        vouchersList = vochersList;
        this.context = context;
        this.eventListener = eventListener;
    }

    @NonNull
    @Override
    public VouchersItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vouchers_lay, parent, false);

        return new VouchersItemViewHolder(itemView);
    }


    public ArrayList<Voucher> getVouchersList() {
        return vouchersList;
    }


    @Override
    public void onBindViewHolder(@NonNull final VouchersItemViewHolder holder, int position) {
        final Voucher voucher = vouchersList.get(position);

        try {

            //.String validity = voucher.getProduct_expiry_and_validity();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.voucherCodeTv.setText(Html.fromHtml(voucher.getProduct_expiry_and_validity(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                holder.voucherCodeTv.setText(Html.fromHtml(voucher.getProduct_expiry_and_validity()));
            }
            //holder.voucherCodeTv.setText(String.format(context.getString(R.string.voucher_validity),validity));

           /* String validity = Html.fromHtml(voucher.getProduct_expiry_and_validity()).toString();
            holder.voucherCodeTv.setText(String.format(context.getString(R.string.voucher_validity), validity));
            if (!TextUtils.isEmpty(voucher.getProduct_id())) {
                holder.voucherCodeTv.setText(String.format(context.getString(R.string.voucher_validity), voucher.getProduct_expiry_and_validity()));
            }*/


            if (!TextUtils.isEmpty(voucher.getProduct_name())) {
                //holder.voucherNameTv.setText(voucher.getProduct_name());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    holder.voucherNameTv.setText(Html.fromHtml(voucher.getProduct_name(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    holder.voucherNameTv.setText(Html.fromHtml(voucher.getProduct_name()));
                }
            }
            holder.voucherPointsTv.setText(String.valueOf(voucher.getLoyalityPoints()));
            holder.voucherRupeesTv.setText(String.format(context.getString(R.string.rupee_value), voucher.getDenomination() + ""));

            if (voucher.getImageId() != null && voucher.getImageId() != 0) {
                Glide.with(context)
                        .load(Constants.FILE_URL + voucher.getImageId())
                        .placeholder(R.drawable.voucher_default)
                        .error(R.drawable.voucher_default)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(holder.voucherIv);
            } else if (voucher.getProduct_image() != null && !TextUtils.isEmpty(voucher.getProduct_image())) {
                Glide.with(context)
                        .load(voucher.getProduct_image())
                        .placeholder(R.drawable.voucher_default)
                        .error(R.drawable.voucher_default)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(holder.voucherIv);
            } else {
                Glide.with(context)
                        .load(R.drawable.voucher_default)
                        .placeholder(R.drawable.voucher_default)
                        .error(R.drawable.voucher_default)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(holder.voucherIv);
            }

            if (voucher.getVoucherCountSelected() == 0) {
                holder.voucherAddBtn.setVisibility(View.VISIBLE);
                holder.voucherAddRemoveLay.setVisibility(View.GONE);
            } else {
                holder.voucherAddBtn.setVisibility(View.GONE);
                holder.voucherAddRemoveLay.setVisibility(View.VISIBLE);
                holder.voucherCountTv.setText(String.valueOf(voucher.getVoucherCountSelected()));
            }

            holder.voucherAddIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isSufficient = ((VouchersActivity) context).UpdatesPointsAndReturnSufficient(voucher.getLoyalityPoints(), true);
                    if (isSufficient) {
                        int voucherCount = voucher.getVoucherCountSelected();
                        voucherCount++;
                        holder.voucherCountTv.setText(String.valueOf(voucherCount));
                        voucher.setVoucherCountSelected(voucherCount);
                    }
                }
            });
            holder.voucherRemoveIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((VouchersActivity) context).UpdatesPointsAndReturnSufficient(voucher.getLoyalityPoints(), false);
                    int voucherCount = voucher.getVoucherCountSelected();
                    if (voucherCount == 1) {
                        holder.voucherAddRemoveLay.setVisibility(View.GONE);
                        voucher.setVoucherCountSelected(0);
                        holder.voucherAddBtn.setVisibility(View.VISIBLE);
                    } else {
                        voucherCount--;
                        holder.voucherCountTv.setText(String.valueOf(voucherCount));
                        voucher.setVoucherCountSelected(voucherCount);
                    }
                }
            });
            holder.voucherAddBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    boolean isSufficient = ((VouchersActivity) context).UpdatesPointsAndReturnSufficient(voucher.getLoyalityPoints(), true);
                    if (isSufficient) {
                        holder.voucherAddRemoveLay.setVisibility(View.VISIBLE);
                        voucher.setVoucherCountSelected(1);
                        holder.voucherCountTv.setText(String.valueOf(voucher.getVoucherCountSelected()));
                        holder.voucherAddBtn.setVisibility(View.GONE);
                    }
                }
            });

            holder.infoIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(voucher.getProduct_terms_conditions())) {
                        MyDialog.showVoucherTermsConditionsPopUp(context, voucher.getProduct_terms_conditions());
                       /* Intent intent = new Intent(context, WebBrowserActivity.class);
                        intent.putExtra("voucherTerms",voucher.getProduct_terms_conditions() );
                        context.startActivity(intent);*/
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return vouchersList.size();
    }


    class VouchersItemViewHolder extends RecyclerView.ViewHolder {


        private ImageView voucherIv;
        private ImageButton voucherRemoveIv, voucherAddIv, infoIv;
        private Button voucherAddBtn;
        private LinearLayout voucherAddRemoveLay;
        private TextView voucherCodeTv, voucherNameTv, voucherPointsTv, voucherRupeesTv, voucherCountTv;

        VouchersItemViewHolder(View view) {
            super(view);

            voucherIv = view.findViewById(R.id.voucherIv);
            voucherRemoveIv = view.findViewById(R.id.voucherRemoveIv);
            voucherAddIv = view.findViewById(R.id.voucherAddIv);
            voucherAddBtn = view.findViewById(R.id.voucherAddBtn);
            infoIv = view.findViewById(R.id.infoIv);
            voucherAddRemoveLay = view.findViewById(R.id.voucherAddRemoveLay);

            voucherCodeTv = view.findViewById(R.id.voucherCodeTv);
            voucherNameTv = view.findViewById(R.id.voucherNameTv);
            voucherPointsTv = view.findViewById(R.id.voucherPointsTv);
            voucherRupeesTv = view.findViewById(R.id.voucherRupeesTv);
            voucherCountTv = view.findViewById(R.id.voucherCountTv);
        }
    }
}