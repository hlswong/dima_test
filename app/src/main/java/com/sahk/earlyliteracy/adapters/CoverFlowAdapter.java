package com.sahk.earlyliteracy.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.util.List;

import com.sahk.earlyliteracy.R;
import com.sahk.earlyliteracy.database.ItemGame;
import com.sahk.earlyliteracy.database.ItemTheme;
import com.sahk.earlyliteracy.database.TableGame;
import com.sahk.earlyliteracy.database.TableTheme;
import com.sahk.earlyliteracy.widgets.CustomTextView;
import com.sahk.earlyliteracy.widgets.StrokeTextView;

public class CoverFlowAdapter extends BaseAdapter {


    public int downloadingGameID = 0;
    public int progress = 0;
    private List<ItemTheme> themeList;
    private Context context;
    private int themeID;

    public CoverFlowAdapter(Context context, int themeID, List<ItemTheme> themeList) {
        this.context = context;
        this.themeID = themeID;
        this.themeList = themeList;
    }

    @Override
    public int getCount() {
        return themeList.size();
    }

    @Override
    public Object getItem(int position) {
        return themeList.get(position);
    }

    public int findItemIndex(int gameId){
        int idx = -1;
        if(themeList!=null){
            for(int i = 0; i<themeList.size();i++){
                if(themeList.get(i).getGameID() == gameId){
                    idx = i;
                    break;
                }
            }
        }
        return  idx;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ItemTheme item = themeList.get(position);
        ViewHolder viewHolder=null;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.featurecoverflow_game, null);
            viewHolder = new ViewHolder();
            viewHolder.customTextViewProgress = (CustomTextView) view.findViewById(R.id.customTextViewProgress);
            viewHolder.imageButtonGame = (ImageButton) view.findViewById(R.id.imageButtonGame);
            viewHolder.imageViewGame = (ImageView) view.findViewById(R.id.imageViewGame);
            viewHolder.imageViewLock = (ImageView) view.findViewById(R.id.imageViewLock);
            viewHolder.relativeLayoutDownload = (RelativeLayout) view.findViewById(R.id.relativeLayoutDownload);
            viewHolder.strokeTextViewTitle = (StrokeTextView) view.findViewById(R.id.strokeTextViewTitle);
            //view.setTag(viewHolder);
        }
        //ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.imageButtonGame.setImageResource(context.getResources().getIdentifier("t" + themeID + "_menu", "drawable", context.getPackageName()));
        try {
            AssetManager assetManager = context.getAssets();
            viewHolder.imageViewGame.setImageBitmap(BitmapFactory.decodeStream(assetManager.open("image/theme" + themeID + "/game" + (item.getGameID()) + ".png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        viewHolder.strokeTextViewTitle.setText(themeList.get(position).getGameName());
        if (downloadingGameID != 0 && downloadingGameID == item.getGameID()) {
            Log.d("test", "test");
            viewHolder.relativeLayoutDownload.setVisibility(View.VISIBLE);
            viewHolder.customTextViewProgress.setText(String.valueOf(progress) + "%");
        }
        ItemGame game = new TableGame(context).get(item.getGameID());
        TableTheme tableTheme = new TableTheme(context);
        if (game.getBasic() == 0 && !tableTheme.isFinishedAllBasicGame(themeID)) {
            viewHolder.imageViewLock.setVisibility(View.VISIBLE);
        }
        return view;
    }


    private class ViewHolder {
        CustomTextView customTextViewProgress;
        ImageButton imageButtonGame;
        ImageView imageViewGame;
        ImageView imageViewLock;
        RelativeLayout relativeLayoutDownload;
        StrokeTextView strokeTextViewTitle;
    }
}