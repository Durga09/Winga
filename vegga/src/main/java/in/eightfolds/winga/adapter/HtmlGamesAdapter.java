package in.eightfolds.winga.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.activity.HomeActivity;
import in.eightfolds.winga.activity.WebBrowserActivity;
import in.eightfolds.winga.model.CategoryContentGame;
import in.eightfolds.winga.model.CategoryTranslation;
import in.eightfolds.winga.model.SurveyResult;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Utils;

public class HtmlGamesAdapter extends RecyclerView.Adapter<HtmlGamesAdapter.HtmlGamesViewHolder>  implements View.OnClickListener{


    private ArrayList<CategoryContentGame> mContentsList;
    private Context context;
    private String userId;

    public HtmlGamesAdapter(ArrayList<CategoryContentGame> optionsList, Context context, long userrID) {
        mContentsList = optionsList;
        this.context = context;
        userId=String.valueOf(userrID);
    }
    @NonNull
    @Override
    public HtmlGamesAdapter.HtmlGamesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_html_game, parent, false);

        return new HtmlGamesAdapter.HtmlGamesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HtmlGamesViewHolder holder, int position) {
         holder.htmlGameName.setText(mContentsList.get(position).getName());
         holder.cll.setOnClickListener(this);
         holder.cll.setTag(mContentsList.get(position));
    }

    @Override
    public int getItemCount() {
        return mContentsList.size();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() ==R.id.cLL) {

                CategoryContentGame categoryContentGame=(CategoryContentGame)v.getTag();
//                Intent intent = new Intent(context, WebBrowserActivity.class);
//
//                intent.putExtra(Constants.DATA, categoryContentGame.getGameUrl()+userId);
//                intent.putExtra(Constants.TITLE, categoryContentGame.getName());
//                context.startActivity(intent);
                Utils.openInBrowser(context, categoryContentGame.getGameUrl()+userId);
        }
    }

    public static class HtmlGamesViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout cll;
        TextView htmlGameName;
        public HtmlGamesViewHolder(@NonNull View itemView) {
            super(itemView);
            cll=itemView.findViewById(R.id.cLL);
            htmlGameName=itemView.findViewById(R.id.htmlGameNameTv);
        }
    }
}
