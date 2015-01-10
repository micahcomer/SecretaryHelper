package mjc.com.secretaryhelper.PublisherRecordFragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import mjc.com.secretaryhelper.Parse.ParseHelper;
import mjc.com.secretaryhelper.Parse.ParseObjects.PublisherInfo;
import mjc.com.secretaryhelper.R;

public class PublisherRecordFragment extends Fragment  {

    private ListView publisherNameList;
    private int selectedPublisherIndex =0;
    private PublisherNameListAdapter nameListAdapter;
    PublisherCardFragment cardFragment;
    PublisherInfoFragment infoFragment;
    Fragment currentFragment;
    TextView button;
    TextView footerView;

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

        if (infoFragment==null){
            infoFragment = new PublisherInfoFragment();
            infoFragment.setRetainInstance(true);

            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, infoFragment)
                    .hide(infoFragment)
                    .commit();
        }


        infoFragment.saveInfo();
        cardFragment.setInfo(infoFragment.mInfo);

        currentFragment = cardFragment;

        LinearLayout mainLayout = (LinearLayout)inflater.inflate(R.layout.fragment_publisher_record, null);

        if (nameListAdapter == null){
            nameListAdapter = new PublisherNameListAdapter(getActivity(), this, cardFragment, infoFragment);
        }

            publisherNameList = (ListView)mainLayout.findViewById(R.id.listView2);
            publisherNameList.setOnItemClickListener(nameListAdapter);



        publisherNameList.setAdapter(nameListAdapter);
        nameListAdapter.setSelectedItem(selectedPublisherIndex);

        infoFragment.setNameListAdapter(nameListAdapter);

        if (footerView==null){
            footerView = (TextView)inflater.inflate(R.layout.publisher_name_footer, null);
            footerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PublisherInfo newPub = new PublisherInfo();
                    newPub.firstName="New publisher...";
                    newPub.middleName="";
                    newPub.lastName="";
                    infoFragment.clearToAddPublisher(newPub);
                    nameListAdapter.addItem(newPub);
                    nameListAdapter.notifyDataSetChanged();
                }
            });
        }



        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);

        button = (TextView)mainLayout.findViewById(R.id.button_textview);
        button.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {buttonOnClick();}});

        return mainLayout;
    }

    private void buttonOnClick(){

        if (currentFragment==cardFragment){

            getChildFragmentManager()
                    .beginTransaction()
                    .hide(cardFragment).commit();

            getChildFragmentManager().beginTransaction().show(infoFragment).commit();

            cardFragment.saveCard();

            currentFragment = infoFragment;
            infoFragment.setInfo(cardFragment.getInfo());
            button.setText("View Publisher Card...");
            publisherNameList.addFooterView(footerView);

        }else{
            getChildFragmentManager()
                    .beginTransaction()
                    .hide(infoFragment)
                    .commit();

            getChildFragmentManager().beginTransaction().show(cardFragment).commit();

            infoFragment.saveInfo();
            cardFragment.setInfo(infoFragment.mInfo);
            currentFragment = cardFragment;
            ParseHelper.GetRecordsForPublisher(cardFragment, cardFragment.getInfo());
            button.setText("View Publisher Detail Information...");
            publisherNameList.removeFooterView(footerView);
        }
    }

}
