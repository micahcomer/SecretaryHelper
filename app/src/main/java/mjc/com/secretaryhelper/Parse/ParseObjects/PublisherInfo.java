package mjc.com.secretaryhelper.Parse.ParseObjects;

import android.text.TextUtils;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import mjc.com.secretaryhelper.Parse.ParseHelper;
import mjc.com.secretaryhelper.PublisherRecordFragment.PublisherNameListAdapter;

@ParseClassName("publisherinfo")
public class PublisherInfo extends ParseObject implements ParseHelper.SaveableParseObjectWithCallback{

    public int key;
    public String firstName;
    public String middleName;
    public String lastName;
    public String address1;
    public String address2;
    public String city;
    public String state;
    public int zipCode;
    public String homePhone;
    public String cellPhone;
    public String workPhone;
    public String emailAddress;
    public boolean baptized;
    public int groupnumber;
    public String dateOfBirth;
    public String dateOfBaptism;
    public boolean elder;
    public boolean minesterialServant;
    public boolean regularPioneer;
    public boolean regularAuxiliaryPioneer;

    public void update(){
        this.key = this.hashCode();
        this.firstName = getString("firstName");
        this.middleName = getString("middleName");
        this.lastName = getString("lastName");
        this.address1 = getString("address1");
        this.address2 = getString("address2");
        this.city = getString("city");
        this.state = getString("state");
        this.zipCode = getInt("zipCode");
        this.homePhone = getString("homePhone");
        this.cellPhone = getString("cellPhone");
        this.workPhone = getString("workPhone");
        this.emailAddress = getString("emailAddress");
        this.baptized = getBoolean("baptized");
        this.groupnumber = getInt("groupNumber");
        this.dateOfBirth = getString("dateOfBirth");
        this.dateOfBaptism = getString("dateOfBaptism");
        this.elder = getBoolean("elder");
        this.minesterialServant = getBoolean("ministerialServant");
        this.regularPioneer = getBoolean("regularPioneer");
        this.regularAuxiliaryPioneer = getBoolean("regularAuxiliarlyPioneer");
    }

    public void saveSelfWithCallback(final Object callbackObject){


        put("key", key);
        if (firstName!=null){put("firstName", firstName);}
        if (middleName!=null){put("middleName", middleName);}
        if (firstName!=null){put("lastName", lastName);}
        if (address1!=null){put("address1", address1);}
        if (address2!=null){put("address2", address2);}
        if (city!=null){put("city", city);}
        if (state!=null){put("state", state);}
        put("zipCode", zipCode);
        if (homePhone!=null){put("homePhone", homePhone);}
        if (cellPhone!=null){put("cellPhone", cellPhone);}
        if (workPhone!=null){put("workPhone", workPhone);}
        if (emailAddress!=null){put("emailAddress", emailAddress);}
        put ("baptized", baptized);
        put ("groupNumber", groupnumber);
        if (dateOfBirth!=null){put("dateOfBirth", dateOfBirth);}
        if (dateOfBaptism!=null){put("dateOfBaptism", dateOfBaptism);}
        put ("elder", elder);
        put ("ministerialServant", minesterialServant);
        put ("regularPioneer", regularPioneer);
        put ("regularAuxiliaryPioneer", regularAuxiliaryPioneer);
        if (callbackObject==null) {
            saveInBackground();
        }else
        {
            if (callbackObject instanceof PublisherNameListAdapter){
                saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        ParseHelper.GetAllNames((PublisherNameListAdapter)callbackObject);
                    }
                });
            }

        }
    }

    @Override
    public boolean equals(Object a){
        if (a==null){
            return false;
        }else{
            if (!(a instanceof PublisherInfo)){
                return false;
            }else{
                if (key == ((PublisherInfo) a).key){
                    return true;
                }else{
                    return false;
                }
            }


        }

    }

    @Override
    public String toString(){

        if (this==null){
            return null;
        }
        if (TextUtils.isEmpty(middleName)){
            return (firstName+" "+lastName);
        }else{
            return firstName+" "+ middleName+ " "+ lastName;
        }
    }



}
