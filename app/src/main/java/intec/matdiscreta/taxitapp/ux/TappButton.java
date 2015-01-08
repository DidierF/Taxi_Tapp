package intec.matdiscreta.taxitapp.ux;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import intec.matdiscreta.taxitapp.R;

/**
 * Created by Louis on 12/23/2014.
 */
public class TappButton extends Button {
    public TappButton(Context context, AttributeSet attrs) {
       super(context, attrs, R.drawable.tapp_btn);
    }

    public TappButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
