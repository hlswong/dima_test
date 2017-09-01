package com.sahk.earlyliteracy.fragments;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import com.sahk.earlyliteracy.BaseActivity;
import com.sahk.earlyliteracy.R;
import com.sahk.earlyliteracy.applications.Config;
import com.sahk.earlyliteracy.constants.GlobalValue;
import com.sahk.earlyliteracy.database.TableTheme;
import com.sahk.earlyliteracy.utils.CommonUtil;
import com.sahk.earlyliteracy.utils.CropImage.CropImgActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AwardFragment extends BaseFragment {

    private CallbackManager callbackManager;
    private ImageButton imageButtonShare, imageButtonContinue, imageButtonPhoto; //Added By Rex
    private ImageView imageViewItem1, imageViewItem2, imageViewItem3, imageViewItem4, imageViewMonster;
    private int score;
    private int themeID,gameID,level;
    private String themeName;
    private int themeScore = 0;
    private RelativeLayout relativeLayoutBackground;
    private ShareDialog shareDialog;
    private TextView textViewScore;
    private String curShareName = "Egg00"; //Added By Rex
    public static Bitmap mImage;  //Added By Rex

    private double[] arrayAwardX = { 0.541667,0.586458,0.526563,0.511458,0.511458,0.511458 };
    private double[] arrayAwardY = { 0.175926,0.194444,0.232407,0.194444,0.194444,0.194444 };


    //Edit By Rex
    //public static AwardFragment newInstance(int themeID) {
    public static AwardFragment newInstance(int themeID, String themeName, int gameID, int level ) {
        AwardFragment awardFragment = new AwardFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("themeID", themeID);
        bundle.putString("themeName", themeName);
        bundle.putInt("gameID", gameID);
        bundle.putInt("level", level);
        awardFragment.setArguments(bundle);
        return awardFragment;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_award, viewGroup, false);
        myFragment.setToolbarMode(Config.TOOLBAR_STATUS_AWARD);
        themeID = getArguments().getInt("themeID");
        themeName = getArguments().getString("themeName");
        gameID = getArguments().getInt("gameID");
        level = getArguments().getInt("level");
        setView(view);
        setListener();
        setScore();
        setItem();
        if (tableTheme.isFinishedAllBasicGame(themeID)) {
            imageButtonShare.setVisibility(View.VISIBLE);
        }

        //Added By Rex
        if ( gameID != 0 || level != 0 )
        {
            imageButtonContinue.setVisibility(View.VISIBLE);
            ((BaseActivity)activity).setToolbarMode(Config.TOOLBAR_STATUS_AWARD_FROM_GAME);
        }
        return view;
    }

    private void setView(View view) {
        imageButtonShare = (ImageButton) view.findViewById(R.id.imageButtonShare);
        //Added By Rex
        imageButtonContinue = (ImageButton) view.findViewById(R.id.imageButtonContinue);
        imageButtonPhoto = (ImageButton) view.findViewById(R.id.imageButtonPhoto);
        imageViewItem1 = (ImageView) view.findViewById(R.id.imageViewItem1);
        imageViewItem2 = (ImageView) view.findViewById(R.id.imageViewItem2);
        imageViewItem3 = (ImageView) view.findViewById(R.id.imageViewItem3);
        imageViewItem4 = (ImageView) view.findViewById(R.id.imageViewItem4);
        imageViewMonster = (ImageView) view.findViewById(R.id.imageViewMonster);
        relativeLayoutBackground = (RelativeLayout) view.findViewById(R.id.relativeLayoutBackground);
        textViewScore = (TextView) view.findViewById(R.id.textViewScore);

        if (mImage != null) {
            mImage.recycle();
            mImage = null;
        }
    }

    private void setScore() {
        if (tableScore.get(themeID) != null) {
            themeScore = tableScore.get(themeID).getScore();
        }

        if (!sharedPreferences.getBoolean("is_ans_survey", false)) {
            textViewScore.setText(String.valueOf(tableGamePlay.getMark(themeID)));
        }
        else
        {
            textViewScore.setText(String.valueOf(tableGamePlay.getMark(themeID)+200 ) );
        }


    }

    private void setItem() {
        TableTheme tableTheme = new TableTheme(context);
        if(tableTheme.isFinishedAllBasicGame(themeID)) {
            // Edit Show Image Resource By Rex
            if (themeScore >= (3300 / GlobalValue.gTestDivide) ) {
                int monResId = getResources().getIdentifier("aw_t" + themeID + "_monster05_1", "drawable", context.getPackageName());
                imageViewItem1.setImageResource(R.drawable.award_teat);
                imageViewItem2.setImageResource(R.drawable.award_bag);
                imageViewItem3.setImageResource(R.drawable.award_skateboard);
                imageViewItem4.setImageResource(R.drawable.award_sign);
//                imageViewItem1.setVisibility(View.GONE);
//                imageViewItem2.setVisibility(View.GONE);
//                imageViewItem3.setVisibility(View.GONE);
//                imageViewItem4.setVisibility(View.GONE);

                imageButtonPhoto.setVisibility(View.VISIBLE);
                relativeLayoutBackground.setBackgroundResource(R.drawable.aw_bg_final);
                if (sharedPreferences.getBoolean("award_img_" + String.valueOf(themeID) , false)) {
                    imageViewMonster.setImageBitmap(loadImageFromStorage("award_img_" + String.valueOf(themeID)));
                }
                else
                {
                    imageViewMonster.setImageResource(monResId);
                    LoopWinAllAward(); //Added By Rex
                }

//                curShareName = "Monster04" ; //Added By Rex
                curShareName = "Monster05" ; //Added By Rex
            } else if (themeScore >= (2800/ GlobalValue.gTestDivide) ) {
                int monResId = getResources().getIdentifier("aw_t" + themeID + "_monster03", "drawable", context.getPackageName());
                imageViewItem1.setImageResource(R.drawable.award_teat);
                imageViewItem2.setImageResource(R.drawable.award_bag);
                imageViewItem3.setImageResource(R.drawable.award_skateboard);
                imageViewItem4.setImageResource(R.drawable.award_sign_off);
                imageViewMonster.setImageResource(monResId);
                relativeLayoutBackground.setBackgroundResource(R.drawable.aw_bg_monster);
                curShareName = "Monster03" ; //Added By Rex
            } else if (themeScore >= (2100/ GlobalValue.gTestDivide) ) {
                int monResId = getResources().getIdentifier("aw_t" + themeID + "_monster02", "drawable", context.getPackageName());
                imageViewItem1.setImageResource(R.drawable.award_teat);
                imageViewItem2.setImageResource(R.drawable.award_bag);
                imageViewItem3.setImageResource(R.drawable.award_skateboard_off);
                imageViewItem4.setImageResource(R.drawable.award_sign_off);
                imageViewMonster.setImageResource(monResId);
                relativeLayoutBackground.setBackgroundResource(R.drawable.aw_bg_monster);
                curShareName = "Monster02" ; //Added By Rex
            } else if (themeScore >= (1500/ GlobalValue.gTestDivide) ) {
                int monResId = getResources().getIdentifier("aw_t" + themeID + "_monster01", "drawable", context.getPackageName());
                imageViewItem1.setImageResource(R.drawable.award_teat);
                imageViewItem2.setImageResource(R.drawable.award_bag_off);
                imageViewItem3.setImageResource(R.drawable.award_skateboard_off);
                imageViewItem4.setImageResource(R.drawable.award_sign_off);
                imageViewMonster.setImageResource(monResId);
                //relativeLayoutBackground.setBackgroundResource(R.drawable.bg_monster01);
                relativeLayoutBackground.setBackgroundResource(R.drawable.aw_bg_monster);
                curShareName = "Monster01" ; //Added By Rex
            }
            else if (themeScore >= (1060/ GlobalValue.gTestDivide) ) {  //Uncommented By Rex
                int monResId = getResources().getIdentifier("aw_t" + themeID + "_monster00", "drawable", context.getPackageName());
                imageViewItem1.setImageResource(R.drawable.award_teat_off);
                imageViewItem2.setImageResource(R.drawable.award_bag_off);
                imageViewItem3.setImageResource(R.drawable.award_skateboard_off);
                imageViewItem4.setImageResource(R.drawable.award_guitar_off);
                imageViewMonster.setImageResource(monResId);
                //relativeLayoutBackground.setBackgroundResource(R.drawable.bg_monster01);
                relativeLayoutBackground.setBackgroundResource(R.drawable.aw_bg_monster);
                curShareName = "Monster00" ; //Added By Rex
            }
            else if (themeScore >= (800/ GlobalValue.gTestDivide) ) {
                int monResId = getResources().getIdentifier("aw_t" + themeID + "_egg04", "drawable", context.getPackageName());
                imageViewItem1.setImageResource(R.drawable.award_grass);
                imageViewItem2.setImageResource(R.drawable.award_light);
                imageViewItem3.setImageResource(R.drawable.award_spray);
                imageViewItem4.setImageResource(R.drawable.award_hand);
                imageViewMonster.setImageResource(monResId);
                relativeLayoutBackground.setBackgroundResource(R.drawable.aw_bg_egg);
                curShareName = "Egg04" ; //Added By Rex
            } else if (themeScore >= (600/ GlobalValue.gTestDivide) ) {
                int monResId = getResources().getIdentifier("aw_t" + themeID + "_egg03", "drawable", context.getPackageName());
                imageViewItem1.setImageResource(R.drawable.award_grass);
                imageViewItem2.setImageResource(R.drawable.award_light);
                imageViewItem3.setImageResource(R.drawable.award_spray);
                imageViewItem4.setImageResource(R.drawable.award_hand_off);
                imageViewMonster.setImageResource(monResId);
                relativeLayoutBackground.setBackgroundResource(R.drawable.aw_bg_egg);
                curShareName = "Egg03" ; //Added By Rex
            } else if (themeScore >= (400/ GlobalValue.gTestDivide) ) {
                int monResId = getResources().getIdentifier("aw_t" + themeID + "_egg02", "drawable", context.getPackageName());
                imageViewItem1.setImageResource(R.drawable.award_grass);
                imageViewItem2.setImageResource(R.drawable.award_light);
                imageViewItem3.setImageResource(R.drawable.award_spray_off);
                imageViewItem4.setImageResource(R.drawable.award_hand_off);
                imageViewMonster.setImageResource(monResId);
                relativeLayoutBackground.setBackgroundResource(R.drawable.aw_bg_egg);
                curShareName = "Egg02" ; //Added By Rex
            } else if (themeScore >= (200/ GlobalValue.gTestDivide) ) {
                int monResId = getResources().getIdentifier("aw_t" + themeID + "_egg01", "drawable", context.getPackageName());
                imageViewItem1.setImageResource(R.drawable.award_grass);
                imageViewItem2.setImageResource(R.drawable.award_light_off);
                imageViewItem3.setImageResource(R.drawable.award_spray_off);
                imageViewItem4.setImageResource(R.drawable.award_hand_off);
                imageViewMonster.setImageResource(monResId);
                relativeLayoutBackground.setBackgroundResource(R.drawable.aw_bg_egg);
                curShareName = "Egg01" ; //Added By Rex
            } else if (( tableTheme.isFinishedAllBasicGame(themeID) )) {   // Added By Rex
                int monResId = getResources().getIdentifier("aw_t" + themeID + "_egg01", "drawable", context.getPackageName());
                imageViewItem1.setImageResource(R.drawable.award_grass_off);
                imageViewItem2.setImageResource(R.drawable.award_light_off);
                imageViewItem3.setImageResource(R.drawable.award_spray_off);
                imageViewItem4.setImageResource(R.drawable.award_hand_off);
                imageViewMonster.setImageResource(monResId);

                //Edit By Rex
//                relativeLayoutBackground.setBackgroundResource(R.drawable.aw_bg_egg);
                relativeLayoutBackground.setBackgroundResource(R.drawable.aw_bg_monster);
                curShareName = "Egg00" ; //Added By Rex
            }
        }

//        //For Testing
//        if (sharedPreferences.getBoolean("award_img_" + String.valueOf(themeID) , false)) {
//            imageButtonShare.setVisibility(View.VISIBLE);
//            imageButtonPhoto.setVisibility(View.VISIBLE);
//            imageViewMonster.setImageBitmap(loadImageFromStorage("award_img_" + String.valueOf(themeID)));
//        }

    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( resultCode == 900) // From CropImageActivity
        {
//            Toast.makeText(activity, "Award HIHI", Toast.LENGTH_SHORT).show();
//            imageViewMonster.setImageBitmap(GlobalValue.tempBitmap);

//            relativeLayoutBackground.setBackgroundResource(R.drawable.aw_bg_monster);
            if (mImage != null) {
                int monResId = getResources().getIdentifier("share_t" + themeID + "_monster06_final", "drawable", context.getPackageName());
                Bitmap curBitmap = BitmapFactory.decodeResource(getResources(), monResId);
                Bitmap newBitmap = overlay( curBitmap , Bitmap.createScaledBitmap(mImage, (int)(curBitmap.getWidth()*0.181771) , (int)(curBitmap.getWidth()*0.181771) , false) , ((float) curBitmap.getWidth()*(float)arrayAwardX[themeID-1]) , ((float) curBitmap.getHeight()*(float)arrayAwardY[themeID-1]) );

                saveToInternalStorage(newBitmap, "award_img_" + String.valueOf(themeID) );
                sharedPreferences.edit().putBoolean( "award_img_" + String.valueOf(themeID), true).apply();

                imageViewMonster.setImageBitmap(newBitmap);
//                int sampleSize = data.getIntExtra("SAMPLE_SIZE", 1);
//                double ratio = ((int) (10 * mImage.getWidth() / (double) mImage.getHeight())) / 10d;
//                int byteCount = 0;
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR1) {
//                    byteCount = mImage.getByteCount() / 1024;
//                }
//                String desc = "(" + mImage.getWidth() + ", " + mImage.getHeight() + "), Sample: " + sampleSize + ", Ratio: " + ratio + ", Bytes: " + byteCount + "K";
////                ((TextView) findViewById(R.id.resultImageText)).setText(desc);
            } else {
//                Toast.makeText(getActivity(), "No image is set to show", Toast.LENGTH_LONG).show();
            }
        }
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
    public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2 , float x , float y) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, x , y , null);
        return bmOverlay;
    }
    private String saveToInternalStorage(Bitmap bitmapImage, String fileName){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageAward", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory, fileName +".png");
        mypath.delete();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }
    private Bitmap loadImageFromStorage( String fileName)
    {

        try {
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            String path = cw.getDir("imageAward", Context.MODE_PRIVATE).getAbsolutePath();
            File f=new File(path, fileName + ".png");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }


    @SuppressWarnings("unchecked")
    private void setListener() {

        imageButtonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callbackManager = CallbackManager.Factory.create();

                //Added by Rex
                if(CommonUtil.checkNetworkConnected(context)) {
                    if (sharedPreferences.getBoolean("award_img_" + String.valueOf(themeID) , false)) {

                        shareDialog = new ShareDialog(getActivity());
                        if (ShareDialog.canShow(SharePhotoContent.class)) {
                            Bitmap image = loadImageFromStorage("award_img_" + String.valueOf(themeID));

////                            int monResId = getResources().getIdentifier("aw_t" + themeID + "_monster05_1", "drawable", context.getPackageName());
//                            Bitmap bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aw_bg_final);
//                            Bitmap image = overlay( bgBitmap , curImage , 0 , 0);
                            SharePhoto photo = new SharePhoto.Builder()
                                    .setBitmap(image)
                                    .build();
                            SharePhotoContent content = new SharePhotoContent.Builder()
                                    .addPhoto(photo)
                                    .build();
                            shareDialog.show(content);
                        }
                        else
                        {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.facebook.katana"));
                            activity.startActivity(browserIntent);
//                            Toast.makeText(activity, "需要安裝Facebook app才可以開啟", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        shareDialog = new ShareDialog(getActivity());

                        if (ShareDialog.canShow(SharePhotoContent.class)) {
                            if (ShareDialog.canShow(ShareLinkContent.class)) {
                                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                        .setContentUrl(Uri.parse("http://sahk.kanhan.com/download.html")) //Edited By Rex
                                        .setImageUrl(Uri.parse(
                                                Config.SERVER_BASE_URL + "facebook/theme" + String.valueOf(themeID) + "/" +
                                                        "share_T" + String.valueOf(themeID) + "_" + curShareName + ".png"
                                        ))
                                        .setContentTitle("香港耀能協會 - 學前語文秘笈")
                                        .build();
                                shareDialog.show(linkContent);
                            }
                        }
                        else
                        {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.facebook.katana"));
                            activity.startActivity(browserIntent);
//                            Toast.makeText(activity, "需要安裝Facebook app才可以開啟", Toast.LENGTH_SHORT).show();
                        }
                    }


                    //file:///android_asset/

//                    .setImageUrl(Uri.fromFile(new File(
//                            "file:///android_asset/share/theme" + themeID + "/" +
//                                    "share_T" + themeID + "_" + curShareName + ".png"
//                    )))

//                    .setImageUrl(Uri.parse(
//                            Config.SERVER_BASE_URL + "facebook/theme" + themeID + "/" +
//                                    "share_T" + themeID + "_" + curShareName + ".png"
//                    ))

                }else{
                    AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                    adb.setCancelable(false);   //Added By Rex
                    adb.setMessage("需連結網絡方能分享。是否要連線？");
                    adb.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                        }
                    });
                    adb.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog alertDialog = adb.create();
                    alertDialog.show();
                }

                //Commented by Rex
