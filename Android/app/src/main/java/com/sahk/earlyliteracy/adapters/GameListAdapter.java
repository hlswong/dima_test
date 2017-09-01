package com.sahk.earlyliteracy.adapters;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.sahk.earlyliteracy.listeners.OnItemClickListener;
import com.sahk.earlyliteracy.widgets.StrokeTextView;

public class GameListAdapter extends PagerAdapter {

    private Context context;
    private ImageButton imageButtonGame;
    private ImageView imageViewGame;
    private ImageView imageViewLock;
    private int gameID;
    private int progress;
    private int themeID;
    private LayoutInflater layoutInflater;
    private List<ItemGame> gameList;
    private List<ItemTheme> themeList;
    private OnItemClickListener onItemClickListener;
    private StrokeTextView strokeTextViewTitle;
    private TableGame tableGame;

    public GameListAdapter(Context context, int themeID) {
        this.context = context;
        this.themeID = themeID;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        tableGame = new TableGame(context);
        TableTheme tableTheme = new TableTheme(context);
        gameList = tableGame.getAll();
        themeList = tableTheme.getThemeGame(themeID);
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, final int position) {
        View view = layoutInflater.inflate(R.layout.featurecoverflow_game, viewGroup, false);
        TableGame tableGame = new TableGame(context);
        TableTheme tableTheme = new TableTheme(context);
        setView(view);
        setTheme();
        AssetManager assetManager = context.getAssets();
        try {
            imageViewGame.setImageBitmap(BitmapFactory.decodeStream(assetManager.open("image/theme" + themeID + "/game" + (position + 1) + ".png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (gameList.get(position).getBasic() != 1 && !tableTheme.isFinishedAllBasicGame(themeID)) {
            imageViewLock.setVisibility(View.VISIBLE);
        } else {
            imageButtonGame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(imageButtonGame, position);
                    }
                }
            });
        }
        strokeTextViewTitle.setText(themeList.get(position).getGameName());
        viewGroup.addView(view);
        return view;
    }

    private void setView(View view) {
        imageButtonGame = (ImageButton) view.findViewById(R.id.imageButtonGame);
        imageButtonGame.setContentDescription("測試測試1");
        imageViewGame = (ImageView) view.findViewById(R.id.imageViewGame);
        imageButtonGame.setContentDescription("測試測試2");
        imageViewLock = (ImageView) view.findViewById(R.id.imageViewLock);
        imageButtonGame.setContentDescription("測試測試3");
        strokeTextViewTitle = (StrokeTextView) view.findViewById(R.id.strokeTextViewTitle);
        imageButtonGame.setContentDescription("測試測試4");
    }

    private void setTheme() {
        imageButtonGame.setImageResource(context.getResources().getIdentifier("t" + themeID + "_menu", "drawable", context.getPackageName()));
    }

    public void setItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getCount() {
        return gameList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(ViewGroup viewGroup, int position, Object object) {
        viewGroup.removeView((RelativeLayout) object);
    }

    public void showDownloadProgress(int gameID, int progress) {
        this.gameID = gameID;
        this.progress = progress;
    }

    public void finishDownloadProgress(int gameID) {
        this.gameID = gameID;
    }
}