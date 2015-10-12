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
    ArrayList<Asset> assetsList;

    private String preferenceTableName;

    private int green;
    private int gray;
    private int red;


    //-----------------------------------------------------------------------------
    public CustomAdapter(Context context,String preferenceTableName,int[] companiesId){
        this.context = context;
        this.preferenceTableName = preferenceTableName;

        //init the colors
        green = context.getResources().getColor(R.color.green);
        red = context.getResources().getColor(R.color.red);
        gray = context.getResources().getColor(R.color.gray);

        //init assets
        assetsList = new ArrayList<Asset>();
        for (int i=0; i<companiesId.length; i++){
            Asset asset = new Asset(companiesId[i],0,gray);
            assetsList.add(asset);
        }



    }
    //-----------------------------------------------------------------------------
    public boolean updateRating(ArrayList<Asset> newAssentsList){
        boolean needToNotify = false;

        for(Asset newAsset : newAssentsList){

            //get the old asset
            Asset oldAsset = getOldAsset(newAsset.assetId);
            if (oldAsset==null)
                break;

            //if just one item change (rating change or color change ) then needToFind=true;
            if(oldAsset.spot != newAsset.spot || oldAsset.color != gray)
                needToNotify=true;
            else
                continue;

            //set the color
            if(oldAsset.spot < newAsset.spot)
                oldAsset.color = green;
            if(oldAsset.spot > newAsset.spot)
                oldAsset.color = red;
            if(oldAsset.spot == newAsset.spot)
                oldAsset.color = gray;
            //set the spot
            oldAsset.spot=newAsset.spot;

        }//close for

        return needToNotify;
    }

    //-----------------------------------------------------------------------------
    private Asset getOldAsset(int LookingForId) {
        for(Asset asset: assetsList){
            if (asset.assetId == LookingForId)
                return asset;
        }
        return null;
    }
    //-----------------------------------------------------------------------------
    @Override
    public int getCount() {
        return assetsList.size();
    }
    //-----------------------------------------------------------------------------
    @Override
    public Asset getItem(int position) {
        return assetsList.get(position);
    }
    //-----------------------------------------------------------------------------
    @Override
    public long getItemId(int position) {
        return position;
    }
    //-----------------------------------------------------------------------------
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        //set the height of one list item
        int parentHeight = parent.getHeight();
        convertView.getLayoutParams().height = parentHeight/3;

        //get the asset details
        int companyId= getItem(position).assetId;
        double spot = getItem(position).spot;
        int color = getItem(position).color;

        //get the asset name and icon from sharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceTableName, context.MODE_PRIVATE);
        String companyName = sharedPreferences.getString(String.valueOf(companyId), "preference doest exist");
        int companyIcon = sharedPreferences.getInt(companyName,0);

        //set icon
        ImageView  icon= (ImageView) convertView.findViewById(R.id.icon);
        icon.setImageResource(companyIcon);
        //set name
        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(companyName);
        //set value
        TextView rate = (TextView) convertView.findViewById(R.id.value);
        rate.setText(String.valueOf(spot));
        rate.setTextColor(color);

        return convertView;
    }

}
