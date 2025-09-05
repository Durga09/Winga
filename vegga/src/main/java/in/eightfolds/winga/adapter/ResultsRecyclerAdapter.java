package in.eightfolds.winga.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import in.eightfolds.winga.R;
import in.eightfolds.winga.model.QuestionResponse;

/**
 * Created by Swapnika on 02-May-18.
 */

public class ResultsRecyclerAdapter extends RecyclerView.Adapter<ResultsRecyclerAdapter.OptionItemsViewHolder> {

    private ArrayList<QuestionResponse> mContentsList;
    private Context context;

    public ResultsRecyclerAdapter(ArrayList<QuestionResponse> optionsList, Context context) {
        mContentsList = optionsList;
        this.context = context;
    }

    @Override
    public ResultsRecyclerAdapter.OptionItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lay_result_template, parent, false);

        return new ResultsRecyclerAdapter.OptionItemsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ResultsRecyclerAdapter.OptionItemsViewHolder holder, int position) {
        try {
            QuestionResponse question = mContentsList.get(position);
           // holder.resultStatusTv.setText(String.format(Utils.getCurrentLocale(), context.getString(R.string.answer), (position + 1) + "", Utils.getSuffixForNumber(position + 1)));

            if (question.isAnsCorrect()) {
                holder.winLossGifIV.setImageResource(R.drawable.correctanswer);
            } else {
                holder.winLossGifIV.setImageResource(R.drawable.wronganswer);
            }
            holder.getView().setAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in));
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mContentsList.size();
    }

    public class OptionItemsViewHolder extends RecyclerView.ViewHolder {


        private ImageView winLossGifIV;
        private TextView resultStatusTv;
        private View view;


        OptionItemsViewHolder(View view) {
            super(view);
            winLossGifIV = view.findViewById(R.id.winLossGifIV);
            resultStatusTv = view.findViewById(R.id.resultStatusTv);
            this.view = view;


        }

        public View getView() {
            return view;
        }
    }

}
