package in.eightfolds.winga.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;

import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.model.QuestionResponse;
import in.eightfolds.winga.utils.Logg;

/**
 * Created by Manohar on 16-05-2018.
 */
public class QuestionOptionsAdapter extends RecyclerView.Adapter<QuestionOptionsAdapter.MyviewHolder> implements View.OnClickListener {

    private static final String TAG = QuestionOptionsAdapter.class.getSimpleName();
    private Context context;

    private QuestionResponse question;
    private OnEventListener eventListener;
    private boolean evenSent = false;

    private String[] mColors = {"#ceb25e", "#ce865e", "#cf5f5e"};
    private String[] mColorsLight = {"#f3c950", "#ffb992", "#ffc5c4"};

    public QuestionOptionsAdapter(Context context, QuestionResponse question, OnEventListener eventListener) {
        this.context = context;
        this.question = question;
        this.eventListener = eventListener;
        Collections.shuffle(this.question.getOptions());
    }

    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_option, parent, false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyviewHolder holder, int position) {

        holder.questionFl.setTag(position);
        holder.questionFl.setOnClickListener(this);



        if (!TextUtils.isEmpty(question.getOptions().get(position).getTxt().trim())) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                holder.answerChoice.setText(Html.fromHtml(question.getOptions().get(position).getTxt().trim(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                holder.answerChoice.setText(Html.fromHtml(question.getOptions().get(position).getTxt().trim()));
            }
        }

        String text = question.getOptions().get(position).getTxt().trim();
        if (text.length() > 0 && text.length() < 80) {
            holder.answerChoice.setTextSize(18);
        } else if (text.length() > 80 && text.length() < 120) {
            holder.answerChoice.setTextSize(14);
        } else if (text.length() > 120) {
            holder.answerChoice.setTextSize(12);
        }

        if (question.getOptions().get(position).getOppId().equals(question.getSelectedOppId())) {
            holder.tickIv.setVisibility(View.VISIBLE);
            holder.selectedLL.setVisibility(View.VISIBLE);
        } else {
            holder.tickIv.setVisibility(View.INVISIBLE);
            holder.selectedLL.setVisibility(View.GONE);
        }


        Logg.v(TAG, "position>> " + position);
        //if(!evenSent) {
        if (position == question.getOptions().size() - 1) {
            holder.orTv.setVisibility(View.GONE);
            holder.orTv1.setVisibility(View.VISIBLE);
            holder.lineView.setVisibility(View.GONE);
        } else if (position == 0) {
            holder.orTv1.setVisibility(View.GONE);
            holder.orTv.setVisibility(View.VISIBLE);

        } else {
            holder.orTv.setVisibility(View.VISIBLE);
            //holder.lineView.setVisibility(View.VISIBLE);
        }
        //}

        if (getItemCount() == 1) {
            holder.orTv.setVisibility(View.GONE);
            holder.orTv1.setVisibility(View.GONE);
        }
       if(position<mColors.length){
           holder.questionFl.setBackgroundColor(Color.parseColor(mColors[position]));
           holder.selectedLL.setBackgroundColor(Color.parseColor(mColorsLight[position]));
       }else{
           holder.questionFl.setBackgroundColor(Color.parseColor(mColors[position%mColors.length]));
           holder.selectedLL.setBackgroundColor(Color.parseColor(mColorsLight[position%mColors.length]));
       }

    }

    @Override
    public int getItemCount() {
        return question.getOptions().size();
    }


    public class MyviewHolder extends RecyclerView.ViewHolder {

        TextView answerChoice, orTv, orTv1;
        LinearLayout selectedLL;
        FrameLayout questionFl;
        ImageView tickIv;
        View lineView;


        public MyviewHolder(View itemView) {
            super(itemView);
            answerChoice = itemView.findViewById(R.id.answerChoice);
            orTv = itemView.findViewById(R.id.orTv);
            orTv1 = itemView.findViewById(R.id.orTv1);
            selectedLL = itemView.findViewById(R.id.selectedLL);
            questionFl = itemView.findViewById(R.id.questionFl);
            tickIv = itemView.findViewById(R.id.tickIv);
            lineView = itemView.findViewById(R.id.lineView);
        }
    }

    @Override
    public void onClick(View v) {

        int position = (int) v.getTag();

        if (v.getId() == R.id.questionFl) {

                Logg.v(TAG, "clicked Position >> " + position);
                question.setSelectedOppId(question.getOptions().get(position).getOppId());
                Logg.v(TAG, "selected opId >> " + question.getSelectedOppId());
                notifyDataSetChanged();
                if (!evenSent) {
                    eventListener.onEventListener();
                    evenSent = true;
                }

        }
    }
}

