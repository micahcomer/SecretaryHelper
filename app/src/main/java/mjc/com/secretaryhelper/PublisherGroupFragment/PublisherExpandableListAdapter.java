package mjc.com.secretaryhelper.PublisherGroupFragment;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import mjc.com.secretaryhelper.Parse.ParseHelper;
import mjc.com.secretaryhelper.Parse.ParseObjects.PublisherGroup;
import mjc.com.secretaryhelper.Parse.ParseObjects.PublisherInfo;
import mjc.com.secretaryhelper.R;

/**
 * Created by Micah on 1/2/2015.
 */
public class PublisherExpandableListAdapter extends BaseExpandableListAdapter {

    //Fields
    private Context mContext;
    private ArrayList<PublisherGroup> mGroups;
    private LinkedHashMap<Integer, ArrayList<PublisherInfo>> mPublishers;
    private ExpandableListView mPublisherView;

    //Constructor
    public PublisherExpandableListAdapter(Context context, PublisherGroupFragment groupFragment){
        Log("constructor");
        mContext = context;
        mGroups = new ArrayList<>();
        mPublishers = new LinkedHashMap<>();
        groupFragment.groups = mGroups;
    }

    //Getters and Setters
    public void setExpandableListView(ExpandableListView v){
        mPublisherView = v;
    }
    public PublisherInfo getPublisherAtGroupAndPosition(int groupOnList, int childPosition){

        int groupNumber = mGroups.get(groupOnList).groupNumber;

        return mPublishers.get(groupNumber).get(childPosition);
    }

    public void addGroup(PublisherGroup group){
        Log("addGroup");
        mGroups.add(group);

    }
    public void addPublisher(PublisherInfo publisher, PublisherGroup newGroup){
        Log("addPub");
        if (mPublishers.get(newGroup.groupNumber)==null){
            mPublishers.put(newGroup.groupNumber, new ArrayList<PublisherInfo>());
        }

        for (PublisherGroup g: mGroups){
            if (g.groupNumber == publisher.groupnumber){
                mPublishers.get(g.groupNumber).remove(publisher);
            }
        }

        mPublishers.get(newGroup.groupNumber).add(publisher);
        publisher.groupnumber = newGroup.groupNumber;
        ParseHelper.SaveParseObjectWithSaveCallback(publisher, null);

    }
    public void onGroupsReceived(List list){

        Log("groups received");
        if ((list!=null)&&(list.size()>0)){
            for (Object o:list){
                if (o instanceof PublisherGroup){
                    PublisherGroup group = (PublisherGroup)o;
                    group.update();
                    addGroup(group);
                    ParseHelper.GetPublishersForGroup(this, group);
                }
            }

        }
    }
    public void onGroupPubsReceived(List list, PublisherGroup group){
        Log("pubs received for group" + group.groupName);
        if ((list!=null)&&(list.size()>0)){
            for (Object o:list){
                if (o instanceof PublisherInfo){
                    PublisherInfo publisher = (PublisherInfo)o;
                    publisher.update();
                    addPublisher(publisher, group);
                }
            }
        }
        mPublisherView.invalidateViews();
        notifyDataSetChanged();

    }

    //Inherited from BaseExpandableListAdapter
    @Override
    public int getGroupCount() {
        Log("get group count");
        return mGroups.size();
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        Log("get child count");
        ArrayList<PublisherInfo> children = mPublishers.get(mGroups.get(groupPosition).groupNumber);
        if (children!=null){
            return children.size();
        }
        return 0;
    }
    @Override
    public Object getGroup(int groupPosition) {
        Log("get group " + groupPosition);
            return mGroups.get(groupPosition);
    }
    @Override
    public Object getChild(int groupPosition, int childPosition) {

        Log("get child "+ groupPosition+ ", "+childPosition);
       return mPublishers.get(mGroups.get(groupPosition).groupNumber).get(childPosition);
    }
    @Override
    public long getGroupId(int groupPosition) {
        Log("get group ID " + groupPosition);
        return groupPosition;
    }
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        Log("get child ID "+groupPosition+", "+ childPosition);
        return childPosition;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View groupHeading, ViewGroup parent) {

        if (groupHeading == null) {
            groupHeading = (LayoutInflater.from(mContext).inflate(R.layout.publisher_group_heading, parent, false));
        }

        Log("Get group view, Group "+ groupPosition);

        TextView nameView = (TextView)groupHeading.findViewById(R.id.groupname_textview);
        TextView overseerView = (TextView)groupHeading.findViewById(R.id.overseername_textview);
        TextView assistantView = (TextView)groupHeading.findViewById(R.id.assistantname_textview);

        PublisherGroup group = mGroups.get(groupPosition);
        nameView.setText(group.groupName);
        if (group.overseer!=null)
        overseerView.setText("Overseer: " + group.overseer.toString());
        if (group.assistant!=null)
        assistantView.setText("Assistant: " + group.assistant.toString());

        nameView.setTypeface(null, Typeface.BOLD);
        overseerView.setTypeface(null, Typeface.BOLD);
        assistantView.setTypeface(null, Typeface.BOLD);

        return groupHeading;
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Log("Get Child View: "+ groupPosition+ " , "+ childPosition);
        LinearLayout mainLayout = (LinearLayout)(LayoutInflater.from(mContext).inflate(R.layout.expandable_list_child, parent, false));
        TextView nameView = (TextView)mainLayout.findViewById(R.id.nametext);
        nameView.setPadding(25, 0, 0, 0);
        mainLayout.setLongClickable(true);
        mainLayout.setFocusableInTouchMode(false);
        nameView.setText(mPublishers.get(mGroups.get(groupPosition).groupNumber).get(childPosition).toString());
        return mainLayout;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }



    private void Log(String string){
        Log.i("PELA", string);
    }

@Override
    public void notifyDataSetChanged(){

    Collections.sort(mGroups, new Comparator<PublisherGroup>() {
        @Override
        public int compare(PublisherGroup lhs, PublisherGroup rhs) {
            //return 0 if lhs comes before rhs, and 1 otherwise
                return lhs.groupName.compareToIgnoreCase(rhs.groupName);
            }
    });

   for (PublisherGroup g:mGroups){
       ArrayList a = mPublishers.get(g.groupNumber);
       if (a!=null){
           Collections.sort(a, new Comparator<PublisherInfo>() {
               @Override
               public int compare(PublisherInfo lhs, PublisherInfo rhs) {
                   //return 0 if lhs comes before rhs, and 1 otherwise
                   if (!lhs.lastName.equals(rhs.lastName)){
                       return lhs.lastName.compareToIgnoreCase(rhs.lastName);
                   }else if (!lhs.firstName.equals(rhs.firstName)){
                       return (lhs.firstName.compareToIgnoreCase(rhs.firstName));
                   }else{
                       return (lhs.middleName.compareToIgnoreCase(rhs.middleName));
                   }

               }
           });
       }

   }
    super.notifyDataSetChanged();
}
}
