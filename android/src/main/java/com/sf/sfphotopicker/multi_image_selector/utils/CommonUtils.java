package com.sf.sfphotopicker.multi_image_selector.utils;

import com.sf.sfphotopicker.multi_image_selector.data.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-03-03.
 */

public class CommonUtils {
    private static List<Image> select_images;
    private static List<String> select_images_id;
    public static void init_select(){
        if (select_images!=null){
            select_images.clear();
            select_images_id.clear();
        }
        select_images = new ArrayList<>();
        select_images_id = new ArrayList<>();
    }
    public static void clear_selcet(){
        select_images.clear();
        select_images_id = new ArrayList<>();
    }
    public static void remove_select(Image image){
        select_images.remove(image);
        select_images_id.remove(""+image.getId());
    }
    public static void add_select(Image image){
        select_images.add(image);
        select_images_id.add(""+image.getId());
    }
    public static int get_select_count(){
        return select_images.size();
    }
    public static boolean is_in_select(Image image){
        return select_images_id.contains(""+image.getId());
    }
    public static int get_item_position(Image image){
        return select_images_id.indexOf(""+image.getId())+1;
    }
    public static List<Image> getSelect_images(){
        return select_images;
    }
}
