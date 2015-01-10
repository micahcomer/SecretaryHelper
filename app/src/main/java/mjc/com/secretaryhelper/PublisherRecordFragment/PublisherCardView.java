package mjc.com.secretaryhelper.PublisherRecordFragment;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import mjc.com.secretaryhelper.Parse.ParseHelper;
import mjc.com.secretaryhelper.Parse.ParseObjects.MonthReport;
import mjc.com.secretaryhelper.Parse.ParseObjects.PublisherInfo;
import mjc.com.secretaryhelper.R;

/**
 * Created by Micah on 12/3/2014.
 */
public class PublisherCardView extends CardView implements TextView.OnEditorActionListener, View.OnFocusChangeListener {

    private static final int TAG_KEY_LOCATION = R.string.tag_key_location;

    private TableRow header;
    private TableRow totals;
    private TableRow[] monthRows;
    ArrayList<MonthReport> reports;
    private PublisherInfo currentPublisher;
    private int currentYear;
    private int saveCount =0;
    private static final int saveThreshold = 10;

    ArrayList<MonthReport>reportsToUpdate;
    ArrayList<Integer>reportsToClear;

    public PublisherCardView(Context context) {
        super(context);
        monthRows = new TableRow[12];
        reportsToUpdate = new ArrayList<MonthReport>();
        reportsToClear = new ArrayList<Integer>();
        currentPublisher = new PublisherInfo();

    }
    public PublisherCardView(Context context, AttributeSet attrs){
        super(context, attrs);
        monthRows = new TableRow[12];
        reportsToUpdate = new ArrayList<MonthReport>();
        reportsToClear = new ArrayList<Integer>();
        currentPublisher = new PublisherInfo();
    }
    public void fillCard(ArrayList<MonthReport> reports, PublisherInfo info){

        this.reports = reports;

        RelativeLayout rl = (RelativeLayout) getChildAt(0);
        TextView nameTextView = (TextView) rl.getChildAt(1);
        TextView yearTextView = (TextView) rl.getChildAt(3);

        if (info!=null){
            nameTextView.setText(info.firstName + " " + info.lastName);
            yearTextView.setText(String.valueOf(reports.get(0).year));
        }


        currentPublisher = info;
        currentYear = reports.get(0).year;

        fillHeaders();
        fillData(reports);
        fillTotals();
    }


