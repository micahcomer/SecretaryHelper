package mjc.com.secretaryhelper.PublisherRecordFragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class PublisherCardHolder extends RecyclerView.ViewHolder{

    private PublisherCardView card;
    public TextView publisherNameTV;
    public TextView serviceYearLabelTV;
    public TextView serviceYearTV;
    public TextView[] headersTV;
    public TextView[] monthsTV;
    public TextView[][]valuesTV;
    public TextView[]totalsTV;

    public PublisherCardHolder(View itemView) {
        super(itemView);
        if (itemView instanceof PublisherCardView){
            card = (PublisherCardView)itemView;
            assignFields();
        }
    }

    private void assignFields(){

        //Assign the local variables of the class to the views on the card.

        RelativeLayout rl = (RelativeLayout) card.getChildAt(0);
        TableLayout tl = (TableLayout) rl.getChildAt(0);

        publisherNameTV = (TextView)rl.getChildAt(1);
        serviceYearLabelTV = (TextView)rl.getChildAt(2);
        serviceYearTV = (TextView)rl.getChildAt(3);

        if (headersTV == null){
            headersTV = new TextView[8];
        }

        if (totalsTV == null){
            totalsTV = new TextView[8];
        }

        for (int i=0; i<8; i++){
            headersTV[i]=(TextView)((FrameLayout)((TableRow)tl.getChildAt(0)).getChildAt(i)).getChildAt(0);
            totalsTV[i]=(TextView)((FrameLayout)((TableRow)tl.getChildAt(13)).getChildAt(i)).getChildAt(0);
        }

        if (monthsTV==null){
            monthsTV=new TextView[14];
        }

        for (int i=0; i<14; i++){
            monthsTV[i] = (TextView)((FrameLayout)((TableRow)tl.getChildAt(i)).getChildAt(0)).getChildAt(0);
        }

        if (valuesTV == null){
            valuesTV = new TextView[7][12];
        }

        for (int x=0; x<7; x++){
            for (int y=0; y<12; y++){
                valuesTV[x][y]= (TextView)((FrameLayout)((TableRow) tl.getChildAt(y+1)).getChildAt(x+1)).getChildAt(0);
            }
        }
    }

}
