package com.sf.sfphotopicker.multi_image_selector.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.sf.sfphotopicker.multi_image_selector.data.Folder;
import com.sf.sfphotopicker.multi_image_selector.data.Image;
import com.sf.sfphotopicker.multi_image_selector.inter.CursorCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-03-02.
 */

public class MediaUtils {
    public static Bitmap getVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
        }
        catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (RuntimeException e) {
            e.printStackTrace();
        }
        finally {
            try {
                retriever.release();
            }
            catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
    public static void getImageAndVideoData(Context context, CursorCallback callback,int type) {
        new ImageAndVideoTase().execute(context, callback,type);
    }
    static class ImageAndVideoTase extends AsyncTask<Object, Object, List<Folder>> {
        Context context;
        CursorCallback callback;
        ArrayList<Folder> mResultFolder = new ArrayList<>();
        int type;
        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media._ID};

        @Override
        protected List<Folder> doInBackground(Object... params) {
            context = (Context) params[0];
            callback = (CursorCallback) params[1];
            type = (int)params[2];
            mResultFolder.clear();
            if(type == 2){
                getAllImages();
                getAllVideo();
                getOther();
            }else if(type == 0){
                getAllImages();
                getOther();
            }else if(type == 1){
                getAllVideo();
            }
            return mResultFolder;
        }

        @Override
        protected void onPostExecute(List<Folder> list) {
            super.onPostExecute(list);
            if (list != null) {
                callback.onFinishCursor(list);
            }

        }

        public void getAllImages() {
            List<Image> imageList = new ArrayList<>();
            ContentResolver contentResolver = context.getContentResolver();
            if (context != null) {
                Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, IMAGE_PROJECTION[4] + ">0 AND " + IMAGE_PROJECTION[3] + "=? OR " + IMAGE_PROJECTION[3] + "=? OR " + IMAGE_PROJECTION[3]+"=? OR " + IMAGE_PROJECTION[3]+"=? ",
                        new String[]{"image/jpeg", "image/png","image/gif","image/mp4"}, IMAGE_PROJECTION[2] + " DESC");
                if (cursor != null) {
                    Folder folder = new Folder();
                    folder.name = "所有图片";
                    folder.path = "所有图片";
                    while (cursor.moveToNext()) {
                        Image image = new Image();
                        image.setId(cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID)));
                        image.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                        image.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)));
                        File folderFile = new File(image.getPath()).getParentFile();
                        if (folderFile != null && folderFile.exists()) {
                            folder.cover = image;
                            imageList.add(image);
                        }
                    }
                    folder.isVideo = false;
                    folder.medias = imageList;
                    mResultFolder.add(folder);
                }
            }
        }

        public void getAllVideo() {
            List<Image> videoList = new ArrayList<>();
            ContentResolver contentResolver = context.getContentResolver();
            if (context != null) {
                Cursor cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Video.Media.DATE_ADDED+" DESC");
                if (cursor != null) {
                    Folder folder = new Folder();
                    folder.name = "所有视频";
                    folder.path = "所有视频";
                    while (cursor.moveToNext()) {
                        Image video = new Image();
                        video.setId(cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID)));
                        video.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)));
                        video.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)));
                        video.setVideo(true);
//                        Bitmap thumbnail = MediaStore.Video.Thumbnails.getThumbnail(contentResolver, video.getId(), MediaStore.Video.Thumbnails.MINI_KIND, null);
                        File folderFile = new File(video.getPath()).getParentFile();
                        if (folderFile != null && folderFile.exists()) {
                            folder.cover = video;
                            videoList.add(video);
                        }
                    }
                    folder.isVideo = true;
                    folder.medias = videoList;
                    mResultFolder.add(folder);
                }
            }
        }
        public void getOther(){
            ContentResolver contentResolver = context.getContentResolver();
            if (context != null) {
                Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, IMAGE_PROJECTION[4] + ">0 AND " + IMAGE_PROJECTION[3] + "=? OR " + IMAGE_PROJECTION[3] + "=? OR " + IMAGE_PROJECTION[3]+"=? ",
                        new String[]{"image/jpeg", "image/png","image/gif"}, IMAGE_PROJECTION[2] + " DESC");
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        Image image = new Image();
                        image.setId(cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID)));
                        image.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                        image.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)));
                        File folderFile = new File(image.getPath()).getParentFile();
                        if (folderFile != null && folderFile.exists()) {
                            String fp = folderFile.getAbsolutePath();
                            Folder f = getFolderByPath(fp);
                            if (f == null) {
                                Folder folder = new Folder();
                                folder.isVideo = false;
                                folder.name = folderFile.getName();
                                folder.path = fp;
                                folder.cover = image;
                                List<Image> imageList = new ArrayList<>();
                                imageList.add(image);
                                folder.medias = imageList;
                                mResultFolder.add(folder);
                            } else {
                               f.medias.add(image);
                            }
                        }
                    }
                }
            }
        }
        private Folder getFolderByPath(String path) {
            if (mResultFolder != null) {
                for (Folder folder : mResultFolder) {
                    if (TextUtils.equals(folder.path, path)) {
                        return folder;
                    }
                }
            }
            return null;
        }
    }

