package com.demo;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.playableads.PlayPreloadingListener;
import com.playableads.PlayableAds;
import com.playableads.SimplePlayLoadingListener;
import com.playableads.demo.R;

public class MainActivity extends Activity {
    private TextView info;
    private EditText mAppIdEdit;
    private EditText mUnitIdEdit;
    private ScrollView mScrollView;
    PlayableAds mAds;
    private View mPresentView;
    private View mRequestView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        info = (TextView) findViewById(R.id.text);
        mAppIdEdit = (EditText) findViewById(R.id.appId);
        mUnitIdEdit = (EditText) findViewById(R.id.unitId);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mRequestView = findViewById(R.id.request);
        mPresentView = findViewById(R.id.present);
        mPresentView.setEnabled(false);

        mAds = PlayableAds.init(this, "androidDemoApp", "androidDemoAdUnit");
    }

    public void request(View view) {
        mRequestView.setEnabled(false);
        mPresentView.setEnabled(false);
        checkWritePermission();

        String appId = mAppIdEdit.getText().toString();
        String unitId = mUnitIdEdit.getText().toString();
        if (!TextUtils.isEmpty(appId) && !TextUtils.isEmpty(unitId)) {
            mAds = PlayableAds.init(this, appId, unitId);
        }

        mAds.requestPlayableAds(mPreloadingListener);
        setInfo(getString(R.string.start_request));
    }

    private PlayPreloadingListener mPreloadingListener = new PlayPreloadingListener() {

        @Override
        public void onLoadFinished() {
            setInfo(getString(R.string.pre_cache_finished));
            mPresentView.setEnabled(true);
            mRequestView.setEnabled(true);
        }

        @Override
        public void onLoadFailed(int errorCode, String msg) {
            setInfo(String.format(getString(R.string.load_failed), errorCode, msg));
            mRequestView.setEnabled(true);
        }
        
    };

    public void present(View view) {
        if (!mAds.canPresentAd()) {
            Toast.makeText(this, R.string.loading_ad, Toast.LENGTH_SHORT).show();
            return;
        }
        mAds.presentPlayableAD(this, new SimplePlayLoadingListener() {
            @Override
            public void playableAdsIncentive() {
                setInfo(getString(R.string.ads_incentive));
                mPresentView.setEnabled(false);
                mRequestView.setEnabled(true);
            }

            @Override
            public void onAdsError(int code, String msg) {
                setInfo(getString(R.string.ads_error, code, msg));
            }

        });
    }

    private void setInfo(final String msg) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (info != null) {
                    info.append(msg + "\n\n");
                }
                mScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAds.onDestroy();
    }

    @TargetApi(23)
	private void checkWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                setInfo(getString(R.string.open_write_permission));
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
                setInfo(getString(R.string.open_phone_permission));
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
            }
        }
    }
}
