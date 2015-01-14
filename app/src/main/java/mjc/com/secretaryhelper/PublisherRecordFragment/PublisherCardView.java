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
public class PublisherCardView extends CardView{

    public PublisherCardHolder holder;

    public PublisherCardView(Context context) {
        super(context);

    }
    public PublisherCardView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

}
