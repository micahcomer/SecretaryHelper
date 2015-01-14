package mjc.com.secretaryhelper.PublisherRecordFragment;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import mjc.com.secretaryhelper.Parse.ParseHelper;
import mjc.com.secretaryhelper.Parse.ParseObjects.MonthReport;
import mjc.com.secretaryhelper.Parse.ParseObjects.PublisherInfo;
import mjc.com.secretaryhelper.R;

public class PublisherCardListAdapter extends RecyclerView.Adapter<PublisherCardHolder> implements View.OnFocusChangeListener, TextView.OnEditorActionListener {

    int currentYear;
    Context mContext;
    PublisherInfo currentPublisher;
    HashMap<Integer, ArrayList<MonthReport>> reports;
    ArrayList<Integer> reportsToClear;
    ArrayList<MonthReport> reportsToUpdate;
    int saveCount = 0;
    private static final int saveThreshold = 6;

    private static final int TAG_KEY_LOCATION = R.string.tag_key_location;


    public PublisherCardListAdapter(Context c, PublisherInfo info){
        reports = new HashMap<>();
        mContext =c;
        currentPublisher = info;
        ParseHelper.GetRecordsForPublisher(this, currentPublisher);
        currentYear = Calendar.getInstance().get(Calendar.YEAR);

        reportsToClear = new ArrayList<>();
        reportsToUpdate = new ArrayList<>();
    }