//    public static void getMediaListData(Context context, CursorCallback callback) {
//        new MediaTask().execute(context, callback);
//    }
//
//    static class MediaTask extends AsyncTask<Object, Object, List<Folder>> {
//        Context context;
//        CursorCallback callback;
//        ArrayList<Folder> mResultFolder = new ArrayList<>();
//        private final String[] IMAGE_PROJECTION = {
//                MediaStore.Images.Media.DATA,
//                MediaStore.Images.Media.DISPLAY_NAME,
//                MediaStore.Images.Media.DATE_ADDED,
//                MediaStore.Images.Media.MIME_TYPE,
//                MediaStore.Images.Media.SIZE,
//                MediaStore.Images.Media._ID};
//
//        @Override
//        protected List<Folder> doInBackground(Object... params) {
//            context = (Context) params[0];
//            callback = (CursorCallback) params[1];
//            mResultFolder.clear();
////            List<Image> list = new ArrayList<>();
//
//                LogUtils.e("" + mResultFolder.size());
//                return mResultFolder;
//            } else {
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(List<Folder> list) {
//            super.onPostExecute(list);
//            if (list != null) {
//                callback.onFinishCursor(list, 0);
//            }
//
//        }
//
//
//    }
//
//    public static void getVideoListData(Context context, CursorCallback callback) {
//        new VideoCursorTask().execute(context, callback);
//    }
//
//    static class VideoCursorTask extends AsyncTask<Object, Object, List<Folder>> {
//        Context context;
//        CursorCallback callback;
//        ArrayList<Folder> mResultFolder = new ArrayList<>();
//
//        @Override
//        protected List<Folder> doInBackground(Object... params) {
//            context = (Context) params[0];
//            callback = (CursorCallback) params[1];
//            mResultFolder.clear();
//            List<Video> list = new ArrayList<>();
//            ContentResolver contentResolver = context.getContentResolver();
//            if (context != null) {
//                Cursor cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
//                if (cursor != null) {
//                    while (cursor.moveToNext()) {
//                        Video video = new Video();
//                        video.setId(cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID)));
//                        video.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)));
//                        video.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)));
//                        video.setThumbnail(MediaStore.Video.Thumbnails.getThumbnail(contentResolver, video.getId(), MediaStore.Video.Thumbnails.MINI_KIND, null));
//                        list.add(video);
//                        File folderFile = new File(video.getPath()).getParentFile();
//                        if (folderFile != null && folderFile.exists()) {
//                            String fp = folderFile.getAbsolutePath();
//                            Folder f = getFolderByPath(fp);
//                            if (f == null) {
//                                Folder folder = new Folder();
//                                folder.name = folderFile.getName();
//                                folder.path = fp;
//                                folder.video_cover = video;
//                                List<Video> videoList = new ArrayList<>();
//                                videoList.add(video);
//                                folder.medias = videoList;
//                                mResultFolder.add(folder);
//                            } else {
//                                f.medias.add(video);
//                            }
//                        }
//                    }
//                }
//                return mResultFolder;
//            } else {
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(List<Folder> list) {
//            super.onPostExecute(list);
//            if (list != null) {
//                callback.onFinishCursor(list, 1);
//            }
//        }
//
//        private Folder getFolderByPath(String path) {
//            if (mResultFolder != null) {
//                for (Folder folder : mResultFolder) {
//                    if (TextUtils.equals(folder.path, path)) {
//                        return folder;
//                    }
//                }
//            }
//            return null;
//        }
//    }
}
