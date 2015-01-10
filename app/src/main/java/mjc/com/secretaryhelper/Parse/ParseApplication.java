package mjc.com.secretaryhelper.Parse;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import mjc.com.secretaryhelper.Parse.ParseObjects.MonthReport;
import mjc.com.secretaryhelper.Parse.ParseObjects.PublisherGroup;
import mjc.com.secretaryhelper.Parse.ParseObjects.PublisherInfo;
import mjc.com.secretaryhelper.R;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {

        super.onCreate();
        // VERY IMPORTANT
        // REQUIRED for all classes that EXTEND ParseObject
        // MUST register BEFORE initialize
        ParseObject.registerSubclass(MonthReport.class);
        ParseObject.registerSubclass(PublisherInfo.class);
        ParseObject.registerSubclass(PublisherGroup.class);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, getResources().getString(R.string.parse_application_id),
                getResources().getString(R.string.parse_client_id));
    }
}