    @Override
    public PublisherCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        PublisherCardView card = (PublisherCardView) LayoutInflater.from(mContext).inflate(R.layout.pubcard, null);
        PublisherCardHolder holder = new PublisherCardHolder(card);
        card.holder = holder;
        card.setOnFocusChangeListener(this);
        return holder;

    }

    @Override
    public void onBindViewHolder(PublisherCardHolder holder, int position) {

        fillHeaders(holder);
        setMonths(holder);
        fillData(holder, position);
        fillTotals(holder);
        setCallBacks(holder);

    }

    @Override
    public int getItemCount() {
        if (reports.size()<=0)
            return 1;
        return reports.size();
    }

    private void fillHeaders(PublisherCardHolder holder){
        for(int i=0; i<8; i++){
            setHeader(holder.headersTV[i], i);
        }
    }
    private void setHeader(TextView textView, int i){

        textView.setGravity(Gravity.CENTER);
        textView.setTypeface(null, Typeface.BOLD);

        switch(i){
            case 0:{
                textView.setText("Month");
                break;
            }
            case 1:{
                textView.setText("Books");
                break;
            }
            case 2:{
                textView.setText("Brochures");
                break;
            }
            case 3:{
                textView.setText("Hours");
                break;
            }
            case 4:{
                textView.setText("Magazines");
                break;
            }
            case 5:{
                textView.setText("RVs");
                break;
            }
            case 6:{
                textView.setText("Studies");
                break;
            }
            case 7:{
                textView.setText("Notes");
                break;
            }
        }

    }
    private void setMonths(PublisherCardHolder holder){


        for (int i=1; i<14; i++){
            holder.monthsTV[i].setText(convertMonthFromRow(i));
            holder.monthsTV[i].setTypeface(null, Typeface.BOLD);
        }

    }
    private String convertMonthFromRow(int m){
        switch (m) {

            case 5:
                return "Jan";
            case 6:
                return "Feb";
            case 7:
                return "Mar";
            case 8:
                return "Apr";
            case 9:
                return "May";
            case 10:
                return "Jun";
            case 11:
                return "Jul";
            case 12:
                return "Aug";
            case 1:
                return "Sep";
            case 2:
                return "Oct";
            case 3:
                return "Nov";
            case 4:
                return "Dec";
            default: return "";
        }
    }
    private void fillData(PublisherCardHolder holder, int position){

        if (currentPublisher!=null){
            holder.publisherNameTV.setText(currentPublisher.toString());
        }


        int key = currentYear - position;
        ArrayList<MonthReport> reportsForYear = reports.get(key);
        if ((reportsForYear !=null)&&(reportsForYear.size()>0)){
            holder.serviceYearTV.setText(String.valueOf(reportsForYear.get(0).year));
        }
        MonthReport r = new MonthReport();

        for (int y=0; y<12; y++){

            int month = y+9;
            if (month>12){
                month = month-12;
            }

            if (reportsForYear!=null){
                for (int i=0; i<reportsForYear.size(); i++){
                    if (reportsForYear.get(i).month==month);
                    r = reportsForYear.get(i);
                }

                if (r!=null){
                    for (int x=0; x<7; x++){
                        TextView tv = holder.valuesTV[x][y];
                        switch (x){
                            case 0:
                                tv.setText(String.valueOf(r.books));
                                break;
                            case 1:
                                tv.setText(String.valueOf(r.brochures));
                                break;
                            case 2:
                                tv.setText(String.valueOf(r.hours));
                                break;
                            case 3:
                                tv.setText(String.valueOf(r.mags));
                                break;
                            case 4:
                                tv.setText(String.valueOf(r.RVs));
                                break;
                            case 5:
                                tv.setText(String.valueOf(r.BS));
                                break;
                            case 6:
                                if (r.notes==null){
                                    tv.setText("");
                                    break;
                                }
                                tv.setText(String.valueOf(r.notes));
                                break;
                        }
                    }
                }
            }

        }
    }
    private void fillTotals(PublisherCardHolder holder){

        int bookTot = 0;
        int brochTot = 0;
        float hourTot = 0;
        int magTot = 0;
        int RVtot = 0;
        int BStot = 0;

        for (int x=1; x<7; x++){
            for (int y=0; y<12; y++){
                try {
                    switch (x) {
                        case 0:
                            bookTot += Integer.valueOf(holder.valuesTV[x][y].getText().toString());
                            break;
                        case 1:
                            brochTot += Integer.valueOf(holder.valuesTV[x][y].getText().toString());
                            break;
                        case 2:
                            hourTot += Integer.valueOf(holder.valuesTV[x][y].getText().toString());
                            break;
                        case 3:
                            brochTot += Integer.valueOf(holder.valuesTV[x][y].getText().toString());
                            break;
                        case 4:
                            magTot += Integer.valueOf(holder.valuesTV[x][y].getText().toString());
                            break;
                        case 5:
                            RVtot += Integer.valueOf(holder.valuesTV[x][y].getText().toString());
                            break;
                        case 6:
                            BStot += Integer.valueOf(holder.valuesTV[x][y].getText().toString());
                            break;

                    }
                }catch (NumberFormatException e){

                }

            }
        }

        holder.totalsTV[0].setText("Totals:");
        holder.totalsTV[1].setText(String.valueOf(bookTot));
        holder.totalsTV[2].setText(String.valueOf(brochTot));
        holder.totalsTV[3].setText(String.valueOf(hourTot));
        holder.totalsTV[4].setText(String.valueOf(magTot));
        holder.totalsTV[5].setText(String.valueOf(RVtot));
        holder.totalsTV[6].setText(String.valueOf(BStot));
        holder.totalsTV[7].setText("");

    }
    public void setCallBacks(PublisherCardHolder holder) {

        TextView[][] views = holder.valuesTV;
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 12; y++) {
                EditText et = ((EditText) views[x][y]);
                et.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                et.setOnEditorActionListener(this);
                et.setTag(TAG_KEY_LOCATION, new int[]{x, y});
                et.setSelectAllOnFocus(true);
                et.setOnFocusChangeListener(this);
                if (x < 6) {
                    et.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                }
            }
        }
    }
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            setFocusToNextEditText(v);
        }
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            // TODO: what happens after the last cell?

            // do you want to hide the soft keyboard?
            //hideSoftKeyboard(v);
        }

        return true;    }
    private void setFocusToNextEditText(TextView v) {

        FrameLayout fl = (FrameLayout)v.getParent();
        FrameLayout nextfl;
        TextView nextTextView;

        TableRow row = (TableRow)fl.getParent();
        int currentCell = row.indexOfChild(fl);

        //At the end of the row?
        if (currentCell >= 6){
            //... if so, check which row we are on.
            TableLayout tl = (TableLayout)row.getParent();
            int currentRow = tl.indexOfChild(row);
            //On the last row?
            if(currentRow>= 12){
                //... if so, go to the top and select the first cell.
                TableRow nextRow = (TableRow)tl.getChildAt(0);
                nextfl = (FrameLayout)nextRow.getChildAt(1);
            }else{
                //... if not, just go to the next row and select the first cell.
                TableRow nextRow = (TableRow)tl.getChildAt(currentRow+1);
                nextfl = (FrameLayout)nextRow.getChildAt(1);
            }
        }else{
            //... if we are not at the end of the row, just go to the next cell in the row.
            nextfl = (FrameLayout)row.getChildAt(currentCell+1);
        }
        nextTextView = (TextView)nextfl.getChildAt(0);
        nextTextView.requestFocus();
    }
    public void OnPublisherRecordsReceived(List<ParseObject> objects, PublisherInfo info){

        if (info!=null){
            currentPublisher = info;
        }
    if ((objects!= null)&&(objects.size()>0)){
        for (ParseObject o:objects){
            if (o instanceof MonthReport){
                MonthReport r = (MonthReport)o;
                if (reports.get(r.year)==null){
                    reports.put(r.year, new ArrayList<MonthReport>());
                }
                reports.get(r.year).add(r);
            }
        }



    }

        if ((currentPublisher!=null)&&(objects.size()==0)){
            MonthReport firstReport = new MonthReport();
            firstReport.publisherInfo = currentPublisher;
            firstReport.year = currentYear;

            if (reports.get(currentYear)==null){
                reports.put(currentYear, new ArrayList<MonthReport>());
            }

            reports.get(currentYear).add(firstReport);

        }

        notifyDataSetChanged();

    }
    @Override
    public void onFocusChange(View v, boolean hasFocus) {

       if (v instanceof PublisherCardView){
           PublisherCardView p = (PublisherCardView)v;
           fillTotals(p.holder);
           saveCount++;
           if (saveCount== saveThreshold){
               packAndSendUpdate(p.holder);
               saveCount=0;
           }

       }
    }


    public void packAndSendUpdate(PublisherCardHolder holder){


        reportsToClear.clear();
        reportsToUpdate.clear();

        TextView[][] views = holder.valuesTV;
        int year = Integer.valueOf(holder.serviceYearTV.toString());


        //Iterate through each row on the card representing a month.
        //If the row contains data, check to see if we have a month report in the hash table for it.  If so, get it and add it to list to save.
        //If we don't have a report in the hash table, create a new report and add it to the hash table, and add it to the list to save.
        //If the row doesn't contain data, check to see if we have a month report in the hash table for it.  If so, add it to the list, to
        //both delete from Parse and from the hash table.

        for (int i=0; i<12; i++){

            int adj_month = i+9;
            if (adj_month>12){
                adj_month=adj_month-12;
            }

            MonthReport r = getReportForMonthAndYear(adj_month, year);
            if (isRowEmpty(views, i)){
                //find the row if it exists in the hash table and add it to be deleted.
                if (r!=null){
                    reportsToClear.add(r.month);
                    deleteLocalMonthReport(r.month, year);
                }
            }else{
                if (r!=null){
                    reportsToUpdate.add(r);
                }else{
                    r = new MonthReport();
                    //Populate the data for m based on the values in the edit text fields for that row.
                    r = populateMonthReport(r, adj_month, year, views);
                    reportsToUpdate.add(r);
                    if (reports.get(year)==null){
                        reports.put(year, new ArrayList<MonthReport>());
                    }
                    reports.get(year).add(r);
                }
            }
        }

        //Send the update to the server.
        ParseHelper.SendCardUpdate(reportsToUpdate, reportsToClear, currentPublisher, currentYear);

    }

    private boolean isRowEmpty(TextView[][] views, int row){

        if (Integer.valueOf(views[0][row].toString())!=0) return false;
        if (Integer.valueOf(views[1][row].toString())!=0) return false;
        if (Float.valueOf(views[2][row].toString())!=0f) return false;
        if (Integer.valueOf(views[3][row].toString())!=0) return false;
        if (Integer.valueOf(views[4][row].toString())!=0) return false;
        if (Integer.valueOf(views[5][row].toString())!=0) return false;
        if (TextUtils.isEmpty(views[6][row].toString())) return false;

        return true;
    }
    private MonthReport populateMonthReport(MonthReport r, int month, int year, TextView[][]views){
        r.books = Integer.valueOf(views[0][month].getText().toString());
        r.brochures = Integer.valueOf(views[1][month].getText().toString());
        r.hours = Float.valueOf(views[2][month].getText().toString());
        r.mags = Integer.valueOf(views[3][month].getText().toString());
        r.RVs = Integer.valueOf(views[4][month].getText().toString());
        r.BS = Integer.valueOf(views[5][month].getText().toString());
        r.notes = views[6][month].getText().toString();

        r.publisherInfo = currentPublisher;
        r.year = year;
        r.update();
        return r;
    }
    private MonthReport getReportForMonthAndYear(int month, int year){
        ArrayList<MonthReport> list = reports.get(year);
        if (list==null){
            return null;
        }else{
            for (MonthReport r:list){
                if (r.month==month){
                    return r;
                }
            }
        }
        return null;
    }
    private void deleteLocalMonthReport(int month, int year){

        ArrayList<MonthReport> list = reports.get(year);
        if (list!=null){
            int count = list.size()-1;
            for (int i = count; i>=0; i--){
                if (list.get(i).month==month){
                    list.remove(i);
                }
            }

        }
    }

}
