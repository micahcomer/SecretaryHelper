package mjc.com.secretaryhelper.PublisherGroupFragment;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.ArrayList;

import mjc.com.secretaryhelper.Parse.ParseObjects.PublisherGroup;
import mjc.com.secretaryhelper.Parse.ParseObjects.PublisherInfo;
import mjc.com.secretaryhelper.R;

public class PublisherInfoLayout extends LinearLayout{

    private Context mContext;
    private PublisherInfo mInfo;
    private ArrayAdapter<PublisherGroup> spinnerAdapter;

    private EditText firstNameET;
    private EditText middleNameET;
    private EditText lastNameET;
    private EditText address1ET;
    private EditText address2ET;
    private EditText cityET;
    private EditText stateET;
    private EditText zipET;
    private EditText homePhoneET;
    private EditText workPhoneET;
    private EditText cellPhoneET;
    private EditText emailET;
    private RadioButton baptizedRB;
    private RadioButton unbaptizedRB;
    private Spinner serviceGroupSP;
    private EditText dateOfBirthET;
    private EditText dateOfBaptismET;
    private CheckBox elderCB;
    private CheckBox ministerialServantCB;
    private CheckBox regularPioneerCB;
    private CheckBox regularAuxiliaryCB;




    public PublisherInfoLayout(Context context) {
        super(context);
        mContext = context;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size =new Point();
        display.getSize(size);
        int w = (int)(size.x *.75);
        setMinimumWidth(w);

    }

    public PublisherInfoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PublisherInfoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void populate(PublisherInfo info, ArrayList<PublisherGroup> groups){
        mInfo = info;
        assignFields();
        fillFields();
        spinnerAdapter = new ArrayAdapter<PublisherGroup>(getContext(), R.layout.groupspinnerentry, groups);
        serviceGroupSP.setAdapter(spinnerAdapter);
    }

    public void updateNewInfo(){
        mInfo.firstName = firstNameET.getText().toString();
        mInfo.middleName = middleNameET.getText().toString();
        mInfo.lastName = lastNameET.getText().toString();

        mInfo.address1 = address1ET.getText().toString();
        mInfo.address1 = address2ET.getText().toString();
        mInfo.city = cityET.getText().toString();
        mInfo.state = stateET.getText().toString();
        mInfo.zipCode = Integer.valueOf(zipET.getText().toString());

        mInfo.homePhone = homePhoneET.getText().toString();
        mInfo.workPhone = workPhoneET.getText().toString();
        mInfo.cellPhone = cellPhoneET.getText().toString();
        mInfo.emailAddress = emailET.getText().toString();

        mInfo.dateOfBirth = dateOfBirthET.getText().toString();
        mInfo.dateOfBaptism = dateOfBaptismET.getText().toString();

        mInfo.baptized = baptizedRB.isChecked();
        mInfo.elder = elderCB.isChecked();
        mInfo.minesterialServant = ministerialServantCB.isChecked();
        mInfo.regularPioneer = regularPioneerCB.isChecked();
        mInfo.regularAuxiliaryPioneer = regularAuxiliaryCB.isChecked();
    }
    private void assignFields(){
        firstNameET = (EditText)findViewById(R.id.et_firstname);
        middleNameET = (EditText)findViewById(R.id.et_middlename);
        lastNameET = (EditText)findViewById(R.id.et_lastname);

        address1ET = (EditText)findViewById(R.id.et_address1);
        address2ET = (EditText)findViewById(R.id.et_address2);
        cityET = (EditText)findViewById(R.id.et_city);
        stateET = (EditText)findViewById(R.id.et_state);
        zipET = (EditText)findViewById(R.id.et_zip);

        homePhoneET = (EditText)findViewById(R.id.et_homephone);
        workPhoneET = (EditText)findViewById(R.id.et_workphone);
        cellPhoneET = (EditText)findViewById(R.id.et_cellphone);
        emailET = (EditText)findViewById(R.id.et_email);

        baptizedRB = (RadioButton)findViewById(R.id.rb_baptized);
        unbaptizedRB = (RadioButton)findViewById(R.id.rb_unbaptized);

        serviceGroupSP = (Spinner)findViewById(R.id.spinner_fieldservicegroup);

        dateOfBirthET = (EditText)findViewById(R.id.et_dateofbirth);
        dateOfBaptismET = (EditText)findViewById(R.id.et_dateofbaptism);

        elderCB = (CheckBox)findViewById(R.id.cb_elder);
        ministerialServantCB = (CheckBox)findViewById(R.id.cb_minesterialservant);
        regularPioneerCB = (CheckBox)findViewById(R.id.cb_regularpioneer);
        regularAuxiliaryCB = (CheckBox)findViewById(R.id.cb_regularauxiliarypioneer);

    }
    private void fillFields() {

        firstNameET.setText(mInfo.firstName);
        middleNameET.setText(mInfo.middleName);
        lastNameET.setText(mInfo.lastName);

        address1ET.setText(mInfo.address1);
        address2ET.setText(mInfo.address2);
        cityET.setText(mInfo.city);
        stateET.setText(mInfo.state);
        zipET.setText(String.valueOf(mInfo.zipCode));

        homePhoneET.setText(mInfo.homePhone);
        workPhoneET.setText(mInfo.workPhone);
        cellPhoneET.setText(mInfo.cellPhone);
        emailET.setText(mInfo.emailAddress);

        baptizedRB.setChecked(mInfo.baptized);
        unbaptizedRB.setChecked((!mInfo.baptized));

        //serviceGroupSP
        dateOfBirthET.setText(mInfo.dateOfBirth);
        dateOfBaptismET.setText(mInfo.dateOfBaptism);

        elderCB.setChecked(mInfo.elder);
        ministerialServantCB.setChecked(mInfo.minesterialServant);
        regularPioneerCB.setChecked(mInfo.regularPioneer);
        regularAuxiliaryCB.setChecked(mInfo.regularAuxiliaryPioneer);
    }


}
