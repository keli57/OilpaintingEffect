package moren.com.oilpaintingeffect;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import data.PictureSave;
import domain.BitmapUtils;
import domain.ImageEffect;

public class MainActivity extends Activity  {
    private static final int CAMERA_REQUEST = 1 ;
    private static final int PICTURE = 2 ;
    private Bitmap bitmapSave;

    @Bind(R.id.qidong) Button qidong;
    @Bind(R.id.xiangce) Button xiangce;
    @Bind(R.id.sava) Button sava;
    @Bind(R.id.imageView1) ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);  //注入！！！！！
    }

    @OnClick(R.id.qidong)
    public void qidong(){
        takePhoto();
    }

    @OnClick(R.id.xiangce)
    public void xiangce(){
        PhotoIn();
    }

    @OnClick(R.id.sava)
    public void save(){
        if(bitmapSave != null){
            PictureSave save = new PictureSave();
            save.SavePicture(this,bitmapSave);
        }
        else
            Toast.makeText(getApplicationContext(), "你还没处理图片", Toast.LENGTH_SHORT).show();
    }

    private void PhotoIn() {
        Intent picture = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(picture, PICTURE);
    }

    private void takePhoto() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//构造intent
        startActivityForResult(cameraIntent, CAMERA_REQUEST);//发出intent，并要求返回调用结果
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(photo);
        }
        if(requestCode == PICTURE){
            Uri selectedImage = data.getData();
            InputStream imageStream;
            try {
                imageStream = getContentResolver().openInputStream(selectedImage);
                Bitmap originBitmap = BitmapFactory.decodeStream(imageStream);
                bitmapSave = new BitmapUtils().BitmapReduce(originBitmap, image.getWidth(), image.getHeight());
                image.setImageBitmap(new ImageEffect().fenqu(bitmapSave));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
