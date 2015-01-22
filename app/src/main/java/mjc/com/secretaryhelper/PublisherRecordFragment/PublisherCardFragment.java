package mjc.com.secretaryhelper.PublisherRecordFragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.parse.ParseObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mjc.com.secretaryhelper.Parse.ParseObjects.MonthReport;
import mjc.com.secretaryhelper.Parse.ParseObjects.PublisherGroup;
import mjc.com.secretaryhelper.Parse.ParseObjects.PublisherInfo;
import mjc.com.secretaryhelper.Parse.ParseQueryListener;
import mjc.com.secretaryhelper.R;

public class PublisherCardFragment extends Fragment{

    Context context;


    public SuperRecyclerView cardRecycler;
    int currentYear;
    private PublisherInfo mInfo;
    ArrayList<PublisherCardView> cards;

    public PublisherCardFragment(){
        super();
        Log.i("PubCardFrag", "Creating new publisher card fragment.");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        context = getActivity();
        LinearLayout mainLayout = (LinearLayout)inflater.inflate(R.layout.fragment_publisher_card, null);

        cardRecycler = (SuperRecyclerView) mainLayout.findViewById(R.id.cardRecycler);
        
        if (cardRecycler.getAdapter()==null){
            cardRecycler.setAdapter(new PublisherCardListAdapter(context, mInfo));
        }
        cardRecycler.setLayoutManager(new LinearLayoutManager(context));


        currentYear = Calendar.getInstance().get(Calendar.YEAR);
        cards = new ArrayList<>();
        return mainLayout;
    }

    public void setInfo(PublisherInfo info){
        mInfo = info;
    }

}
