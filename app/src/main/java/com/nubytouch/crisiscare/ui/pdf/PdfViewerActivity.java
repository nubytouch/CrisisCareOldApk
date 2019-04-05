package com.nubytouch.crisiscare.ui.pdf;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.artifex.mupdfdemo.MuPDFActivity;
import com.nubytouch.crisiscare.R;
import com.nubytouch.crisiscare.core.Session;

public class PdfViewerActivity extends MuPDFActivity {

    private static final String TAG = "PdfViewerActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mOutlineActivityClass = PdfViewerOutlineActivity.class;
        mPrintDialogActivityClass = PdfViewerPrintDialogActivity.class;

//        mButtonsView.setBackgroundColor(Session.getPrimaryColor());
        if (mButtonsView != null)
        {
            RelativeLayout topBar0Main   = (RelativeLayout) mButtonsView.findViewById(R.id.topBar0Main);
            RelativeLayout topBar5Accept = (RelativeLayout) mButtonsView.findViewById(R.id.topBar5Accept);
            RelativeLayout topBar4More   = (RelativeLayout) mButtonsView.findViewById(R.id.topBar4More);
            RelativeLayout topBar3Delete = (RelativeLayout) mButtonsView.findViewById(R.id.topBar3Delete);
            RelativeLayout topBar2Annot  = (RelativeLayout) mButtonsView.findViewById(R.id.topBar2Annot);
            RelativeLayout topBar1Search = (RelativeLayout) mButtonsView.findViewById(R.id.topBar1Search);
            SeekBar        pageSlider    = (SeekBar) mButtonsView.findViewById(R.id.pageSlider);
            TextView       pageNumber    = (TextView) mButtonsView.findViewById(R.id.pageNumber);
            TextView       info          = (TextView) mButtonsView.findViewById(R.id.info);

            topBar0Main.setBackgroundColor(Session.getPrimaryColor());
            topBar5Accept.setBackgroundColor(Session.getPrimaryColor());
            topBar4More.setBackgroundColor(Session.getPrimaryColor());
            topBar3Delete.setBackgroundColor(Session.getPrimaryColor());
            topBar2Annot.setBackgroundColor(Session.getPrimaryColor());
            topBar1Search.setBackgroundColor(Session.getPrimaryColor());
            pageSlider.setBackgroundColor(Session.getPrimaryColor());
            pageNumber.setBackgroundColor(Session.getPrimaryColor());
            info.setBackgroundColor(Session.getPrimaryColor());
        }
    }
}