    public void fillEmptyCard(){

        fillHeaders();
    }
    public void setCallBacks(){

        for(int i=0; i<12; i++) {
            for (int j = 1; j < 8; j++){
                EditText et = (EditText) ((FrameLayout) monthRows[i].getChildAt(j)).getChildAt(0);
                et.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                et.setOnEditorActionListener(this);
                et.setTag(TAG_KEY_LOCATION, new int[]{i, j});
                et.setSelectAllOnFocus(true);
                et.setOnFocusChangeListener(this);
                if (j<7){
                    et.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                }
            }

        }



    }
    private void fillHeaders(){
        RelativeLayout rl = (RelativeLayout) getChildAt(0);
        TableLayout tl  = (TableLayout) rl.getChildAt(0);
        header = (TableRow)tl.getChildAt(0);
        FrameLayout f;
        for(int i=0; i<8; i++){
            f = (FrameLayout) header.getChildAt(i);
            TextView tv = (TextView)f.getChildAt(0);
            setHeader(tv, i);
        }


    }
    private void fillData(ArrayList<MonthReport> reports){

        RelativeLayout rl = (RelativeLayout) getChildAt(0);
        TableLayout tl = (TableLayout)rl.getChildAt(0);
        for (int i=0; i<12; i++){
            monthRows[i] = (TableRow) tl.getChildAt(i + 1);
        }

        /*
        int rowToFillBasedOnMonth = reports.get(i).month-9;
        if (rowToFillBasedOnMonth<0){
            rowToFillBasedOnMonth = rowToFillBasedOnMonth+12;
        }
        TableRow row = monthRows[rowToFillBasedOnMonth];
        */

        for (int i=0; i<12; i++){

            TableRow row = monthRows[i];

            TextView monthText = (TextView)((FrameLayout)row.getChildAt(0)).getChildAt(0);
            EditText booksText = (EditText)((FrameLayout)row.getChildAt(1)).getChildAt(0);
            EditText brochText = (EditText)((FrameLayout)row.getChildAt(2)).getChildAt(0);
            EditText hoursText = (EditText)((FrameLayout)row.getChildAt(3)).getChildAt(0);
            EditText magsText =  (EditText)((FrameLayout)row.getChildAt(4)).getChildAt(0);
            EditText RVsText = (EditText)((FrameLayout)row.getChildAt(5)).getChildAt(0);
            EditText BSText = (EditText)((FrameLayout)row.getChildAt(6)).getChildAt(0);
            EditText notesText = (EditText)((FrameLayout)row.getChildAt(7)).getChildAt(0);

            MonthReport r = getAReportForRow(i, reports);

            if (r!=null){
                monthText.setText(convertMonthFromRow(i));
                booksText.setText(String.valueOf(r.books));
                brochText.setText(String.valueOf(r.brochures));
                hoursText.setText(String.valueOf(r.hours));
                magsText.setText(String.valueOf(r.mags));
                RVsText.setText(String.valueOf(r.RVs));
                BSText.setText(String.valueOf(r.BS));
                notesText.setText(r.notes);
            }else{

                monthText.setText(convertMonthFromRow(i));
                booksText.setText("0");
                brochText.setText("0");
                hoursText.setText("0");
                magsText.setText("0");
                RVsText.setText("0");
                BSText.setText("0");
                notesText.setText("");
            }
        }
    }
    private void fillTotals(){

        totals = (TableRow)((TableLayout)((RelativeLayout)getChildAt(0)).getChildAt(0)).getChildAt(13);


        int bookTot = 0;
        int brochTot = 0;
        float hourTot = 0;
        int magTot = 0;
        int RVtot = 0;
        int BStot = 0;

        try{
        for (int i=0; i<12; i++){
            bookTot += Integer.valueOf (String.valueOf(((EditText) ((FrameLayout) monthRows[i].getChildAt(1)).getChildAt(0)).getText()));
            brochTot += Integer.valueOf (String.valueOf(((EditText) ((FrameLayout) monthRows[i].getChildAt(2)).getChildAt(0)).getText()));
            hourTot += Float.valueOf (String.valueOf(((EditText) ((FrameLayout) monthRows[i].getChildAt(3)).getChildAt(0)).getText()));
            magTot += Integer.valueOf (String.valueOf(((EditText) ((FrameLayout) monthRows[i].getChildAt(4)).getChildAt(0)).getText()));
            RVtot += Integer.valueOf (String.valueOf(((EditText) ((FrameLayout) monthRows[i].getChildAt(5)).getChildAt(0)).getText()));
            BStot += Integer.valueOf (String.valueOf(((EditText) ((FrameLayout) monthRows[i].getChildAt(6)).getChildAt(0)).getText()));
        }
        }catch (NumberFormatException e){
            Log.i("NFE", "NumberFormatException");
        }

        TextView Totals;
        TextView Books;
        TextView Brochures;
        TextView Hours;
        TextView Mags;
        TextView RVs;
        TextView BSs;



        Totals = ((TextView)((FrameLayout)totals.getChildAt(0)).getChildAt(0));
        Totals.setText("Totals:");
        Totals.setTypeface(null, Typeface.BOLD);

        Books = ((TextView)((FrameLayout)totals.getChildAt(1)).getChildAt(0));
        Books.setText(String.valueOf(bookTot));
        Books.setTypeface(null, Typeface.BOLD);

        Brochures = ((TextView)((FrameLayout)totals.getChildAt(2)).getChildAt(0));
        Brochures.setText(String.valueOf(brochTot));
        Brochures.setTypeface(null, Typeface.BOLD);

        Hours = ((TextView)((FrameLayout)totals.getChildAt(3)).getChildAt(0));
        Hours.setText(String.valueOf(hourTot));
        Hours.setTypeface(null, Typeface.BOLD);

        Mags = ((TextView)((FrameLayout)totals.getChildAt(4)).getChildAt(0));
        Mags.setText(String.valueOf(magTot));
        Mags.setTypeface(null, Typeface.BOLD);

        RVs = ((TextView)((FrameLayout)totals.getChildAt(5)).getChildAt(0));
        RVs.setText(String.valueOf(RVtot));
        RVs.setTypeface(null, Typeface.BOLD);

        BSs = ((TextView)((FrameLayout)totals.getChildAt(6)).getChildAt(0));
        BSs.setText(String.valueOf(BStot));
        BSs.setTypeface(null, Typeface.BOLD);

        ((TextView)((FrameLayout)totals.getChildAt(7)).getChildAt(0)).setText("");

    }
    private String convertMonthFromRow(int m){
        switch (m) {

            case 4:
                return "Jan";
            case 5:
                return "Feb";
            case 6:
                return "Mar";
            case 7:
                return "Apr";
            case 8:
                return "May";
            case 9:
                return "Jun";
            case 10:
                return "Jul";
            case 11:
                return "Aug";
            case 0:
                return "Sep";
            case 1:
                return "Oct";
            case 2:
                return "Nov";
            case 3:
                return "Dec";
            default: return "";
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
    private MonthReport getAReportForRow(int row, ArrayList<MonthReport>reports){

        int month = row + 9;
        if (month>12){
            month=month-12;
        }

        for (MonthReport r:reports){
            if (r.month==month){
                return r;
            }
        }
        return null;
    }

    public void packAndSendUpdate(){

        reportsToClear.clear();
        reportsToUpdate.clear();

        MonthReport m;

        //Run through each row of the card.  If it is really empty, add it to a list of rows to be
        //cleared out (deleted from the DB).  If not, add it to a list of rows to update.

        for (int i=0; i<12; i++){
            if (isRowEmpty(i)){
                //Clear this row
                int adj_month = i+9;
                if (adj_month>12){adj_month = adj_month-12;}
                reportsToClear.add(adj_month);
                //We also need to clear the local copy out of the reports list...
                deleteLocalMonthReport(adj_month);

            }else{
                //Determine if we already have a reference to a report for this month.
                //If so, get/edit/push it.  If not, create one and push it.

                m = getReportForMonth(i);
                if (m!=null){
                    populateMonthReport(m, i);
                    ParseHelper.SaveParseObject(m);
                    reportsToUpdate.add(m);
                }else{
                    m = new MonthReport();
                    //Populate the data for m based on the values in the edit text fields for that row.
                    m = populateMonthReport(m, i);
                    ParseHelper.SaveParseObject(m);
                    reportsToUpdate.add(m);
                    reports.add(m);
                }
            }
        }

        //Send the update to the server.
        ParseHelper.SendCardUpdate(reportsToUpdate, reportsToClear, currentPublisher, currentYear);

    }

    private boolean isRowEmpty(int row){

        int emptyCount =0;

        TableRow rowToCheck = monthRows[row];

        if (rowToCheck==null){
            return true;
        }

        FrameLayout fl = (FrameLayout)(rowToCheck.getChildAt(1));
        EditText et = (EditText) fl.getChildAt(0);
        if ((et.getText().toString().equals("0")) || (et.getText().toString().equals(""))){
            emptyCount++;
        }

        fl = (FrameLayout)(rowToCheck.getChildAt(2));
        et = (EditText) fl.getChildAt(0);
        if ((et.getText().toString().equals("0")) || (et.getText().toString().equals(""))){
            emptyCount++;
        }

        fl = (FrameLayout)(rowToCheck.getChildAt(3));
        et = (EditText) fl.getChildAt(0);
        if ((et.getText().toString().equals("0")) || (et.getText().toString().equals("")) || (et.getText().toString().equals("0.0"))){
            emptyCount++;
        }

        fl = (FrameLayout)(rowToCheck.getChildAt(4));
        et = (EditText) fl.getChildAt(0);
        if ((et.getText().toString().equals("0")) || (et.getText().toString().equals(""))){
            emptyCount++;
        }

        fl = (FrameLayout)(rowToCheck.getChildAt(5));
        et = (EditText) fl.getChildAt(0);
        if ((et.getText().toString().equals("0")) || (et.getText().toString().equals(""))){
            emptyCount++;
        }

        fl = (FrameLayout)(rowToCheck.getChildAt(6));
        et = (EditText) fl.getChildAt(0);
        if ((et.getText().toString().equals("0")) || (et.getText().toString().equals(""))){
            emptyCount++;
        }

        fl = (FrameLayout)(rowToCheck.getChildAt(7));
        et = (EditText) fl.getChildAt(0);
        if (et.getText().toString().equals("")){
            emptyCount++;
        }

        if (emptyCount==7){
            return true;
        }else
        return false;
    }

    private MonthReport getReportForMonth(int month){

        int adj_month = month+9;
        if (adj_month>12){
            adj_month = adj_month-12;
        }

        for (MonthReport r:reports){
            if (r.month==adj_month){
                return r;
            }
        }
        return null;
    }

    private MonthReport populateMonthReport(MonthReport m, int month){

        TableRow row = monthRows[month];
        FrameLayout fl = (FrameLayout) row.getChildAt(1);
        EditText et = (EditText) fl.getChildAt(0);
        m.books = Integer.valueOf(et.getText().toString());

        fl = (FrameLayout) row.getChildAt(2);
        et = (EditText) fl.getChildAt(0);
        m.brochures = Integer.valueOf(et.getText().toString());

        fl = (FrameLayout) row.getChildAt(3);
        et = (EditText) fl.getChildAt(0);
        m.hours = Float.valueOf(et.getText().toString());

        fl = (FrameLayout) row.getChildAt(4);
        et = (EditText) fl.getChildAt(0);
        m.mags = Integer.valueOf(et.getText().toString());

        fl = (FrameLayout) row.getChildAt(5);
        et = (EditText) fl.getChildAt(0);
        m.RVs = Integer.valueOf(et.getText().toString());

        fl = (FrameLayout) row.getChildAt(6);
        et = (EditText) fl.getChildAt(0);
        m.BS = Integer.valueOf(et.getText().toString());

        fl = (FrameLayout) row.getChildAt(7);
        et = (EditText) fl.getChildAt(0);
        m.notes = et.getText().toString();

        m.publisherInfo = currentPublisher;
        m.year = currentYear;

        int adj_month = month +9;
        if (adj_month>12){
            adj_month = adj_month -12;
        }
        m.month = adj_month;

        return m;
    }

    private void deleteLocalMonthReport(int month){

        if (reports!=null){
            int count = reports.size()-1;
            for (int i = count; i>=0; i--){
                if (reports.get(i).month==month){
                    reports.remove(i);
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
            Toast.makeText(getContext(), "IME_ACTION_DONE", Toast.LENGTH_SHORT).show();

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

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        fillTotals();
        saveCount++;
        if(saveCount>=saveThreshold){
            packAndSendUpdate();
            saveCount=0;
        }
    }

}
