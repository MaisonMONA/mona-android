/*package com.example.dajc.tabs;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "Picture")
public class PictureObject {
        @PrimaryKey
        @NonNull
        private String Id;

        @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
        private byte[] Image;

    public byte[] getImage(){return Image;}
    public String getId(){return Id;}
    public void setId(String id){Id=id;}
    public void setImage(byte[] image){Image = image;}

    public PictureObject(String id, byte[] image){
        Id=id;
        Image=image;
    }
    public PictureObject(){}
}
*/