package mjc.com.secretaryhelper.PublisherRecordFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.parse.ParseObject;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mjc.com.secretaryhelper.Parse.ParseObjects.PublisherGroup;
import mjc.com.secretaryhelper.Parse.ParseObjects.PublisherInfo;
import mjc.com.secretaryhelper.PublisherGroupFragment.PublisherGroupFragment;
import mjc.com.secretaryhelper.R;

public class PublisherRecordFragment extends Fragment  {

    private SuperRecyclerView publisherNameList;
    private LinearLayoutManager linearLayoutManager;
    private int selectedPublisherIndex =0;
    private PublisherNameListAdapter nameListAdapter;
    private PublisherCardFragment cardFragment;
    private ArrayList<PublisherGroup> groups;
    private ImageView addPubsButton;
    private TextView cardsForLabel;
    private ImageView addCardsButton;

    public void setGroupFragment(PublisherGroupFragment groupFragment) {
        this.groupFragment = groupFragment;
    }

    PublisherGroupFragment groupFragment;

    public ArrayList<PublisherGroup> getGroups() {
        return groups;
    }

    public PublisherRecordFragment() {
    }

    public void setSelectedPublisher(PublisherInfo pub, int val){
        selectedPublisherIndex = val;
        cardsForLabel.setText("Publisher cards for " + pub.toString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        if (cardFragment==null){
            cardFragment = new PublisherCardFragment();
            cardFragment.setRetainInstance(true);
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, cardFragment)
                    .commit();
        }else{
            cardFragment.onCreateView(inflater, container, savedInstanceState);
        }

        LinearLayout mainLayout = (LinearLayout)inflater.inflate(R.layout.fragment_publisher_record, null);


        publisherNameList = (SuperRecyclerView)mainLayout.findViewById(R.id.listView2);
       if (nameListAdapter==null){

            nameListAdapter = new PublisherNameListAdapter(getActivity(), this, cardFragment);
        }
        publisherNameList.setAdapter(nameListAdapter);
        linearLayoutManager = new LinearLayoutManager(getActivity());


        addPubsButton = (ImageView)mainLayout.findViewById(R.id.addPubsImage);
        addPubsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //TODO - Add new pub via dialog here.
                PublisherInfo newPub = new PublisherInfo();
                newPub.firstName="New Publisher";
                newPub.lastName = "";
                nameListAdapter.addItem(newPub);
                nameListAdapter.notifyDataSetChanged();
            }
        });

        publisherNameList.setLayoutManager(linearLayoutManager);


        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);


        cardsForLabel = (TextView) mainLayout.findViewById(R.id.pubcardsfor_label);
        addCardsButton = (ImageView) mainLayout.findViewById(R.id.addcardButton);

        addCardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showYearPickerDialog();
            }
        });


        return mainLayout;
    }

    private void showYearPickerDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder

                .setMessage("Would you like to add an older publisher record card to this list?  (Note that card will be saved once data is entered.)")
                .setPositiveButton("Add Card", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addOlderCard();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do Nothing if cancelled.
                    }
                });

        builder.create();
        builder.show();
    }

    private void addOlderCard(){
        ((PublisherCardListAdapter)(cardFragment.cardRecycler.getAdapter())).addNewCard();
    }
    public void onPublisherGroupsReceived(List<ParseObject> objects){

        if (groups==null){
            groups = new ArrayList<>();
        }

        if ((objects!=null)&&(objects.size()>0)){
            for (ParseObject o:objects){
                if (o instanceof PublisherGroup){
                    PublisherGroup g = (PublisherGroup)o;
                    if (!groups.contains(g)){
                        groups.add(g);
                    }
                }
            }
        }


        if (nameListAdapter == null){
            nameListAdapter = new PublisherNameListAdapter(getActivity(), this, cardFragment);
        }
        nameListAdapter.setSelectedItem(selectedPublisherIndex);
        publisherNameList.setAdapter(nameListAdapter);

    }

    public void setGroups(ArrayList<PublisherGroup> groups){
        this.groups = groups;
        nameListAdapter = new PublisherNameListAdapter(getActivity(), this, cardFragment);
        nameListAdapter.setSelectedItem(selectedPublisherIndex);
        publisherNameList.setAdapter(nameListAdapter);
    }

}
