package com.hva.joris.gamebacklog;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "game")
public class GameObject implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "platform")
    private String platform;

    @ColumnInfo(name = "notes")
    private String notes;

    @ColumnInfo(name = "status")
    private int status;

    @ColumnInfo(name = "lastModified")
    private String lastModified;

    public GameObject(String title, String platform, String notes, int status, String lastModified) {
        this.title = title;
        this.platform = platform;
        this.notes = notes;
        this.status = status;
        this.lastModified = lastModified;
    }

    protected GameObject(Parcel in) {
        id = in.readLong();
        title = in.readString();
        platform = in.readString();
        notes = in.readString();
        status = in.readInt();
        lastModified = in.readString();
    }

    public static final Creator<GameObject> CREATOR = new Creator<GameObject>() {
        @Override
        public GameObject createFromParcel(Parcel in) {
            return new GameObject(in);
        }

        @Override
        public GameObject[] newArray(int size) {
            return new GameObject[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long mId) {
        this.id = mId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(platform);
        dest.writeString(notes);
        dest.writeInt(status);
        dest.writeString(lastModified);
    }
}
