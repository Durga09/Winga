package in.eightfolds.winga.v2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.OnEventListener;
import in.eightfolds.winga.scratch.ScratchTextView;
import in.eightfolds.winga.v2.model.GuessItem;

public class ScratchAdapter extends RecyclerView.Adapter<ScratchAdapter.ScratchViewHolder> implements View.OnClickListener{

    private ArrayList<GuessItem> guessItems;
    private Context context;
    private OnEventListener onEventListener;

    public ScratchAdapter(ArrayList<GuessItem> guessItems, Context context, OnEventListener onEventListener) {
        this.guessItems = guessItems;
        this.context = context;
        this.onEventListener = onEventListener;
    }

    int sendToNextScreen=0;
    @Override
    public void onClick(View v) {

    }

    @NonNull
    @Override
    public ScratchAdapter.ScratchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.v2_item_scratch_and_win, parent, false);

        return new ScratchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ScratchAdapter.ScratchViewHolder holder, final int position) {
        holder.scratchTextView.setText(guessItems.get(position).getTitle());

        if(holder.scratchTextView!=null){
            holder.scratchTextView.setRevealListener(new ScratchTextView.IRevealListener(){

                @Override
                public void onRevealed(ScratchTextView tv) {

                }

                @Override
                public void onRevealPercentChangedListener(ScratchTextView stv, float percent) {
                    if(sendToNextScreen==0){
                        stv.revealWholeArea();
                        onEventListener.onEventListener(stv.getId(),guessItems.get(position));
                        sendToNextScreen++;
                    }
                    }
            });
        }


    }

    @Override
    public int getItemCount() {
        return guessItems.size();
    }

    public static class ScratchViewHolder extends RecyclerView.ViewHolder {
        private ScratchTextView scratchTextView;
        public ScratchViewHolder(@NonNull View itemView) {
            super(itemView);
            scratchTextView=itemView.findViewById(R.id.scratch_view);
        }
    }
}
