package domain;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.Log;

import java.util.Random;

/**
 * User: XuJunjie(Xujj@ysion.com)
 * Date: 2015-11-19
 * Time: 14:24
 * 图片的效果处理
 */
public class ImageEffect {

    /**
     * 设置随机点生成油画（效果很差）
     * @param bmpSource
     * @return Bitmap
     */
    public static Bitmap toYouHua(Bitmap bmpSource)
    {
        Bitmap bmpReturn = Bitmap.createBitmap(bmpSource.getWidth(),
                bmpSource.getHeight(), Bitmap.Config.RGB_565);
        int color = 0;
        int Radio = 0;
        int width = bmpSource.getWidth();
        int height = bmpSource.getHeight();

        Random rnd = new Random();
        int iModel = 10;
        int i = width - iModel;
        while (i > 1)
        {
            int j = height - iModel;
            while (j > 1)
            {
                int iPos = rnd.nextInt(100000) % iModel;
                color = bmpSource.getPixel(i + iPos, j + iPos);
                bmpReturn.setPixel(i, j, color);
                j = j - 1;
            }
            i = i - 1;
        }
        return bmpReturn;
    }


    /**
     * 调整RGB达到暖色调效果
     * @param bitmap
     * @return  Bitmap
     */
    public static Bitmap nuanse(Bitmap bitmap){
        Bitmap bm = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        int max = 0;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int y = 0,z=0;
        for(int row=0; row<height; row++){
            for(int col=0; col<width; col++){
                int pixel = bitmap.getPixel(col, row);// ARGB
                int red = Color.red(pixel); // same as (pixel >> 16) &0xff
                int green = Color.green(pixel); // same as (pixel >> 8) &0xff
                int blue = Color.blue(pixel); // same as (pixel & 0xff)
                int alpha = Color.alpha(pixel); // same as (pixel >>> 24)

                // Log.e("red:", red + "");

                if((red + green + blue) > max ){
                    max = (red + green + blue);
                    y = row ;
                    z = col;

                }

                if(red <100){
                    red += 50;
                }
                if(green>100){
                    green -= 50;
                }
                if(blue>100){
                    blue-=50;
                }
                bm.setPixel(col, row, Color.argb(alpha, red, green, blue));
            }
        }
        Log.e("row:", y + "");
        Log.e("col:", z + "");
        Log.e("with:", width + "");
        Log.e("height", height + "");

        return bm;
    }
    /**色阶分区
     *
     */
    public static Bitmap fenqu(Bitmap bitmap){
        Bitmap bm = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] array = new int[256];
        for(int row = 0;row < height ;row++){
            for(int col = 0;col < width ; col++){
                int pixel = bitmap.getPixel(col, row);// ARGB
                int red = Color.red(pixel); // same as (pixel >> 16) &0xff
                int green = Color.green(pixel); // same as (pixel >> 8) &0xff
                int blue = Color.blue(pixel);
                int alpha = Color.alpha(pixel);//亮度 色阶0.229×R + 0.587*G + 0.114*B
                double a = 0.229*red + 0.587*green + 0.114*blue;
                if(a<50){
                    bm.setPixel(col, row, Color.argb(alpha, 255, 0, 0));
                }
                if(a>50 && a<100){
                    bm.setPixel(col , row, Color.argb(alpha,0,255,0));
                }
                if(a>100 && a <150){
                    bm.setPixel(col , row, Color.argb(alpha,0,0,255));
                }
                if(a>150 && a <200){
                    bm.setPixel(col , row, Color.argb(alpha,0,255,255));
                }
                if(a>200 && a <250){
                    bm.setPixel(col , row, Color.argb(alpha,255,255,255));
                }
                array[alpha]++;
            }
        }
        int s = 0;
        for(int i = 0;i<255;i++){
            if(array[i] != 0){
                s++;
            }
        }
        Log.e("dasd",s+"");
        return bm;
    }


    /**
     * 提高亮度
     * @param bitmap
     * @param scale 在1.0f到2.0f之间合适
     * @return
     */
    public static Bitmap brightness(Bitmap bitmap,float scale){
        Bitmap newBitMap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        ColorMatrix scaleMatrix = new ColorMatrix();
        scaleMatrix.setScale(scale,scale,scale,1);

        //生效
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.postConcat(scaleMatrix);

        Canvas canvas = new Canvas(newBitMap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return newBitMap;
    }

    /**
     * 光效增强（感觉效果不好）
     *
     * @param bmp
     *            原图片
     * @param centerX
     *            光源横坐标
     * @param centerY
     *            光源纵坐标
     * @return 光效增强图片
     */
    public static Bitmap warmthFilter(Bitmap bmp, int centerX, int centerY) {
        final int width = bmp.getWidth();
        final int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int pixR = 0;
        int pixG = 0;
        int pixB = 0;

        int pixColor = 0;

        int newR = 0;
        int newG = 0;
        int newB = 0;
        int radius = Math.min(centerX, centerY);

        final float strength = 1000F; // 光照强度 100~150
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int pos = 0;
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                pos = i * width + k;
                pixColor = pixels[pos];

                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);

                newR = pixR;
                newG = pixG;
                newB = pixB;

                // 计算当前点到光照中心的距离，平面座标系中求两点之间的距离
                int distance = (int) (Math.pow((centerY - i), 2) + Math.pow(centerX - k, 2));
                if (distance < radius * radius) {
                    // 按照距离大小计算增加的光照值
                    int result = (int) (strength * (1.0 - Math.sqrt(distance) / radius));
                    newR = pixR + result;
                    newG = pixG + result;
                    newB = pixB + result;
                }

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));

                pixels[pos] = Color.argb(255, newR, newG, newB);
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 色块分区
     * @param bitmap
     * @param bitwidth
     * @param bitheight
     * @return
     */
    public static Bitmap bichuhua(Bitmap bitmap,int bitwidth,int bitheight) {
        Bitmap bm = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //二维数组存储数据
        int a[][] = new int[height][width];

        int sum1 = 0,sum2=0,sum3=0,sum4=0;
        int avg = 0;
        System.out.println("chang " + height + " kuan " + width);

      /*  for (int row = 0; row < height ; row++) {
            for (int col = 0; col < width ; col++) {
               if((row-bitheight/2) >=0 && (col - bitwidth/2)>=0 && (row+bitheight) <= height && (col+bitwidth) <= width){
                   sum1 = 0;sum2=0;sum3=0;sum4=0;
                   for(int i = row-bitheight/2 ;i < (row+bitheight/2);i++){
                   for(int j = col - bitwidth/2;j < (col+bitwidth/2);j++){
                           int pixel = bitmap.getPixel(j, i);// ARGB
                           int red = Color.red(pixel); // same as (pixel >> 16) &0xff
                           int green = Color.green(pixel); // same as (pixel >> 8) &0xff
                           int blue = Color.blue(pixel);
                           int alpha = Color.alpha(pixel);
                           sum1 += red;
                           sum2 += green;
                           sum3 += blue;
                           sum4 += alpha;
                       }
                   }
                   int pixel = bitmap.getPixel(col, row);// ARGB
                   int alpha = Color.alpha(pixel);
                   sum1 = sum1 / (bitheight*bitwidth);
                   sum2 = sum2 / (bitheight*bitwidth);
                   sum3 = sum3 / (bitheight*bitwidth);
                   sum4 = sum4 / (bitheight*bitwidth);
                   System.out.println("ddddd"+sum1+" dddas"+sum2+"dd"+sum3);
                   bm.setPixel(col,row, Color.argb(sum4 , sum1, sum2, sum3));
               }
            }
        }*/
        for (int row = 0; row < height / bitheight; row++) {
            for (int col = 0; col < width / bitwidth; col++) {
                sum1 = 0; sum2=0;sum3=0;
                for (int i = row * bitheight; i < (row + 1) * bitheight; i++) {
                    for (int j = col * bitwidth; j < (col + 1) * bitwidth; j++) {
                        int pixel = bitmap.getPixel(j, i);// ARGB
                        int red = Color.red(pixel); // same as (pixel >> 16) &0xff
                        int green = Color.green(pixel); // same as (pixel >> 8) &0xff
                        int blue = Color.blue(pixel); // same as (pixel & 0xff)
                        int alpha = Color.alpha(pixel);
                        sum1 += red;
                        sum2 += green;
                        sum3 += blue;
                    }
                }
                sum1 = sum1 / (bitheight * bitwidth);
                sum2 = sum2 / (bitheight * bitwidth);
                sum3 = sum3 / (bitheight * bitwidth);
                for (int i = row * bitheight; i < (row + 1) * bitheight; i++) {
                    for (int j = col * bitwidth; j < (col + 1) * bitwidth; j++) {
                        int pixel = bitmap.getPixel(j, i);// ARGB
                        int alpha = Color.alpha(pixel);
                        bm.setPixel(j, i, Color.argb(alpha, sum1, sum2, sum3));
                    }
                }
            }
        }
        return bm;
    }


}
