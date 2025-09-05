package in.eightfolds.winga.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skydoves.progressview.ProgressView;

import java.util.ArrayList;
import java.util.List;

import in.eightfolds.winga.R;
import in.eightfolds.winga.model.QuestionResponse;
import in.eightfolds.winga.model.SurveyResult;

public class FormResultsRecyclerAdapter extends RecyclerView.Adapter<FormResultsRecyclerAdapter.ProgressItemsViewHolder>{

    private ArrayList<SurveyResult> mContentsList;
    private Context context;

    public FormResultsRecyclerAdapter(ArrayList<SurveyResult> optionsList, Context context) {
        mContentsList = optionsList;
        this.context = context;
    }
    @NonNull
    @Override
    public FormResultsRecyclerAdapter.ProgressItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_form_result, parent, false);

        return new FormResultsRecyclerAdapter.ProgressItemsViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ProgressItemsViewHolder holder, int position) {
            holder.optionsTextTv.setText(mContentsList.get(position).getOptionTxt()+"-"+mContentsList.get(position).getUserPercentage()+"%");
            holder.progressView.setMax(mContentsList.get(position).getTotalNumberOfUsers());
            holder.progressView.setProgress(mContentsList.get(position).getUserCount());

            holder.progressView.setBorderWidth(2);
    }



    @Override
    public int getItemCount() {
        return mContentsList.size();
    }

    public static class ProgressItemsViewHolder extends RecyclerView.ViewHolder {

        private ProgressView progressView;
        private TextView optionsTextTv;

        public ProgressItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            progressView=itemView.findViewById(R.id.progressView);
            optionsTextTv=itemView.findViewById(R.id.optionsTextTv);
        }

    }
}
