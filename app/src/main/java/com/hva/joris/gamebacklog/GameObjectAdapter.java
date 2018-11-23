package com.hva.joris.gamebacklog;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class GameObjectAdapter extends RecyclerView.Adapter<GameObjectAdapter.GameObjectViewHolder>{
    private GameClickListener gameClickListener;
    private List<GameObject> gameObjects;
    private final Resources resources;

    public GameObjectAdapter(List<GameObject> gameObjects, Resources resources, GameClickListener gameClickListener) {
        this.gameObjects = gameObjects;
        this.resources = resources;
        this.gameClickListener = gameClickListener;
    }

    @NonNull
    @Override
    public GameObjectViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.game_card, viewGroup,false);

        return new GameObjectViewHolder(view, gameClickListener);
    }

    public class GameObjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView titleView;
        public TextView platformView;
        public TextView statusView;
        public TextView dateView;
        public GameObjectAdapter.GameClickListener gameClickListener;

        public GameObjectViewHolder(View itemView, GameObjectAdapter.GameClickListener gameClickListener){
            super(itemView);
            itemView.setOnClickListener(this);

            titleView = itemView.findViewById(R.id.titleView);
            platformView = itemView.findViewById(R.id.platformView);
            statusView = itemView.findViewById(R.id.statusView);
            dateView = itemView.findViewById(R.id.dateView);
            this.gameClickListener = gameClickListener;
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            gameClickListener.gameOnClick(position);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull GameObjectViewHolder viewHolder, int i) {
        GameObject gameObject = gameObjects.get(i);

        viewHolder.titleView.setText(gameObject.getTitle());
        viewHolder.platformView.setText(gameObject.getPlatform());
        viewHolder.dateView.setText(gameObject.getLastModified());
        viewHolder.statusView.setText(resources.getStringArray(R.array.status_array)[gameObject.getStatus()]);
    }

    @Override
    public int getItemCount() {
        return gameObjects.size();
    }

    public void swapList(List<GameObject> newList){
        gameObjects = newList;
        if(newList != null){
            this.notifyDataSetChanged();
        }
    }

    interface GameClickListener {
        public void gameOnClick(int i);
    }
}
