package mjc.com.secretaryhelper.PublisherRecordFragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import mjc.com.secretaryhelper.Parse.ParseHelper;
import mjc.com.secretaryhelper.Parse.ParseObjects.PublisherInfo;
import mjc.com.secretaryhelper.Parse.ParseQueryListener;
import mjc.com.secretaryhelper.R;


public class PublisherNameListAdapter extends RecyclerView.Adapter<PublisherNameHolder> implements AdapterView.OnItemClickListener, ParseQueryListener, View.OnClickListener {

    Context context;
    ArrayList<PublisherInfo> infoRecords;
    PublisherInfo selectedRecord;
    int selectedItem = 0;
    PublisherRecordFragment publisherRecordFragment;
    PublisherCardFragment publisherCardFragment;
    private static final int VIEW_TYPE = 0;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;


    public PublisherNameListAdapter(Context c, PublisherRecordFragment publisherRecordFragment, PublisherCardFragment publisherCardFragment) {
        context = c;
        infoRecords = new ArrayList<>();
        this.publisherRecordFragment = publisherRecordFragment;
        this.publisherCardFragment = publisherCardFragment;
        ParseHelper.GetAllNames(this);
    }


    public void addItem(PublisherInfo name) {
        if (!infoRecords.contains(name)) {
            infoRecords.add(name);
            notifyDataSetChanged();
        }
    }

    public void removeItem(PublisherInfo name){
        if (infoRecords.contains(name)){
            infoRecords.remove(name);
            ParseHelper.DeletePublisher(name);
            notifyDataSetChanged();
            ((PublisherCardListAdapter)(publisherCardFragment.cardRecycler.getAdapter())).setPublisher(null);
            publisherCardFragment.cardRecycler.getAdapter().notifyDataSetChanged();
        }
    }

    public void setSelectedItem(int position) {
        selectedItem = position;
        if (position<infoRecords.size()){
            publisherRecordFragment.setSelectedPublisher(infoRecords.get(position), position);
        }
        if (selectedItem < infoRecords.size()) {
            selectedRecord = infoRecords.get(selectedItem);
            publisherCardFragment.setInfo(infoRecords.get(selectedItem));
           ((PublisherCardListAdapter)publisherCardFragment.cardRecycler.getAdapter()).packAndSendUpdateAll();
            ParseHelper.GetRecordsForPublisher((PublisherCardListAdapter)publisherCardFragment.cardRecycler.getAdapter(), infoRecords.get(position));
        }

        notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        setSelectedItem(position);

    }

    @Override
    public void onQueryCompleted(ArrayList<? extends ParseObject> objects, Class<? extends ParseObject> c) {


        if (objects.size() > 0) {
            if (c.equals(PublisherInfo.class)) {

                for (ParseObject o : objects) {
                    PublisherInfo info = (PublisherInfo) o;
                    info.update();
                    addItem(info);
                }

                sortRecords();
                setSelectedItem(0);

                if (selectedItem < 0) {
                    selectedItem = 0;
                }
                notifyDataSetChanged();
                ParseHelper.GetRecordsForPublisher((PublisherCardListAdapter)publisherCardFragment.cardRecycler.getAdapter(), infoRecords.get(selectedItem));

            }
        }

    }

    @Override
    public PublisherNameHolder onCreateViewHolder(ViewGroup viewGroup, int position) {

        LinearLayout mainLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.recycler_pubnamerow, null);
        PublisherNameHolder holder = new PublisherNameHolder(mainLayout);
        holder.setAdapter(this);
        holder.setContext(context);
        holder.setRecordFragment(publisherRecordFragment);

        return holder;
    }



    @Override
    public void onBindViewHolder(PublisherNameHolder publisherNameHolder, int i) {

                PublisherInfo info = infoRecords.get(i);
                publisherNameHolder.bindPublisher(info, selectedItem == i);
                info.holder = publisherNameHolder;
        if (info.firstName == "New Publisher"){
            publisherNameHolder.showPubInfoDialog();
        }
    }

    @Override
    public int getItemCount() {
        //if (footerEnabled)
        //    return infoRecords.size() + 1;
        return infoRecords.size();
    }

    @Override
    public void onClick(View v) {

        setSelectedItem(((ViewGroup) v.getParent()).indexOfChild(v));
    }

    @Override
    public int getItemViewType(int position){
        if (position == TYPE_HEADER){
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private void sortRecords() {
        Collections.sort(infoRecords, new Comparator<PublisherInfo>() {
            @Override
            public int compare(PublisherInfo lhs, PublisherInfo rhs) {

                //return 0 if lhs comes before rhs, and 1 otherwise
                if (lhs.lastName != rhs.lastName) {
                    return lhs.lastName.compareToIgnoreCase(rhs.lastName);

                } else {
                    if (lhs.firstName != rhs.firstName) {
                        return lhs.firstName.compareToIgnoreCase(rhs.firstName);
                    } else {
                        return lhs.middleName.compareToIgnoreCase(rhs.middleName);
                    }
                }

            }
        });
    }

}






