package mjc.com.secretaryhelper.PublisherRecordFragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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
    PublisherCardView blankCard;
    int currentYear;
    private ArrayList<MonthReport> allReports;
    private PublisherInfo mInfo;
    private static final int TAG_KEY_LOCATION = R.string.tag_key_location;
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
        cardRecycler.setAdapter(new PublisherCardListAdapter(context, mInfo));
        cardRecycler.setLayoutManager(new LinearLayoutManager(context));
        currentYear = Calendar.getInstance().get(Calendar.YEAR);
        allReports = new ArrayList<>();
        cards = new ArrayList<>();
        return mainLayout;
    }

    public void setInfo(PublisherInfo info){
        mInfo = info;
    }




   /* public void onQueryCompleted(ArrayList<? extends ParseObject> objects, Class<? extends ParseObject> c, PublisherInfo info) {

        mInfo = info;
        allReports.clear();
        if (c.equals(MonthReport.class) ) {

            if (objects != null) {
                for (ParseObject o : objects) {

                    MonthReport r = (MonthReport) o;
                    r.update();
                    allReports.add(r);
                }

                //find which years need to be created
                ArrayList<Integer> yearsToCreate = findYearsToCreate(allReports);

                //create a card for each of those years and put in a list
                ArrayList<PublisherCardView> cardsToReturn = getCards(yearsToCreate, allReports);

                //hand the list of cards out to the fragment to deal with.
                ((PublisherCardListAdapter)cardRecycler.getAdapter()).setCards(cardsToReturn);
                ((PublisherCardListAdapter)cardRecycler.getAdapter()).notifyDataSetChanged();
            }
        }
    }
*/
    /*private ArrayList<Integer> findYearsToCreate(ArrayList<MonthReport>reports){

        ArrayList<Integer> yearsToAdd = new ArrayList<Integer>();
        yearsToAdd.add(currentYear);
        for(MonthReport r:reports){
            boolean addYear = true;
            for(Integer y:yearsToAdd){
                if (r.year==y){
                    addYear = false;
                }
            }
            if(addYear){
                yearsToAdd.add(r.year);
            }
        }

        return yearsToAdd;
    }*/

/*    private ArrayList<PublisherCardView> getCards(ArrayList<Integer>yearsToCreate, ArrayList<MonthReport> allReports){

        ArrayList<PublisherCardView>cards = new ArrayList<PublisherCardView>();

        for (Integer year:yearsToCreate){
            ArrayList<MonthReport> reportsForOneMonth = new ArrayList<MonthReport>();
            for(MonthReport r:allReports){
                if (r.year==year){
                    reportsForOneMonth.add(r);
                }
            }

            //TODO - If there are no reports for the current year in the list, make one and add it.
            if (reportsForOneMonth.size()==0){
                MonthReport reportForEmptyMonth = new MonthReport();

                reportForEmptyMonth.year = currentYear;
                reportForEmptyMonth.publisherInfo = mInfo;
                reportsForOneMonth.add(reportForEmptyMonth);
            }
            cards.add(getCardForOneMonth(reportsForOneMonth));
        }
        return cards;
    }

    private PublisherCardView getCardForOneMonth(ArrayList<MonthReport> reports){

        PublisherCardView card = (PublisherCardView) LayoutInflater.from(context).inflate(R.layout.pubcard, null);
        card.fillCard(reports, mInfo);
        //card.setCallBacks();


        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            card.setElevation(5);
        }
        return card;
    }

    public void saveCard(){

        //((PublisherCardListAdapter)cardRecycler.getAdapter()).saveCards();

    }*/



}
