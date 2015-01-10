package mjc.com.secretaryhelper.Parse;

import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by Micah on 11/29/2014.
 */
public interface ParseQueryListener {

public void onQueryCompleted(ArrayList<? extends ParseObject> objects, Class<? extends ParseObject> c);

}
