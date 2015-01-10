package mjc.com.secretaryhelper.PublisherRecordFragment;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import mjc.com.secretaryhelper.Parse.ParseHelper;
import mjc.com.secretaryhelper.Parse.ParseObjects.PublisherInfo;
import mjc.com.secretaryhelper.Parse.ParseQueryListener;
import mjc.com.secretaryhelper.R;


public class PublisherNameListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener, ParseQueryListener {

    Context context;
    ArrayList<PublisherInfo> infoRecords;
    PublisherInfo selectedRecord;
    int selectedItem=0;
    PublisherRecordFragment publisherRecordFragment;
    PublisherCardFragment publisherCardFragment;
    PublisherInfoFragment publisherInfoFragment;

    public PublisherNameListAdapter(Context c, PublisherRecordFragment publisherRecordFragment, PublisherCardFragment publisherCardFragment, PublisherInfoFragment publisherInfoFragment){
        context = c;
        infoRecords = new ArrayList<>();
        this.publisherRecordFragment = publisherRecordFragment;
        this.publisherCardFragment = publisherCardFragment;
        this.publisherInfoFragment = publisherInfoFragment;
        ParseHelper.GetAllNames(this);
    }

    @Override
    public int getCount() {
        return infoRecords.size();
    }

    @Override
    public Object getItem(int position) {
        return infoRecords.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.publisher_name, null);
        textView.setText(infoRecords.get(position).firstName + " " + infoRecords.get(position).lastName);
        if (selectedItem == position){
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        }else{
            textView.setTypeface(textView.getTypeface(), Typeface.NORMAL);
        }
        textView.setPadding(15, 15, 0, 15);
        return textView;
    }

    public void addItem(PublisherInfo name){
        if (!infoRecords.contains(name)) {
            infoRecords.add(name);
            notifyDataSetChanged();
        }
    }

    public void setSelectedItem(int position){
        selectedItem = position;
        publisherRecordFragment.setSelectedPublisherIndex(position);
        if (selectedItem<infoRecords.size()){
            selectedRecord = infoRecords.get(selectedItem);
            publisherCardFragment.setInfo(infoRecords.get(selectedItem));
            publisherInfoFragment.setInfo(infoRecords.get(selectedItem));
            publisherCardFragment.saveCard();
            publisherInfoFragment.populateView(infoRecords.get(selectedItem));
            ParseHelper.GetRecordsForPublisher(publisherCardFragment, infoRecords.get(position));
        }

        notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        setSelectedItem(position);

    }

    @Override
    public void onQueryCompleted(ArrayList<? extends ParseObject> objects, Class<? extends ParseObject> c) {


       if (objects.size()>0){
            if (c.equals(PublisherInfo.class)){

                for(ParseObject o:objects){
                    PublisherInfo info = (PublisherInfo)o;
                    info.update();

                    /*String name;
                    if ( TextUtils.isEmpty(info.middleName)){
                        name = info.firstName + " " + info.lastName;
                    } else{
                        name = info.firstName+ " " + info.middleName + " " + info.lastName;
                    }
                    */
                    addItem(info);

                }

                notifyDataSetChanged();
                publisherInfoFragment.setInfo(infoRecords.get(selectedItem));
                ParseHelper.GetRecordsForPublisher(publisherCardFragment, infoRecords.get(selectedItem));
            }
        }

    }

    @Override
    public void notifyDataSetChanged(){


        Collections.sort(infoRecords, new Comparator<PublisherInfo>() {
            @Override
            public int compare(PublisherInfo lhs, PublisherInfo rhs) {

                //return 0 if lhs comes before rhs, and 1 otherwise
                if (lhs.lastName!=rhs.lastName){
                    return lhs.lastName.compareToIgnoreCase(rhs.lastName);

                }else{
                    if (lhs.firstName!=rhs.firstName){
                        return lhs.firstName.compareToIgnoreCase(rhs.firstName);
                    }
                    else{
                        return lhs.middleName.compareToIgnoreCase(rhs.middleName);
                    }
                }

            }
        });


        selectedItem = infoRecords.indexOf(selectedRecord);
        if (selectedItem<0){
            selectedItem = 0;
        }
        super.notifyDataSetChanged();
    }

}
