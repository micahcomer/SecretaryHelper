package mjc.com.secretaryhelper.PublisherRecordFragment;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;

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
    ArrayList<PublisherCardHolder>holders;

    private int saveCount=0;
    private static final int saveThreshold = 6;

    private static final int TAG_KEY_LOCATION = R.string.tag_key_location;


    public PublisherCardListAdapter(Context c, PublisherInfo info){
        reports = new HashMap<>();
        mContext =c;
        currentPublisher = info;
        ParseHelper.GetRecordsForPublisher(this, currentPublisher);
        currentYear = Calendar.getInstance().get(Calendar.YEAR);
        holders = new ArrayList<>();
        reportsToClear = new ArrayList<>();
        reportsToUpdate = new ArrayList<>();

    }

    @Override
    public PublisherCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        PublisherCardView card = (PublisherCardView) LayoutInflater.from(mContext).inflate(R.layout.pubcard, null);
        CardView.LayoutParams lp = new CardView.LayoutParams(CardView.LayoutParams.WRAP_CONTENT, CardView.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        lp.bottomMargin = 75;

        card.setLayoutParams(lp);
        PublisherCardHolder holder = new PublisherCardHolder(card);
        card.holder = holder;
        holders.add(holder);
        return holder;

    }

    @Override
    public void onBindViewHolder(PublisherCardHolder holder, int position) {

            fillHeaders(holder);
            setMonths(holder);
            fillData(holder, position);
            disableTotals(holder);
            fillTotals(holder);
            setCallBacks(holder);


    }

    private void disableTotals(PublisherCardHolder holder){
        for (int i=0; i<8; i++)
        holder.totalsTV[i].setFocusable(false);
    }

    public void setPublisher(PublisherInfo pub){
        currentPublisher = pub;

        if (currentPublisher==null){
            reports.clear();
            holders.clear();
        }
    }

    @Override
    public int getItemCount() {
        if (reports.size()<=0)   {
            if (currentPublisher!=null)
                return 1;
        }
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
        if (reportsForYear == null){
            reportsForYear = new ArrayList<>();
            reports.put(key, reportsForYear);
        }

            holder.serviceYearTV.setText(String.valueOf(key));


        MonthReport r = new MonthReport();

        for (int y=0; y<12; y++){

            int month = y+9;
            if (month>12){
                month = month-12;
            }

                for (int i=0; i<reportsForYear.size(); i++){
                    if (reportsForYear.get(i).month==month){
                        r = reportsForYear.get(i);
                        i= reportsForYear.size();
                    }
                    else{
                        if (i==reportsForYear.size()-1){
                            r = null;
                        }
                    }
                }

                if (r!=null){
                    for (int x=0; x<7; x++){
                        TextView tv = holder.valuesTV[x][y];
                        tv.setOnFocusChangeListener(this);
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
                else{
                    for (int x=0; x<6; x++) {
                        TextView tv = holder.valuesTV[x][y];
                        tv.setText("0");
                        tv.setOnFocusChangeListener(this);
                    }
                    holder.valuesTV[6][y].setText("");
                    holder.valuesTV[6][y].setOnFocusChangeListener(this);
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

        for (int x=0; x<7; x++){
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
                            hourTot += Float.valueOf(holder.valuesTV[x][y].getText().toString());
                            break;
                        case 3:
                            magTot += Integer.valueOf(holder.valuesTV[x][y].getText().toString());
                            break;
                        case 4:
                            RVtot += Integer.valueOf(holder.valuesTV[x][y].getText().toString());
                            break;
                        case 5:
                            BStot += Integer.valueOf(holder.valuesTV[x][y].getText().toString());
                            break;
                        case 6:
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

        final TextView[][] views = holder.valuesTV;
        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 12; y++) {
                EditText et = ((EditText) views[x][y]);
                et.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                et.setOnEditorActionListener(this);
                et.setTag(TAG_KEY_LOCATION, new int[]{x, y});
                et.setSelectAllOnFocus(true);
                if (x < 6) {
                    et.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                }
            }
        }

        for (int i=0; i<12; i++){
            final int j=i;
            views[6][i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    FrameLayout mainLayout = (FrameLayout)LayoutInflater.from(mContext).inflate(R.layout.notesdialoglayout, null);
                    final EditText notesField = (EditText)mainLayout.findViewById(R.id.notes_edittext);
                    notesField.setText(views[6][j].getText());

                    builder.setView(mainLayout);
                    builder.setTitle("Notes for " + currentPublisher.toString() + " for " + convertMonthFromRow(j-1));
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //add notes here.
                            views[6][j].setText(notesField.getText());
                            packAndSendUpdateAll();
                        }
                    })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //do nothing on cancel
                                }
                            })
                            .create().show();
                }
            });
        }
    }
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            setFocusToNextEditText(v);
            for (PublisherCardHolder h:holders){
                fillTotals(h);
            }
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

        reports.clear();
        holders.clear();

        if (info!=null){
            currentPublisher = info;
        }
    if ((objects!= null)&&(objects.size()>0)){
        for (ParseObject o:objects){
            if (o instanceof MonthReport){
                MonthReport r = (MonthReport)o;
                r.update();
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

    public void packAndSendUpdate(PublisherCardHolder holder){



        reportsToClear.clear();
        reportsToUpdate.clear();

        int year = currentYear;
        TextView[][] views = holder.valuesTV;
        if (isInteger(holder.serviceYearTV.getText().toString())){
            year = Integer.valueOf(holder.serviceYearTV.getText().toString());
        }

        //Iterate through each row on the card representing a month.
        //If the row contains data, check to see if we have a month report in the hash table for it.  If so, get it and add it to list to save.
        //If we don't have a report in the hash table, create a new report and add it to the hash table, and add it to the list to save.
        //If the row doesn't contain data, check to see if we have a month report in the hash table for it.  If so, add it to the list, to
        //both delete from Parse and from the hash table.

        for (int i=0; i<12; i++) {

            int adj_month = i + 9;
            if (adj_month > 12) {
                adj_month = adj_month - 12;
            }


            //1.  Try to find a report in our hash table matching our current year and month.
            //2.  Look at what returns and determine if we found a report.
            //3.  Next, see if the current card is empty.  There are now four scenarios:
            // a - No report, row empty: Do nothing.
            // b - Report found, row empty: add report to clear list.
            // c - No report, row full: make new report and populate with row data, add new row to save list
            // d - Report found, row full.  add existing report to save list.

            boolean reportFound;
            boolean rowEmpty;

            MonthReport r = getReportForMonthAndYear(adj_month, year);

            if (r == null) {
                reportFound = false;
            } else {
                reportFound = true;
            }
            rowEmpty = isRowEmpty(views, i);

            //a  - Do nothing!

            //b
            if ((reportFound)&&(rowEmpty)){
               reportsToClear.add(r.month);
                deleteLocalMonthReport(r.month, year);
            }

            //c
            if ((!reportFound)&&(!rowEmpty)){
                MonthReport report = new MonthReport();
                populateMonthReport(report, adj_month, year, views);

                reports.get(year).add(report);
                reportsToUpdate.add(report);
            }

            //d
            if ((reportFound)&&(!rowEmpty)){
                populateMonthReport(r, adj_month, year, views);
                reportsToUpdate.add(r);
            }



        }

        //Send the update to the server.
        ParseHelper.SendCardUpdate(reportsToUpdate, reportsToClear, currentPublisher, currentYear);

    }
    public void packAndSendUpdateAll(){
        for (PublisherCardHolder h:holders){
            packAndSendUpdate(h);
        }
    }
    private boolean isRowEmpty(TextView[][] views, int row){

        int emptyCount = 0;
        try{
            if (Integer.valueOf(views[0][row].getText().toString()) == 0) emptyCount++;
        }catch (NumberFormatException e){

        }
        try{
            if (Integer.valueOf(views[1][row].getText().toString()) == 0) emptyCount++;
        }catch (NumberFormatException e){

        }
        try{
            if (Float.valueOf(views[2][row].getText().toString()) == 0f) emptyCount++;
        }catch (NumberFormatException e){
        }
        try{
            if (Integer.valueOf(views[3][row].getText().toString()) == 0) emptyCount++;
        }catch (NumberFormatException e){
        }
        try{
            if (Integer.valueOf(views[4][row].getText().toString())== 0)emptyCount++;
        }catch(NumberFormatException e){
        }
        try{
            if (Integer.valueOf(views[5][row].getText().toString()) == 0) emptyCount++;
        }catch (NumberFormatException e){
        }
        try{
            if (TextUtils.isEmpty(views[6][row].getText().toString())) emptyCount++;
        }catch (NumberFormatException e){
        }

        if (emptyCount==7)
        return true;
        else
            return false;
    }
    private MonthReport populateMonthReport(MonthReport r, int month, int year, TextView[][]views){

        int unadjmonth = month-9;
        if (unadjmonth<0){
            unadjmonth = unadjmonth + 12;
        }

        r.month = month;
        r.publisherInfo = currentPublisher;
        r.year = year;
        r.notes = views[6][unadjmonth].getText().toString();

        try{
            r.books = Integer.valueOf(views[0][unadjmonth].getText().toString());
            r.brochures = Integer.valueOf(views[1][unadjmonth].getText().toString());
            r.hours = Float.valueOf(views[2][unadjmonth].getText().toString());
            r.mags = Integer.valueOf(views[3][unadjmonth].getText().toString());
            r.RVs = Integer.valueOf(views[4][unadjmonth].getText().toString());
            r.BS = Integer.valueOf(views[5][unadjmonth].getText().toString());
        }catch (NumberFormatException e){

        }
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
    private boolean isInteger( String input ) {
        try {
            Integer.parseInt( input );
            return true;
        }
        catch( NumberFormatException e ) {
            return false;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        saveCount++;
        if (saveCount>=saveThreshold){
            saveCount=0;
            for (PublisherCardHolder h:holders){
                fillTotals(h);
                packAndSendUpdate(h);
            }
        }
    }


    public void addNewCard(){

        int oldestYear = currentYear;
        for (int key:reports.keySet()){
            if (key<oldestYear){
                oldestYear = key;
            }
        }

        reports.put(oldestYear-1, new ArrayList<MonthReport>());
        notifyDataSetChanged();
    }
}
