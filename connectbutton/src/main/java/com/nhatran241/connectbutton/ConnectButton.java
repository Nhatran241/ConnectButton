package com.nhatran241.connectbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;


import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieListener;
import com.nhatran241.utils.ResourceHelper;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class ConnectButton extends CardView {
    AnimationDrawable frameAnimation;
    public static final int STATUS_CONNECT = 1;
    public static final int STATUS_CONNECTING = 2;
    public static final int STATUS_CONNECTED = 3;
    private CustomDrawable connectRes;
    private CustomDrawable connectingRes;
    private CustomDrawable connectedRes;
    private int startFrame, endFrame;
    private int currentStatus = STATUS_CONNECT;
    private float elevation,maxElevation;
    private boolean shadowConnect,shadowConnecting,shadowConnected;
    private Handler handler = new Handler();
    AppCompatImageView connectButtonView;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            setClickable(true);
        }
    };


    public ConnectButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs,context);
    }

    public ConnectButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs,context);
    }

    public ConnectButton(Context context) {
        super(context);
        init(null,context);
    }


    private void init(AttributeSet attrs,Context context) {
//        if (attrs == null)
//            return;
//        new Thread(() -> {

        connectButtonView = new AppCompatImageView(context);
        final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        addView(connectButtonView,lp);
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.ConnectButtonShadowView);
        int connectResId = ta.getResourceId(R.styleable.ConnectButtonShadowView_connectBackground, -1);
        int connectingResId = ta.getResourceId(R.styleable.ConnectButtonShadowView_connectingBackground, -1);
        int connectedResId = ta.getResourceId(R.styleable.ConnectButtonShadowView_connectedBackground, -1);
        float speed = ta.getFloat(R.styleable.ConnectButtonShadowView_lottie_speed, 1f);

        setRadius(ta.getDimension(R.styleable.ConnectButtonShadowView_cardCornerRadius,0f));
        setCardBackgroundColor(ta.getColor(R.styleable.ConnectButtonShadowView_cardBackgroundColor, Color.TRANSPARENT));
        shadowConnect=ta.getBoolean(R.styleable.ConnectButtonShadowView_shadowOnConnect,false);
        shadowConnecting=ta.getBoolean(R.styleable.ConnectButtonShadowView_shadowOnConnecting,false);
        shadowConnected=ta.getBoolean(R.styleable.ConnectButtonShadowView_shadowOnConnected,false);
        elevation=ta.getDimension(R.styleable.ConnectButtonShadowView_cardElevation,0f);
        maxElevation=ta.getDimension(R.styleable.ConnectButtonShadowView_cardMaxElevation,elevation);
        startFrame = ta.getInt(R.styleable.ConnectButtonShadowView_startFrame, -1);
        endFrame = ta.getInt(R.styleable.ConnectButtonShadowView_endFrame, -1);
        Log.d("ICT", "START FRAME: " + startFrame + " \n END FRAME: " + endFrame);
        Log.d("ICT", "ConnectButtonView  " + ta.getString(R.styleable.ConnectButtonShadowView_connectBackground));
        ta.recycle();

//        new Thread(() -> {
        connectRes = new CustomDrawable(getContext(), connectResId, speed);
        connectingRes = new CustomDrawable(getContext(), connectingResId, speed);
        connectedRes = new CustomDrawable(getContext(), connectedResId, speed);
        setStatus(STATUS_CONNECT);
