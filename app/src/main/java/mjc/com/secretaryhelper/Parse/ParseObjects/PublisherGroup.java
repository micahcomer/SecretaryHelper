package mjc.com.secretaryhelper.Parse.ParseObjects;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import mjc.com.secretaryhelper.Parse.ParseHelper;
import mjc.com.secretaryhelper.PublisherGroupFragment.PublisherExpandableListAdapter;

/**
 * Created by Micah on 12/30/2014.
 */

@ParseClassName("publishergroup")
public class PublisherGroup extends ParseObject implements ParseHelper.SaveableParseObject{

    public int groupNumber;
    public String groupName;
    public PublisherInfo overseer;
    public PublisherInfo assistant;

    private PublisherExpandableListAdapter adapter;
    public PublisherExpandableListAdapter getAdapter(){return adapter;}
    public void setAdapter(PublisherExpandableListAdapter adapter){this.adapter = adapter;}

    public void update(){
        groupNumber = (int)(getNumber("groupNumber"));
        groupName = getString("groupName");
        overseer = (PublisherInfo)getParseObject("overseer");
        assistant = (PublisherInfo)getParseObject("assistant");
    }

    public void saveSelf(){

        put("groupNumber", groupNumber);
        put("groupName", groupName);
        if (overseer!=null){
            put("overseer", overseer);
        }
        if (assistant!=null){
            put("assistant", assistant);
        }

        saveInBackground();
    }
}
