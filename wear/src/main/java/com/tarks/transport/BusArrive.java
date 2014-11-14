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
import android.widget.TextView;

public class BusArrive extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.almost);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);


                CardScrollView cardScrollView =
                        (CardScrollView) findViewById(R.id.card_scroll_view);
                cardScrollView.setCardGravity(Gravity.BOTTOM);



                cardScrollView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(BusArrive.this, BusList.class);
                        startActivity(i);
                    }
                });


        TextView card_title = (TextView) findViewById(R.id.card_title);
        TextView card_subtitle = (TextView) findViewById(R.id.card_subtitle);


        card_title.setText("목적지 도착");
        card_subtitle.setText("천안역 -> 갈산역\n 43.725km");

        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(5000);


    }
}
