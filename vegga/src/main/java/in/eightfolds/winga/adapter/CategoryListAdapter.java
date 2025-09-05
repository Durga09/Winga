package in.eightfolds.winga.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.activity.ChildCategoriesActivity;
import in.eightfolds.winga.activity.CodesAndInvitesActivity;
import in.eightfolds.winga.activity.HomeActivity;
import in.eightfolds.winga.activity.HtmlGameSessionActicity;
import in.eightfolds.winga.model.CategoryGame;
import in.eightfolds.winga.model.CategoryHomePageResponse;
import in.eightfolds.winga.model.CategoryResponse;
import in.eightfolds.winga.model.CategoryTranslation;
import in.eightfolds.winga.model.ContentTranslation;
import in.eightfolds.winga.model.GameSessionPrize;
import in.eightfolds.winga.utils.Constants;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> implements View.OnClickListener {

    private ArrayList<CategoryTranslation> categoryHomePageResponsesList;
    private Context mContext;

    private CategoryResponse categoryResponse;
    public CategoryListAdapter(Context context, ArrayList<CategoryTranslation> cgHomeResponseList,CategoryResponse categoryResponse) {
        categoryHomePageResponsesList = cgHomeResponseList;
        mContext=context;
        this.categoryResponse=categoryResponse;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);

        return new CategoryListAdapter.CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        if(categoryHomePageResponsesList.get(position).getTranslations().size()!=0){
            holder.gamesTv.setText(categoryHomePageResponsesList.get(position).getTranslations().get(0).getName());
        }else{
            holder.gamesTv.setText(categoryHomePageResponsesList.get(position).getCategoryName());
        }


      holder.categorLL.setOnClickListener(this);
      holder.categorLL.setTag(categoryHomePageResponsesList.get(position));
    }

    @Override
    public int getItemCount() {
        return categoryHomePageResponsesList.size();
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.games) {
                CategoryTranslation contentTranslation=(CategoryTranslation)v.getTag();
                EightfoldsUtils.getInstance().saveToSharedPreference(mContext, Constants.SELECTED_CATEGORY,String.valueOf(contentTranslation.getCatagoryId()));

                if(contentTranslation.getChildCategories()==null){
                    if(contentTranslation.getCategoryType()!=4){
                        Intent intent = new Intent(mContext, HomeActivity.class);

                        mContext.startActivity(intent);
                    }else{
                        if(contentTranslation.getCategoryType()==4)
                        {
                            Intent intent = new Intent(mContext, HtmlGameSessionActicity.class);
                            intent.putExtra(Constants.DATA,categoryResponse);
                            mContext.startActivity(intent);
                        }
                    }
                }else{
                    Intent intent = new Intent(mContext, ChildCategoriesActivity.class);
                    intent.putExtra(Constants.DATA,contentTranslation);
                    intent.putExtra(Constants.CATEGORY_TRANSLATION,categoryResponse);
                    mContext.startActivity(intent);
                }



        }
    }


    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        public TextView gamesTv;
        public LinearLayout categorLL;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            gamesTv=itemView.findViewById(R.id.gameTitle);
            categorLL=itemView.findViewById(R.id.games);
        }
    }
}
