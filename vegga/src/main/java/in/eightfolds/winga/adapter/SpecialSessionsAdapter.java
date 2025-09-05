package in.eightfolds.winga.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import in.eightfolds.utils.DateTime;
import in.eightfolds.winga.R;
import in.eightfolds.winga.model.GameSession;

public class SpecialSessionsAdapter extends RecyclerView.Adapter<SpecialSessionsAdapter.OptionItemsViewHolder> implements View.OnClickListener {

    private static final String TAG = SpecialSessionsAdapter.class.getSimpleName();
    private ArrayList<GameSession> gameSessions;
    private String status;
    private Context context;


    public SpecialSessionsAdapter(Context context, ArrayList<GameSession> gameSessions, String status) {
        this.gameSessions = gameSessions;
        this.status = status;
        this.context = context;
    }

    @Override
    public SpecialSessionsAdapter.OptionItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_special_session, parent, false);

        return new SpecialSessionsAdapter.OptionItemsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SpecialSessionsAdapter.OptionItemsViewHolder holder, int position) {
        GameSession gameSession = gameSessions.get(position);

        try {

            if (!TextUtils.isEmpty(gameSession.getStartTime())) {
                String startTime = gameSession.getStartTime();
                String formattedStartDate = DateTime.getDateFromUTC(startTime, "yyyy-MM-dd HH:mm:ss", "MMM-dd, yyyy");
                holder.dateTv.setText(formattedStartDate);

                if (!TextUtils.isEmpty(gameSession.getEndTime())) {
                    String endTime = gameSession.getEndTime();
                    String formattedStartTime = DateTime.getDateFromUTC(startTime, "yyyy-MM-dd HH:mm:ss", "hh:mm aa");
                    String formattedEndTime = DateTime.getDateFromUTC(endTime, "yyyy-MM-dd HH:mm:ss", "hh:mm aa");

                    String sessionTime = formattedStartTime + " - " + formattedEndTime;
                    holder.timeTv.setText(sessionTime);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return gameSessions.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class OptionItemsViewHolder extends RecyclerView.ViewHolder {


        private TextView timeTv, dateTv, numberOfGiftsTv, giftNameTv;
        private LinearLayout giftsLL;

        public OptionItemsViewHolder(View view) {
            super(view);
            dateTv = view.findViewById(R.id.dateTv);
            timeTv = view.findViewById(R.id.timeTv);
            numberOfGiftsTv = view.findViewById(R.id.numberOfGiftsTv);
            giftNameTv = view.findViewById(R.id.giftNameTv);
            giftsLL = view.findViewById(R.id.giftsLL);
        }
    }
}