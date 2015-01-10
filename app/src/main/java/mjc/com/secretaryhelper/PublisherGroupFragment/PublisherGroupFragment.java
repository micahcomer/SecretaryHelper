package mjc.com.secretaryhelper.PublisherGroupFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.HeaderViewListAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mjc.com.secretaryhelper.Parse.ParseHelper;
import mjc.com.secretaryhelper.Parse.ParseObjects.PublisherGroup;
import mjc.com.secretaryhelper.Parse.ParseObjects.PublisherInfo;
import mjc.com.secretaryhelper.R;

public class PublisherGroupFragment extends Fragment {

    ArrayList<PublisherGroup> groups;
    ArrayList<PublisherInfo>servants;
    ArrayList<PublisherInfo>unassigned;

    ExpandableListView masterGroupListView;
    PublisherExpandableListAdapter masterGroupAdapter;
    boolean eldersPopulated = false;
    boolean msPopulated = false;
    ListView unassignedListView;
    AlertDialog alertDialog;
    CreateGroupLayout dialogView;
    PublisherInfo pubToEdit;

    public PublisherGroupFragment(){
        super();
        groups = new ArrayList<>();
        servants = new ArrayList<>();
        unassigned = new ArrayList<>();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v==unassignedListView){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            int position = info.position;
            pubToEdit = unassigned.get(position-1);
            menu.setHeaderTitle("Select a Group For This Publisher");
            for (PublisherGroup g : groups) {
                menu.add(0, v.getId(), 0, g.groupName);
            }
        }

