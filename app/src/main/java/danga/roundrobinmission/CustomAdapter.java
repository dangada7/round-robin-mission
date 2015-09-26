package danga.roundrobinmission;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.security.PublicKey;
import java.util.ArrayList;

/**
 * Created by dan on 9/17/2015.
 */
public class CustomAdapter extends BaseAdapter{

    private Context context;
    String[] companiesId;
    String preferenceTableName;
    private int[] rating = {0,0,0,0,0};
    private int[] color;

    //-----------------------------------------------------------------------------
    public CustomAdapter(Context context,String preferenceTableName,String[] companiesId){
        this.context = context;
        this.preferenceTableName = preferenceTableName;
        this.companiesId=companiesId;

        color = new int[5];
        for (int i=0; i< color.length; i++)
            color[i] = context.getResources().getColor(R.color.gray);
    }
    //-----------------------------------------------------------------------------
    public void updateRating(int[] newRating){
        for(int i=0; i<newRating.length; i++){
            if(rating[i] < newRating[i])
                color[i] = context.getResources().getColor(R.color.green);
            if(rating[i] > newRating[i])
                color[i] = context.getResources().getColor(R.color.red);
            if(rating[i] == newRating[i])
                color[i] = context.getResources().getColor(R.color.gray);
            rating[i]=newRating[i];
        }
    }
    //-----------------------------------------------------------------------------
    @Override
    public int getCount() {
        return companiesId.length;
    }
    //-----------------------------------------------------------------------------
    @Override
    public String getItem(int position) {
        return companiesId[position];
    }
    //-----------------------------------------------------------------------------
    @Override
    public long getItemId(int position) {
        return position;
    }
    //-----------------------------------------------------------------------------
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int parentHeight = parent.getHeight();

        convertView = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);

        convertView.getLayoutParams().height = parentHeight/3;

        String companyId= companiesId[position];
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceTableName, context.MODE_PRIVATE);
        String companyName = sharedPreferences.getString(companyId, "preference doest exist");
        int companyIcon = sharedPreferences.getInt(companyName,0);

        //set icon
        ImageView  icon= (ImageView) convertView.findViewById(R.id.icon);
        icon.setImageResource(companyIcon);
        //set name
        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(companyName);
        //set value
        TextView rate = (TextView) convertView.findViewById(R.id.value);
        rate.setText(String.valueOf(rating[position]));
        rate.setTextColor(color[position]);

        return convertView;
    }
}
