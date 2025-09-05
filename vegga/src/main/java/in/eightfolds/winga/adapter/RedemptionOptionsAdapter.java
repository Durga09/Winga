package in.eightfolds.winga.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.model.RedemptionOption;
import in.eightfolds.winga.utils.Constants;

public class RedemptionOptionsAdapter extends RecyclerView.Adapter<RedemptionOptionsAdapter.OptionItemsViewHolder> implements View.OnClickListener {

    private static final String TAG = RedemptionOptionsAdapter.class.getSimpleName();
    private ArrayList<RedemptionOption> redemptionOptions;
    private Context context;
    private OnEventListener onEventListener;


    public RedemptionOptionsAdapter(Context context, ArrayList<RedemptionOption> redemptionOptions, final OnEventListener onEventListener) {
        this.redemptionOptions = redemptionOptions;
        this.context = context;
        this.onEventListener = onEventListener;
    }

    @Override
    public RedemptionOptionsAdapter.OptionItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_redemption_option, parent, false);

        return new RedemptionOptionsAdapter.OptionItemsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RedemptionOptionsAdapter.OptionItemsViewHolder holder, final int position) {
        RedemptionOption redemptionOption = redemptionOptions.get(position);

        try {
            if (redemptionOption.getTitle().equalsIgnoreCase(Constants.OPTION_VOUCHERS_TITLE)) {
                holder.optionBtn.setText(context.getString(R.string.vouchers));
            } else {
                holder.optionBtn.setText(redemptionOption.getSubTitle());
            }

            holder.optionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onEventListener != null) {
                        onEventListener.onEventListener(R.id.optionBtn, redemptionOptions.get(position));
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return redemptionOptions.size();
    }

    @Override
    public void onClick(View v) {


    }

    public class OptionItemsViewHolder extends RecyclerView.ViewHolder {


        private Button optionBtn;

        public OptionItemsViewHolder(View view) {
            super(view);
            optionBtn = view.findViewById(R.id.optionBtn);

        }
    }
}