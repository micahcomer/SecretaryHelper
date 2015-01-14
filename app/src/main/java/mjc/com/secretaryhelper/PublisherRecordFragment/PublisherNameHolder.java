package mjc.com.secretaryhelper.PublisherRecordFragment;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import mjc.com.secretaryhelper.Parse.ParseObjects.PublisherInfo;
import mjc.com.secretaryhelper.PublisherGroupFragment.PublisherInfoLayout;
import mjc.com.secretaryhelper.R;

public class PublisherNameHolder extends RecyclerView.ViewHolder {

    private LinearLayout mLinearLayout;
    private TextView mTextView;
    private ImageView mImageView;
    private static PublisherNameListAdapter mAdapter;
    private static PublisherRecordFragment recordFragment;
    private Context mContext;
    private PublisherInfoLayout dialogView;
    AlertDialog pubInfoDialog;

    public PublisherNameHolder(View itemView) {
        super(itemView);
        mLinearLayout = (LinearLayout)itemView;
        mTextView = (TextView)mLinearLayout.findViewById(R.id.pubname_textview);
        mImageView = (ImageView)mLinearLayout.findViewById(R.id.editbutton_imageview);
    }

    public void setAdapter(PublisherNameListAdapter adapter){
        mAdapter = adapter;
    }
    public void setContext(Context context){
        mContext = context;
    }
    public void setRecordFragment (PublisherRecordFragment f){
        recordFragment =f;
    }
    public void bindPublisher(final PublisherInfo info, boolean selected){


        if (selected) {
            mTextView.setTypeface(mTextView.getTypeface(), Typeface.BOLD);
        } else {
            mTextView.setTypeface(mTextView.getTypeface(), Typeface.NORMAL);
        }

        mTextView.setText(info.toString());
        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.onClick(v);
            }
        });
        if (selected){
            mTextView.setTypeface(null, Typeface.BOLD);
        }else{
            mTextView.setTypeface(null, Typeface.NORMAL);
        }

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO - Launch action dialog with pub info.
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                dialogView = (PublisherInfoLayout) LayoutInflater.from(mContext).inflate(R.layout.fragment_publisher_info, null);
                dialogView.populate(info, recordFragment.getGroups());

                DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("dialogListener:onClick", "which: " + which);
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                dialogView.updateNewInfo();
                                info.saveSelfWithCallback(mAdapter);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                // do nothing
                                break;
                        }
                    }
                };

                builder.setView(dialogView);

                WindowManager wm = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE));
                Display d = wm.getDefaultDisplay();
                Point size =new Point();
                d.getSize(size);
                dialogView.setMinimumWidth((int)(size.x*.75));

                builder.setPositiveButton("OK", dialogListener);
                builder.setNegativeButton("Cancel", dialogListener);

                pubInfoDialog = builder.create();
                showPubInfoDialog();
            }
        });
    }

    private void showPubInfoDialog(){
        pubInfoDialog.show();
    }
}
