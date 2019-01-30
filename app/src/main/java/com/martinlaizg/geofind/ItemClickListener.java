package com.martinlaizg.geofind;

import android.view.View;

public interface ItemClickListener extends View.OnClickListener {
    void onClick(View view, int position, boolean isLongClick);
}
