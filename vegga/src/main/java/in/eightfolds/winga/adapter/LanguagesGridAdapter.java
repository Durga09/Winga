package in.eightfolds.winga.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import in.eightfolds.winga.R;
import in.eightfolds.winga.model.Language;

/**
 * Created by sp on 07/05/18.
 */

public class LanguagesGridAdapter extends BaseAdapter {
    private static final String TAG = LanguagesGridAdapter.class.getSimpleName() ;
    private Context mContext;
    private ArrayList<Language> languages;
    private LayoutInflater inflater;

    public LanguagesGridAdapter(Context context, ArrayList<Language> languages) {
        mContext = context;
        this.languages = languages;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return languages.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        private TextView languageTv;
        private LinearLayout languageLL;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        Language language = languages.get(position);
        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_language, parent, false);
            viewHolder.languageTv =  convertView.findViewById(R.id.languageTv);
            viewHolder.languageLL = (LinearLayout)convertView.findViewById(R.id.languageLL);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.languageTv.setText(language.getDispTitle());
        if(language.isSelected()){
            viewHolder.languageLL.setBackground(mContext.getResources().getDrawable(R.drawable.language_item_selected_bg));
            viewHolder.languageTv.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
        }else{
            viewHolder.languageLL.setBackground(mContext.getResources().getDrawable(R.drawable.language_item_bg));
            viewHolder.languageTv.setTextColor(mContext.getResources().getColor(R.color.colorBlack));

        }
        return convertView;
    }
}