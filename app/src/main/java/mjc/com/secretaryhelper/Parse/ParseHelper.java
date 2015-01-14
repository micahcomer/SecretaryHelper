package mjc.com.secretaryhelper.Parse;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import mjc.com.secretaryhelper.Parse.ParseObjects.MonthReport;
import mjc.com.secretaryhelper.PublisherGroupFragment.PublisherExpandableListAdapter;
import mjc.com.secretaryhelper.Parse.ParseObjects.PublisherGroup;
import mjc.com.secretaryhelper.PublisherGroupFragment.PublisherGroupFragment;
import mjc.com.secretaryhelper.Parse.ParseObjects.PublisherInfo;
import mjc.com.secretaryhelper.PublisherRecordFragment.PublisherCardFragment;
import mjc.com.secretaryhelper.PublisherRecordFragment.PublisherCardListAdapter;
import mjc.com.secretaryhelper.PublisherRecordFragment.PublisherNameListAdapter;
import mjc.com.secretaryhelper.PublisherRecordFragment.PublisherRecordFragment;

/**
 * Created to interact with Parse
 */
public class ParseHelper {

    public static void GetAllNames(final PublisherNameListAdapter adapter) {

        ParseQuery query = new ParseQuery(PublisherInfo.class);
        query.whereExists("key");
        query.orderByAscending("lastName").orderByAscending("firstName").orderByDescending("middleName").findInBackground(new FindCallback() {
            @Override
            public void done(List list, ParseException e) {
                ArrayList parseList = (ArrayList<ParseObject>) list;
                adapter.onQueryCompleted(parseList, PublisherInfo.class);
            }
        });
    }

    public static void GetRecordsForPublisher(final PublisherCardListAdapter adapter, final PublisherInfo info) {

        ParseQuery query = new ParseQuery(MonthReport.class);
        query.whereEqualTo("Publisher", info);
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List list, ParseException e) {
                ArrayList parseList = (ArrayList<ParseObject>) list;
                adapter.OnPublisherRecordsReceived(list, info);
            }
        });
    }



    public static void SendCardUpdate(ArrayList<MonthReport> monthsToUpdate, ArrayList<Integer> monthsToClear, PublisherInfo info, int year) {

        if (info==null){
            return;
        }
        for (Integer i : monthsToClear) {
            ParseQuery q = new ParseQuery(MonthReport.class);

            q
                    .whereEqualTo("Month", i)
                    .whereEqualTo("firstName", info.firstName)
                    .whereEqualTo("lastName", info.lastName)
                    .whereEqualTo("Year", year)
                    .findInBackground(new FindCallback() {
                        @Override
                        public void done(List list, ParseException e) {

                            if (list != null) {
                                for (Object o : list) {
                                    if (o instanceof ParseObject) {
                                        ((ParseObject) o).deleteInBackground();
                                    }
                                }
                            }
                        }
                    });
        }

        for (MonthReport r : monthsToUpdate) {
            r.saveInBackground();
        }
    }

    public static void GetGroups(final PublisherGroupFragment groupFragment, final PublisherRecordFragment recordFragment) {

        ParseQuery q = new ParseQuery(PublisherGroup.class);
        q.whereExists("groupNumber").orderByAscending("groupName").findInBackground(new FindCallback() {
            @Override
            public void done(List list, ParseException e) {
                groupFragment.getMasterGroupAdapter().onGroupsReceived(list);
                recordFragment.onPublisherGroupsReceived(list);
            }
        });
    }

    public static void GetPublishersForGroup(final PublisherExpandableListAdapter masterGroupAdapter, final PublisherGroup group) {

        ParseQuery q = new ParseQuery(PublisherInfo.class);
        q.whereEqualTo("groupNumber", group.groupNumber).orderByDescending("lastName").orderByDescending("firstName").orderByDescending("middleName").findInBackground(new FindCallback() {
            @Override
            public void done(List list, ParseException e) {
                masterGroupAdapter.onGroupPubsReceived(list, group);
            }
        });
    }

    public static void GetUnassignedPubs(final PublisherGroupFragment groupFragment){
        ParseQuery q = new ParseQuery(PublisherInfo.class);
        q.whereEqualTo("groupNumber", 0).orderByDescending("lastName").orderByDescending("firstName").orderByDescending("middleName").findInBackground(new FindCallback() {
            @Override
            public void done(List list, ParseException e) {
                groupFragment.onUnassignedPubsReceived(list);
            }
        });
    }

    public static void GetMinesterialServants(final PublisherGroupFragment groupFragment){

        ParseQuery q = new ParseQuery(PublisherInfo.class);
        q.whereEqualTo("ministerialServant", true).orderByDescending("lastName").orderByDescending("firstName").orderByDescending("middleName").findInBackground(new FindCallback() {
            @Override
            public void done(List list, ParseException e) {
                groupFragment.onMServantsReceived(list);
            }
        });
    }

    public static void GetElders(final PublisherGroupFragment groupFragment){

        ParseQuery q = new ParseQuery(PublisherInfo.class);
        q.whereEqualTo("elder", true).orderByDescending("lastName").orderByDescending("firstName").orderByDescending("middleName").findInBackground(new FindCallback() {
            @Override
            public void done(List list, ParseException e) {
                groupFragment.onEldersReceived(list);
            }
        });
    }

    public static void SaveParseObject(SaveableParseObject object){
       if (object!=null){
           object.saveSelf();

       }
    }


    public static void SaveParseObjectWithSaveCallback(SaveableParseObjectWithCallback object, final Object callbackObject){
       if (object!=null){
            object.saveSelfWithCallback(callbackObject);
        }


    }

    public interface SaveableParseObject{
        public void saveSelf();
    }

    public interface SaveableParseObjectWithCallback{
        public void saveSelfWithCallback(final Object callbackObject);
    }

}
