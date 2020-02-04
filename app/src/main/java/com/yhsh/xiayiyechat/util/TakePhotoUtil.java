package com.yhsh.xiayiyechat.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.lcw.library.imagepicker.manager.SelectionManager;
import com.lcw.library.imagepicker.provider.ImagePickerProvider;
import com.lcw.library.imagepicker.utils.MediaFileUtil;
import com.yhsh.xiayiyechat.R;

import java.io.File;
import java.util.ArrayList;

import androidx.core.content.FileProvider;

/**
 * @author 下一页5（轻飞扬）
 */
public class TakePhotoUtil {
    /**
     * 跳转相机拍照
     */
    public static String openCamera(Activity activity, int code) {
        //如果是单类型选取，判断添加类型是否满足（照片视频不能共存）
        ArrayList<String> selectPathList = SelectionManager.getInstance().getSelectPaths();
        if (!selectPathList.isEmpty()) {
            if (MediaFileUtil.isVideoFileType(selectPathList.get(0))) {
                //如果存在视频，就不能拍照了
                Toast.makeText(activity, activity.getString(R.string.single_type_choose), Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        //拍照存放路径
        File fileDir = new File(Environment.getExternalStorageDirectory(), "Pictures");
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        String mFilePath = fileDir.getAbsolutePath() + "/IMG_" + System.currentTimeMillis() + ".jpg";

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(activity, ImagePickerProvider.getFileProviderName(activity), new File(mFilePath));
        } else {
            uri = Uri.fromFile(new File(mFilePath));
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, code);
        return mFilePath;
    }
}
