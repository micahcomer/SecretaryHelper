package mjc.com.secretaryhelper.PublisherRecordFragment;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ViewFlipper;

import mjc.com.secretaryhelper.R;

/**
 * Created by Micah on 12/1/2014.
 */
public class CardFlipper extends ViewFlipper {

    private float startX;
    private float slop;
    private boolean isDragging;

    public CardFlipper(Context context) {
        super(context);
        slop = ViewConfiguration.get(context).getScaledTouchSlop();
    }
    public CardFlipper(Context context, AttributeSet attrs){
        super(context, attrs);
        slop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

@Override
public boolean onInterceptTouchEvent(MotionEvent event){
    switch (event.getAction()){
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:{
            isDragging = false;
            break;
        }
        case MotionEvent.ACTION_DOWN:{
            startX = event.getX();
            this.getParent().requestDisallowInterceptTouchEvent(true);
            Log.i("CardFlipper", "Down");
            break;
        }
        case MotionEvent.ACTION_MOVE: {
            float deltaX = Math.abs(event.getX() - startX);
            if (deltaX > slop) {
                isDragging = true;
                Log.i("CardFlipper", "Move");
                return true;
            }
        }
    }
    return false;

}

    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:{
                isDragging = false;
                break;
            }
            case MotionEvent.ACTION_DOWN:{
                if (isDragging) return true;
            }
            case MotionEvent.ACTION_MOVE:{

                float deltaX = Math.abs(event.getX() - startX);
                if (deltaX > slop) {
                    isDragging = true;
                }

                if (isDragging){
                    //left or right?
                    //left to right...
                    if (startX<event.getX()){
                        if (indexOfChild(getCurrentView())!=0){
                            setInAnimation(getContext(), R.anim.in_from_left);
                            setOutAnimation(getContext(), R.anim.out_to_right);
                            showPrevious();
                            isDragging = false;
                        }

                    }
                    //right to left...
                    else{
                        if (indexOfChild(getCurrentView())!=getChildCount()-1){
                            setInAnimation(getContext(), R.anim.in_from_right);
                            setOutAnimation(getContext(), R.anim.out_to_left);
                            showNext();
                            isDragging = false;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

}
