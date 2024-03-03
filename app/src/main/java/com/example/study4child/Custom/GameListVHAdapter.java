package com.example.study4child.Custom;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.study4child.Activities.GameListActivity;
import com.example.study4child.R;
import com.example.study4child.Tools.GameListSelectInterface;
import com.example.study4child.Tools.GamePreviewData;
import com.example.study4child.Tools.ImageStorageLoader;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GameListVHAdapter extends RecyclerView.Adapter<PagerViewHolder> {

    private ArrayList<GamePreviewData> data_list = new ArrayList<>();
    private Context ctx;
    private GameListSelectInterface listener;

    private GamePreviewData emptyData;

    public GameListVHAdapter(Context _ctx, GameListSelectInterface _listener) {
        ctx = _ctx;
        listener = _listener;

        emptyData = new GamePreviewData();
        emptyData.title = "empty";
        emptyData.image = ImageStorageLoader.emptyImage(ctx);
    }

    public void addDataItem(GamePreviewData item) {
        data_list.add(item);
    }

    @NonNull
    @NotNull
    @Override
    public PagerViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new PagerViewHolder(new GameListCard(ctx, emptyData));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PagerViewHolder holder, int position) {
        GamePreviewData item = data_list.get(position);

        ((ImageView)holder.itemView.findViewById(R.id.card_image)).setImageBitmap(item.image);
        ((TextView)holder.itemView.findViewById(R.id.card_title)).setText(item.title);
        (holder.itemView.findViewById(R.id.card_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onGameSelected(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data_list.size();
    }
}
