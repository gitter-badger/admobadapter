package com.clockbyte.admobadapter.sampleapp.express;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.clockbyte.admobadapter.expressads.AdmobExpressAdapterWrapper;
import com.clockbyte.admobadapter.sampleapp.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity_ListView_Express extends Activity {

    ListView lvMessages;
    AdmobExpressAdapterWrapper adapterWrapper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_listview);

        //highly-recommended in Firebase docs to initialize things early as possible
        //test_admob_app_id is different with unit_id! you could get it in your Admob console
        MobileAds.initialize(getApplicationContext(), getString(R.string.test_admob_app_id));

        initListViewItems();
    }

    /**
     * Inits an adapter with items, wrapping your adapter with a {@link AdmobExpressAdapterWrapper} and setting the listview to this wrapper
     * FIRST OF ALL Please notice that the following code will work on a real devices but emulator!
     */
    private void initListViewItems() {
        lvMessages = (ListView) findViewById(R.id.lvMessages);

        //creating your adapter, it could be a custom adapter as well
        ArrayAdapter<String> adapter  = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);

        //your test devices' ids
        String[] testDevicesIds = new String[]{getString(R.string.testDeviceID),AdRequest.DEVICE_ID_EMULATOR};
        //when you'll be ready for release please use another ctor with admobReleaseUnitId instead.
        adapterWrapper = new AdmobExpressAdapterWrapper(this, testDevicesIds);
        //By default the ad size is set to FULL_WIDTHx150
        //To set a custom size you should use an appropriate ctor
        //adapterWrapper = new AdmobExpressAdapterWrapper(this, testDevicesIds, new AdSize(AdSize.FULL_WIDTH, 150));

        adapterWrapper.setAdapter(adapter); //wrapping your adapter with a AdmobExpressAdapterWrapper.

        //Sets the max count of ad blocks per dataset, by default it equals to 3 (according to the Admob's policies and rules)
        adapterWrapper.setLimitOfAds(3);

        //Sets the number of your data items between ad blocks, by default it equals to 10.
        //You should set it according to the Admob's policies and rules which says not to
        //display more than one ad block at the visible part of the screen,
        // so you should choose this parameter carefully and according to your item's height and screen resolution of a target devices
        adapterWrapper.setNoOfDataBetweenAds(10);

        adapterWrapper.setFirstAdIndex(2);

        lvMessages.setAdapter(adapterWrapper); // setting an AdmobAdapterWrapper to a ListView

        //preparing the collection of data
        final String sItem = "item #";
        ArrayList<String> lst = new ArrayList<String>(100);
        for(int i=1;i<=100;i++)
            lst.add(sItem.concat(Integer.toString(i)));

        //adding a collection of data to your adapter and rising the data set changed event
        adapter.addAll(lst);
        adapter.notifyDataSetChanged();
    }

    /*
    * Seems to be a good practice to destroy all the resources you have used earlier :)
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapterWrapper.destroyAds();
    }
}
