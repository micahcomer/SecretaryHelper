package mjc.com.secretaryhelper;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import mjc.com.secretaryhelper.PublisherRecordFragment.CardFlipper;

/**
 * Created by Micah on 12/20/2014.
 */
public class SecretaryPager extends ViewPager {

    private RectF flipperBounds;
    LinearLayout ll;
    CardFlipper flipper;

    public SecretaryPager(Context context) {
        super(context);
    }

    public SecretaryPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void setFlipperBounds(){
        ll = (LinearLayout) getChildAt(0);
        if (ll!=null){
            flipper = (CardFlipper) ll.getChildAt(2);
        }

        if (flipper!=null){
            Rect bounds = new Rect();
            flipper.getHitRect(bounds);
            flipperBounds = new RectF(bounds);
        }
    }


    /*
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event){


        if (event.getAction()==MotionEvent.ACTION_DOWN){
            if (flipperBounds==null){
                setFlipperBounds();
            }

            if (flipper!=null){
                if (flipperBounds.contains(event.getX(), event.getY())){
                    return false;
                }
            }
        }
        return super.onInterceptTouchEvent(event);
    }
    */
}
