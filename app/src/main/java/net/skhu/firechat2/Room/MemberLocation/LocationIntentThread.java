package net.skhu.firechat2.Room.MemberLocation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import net.skhu.firechat2.Item.RoomMemberLocationItem;
import net.skhu.firechat2.ListenerInterface.RoomLocationListener.OnMapIntentPrepareListener;

public class LocationIntentThread implements Runnable {
    //sleep을 사용하기 위해서 Thread를 사용해주었습니다.
    //View view;
    Context context;
    int selectIndex;
    OnMapIntentPrepareListener onMapIntentPrepareListener;

    public LocationIntentThread(Context context, int selectIndex, OnMapIntentPrepareListener onMapIntentPrepareListener) {
        //this.view = view;
        this.context = context;
        this.selectIndex = selectIndex;
        this.onMapIntentPrepareListener = onMapIntentPrepareListener;
    }

    public void run() {
        RoomMemberLocationListActivity activity = (RoomMemberLocationListActivity)context;

        //업데이트까지 지연시간.
        try {
            Thread.sleep(500); //0.5초 대기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onMapIntentPrepareListener.onMapIntentPrepareListener();

        RoomMemberLocationItem roomMemberLocationItem = activity.roomMemberLocationRecyclerViewAdapter.get(selectIndex);//업데이트 받은 것 저장

        Log.v("pjw", "현재위치 \n위도 " + roomMemberLocationItem.getLatitude() + "\n경도 " + roomMemberLocationItem.getLongitude());

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setPackage("com.google.android.apps.maps");
        //String data = "geo:"+roomMemberLocationItem.getLatitude()+", "+roomMemberLocationItem.getLongitude();
        String data = LocationFunc.locationDataStr(roomMemberLocationItem.getLatitude(), roomMemberLocationItem.getLongitude());
        intent.setData(Uri.parse(data));
        activity.startActivity(intent);
    }
}