//                shareDialog = new ShareDialog(getActivity());
//                if (ShareDialog.canShow(ShareLinkContent.class)) {
//                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
//                            .setContentUrl(Uri.parse("http://www.facebook.com/"))
//                            .build();
//                    shareDialog.show(linkContent);
//                }
            }
        });

        //Added by Rex
        imageButtonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( gameID != 0 || level != 0 ) {
                    myFragment.getContainer().setFragmentChild(GameRateFragment.newInstance(themeID, themeName, gameID, level));
                }
            }
        });

        //Added By Rex
        imageButtonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( activity ,CropImgActivity.class);
//                startActivity(intent);
                startActivityForResult(intent,200);
            }
        });
    }


    private boolean started = false;
    private int totTime = 0;
    private Handler handler = new Handler();

    //Added By Rex
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            totTime++;
            if ( totTime % 2 == 0) {
                relativeLayoutBackground.setBackgroundResource(R.drawable.aw_bg_final);
            }
            else
            {
                relativeLayoutBackground.setBackgroundResource(R.drawable.aw_bg_final2);
            }
            if(started) {
                LoopWinAllAward();
            }
        }
    };

    //Added By Rex
    public void stop() {
        started = false;
        handler.removeCallbacks(runnable);
    }

    //Added By Rex
    public void LoopWinAllAward() {
        started = true;
        handler.postDelayed(runnable, 500);
    }

    //Added By Rex
    @Override
    public void onDestroyView() {
        stop();
        super.onDestroyView();
    }
}