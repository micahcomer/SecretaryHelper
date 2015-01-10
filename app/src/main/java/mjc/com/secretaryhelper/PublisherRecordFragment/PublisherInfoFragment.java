package mjc.com.secretaryhelper.PublisherRecordFragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;

import java.util.ArrayList;

import mjc.com.secretaryhelper.Parse.ParseHelper;
import mjc.com.secretaryhelper.Parse.ParseObjects.PublisherInfo;
import mjc.com.secretaryhelper.Parse.ParseQueryListener;
import mjc.com.secretaryhelper.R;


/**
 * Created by Micah on 12/21/2014.
 */



public class PublisherInfoFragment extends Fragment implements ParseQueryListener {

    LinearLayout mainLayout;
    PublisherInfo mInfo;
    PublisherNameListAdapter mAdapter;
    boolean created = false;
    boolean infoReceived = false;

    public void setNameListAdapter(PublisherNameListAdapter adapter){
        mAdapter = adapter;
    }

    EditText firstNameText;
    EditText middleNameText;
    EditText lastNameText;
    EditText address1Text;
    EditText address2Text;
    EditText cityText;
    EditText stateText;
    EditText zipCodeText;
    FloatingHintEditText homePhoneText;
    FloatingHintEditText cellPhoneText;
    FloatingHintEditText workPhoneText;
    FloatingHintEditText emailText;
    RadioButton baptizedButton;
    RadioButton unbaptizedButton;
    Spinner groupSpinner;
    FloatingHintEditText dateOfBirthText;
    FloatingHintEditText dateOfBaptismText;
    CheckBox elderCheckBox;
    CheckBox ministerialServantCheckBox;
    CheckBox regularPioneerCheckBox;
    CheckBox regularAuxiliaryPioneerCheckBox;
    TextView saveButton;


    public PublisherInfoFragment(){
        super();
        Log.i("Pubinfofrag", "Creating new publisher info fragment.");
    }

