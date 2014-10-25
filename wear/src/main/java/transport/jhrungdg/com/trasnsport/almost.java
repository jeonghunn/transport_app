package transport.jhrungdg.com.trasnsport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.CardScrollView;
import android.support.wearable.view.WatchViewStub;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class almost extends Activity {

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
                        Intent i = new Intent(almost.this, BusList.class);
                        startActivity(i);
                    }
                });

//                cardFragment.getView().setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////
//                    }
//                });





    }
}
