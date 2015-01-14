package mjc.com.secretaryhelper;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.LinearLayout;


/**
 * Created by Micah on 12/20/2014.
 */
public class SecretaryPager extends ViewPager {

    private RectF flipperBounds;
    LinearLayout ll;

    public SecretaryPager(Context context) {
        super(context);
    }

    public SecretaryPager(Context context, AttributeSet attrs) {
        super(context, attrs);
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