    public void setInfo(PublisherInfo info){
        mInfo = info;
        if (mInfo!=null){
            mInfo.update();
            infoReceived = true;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mainLayout = (LinearLayout) inflater.inflate(R.layout.fragment_publisher_info, null);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
            params.weight = 3.0f;
            mainLayout.setLayoutParams(params);

            firstNameText = (EditText) mainLayout.findViewById(R.id.et_firstname);
            middleNameText = (EditText) mainLayout.findViewById(R.id.et_middlename);
            lastNameText = (EditText) mainLayout.findViewById(R.id.et_lastname);

            address1Text = (EditText) mainLayout.findViewById(R.id.et_address1);
            address2Text = (EditText) mainLayout.findViewById(R.id.et_address2);

            cityText = (EditText) mainLayout.findViewById(R.id.et_city);
            stateText = (EditText) mainLayout.findViewById(R.id.et_state);
            zipCodeText = (EditText) mainLayout.findViewById(R.id.et_zip);

            homePhoneText = (FloatingHintEditText) mainLayout.findViewById(R.id.et_homephone);
            cellPhoneText = (FloatingHintEditText) mainLayout.findViewById(R.id.et_cellphone);
            workPhoneText = (FloatingHintEditText) mainLayout.findViewById(R.id.et_workphone);
            emailText = (FloatingHintEditText) mainLayout.findViewById(R.id.et_email);

            baptizedButton = (RadioButton) mainLayout.findViewById(R.id.rb_baptized);
            unbaptizedButton = (RadioButton) mainLayout.findViewById(R.id.rb_unbaptized);

            groupSpinner = (Spinner) mainLayout.findViewById(R.id.spinner_fieldservicegroup);

            dateOfBirthText = (FloatingHintEditText) mainLayout.findViewById(R.id.et_dateofbirth);
            dateOfBaptismText = (FloatingHintEditText) mainLayout.findViewById(R.id.et_dateofbaptism);

            elderCheckBox = (CheckBox) mainLayout.findViewById(R.id.cb_elder);
            ministerialServantCheckBox = (CheckBox) mainLayout.findViewById(R.id.cb_minesterialservant);
            regularPioneerCheckBox = (CheckBox) mainLayout.findViewById(R.id.cb_regularpioneer);
            regularAuxiliaryPioneerCheckBox = (CheckBox) mainLayout.findViewById(R.id.cb_regularauxiliarypioneer);

        saveButton = (TextView) mainLayout.findViewById(R.id.button_textview);
        saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getRecordFromViews();
                    saveInfo();

                    mAdapter.selectedRecord=mInfo;
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "Saving Publisher Information", Toast.LENGTH_SHORT).show();

                }
            });
            created = true;
        return mainLayout;
    }

    @Override
    public void onResume(){
        super.onResume();
        populateView(mInfo);
    }

    public void populateView(PublisherInfo info){

        mInfo = info;

        if ((info!=null)&&(created)){
            infoReceived = true;
            firstNameText.setText(info.firstName);
            middleNameText.setText(info.middleName);
            lastNameText.setText(info.lastName);
            address1Text.setText(info.address1);
            address2Text.setText(info.address2);
            cityText.setText(info.city);
            stateText.setText(info.state);
            zipCodeText.setText(String.valueOf(info.zipCode));
            homePhoneText.setText(info.homePhone);
            cellPhoneText.setText(info.cellPhone);
            workPhoneText.setText(info.workPhone);
            emailText.setText(info.emailAddress);
            baptizedButton.setChecked(info.baptized);
            unbaptizedButton.setChecked(!info.baptized);
            //TODO - Spinner stuff
            dateOfBirthText.setText(info.dateOfBirth);
            dateOfBaptismText.setText(info.dateOfBaptism);
            elderCheckBox.setChecked(info.elder);
            ministerialServantCheckBox.setChecked(info.minesterialServant);
            regularPioneerCheckBox.setChecked(info.regularPioneer);
            regularAuxiliaryPioneerCheckBox.setChecked(info.regularAuxiliaryPioneer);

        }
    }

    private PublisherInfo getRecordFromViews(){
        boolean addName=false;
        if (mInfo==null){
            mInfo = new PublisherInfo();
            addName = true;
        }
        mInfo.firstName = firstNameText.getText().toString();
        mInfo.middleName = middleNameText.getText().toString();
        mInfo.lastName = lastNameText.getText().toString();

        mInfo.address1 = address1Text.getText().toString();
        mInfo.address2 = address2Text.getText().toString();
        mInfo.city = cityText.getText().toString();
        mInfo.state = stateText.getText().toString();
        if (!TextUtils.isEmpty(zipCodeText.getText().toString())){
            mInfo.zipCode = Integer.valueOf(zipCodeText.getText().toString());
        }

        mInfo.homePhone = homePhoneText.getText().toString();
        mInfo.cellPhone = cellPhoneText.getText().toString();
        mInfo.workPhone = cellPhoneText.getText().toString();
        mInfo.emailAddress = emailText.getText().toString();
        mInfo.baptized = baptizedButton.isChecked();
        //TODO - Spinner stuff
        mInfo.dateOfBirth = dateOfBirthText.getText().toString();
        mInfo.dateOfBaptism = dateOfBaptismText.getText().toString();
        mInfo.elder = elderCheckBox.isChecked();
        mInfo.minesterialServant = ministerialServantCheckBox.isChecked();
        mInfo.regularPioneer = regularPioneerCheckBox.isChecked();
        mInfo.regularAuxiliaryPioneer = regularAuxiliaryPioneerCheckBox.isChecked();
        return mInfo;
    }

    public void clearToAddPublisher(PublisherInfo info){

        mInfo = info;
        firstNameText.setText("");
        middleNameText.setText("");
        lastNameText.setText("");
        address1Text.setText("");
        address2Text.setText("");
        cityText.setText("");
        stateText.setText("");
        zipCodeText.setText("");
        homePhoneText.setText("");
        cellPhoneText.setText("");
        workPhoneText.setText("");
        emailText.setText("");
        baptizedButton.setChecked(true);
        unbaptizedButton.setChecked(false);
        //groupSpinner;
        dateOfBirthText.setText("");
        dateOfBaptismText.setText("");
        elderCheckBox.setChecked(false);
        ministerialServantCheckBox.setChecked(false);
        regularPioneerCheckBox.setChecked(false);
        regularAuxiliaryPioneerCheckBox.setChecked(false);
    }

    public void saveInfo(){
        ParseHelper.SaveParseObjectWithSaveCallback(mInfo, mAdapter);

    }

    @Override
    public void onQueryCompleted(ArrayList<? extends ParseObject> objects, Class<? extends ParseObject> c) {
        if (c == PublisherInfo.class){
            if ((objects!=null)&&(objects.size()>0)){
                mInfo = (PublisherInfo)objects.get(0);
                mInfo.update();
                populateView(mInfo);
            }
        }
    }
}
