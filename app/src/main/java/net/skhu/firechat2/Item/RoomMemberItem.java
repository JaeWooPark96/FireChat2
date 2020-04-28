package net.skhu.firechat2.Item;

import android.location.Location;

import java.io.Serializable;

public class RoomMemberItem implements Serializable {
    String userName;
    String userEmail;
    double latitude;
    double longitude;

    public RoomMemberItem(){
        userName = "";
        userEmail = "";
        latitude = 0;
        longitude = 0;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }


}
