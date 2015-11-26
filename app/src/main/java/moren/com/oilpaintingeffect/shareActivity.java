package moren.com.oilpaintingeffect;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import butterknife.Bind;

/**
 * User: XuJunjie(Xujj@ysion.com)
 * Date: 2015-11-25
 * Time: 09:23
 */
public class shareActivity extends Activity {
    @Bind(R.id.share) Button share;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shareactivity);

    }
}
