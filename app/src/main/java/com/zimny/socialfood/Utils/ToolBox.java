package com.zimny.socialfood.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ideo7 on 09.08.2017.
 */

public class ToolBox {
    public static void MyToast(String s,Context context ){
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }
}
