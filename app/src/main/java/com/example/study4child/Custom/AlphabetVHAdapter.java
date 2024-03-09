package com.example.study4child.Custom;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.study4child.Activities.AlphabetActivity;
import com.example.study4child.R;
import com.example.study4child.Tools.AlphabetData;
import com.example.study4child.Tools.GameListSelectInterface;
import com.example.study4child.Tools.GamePreviewData;
import com.example.study4child.Tools.ImageStorageLoader;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AlphabetVHAdapter extends RecyclerView.Adapter<PagerViewHolder> {

    private ArrayList<AlphabetData> data_list = new ArrayList<>();
    private Context ctx;
    private AlphabetActivity listener;

    private AlphabetData emptyData;

    public AlphabetVHAdapter(Context _ctx, AlphabetActivity _listener) {
        ctx = _ctx;
        listener = _listener;

        emptyData = new AlphabetData(ctx, null, listener);
        emptyData.title = "empty";
        emptyData.image = ImageStorageLoader.emptyImage(ctx);
    }

    public void addDataItem(AlphabetData item) {
        data_list.add(item);
    }
    public void removeDataItem(int index) {
        data_list.remove(index);
    }

    @NonNull
    @NotNull
    @Override
    public PagerViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new PagerViewHolder(new AlphabetCard(ctx, emptyData));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PagerViewHolder holder, int position) {
        AlphabetData item = data_list.get(position);
        ImageView image = holder.itemView.findViewById(R.id.card_image);

        image.setImageBitmap(item.image);
        image.setOnClickListener(listener::onCardClicked);

        TextView title = holder.itemView.findViewById(R.id.card_title);
        String text = "Буква "+item.letter+" - "+item.title;
        title.setText(text);
    }



    @Override
    public int getItemCount() {
        return data_list.size();
    }
}
