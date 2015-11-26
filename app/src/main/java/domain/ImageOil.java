package domain;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.Arrays;

/**
 * User: XuJunjie(Xujj@ysion.com)
 * Date: 2015-11-19
 * Time: 14:33
 */
public class ImageOil {

    /**
     * 油画特效目前最好的
     * @param sourceImage
     * @param radius
     * @param intensityLevels
     * @return
     */
    public static Bitmap oil(Bitmap sourceImage,int radius,int intensityLevels){
        Bitmap bm = Bitmap.createBitmap(sourceImage.getWidth(), sourceImage.getHeight(),sourceImage.getConfig());
        int averageR[] = new int[intensityLevels];
        int averageG[]=new int[intensityLevels];
        int averageB[]=new int[intensityLevels];
        int intensityCount[]=new int[intensityLevels];

        for(int x=0;x< sourceImage.getWidth();++x)
        {
            int left = Math.max(0,x-radius);
            int right = Math.min(x+radius,sourceImage.getWidth()-1);
            for(int y=0;y< sourceImage.getHeight();++y)
            {

                int top = Math.max(0,y-radius);
                int bottom = Math.min(y+radius,sourceImage.getHeight()-1);

                Arrays.fill(averageR, 0);
                Arrays.fill(averageG,0);
                Arrays.fill(averageB,0);
                Arrays.fill(intensityCount,0);
                int maxIndex=-1;

                for(int j=top;j<=bottom;++j)
                {
                    for(int i=left;i<=right;++i)
                    {
                        if(!inRange(x,y,i, j,radius)) continue;

                        int pixel = sourceImage.getPixel(i, j);// ARGB
                        int red = Color.red(pixel); // same as (pixel >> 16) &0xff
                        int green = Color.green(pixel); // same as (pixel >> 8) &0xff
                        int blue = Color.blue(pixel); // same as (pixel & 0xff)
                        int alpha = Color.alpha(pixel);
                        int intensityIndex = (int)((((red+green+blue)/3.0)/256.0)*intensityLevels);

                        intensityCount[intensityIndex]++;
                        averageR[intensityIndex] += red;
                        averageG[intensityIndex] += green;
                        averageB[intensityIndex] += blue;

                        if( maxIndex==-1 || intensityCount[maxIndex]< intensityCount[intensityIndex])
                        {
                            maxIndex = intensityIndex;
                        }
                    }
                }

                int curMax = intensityCount[maxIndex];
                int r = averageR[maxIndex] / curMax;
                int g = averageG[maxIndex] / curMax;
                int b = averageB[maxIndex] / curMax;

                int pixel = sourceImage.getPixel(y, x);// ARGB
                int alpha = Color.alpha(pixel);
                bm.setPixel(x, y, Color.argb(alpha, r, g, b));
                // int rgb=((r << 16) | ((g << 8) | b));
                // dest.setPixel(x,y,rgb);
            }
        }
        return bm;
    }

    private static boolean inRange(int cx, int cy, int i, int j,int radius) {
        double d;
        d = Math.sqrt(Math.pow((cx-i),2)+Math.pow((cy-j),2));
        return d<radius;
    }
}
