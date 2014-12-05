package com.tarks.transport;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.wearable.view.CardScrollView;
import android.support.wearable.view.WatchViewStub;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.tarks.transport.core.global;

import java.util.Map;

public class BusArrive extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.almost);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);

        Intent intent = getIntent(); // 인텐트 받아오고
        String title = intent.getStringExtra("title"); // 인텐트로 부터 데이터 가져오고
        String content = intent.getStringExtra("content"); // 인텐트로 부터 데이터 가져오고






        CardScrollView cardScrollView =
                        (CardScrollView) findViewById(R.id.card_scroll_view);
                cardScrollView.setCardGravity(Gravity.BOTTOM);



                cardScrollView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       //finish();

                    }
                });


        TextView card_title = (TextView) findViewById(R.id.card_title);
        TextView card_subtitle = (TextView) findViewById(R.id.card_subtitle);


        card_title.setText(title);
        card_subtitle.setText(content);




    }

    @Override
    public void onPause() {
        super.onPause();

    }
}