//        }).start();

    }

    private void showShadow(Boolean b){
        if(b){
            setCardElevation(elevation);
            setMaxCardElevation(maxElevation);
        }else {
            setCardElevation(0f);
            setMaxCardElevation(0f);
        }
    }

    public void setStatus(int status) {
        switch (status) {
            case STATUS_CONNECT:
                showShadow(shadowConnect);
                setBackgroundResource(connectRes);
                setClickable(true);
                break;
            case STATUS_CONNECTING:
                showShadow(shadowConnecting);
                setBackgroundResource(connectingRes, startFrame, endFrame);
                setClickable(false);
                handler.postDelayed(runnable, 10000);
                break;
            case STATUS_CONNECTED:
                showShadow(shadowConnected);
                setBackgroundResource(connectedRes);
                setClickable(false);
                handler.postDelayed(runnable, 10000);
                break;
            default:
                return;
        }
        currentStatus = status;
    }

    public int getStatus() {
        return currentStatus;
    }


    private void setBackgroundResource(CustomDrawable resource) {
        if (resource == null)
            return;
        post(() -> {
            if (resource.isLottie()) {
                connectButtonView.setImageDrawable(resource.getDrawable());
            } else {
                connectButtonView.setImageDrawable(resource.getDrawable());

                if (frameAnimation != null && frameAnimation.isRunning()) {
                    frameAnimation.stop();
                    frameAnimation = null;

                }
                try {
                    frameAnimation = (AnimationDrawable) getBackground();
                    if (frameAnimation != null)
                        frameAnimation.start();
                } catch (ClassCastException e) {
//                    return;
                }
            }

        });
    }

    private void setBackgroundResource(CustomDrawable resource, int startFrame, int endFrame) {
        if (resource == null)
            return;
        post(() -> {
            if (resource.isLottie()) {
                Log.d("ICT", "isLottie");
                LottieDrawable lottieDrawable = ((LottieDrawable) resource.drawable);
                if (startFrame > 0 && endFrame > 0)
                    lottieDrawable.setMinAndMaxFrame(startFrame, endFrame);
                connectButtonView.setImageDrawable(lottieDrawable);
            } else {
                connectButtonView.setImageDrawable(resource.getDrawable());

                if (frameAnimation != null && frameAnimation.isRunning()) {
                    frameAnimation.stop();
                    frameAnimation = null;

                }
                try {
                    frameAnimation = (AnimationDrawable) getBackground();
                    if (frameAnimation != null)
                        frameAnimation.start();
                } catch (ClassCastException e) {
//                    return;
                }

            }

        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (frameAnimation == null)
            return;
        if (frameAnimation.isRunning())
            return;
        post(() -> frameAnimation.start());

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (frameAnimation == null)
            return;
        if (!frameAnimation.isRunning())
            return;
        post(() -> frameAnimation.stop());

    }

    private LottieDrawable createLottieDrawable(int id, float speed) {
        final LottieDrawable lottieDrawable = new LottieDrawable();


        LottieCompositionFactory.fromRawRes(getContext(), id).addListener(new LottieListener<LottieComposition>() {
            @Override
            public void onResult(LottieComposition result) {
                lottieDrawable.setComposition(result);
                lottieDrawable.setRepeatCount(LottieDrawable.INFINITE);
                lottieDrawable.setSpeed(speed);
                lottieDrawable.playAnimation();
            }
        });

        return lottieDrawable;
    }

    private LottieDrawable createLottieDrawable(int id) {
        return createLottieDrawable(id, 1f);
    }

    private class CustomDrawable {
        Drawable drawable;
        boolean isLottie = false;


        public CustomDrawable(Context context, int id) {
            Log.d("ICT", "CustomDrawable  " + id);
            if (ResourceHelper.checkTypeId(context, id, ResourceHelper.TYPE_RAW)) {
                drawable = createLottieDrawable(id);
                isLottie = true;
            } else
                drawable = getResources().getDrawable(id);
        }

        public CustomDrawable(Context context, int id, float speed) {
            Log.d("ICT", "CustomDrawable  " + id);
            if (ResourceHelper.checkTypeId(context, id, ResourceHelper.TYPE_RAW)) {
                drawable = createLottieDrawable(id, speed);
                isLottie = true;
            } else
                drawable = getResources().getDrawable(id);
        }

        public Drawable getDrawable() {
            return drawable;
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
        }

        public boolean isLottie() {
            return isLottie;
        }

    }

}
