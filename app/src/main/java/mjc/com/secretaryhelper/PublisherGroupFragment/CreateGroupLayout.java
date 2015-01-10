package mjc.com.secretaryhelper.PublisherGroupFragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import mjc.com.secretaryhelper.Parse.ParseObjects.PublisherInfo;
import mjc.com.secretaryhelper.R;

public class CreateGroupLayout extends RelativeLayout {

    String groupName;
    ArrayList<PublisherInfo> servants;
    Spinner overseerSpinner;

    public Spinner getAssistantSpinner() {
        return assistantSpinner;
    }

    public Spinner getOverseerSpinner() {
        return overseerSpinner;
    }

    Spinner assistantSpinner;

    public CreateGroupLayout(Context context) {
        super(context);
        init();
    }

    public CreateGroupLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CreateGroupLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CreateGroupLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        overseerSpinner = (Spinner) findViewById(R.id.spinner);
        assistantSpinner = (Spinner) findViewById(R.id.spinner2);

        //overseerSpinner.setAdapter(new GroupBrotherAssignmentAdapter(getContext()));
        //assistantSpinner.setAdapter(new GroupBrotherAssignmentAdapter(getContext()));

        //overseerSpinner.getAdapter();
        //servants = new ArrayList<>();

        TextView nameText = (TextView) findViewById(R.id.editText);
        groupName = nameText.getText().toString();
    }
}
