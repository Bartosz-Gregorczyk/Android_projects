package com.example.projektv3;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {

    private ArrayList<ExampleItem> mExampleList;
    private OnItemClickListener mListener;
    private OnItemLongClickListener mListener2;

    public interface OnItemClickListener {
        void onDeleteClick(int position);
        void onItemClick(int position);
    }
public interface OnItemLongClickListener
{
    void onCallClick(int position);
}

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener listener2) {
        mListener2=listener2;
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public ImageView mImageView;
        public TextView mTextView;
        public ImageView mDeleteImage;
        public Button mAddContact;

        public ExampleViewHolder(View itemView, final OnItemClickListener listener, final OnItemLongClickListener listener2) {
            super(itemView);
            mImageView=itemView.findViewById(R.id.imageView);
            mTextView=itemView.findViewById(R.id.textView);
            mDeleteImage=itemView.findViewById(R.id.image_delete);
            mAddContact=itemView.findViewById(R.id.contact_button_add);

//--------odniesienie do ImageView odpowiedniego elementu w przypadku dlugiego klikniecia-----------
            mImageView.setOnLongClickListener(new View.OnLongClickListener() {
             @Override
              public boolean onLongClick(View v) {
                 if (listener2!=null)
                 {
                     int position = getAdapterPosition();
                     if (position != RecyclerView.NO_POSITION) {
                         listener2.onCallClick(position);
                     }
                 }
               return true;
                    }
                 });
//--------------------------------------------------------------------------------------------------

//--------odniesienie do TextView odpowiedniego elementu w przypadku pojedynczego klikniecia--------
            mTextView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener2!=null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener2.onCallClick(position);
                        }
                    }
                    return true;
                }
            });
//--------------------------------------------------------------------------------------------------

//--------odniesienie do ImageView odpowiedniego elementu w przypadku pojedynczego klikniecia-------

                    mImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listener != null) {
                                int position = getAdapterPosition();
                                if (position != RecyclerView.NO_POSITION) {
                                    listener.onItemClick(position);
                                }
                            }
                        }
                    });
//--------------------------------------------------------------------------------------------------

//------odniesienie do TextView odpowiedniego elementu w przypadku pojedynczego klikniecia---------
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
//--------------------------------------------------------------------------------------------------
//------------------------odniesienie do elementu kosza---------------------------------------------
            mDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
//--------------------------------------------------------------------------------------------------
        }
        @Override
        public void onClick(View v) {}

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    public ExampleAdapter(ArrayList<ExampleItem> exampleList) {
        mExampleList=exampleList;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item,parent,false);
        ExampleViewHolder evh = new ExampleViewHolder(v,mListener, mListener2);
        return evh;
    }
    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        ExampleItem currentItem = mExampleList.get(position);
        holder.mImageView.setImageResource(currentItem.getmImageResource());
        holder.mTextView.setText(currentItem.getmText());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
