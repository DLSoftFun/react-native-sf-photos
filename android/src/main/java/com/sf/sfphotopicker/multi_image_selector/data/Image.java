package com.sf.sfphotopicker.multi_image_selector.data;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-03-03.
 */

public class Image implements Serializable{
    int Id;
    String Path;
    String Name;
    boolean isVideo;

    public Image() {
        isVideo = false;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }
}
