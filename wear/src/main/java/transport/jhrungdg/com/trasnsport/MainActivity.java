package transport.jhrungdg.com.trasnsport;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.CardScrollView;
import android.support.wearable.view.WatchViewStub;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                CardFragment cardFragment = CardFragment.create("WhereBus", "asdf", R.drawable.ic_launcher);
//                fragmentTransaction.add(R.id.frame_layout, cardFragment);
//                fragmentTransaction.commit();


              //  cardFragment.getView().setClickable(true);

                CardScrollView cardScrollView =
                        (CardScrollView) findViewById(R.id.card_scroll_view);
                cardScrollView.setCardGravity(Gravity.BOTTOM);

                cardScrollView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MainActivity.this, BusList.class);
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
        });


    }
}
