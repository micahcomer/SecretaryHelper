package mjc.com.secretaryhelper.PublisherRecordFragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.parse.ParseObject;


import java.util.ArrayList;
import java.util.List;

import mjc.com.secretaryhelper.Parse.ParseObjects.PublisherGroup;
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

    public void setGroupFragment(PublisherGroupFragment groupFragment) {
        this.groupFragment = groupFragment;
    }

    PublisherGroupFragment groupFragment;

    public ArrayList<PublisherGroup> getGroups() {
        return groups;
    }

    public PublisherRecordFragment() {
    }

    public void setSelectedPublisherIndex(int val){
        selectedPublisherIndex = val;
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
        }

        LinearLayout mainLayout = (LinearLayout)inflater.inflate(R.layout.fragment_publisher_record, null);

        publisherNameList = (SuperRecyclerView)mainLayout.findViewById(R.id.listView2);
        linearLayoutManager = new LinearLayoutManager(getActivity());


        addPubsButton = (ImageView)mainLayout.findViewById(R.id.addPubsImage);
        addPubsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //TODO - Add new pub via dialog here.

            }
        });

        publisherNameList.setLayoutManager(linearLayoutManager);


        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);

        return mainLayout;
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
            nameListAdapter = new PublisherNameListAdapter(getActivity(), this, cardFragment, (PublisherCardListAdapter)cardFragment.cardRecycler.getAdapter());
        }
        nameListAdapter.setSelectedItem(selectedPublisherIndex);
        publisherNameList.setAdapter(nameListAdapter);

    }

    public void setGroups(ArrayList<PublisherGroup> groups){
        this.groups = groups;
        nameListAdapter = new PublisherNameListAdapter(getActivity(), this, cardFragment, (PublisherCardListAdapter)cardFragment.cardRecycler.getAdapter());
        nameListAdapter.setSelectedItem(selectedPublisherIndex);
        publisherNameList.setAdapter(nameListAdapter);
    }

}
