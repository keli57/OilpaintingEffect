package data;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * User: XuJunjie(Xujj@ysion.com)
 * Date: 2015-11-18
 * Time: 10:26
 *保存图片
 */
public class PictureSave {

    /**
     * Sava Picture in load
     * @param bitmap
     * @return null
     */
    public void SavePicture(Context context,Bitmap bitmap){
        //插入系统图库。
        //MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", "description");

        //保存图片
        File appDir = new File( Environment.getExternalStorageDirectory(),"Oil Painting Effect"); //存储路径
        if(!appDir.exists()){
            appDir.mkdir(); //不存在则建立
        }
       String fileName = System.currentTimeMillis()+".jpg";//文件名为系统时间
        File file = new File(appDir,fileName); //把文件存在appDir中

        try {
            FileOutputStream fos = new FileOutputStream(file); //文件写入
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos); //写入fos中
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //把文件插入系统相册
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),file.getAbsolutePath(),fileName,null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,	Uri.fromFile(new File(file.getPath()))));
    }

}
