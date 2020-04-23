package net.skhu.firechat2.Room.MemberLocation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.skhu.firechat2.Item.RoomMemberItem;
import net.skhu.firechat2.Item.RoomMemberItemList;
import net.skhu.firechat2.Item.RoomMemberLocationItem;
import net.skhu.firechat2.Item.RoomMemberLocationItemList;
import net.skhu.firechat2.R;

public class RoomMemberLocationRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        TextView textViewRoomMemberLocation;
        //CheckBox checkBox;


        public ViewHolder(View view) {
            super(view);
            this.textViewRoomMemberLocation = view.findViewById(R.id.textViewRoomMemberLocation);
            view.setOnClickListener(this);
        }

        //RecyclerView에 보이는 내용을 설정하는 함수입니다.
        public void setData() {
            RoomMemberLocationItem roomMemberLocationItem = roomMemberLocationItemList.get(super.getAdapterPosition());
            //this.textViewRoomMember.setText(roomMemberItem.getUserName()+" : "+"현재위치 \n위도 " + roomMemberItem.getLatitude() + "\n경도 " + roomMemberItem.getLongitude());
            this.textViewRoomMemberLocation.setText(roomMemberLocationItem.getUserName());
        }

        @Override
        public void onClick(View view) {
            RoomMemberLocationListActivity activity = (RoomMemberLocationListActivity)view.getContext();
            activity.firebaseDbServiceForRoomMemberLocationList.updateInServer(super.getAdapterPosition());

            RoomMemberLocationItem roomMemberLocationItem = activity.roomMemberLocationItemList.get(super.getAdapterPosition());

            Toast.makeText(activity.getApplicationContext(), "현재위치 \n위도 " + roomMemberLocationItem.getLatitude() + "\n경도 " + roomMemberLocationItem.getLongitude(), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.apps.maps");
            String data = "geo:"+roomMemberLocationItem.getLatitude()+", "+roomMemberLocationItem.getLongitude();
            intent.setData(Uri.parse(data));
            activity.startActivity(intent);
        }
    }

    LayoutInflater layoutInflater;
    RoomMemberLocationItemList roomMemberLocationItemList;
    Context context;

    final int ROOM_MEMBER_LOCATION=0;

    public RoomMemberLocationRecyclerViewAdapter(Context context, RoomMemberLocationItemList roomMemberLocationItemList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.roomMemberLocationItemList = roomMemberLocationItemList;
    }

    @Override
    public int getItemCount() {
        return roomMemberLocationItemList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType==ROOM_MEMBER_LOCATION) {
            View view = layoutInflater.inflate(R.layout.item_room_member_location_list, viewGroup, false);
            return new RoomMemberLocationRecyclerViewAdapter.ViewHolder(view);
        }

        View view = layoutInflater.inflate(R.layout.item_room_member_location_list, viewGroup, false);
        return new RoomMemberLocationRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof RoomMemberLocationRecyclerViewAdapter.ViewHolder) {
            ((RoomMemberLocationRecyclerViewAdapter.ViewHolder)viewHolder).setData();
        }
        else{
            ((RoomMemberLocationRecyclerViewAdapter.ViewHolder)viewHolder).setData();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return ROOM_MEMBER_LOCATION;
    }
}
