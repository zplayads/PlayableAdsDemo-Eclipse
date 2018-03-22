# 1 Overview


## 1.1 Introduction
This guide is designed for developers who are going to integrate the ZPLAY Ads SDK into their Android Apps via Android Studio.  Please contact service@zplayads.com, if you need any assistance in this work.

## 1.2 Development Environment
- OS：WinAll, Linux, Mac
- IDE：Android Studio 2.x
- Deploy Target：Android 4.0 and above

## 1.3 ZPLAY Ads Account Requirements
An account is required on our platform before SDK integration can be completed.  The following App specific data items are the minimum needed to proceed.

APPID: An ID for your App, obtained when setting up the App for monetization within your account on the ZPLAY Ads website.

adUnitID: An ID for a specific ad placement within your App, as generated for your Apps within your account on the ZPLAY Ads website. 

# 2 SDK Integration
Add the playableads library to lib, Optional add google play library.

# 3 Access Code
To pre-load an ad may take several seconds, so it’s recommended to initialize the SDK and load ads as early as possible. When you initialize the SDK, you need to provide your APPID and adUnitID (as previously registered on ZPLAYAds.com) into the relevant places. 

## 3.1 Initialize SDK
When you initialize the SDK, you need to provide your APPID and adUnitID (as previously registered on en.zplayads.com) into the marked places. 

Call the method below  to initialize SDK
```
PlayableAds.init(context, APPID, adUnitID)
```

Note: You can use the following test id when you are testing. Test id won't generate revenue, please use official id when you release your App.

|OS|  App_ID  |  Ad_Unit_id|
|--------|----------|------------|
|Android |5C5419C7-A2DE-88BC-A311-C3E7A646F6AF|3FBEFA05-3A8B-2122-24C7-A87D0BC9FEEC|

## 3.2 Request Ad
To pre-load an ad may take several seconds, so it's recommended to initialize the SDK and load ads as early as possible. 

Call the following method to pre-load playable ad

```
PlayableAds.getInstance().requestPlayableAds(playPreloadingListener)
```
You can judge the availability of an ad by this listener callback.
```
public interface PlayPreloadingListener {
    // An ad is loaded
    void onLoadFinished();
    // Fail to load an ad
    void onLoadFailed(int errorCode, String msg);
}
```

Code Sample：

```
PlayableAds.getInstance().requestPlayableAds(new PlayPreloadingListener() {
    @Override
    public void onLoadFinished() {
        // Ad preload successfully, call method presentPlayableAd(...)to show playable ad.
    }

    @Override
    public void onLoadFailed(int errorCode, String message) {
        //  Ad preload failed, locate problem according error code and error messages.
    }
})
```
## 3.3 Show Ads
When an ad is ready to display, you can show it using following method.
```
PlayableAds.getInstance().presentPlayableAD(this, playLoadingListener)
```
You can confirm the completed ad show with this listener callback.  
```
public interface PlayLoadingListener {
    // This is a callback of completing the whole event (showing, playing, quitting from landing page), which means the reward shall be given
    void playableAdsIncentive();
    // Mistake occurred during the showing
    void onAdsError(int code, String msg);
}
```

Code Sample:
```
PlayableAds.getInstance().presentPlayableAD(activity, new PlayLoadingListener() {
    @Override
    public void playableAdsIncentive() {
        // Ad impression success, use this to judge if the reward should be given.
    }

    @Override
    public void onAdsError(int errorCode, String message) {
        // Ad impression fail,locate problem according error code and error messages.
    }
});
```

# 4 Proguard
If the project need to be proguarded, put the following code into the proguard.pro file or a custom file.
```
# playableAds
-keep class com.playableads.PlayPreloadingListener {*;}
-keep class com.playableads.PlayLoadingListener {*;}
-keep class * implements com.playableads.PlayPreloadingListener {*;}
-keep class * implements com.playableads.PlayLoadingListener {*;}
-keep class com.playableads.PlayableReceiver {*;}
-keep class com.playableads.constants.StatusCode {*;}
-keep class com.playableads.MultiPlayLoadingListener {*;}
-keep class com.playableads.MultiPlayPreloadingListener {*;}
-keep class * implements com.playableads.MultiPlayLoadingListener {*;}
-keep class * implements com.playableads.MultiPlayPreloadingListener {*;}
-keep class com.playableads.PlayableAds {
    public void onDestroy();
    public static com.playableads.PlayableAds getInstance();
    public void requestPlayableAds(com.playableads.PlayPreloadingListener, java.lang.String);
    public void requestPlayableAds(java.lang.String, com.playableads.PlayPreloadingListener);
    public synchronized static com.playableads.PlayableAds init(android.content.Context, java.lang.String);
    public void presentPlayableAD(java.lang.String, com.playableads.PlayLoadingListener);
    public void presentPlayableAd(com.playableads.PlayLoadingListener);
    public boolean canPresentAd(java.lang.String);
    public void setMultiLoadingListener(com.playableads.MultiPlayLoadingListener);
    public void setMultiPreloadingListener(com.playableads.MultiPlayPreloadingListener);
    public void setCacheCountPerUnitId(int);
    public void setAutoLoadAd(boolean);
}
```

# 5 Code Sample
Click [HERE](https://github.com/zplayads/PlayableAdsDemo-Eclipse) to download Demo

# 6 Notes
## 6.1 Request Ads ASAP
To ensure the ad resource can be successfully loaded, it’s encouraged to request ads as soon as possible.
## 6.2 Permissions
Make sure your app was granted Phone State permission and Storage Permission, otherwise there may be no ads in your app.
## 6.3 Request Next Ad
PlayableAds sdk will autoload next ad by default, when it failed to load ad they will try again 5 seconds later. You can forbiden autoload action by calling setAutoLoadAd(false).