        else {

            ExpandableListView.ExpandableListContextMenuInfo info =
                    (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;

            int type =
                    ExpandableListView.getPackedPositionType(info.packedPosition);

            int group =
                    ExpandableListView.getPackedPositionGroup(info.packedPosition);

            int child =
                    ExpandableListView.getPackedPositionChild(info.packedPosition);

            // Only create a context menu for child items
            if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                pubToEdit = masterGroupAdapter.getPublisherAtGroupAndPosition(group, child);

                menu.setHeaderTitle("Select a Group For This Publisher");
                for (PublisherGroup g : groups) {
                    menu.add(0, v.getId(), 0, g.groupName);
                }
            }
        }
    }


    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        for (PublisherGroup g:groups){
            if (item.getTitle()==g.groupName){

                masterGroupAdapter.addPublisher(pubToEdit, g);
                unassigned.remove(pubToEdit);
            }
        }

        masterGroupAdapter.notifyDataSetChanged();
        masterGroupListView.invalidateViews();
        notifyDataSetChangedOnUnassignedPubs();
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){


        //Get the main layout.
        LinearLayout mainLayout = (LinearLayout) inflater.inflate(R.layout.field_service_group_layout, container, false);

         //Create and assign the expandable list view.
        masterGroupListView = (ExpandableListView)mainLayout.findViewById(R.id.expandableListView);
        masterGroupAdapter = new PublisherExpandableListAdapter(getActivity(), this);
        masterGroupListView.setAdapter(masterGroupAdapter);
        masterGroupAdapter.setExpandableListView(masterGroupListView);
        registerForContextMenu(masterGroupListView);

        ParseHelper.GetGroups(masterGroupAdapter);

            //Add footer...
        TextView footerView = (TextView)(inflater.inflate(R.layout.publisher_name, null));
        footerView.setText("Add Group");
        footerView.setGravity(Gravity.CENTER);
        //masterGroupListView.addFooterView(footerView);
        footerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addGroup();
                }
            });
        masterGroupListView.addFooterView(footerView);

            //Deal with the listview for unassigned publishers...
        unassignedListView = (ListView) mainLayout.findViewById(R.id.unassigned_listview);
        TextView header = (TextView)inflater.inflate(R.layout.publisher_name, null);
        unassignedListView.addHeaderView(header);
        unassignedListView.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.publisher_name, unassigned));
        header.setText("Unassigned Publishers:");
        header.setTypeface(null, Typeface.BOLD);
        unassignedListView.setDivider(null);
        unassignedListView.setDividerHeight(10);
        ParseHelper.GetUnassignedPubs(this);
        registerForContextMenu(unassignedListView);

        return mainLayout;
    }

    private void addGroup(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        dialogView = (CreateGroupLayout) LayoutInflater.from(getActivity()).inflate(R.layout.group_create_dialog, null);
        DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("dialogListener:onClick", "which: " + which);
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Spinner overseerSpinner = (Spinner) dialogView.findViewById(R.id.spinner);
                        Spinner assistantSpinner = (Spinner)dialogView.findViewById(R.id.spinner2);
                        PublisherInfo overseer = (PublisherInfo) overseerSpinner.getSelectedItem();
                        PublisherInfo assistant = (PublisherInfo)assistantSpinner.getSelectedItem();
                        String name = ((TextView)dialogView.findViewById(R.id.editText)).getText().toString();
                        // do what you want with this info
                        createNewGroup(overseer, assistant, name);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        // do nothing
                        break;
                }
            }
        };
        builder.setView(dialogView);
        builder.setPositiveButton("OK", dialogListener);
        builder.setNegativeButton("CANCEL", dialogListener);
        alertDialog = builder.create();

        if ((eldersPopulated)&&(msPopulated)){
            showDialog();
        }else{
            if (!msPopulated){
                ParseHelper.GetMinesterialServants(this);
            }

            if (!eldersPopulated)
                ParseHelper.GetElders(this);
            }
    }

    private void showDialog(){

        Spinner os = (Spinner) dialogView.getChildAt(3);
        Spinner as = (Spinner) dialogView.getChildAt(5);

        os.setAdapter(new ArrayAdapter<PublisherInfo>(getActivity(), R.layout.publisher_name , servants));
        as.setAdapter(new ArrayAdapter<PublisherInfo>(getActivity(), R.layout.publisher_name , servants));
        alertDialog.show();
    }

    public void onMServantsReceived(List list){
        if ((list!=null)&&(list.size()>0)){
            for (Object o:list){
                if (o instanceof PublisherInfo){
                    PublisherInfo pub = (PublisherInfo)o;
                    pub.update();
                    servants.add(pub);
                }
            }
        }
        msPopulated = true;
        if (eldersPopulated){
            showDialog();
        }
    }

    public void onEldersReceived(List list){
        if ((list!=null)&&(list.size()>0)){
            for (Object o:list){
                if (o instanceof PublisherInfo){
                    PublisherInfo pub = (PublisherInfo)o;
                    pub.update();
                    servants.add(pub);
                }
            }
        }
        eldersPopulated=true;
        if (msPopulated){
            showDialog();
        }
    }

    private void createNewGroup(PublisherInfo overseer, PublisherInfo assistant, String name) {
        PublisherGroup group = new PublisherGroup();
        group.groupName = name;
        group.overseer = overseer;
        group.assistant = assistant;
        group.groupNumber = name.hashCode();
        ParseHelper.SaveParseObject(group);
        groups.add(group);
        masterGroupAdapter.notifyDataSetChanged();
    }

    public void onUnassignedPubsReceived(List list){

        if (list!=null){
            for (Object o:list){
                if (o instanceof PublisherInfo){
                    PublisherInfo pub = (PublisherInfo)o;
                    pub.update();
                    unassigned.add(pub);
                }
            }
            notifyDataSetChangedOnUnassignedPubs();
        }

    }

        private void notifyDataSetChangedOnUnassignedPubs(){
            HeaderViewListAdapter a = (HeaderViewListAdapter)unassignedListView.getAdapter();
            ArrayAdapter<PublisherInfo> aa = (ArrayAdapter<PublisherInfo>)a.getWrappedAdapter();
            aa.notifyDataSetChanged();
        }

}