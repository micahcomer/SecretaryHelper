package mjc.com.secretaryhelper.Parse.ParseObjects;


        import com.parse.ParseClassName;
        import com.parse.ParseObject;

        import mjc.com.secretaryhelper.Parse.ParseHelper;


/**
 *	required @ParseClassName annotation for reference to corresponding Parse class name
 *	extends ParseObject
 *	do not forget to register all subclasses of ParseObject in ParseApplication
 */
@ParseClassName("monthreport")
public class MonthReport extends ParseObject implements ParseHelper.SaveableParseObject {

    public int key;
    public PublisherInfo publisherInfo;
    public int month;
    public int year;
    public int books;
    public int brochures;
    public float hours;
    public int mags;
    public int RVs;
    public int BS;
    public String notes;

    /**
     * This method will get data from the ParseObject data and populate our local Java object fields.
     *///

    public MonthReport(){
        super();
    }

    public void update() {
        key = this.getInt("key");
        publisherInfo = (PublisherInfo) this.getParseObject("Publisher");
        month = this.getInt("Month");
        year = this.getInt("Year");
        books = this.getInt("Books");
        brochures = this.getInt("Brochures");
        Number n = this.getNumber("Hours");
        if (n!=null){
            hours = n.floatValue();
        }
        mags = this.getInt("Mags");
        RVs = this.getInt("Rvs");
        BS = this.getInt("BS");
        notes = this.getString("Notes");
    }


    public void saveSelf(){

        put("key", key);
        publisherInfo.update();
        put("Publisher", publisherInfo);

        put("Month", month);
        put("Year", year);

        put("Books", books);
        put("Brochures", brochures);
        put("Hours", hours);
        put("Mags", mags);
        put("Rvs", RVs);
        put("BS", BS);

        if (notes!=null){put("Notes", notes);}

        saveInBackground();

    }

}

