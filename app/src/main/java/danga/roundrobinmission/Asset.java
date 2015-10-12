package danga.roundrobinmission;

/**
 * Created by dan on 10/11/2015.
 * just a basic class to hold all the info about an asset
 */
public class Asset {
    public int assetId;
    public double spot;
    public int color;

    public Asset(int assetId, double spot) {
        this.assetId=assetId;
        this.spot = spot;
        this.color = 0;
    }
    public Asset(int assetId, double spot,int color) {
        this.assetId=assetId;
        this.spot = spot;
        this.color = color;
    }
}
