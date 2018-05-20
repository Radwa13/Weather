package com.example.alfa.weather;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.alfa.weather.model.Example;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.alfa.weather.data.DBHelper;

/**
 * Created by Alfa on 5/9/2018.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.BakingViewHolder> {
    private ArrayList<Example> mWeatherList;
    private Context mContext;
    ImageView imageView;
    final private ListItemClickListner mClickHandler;

    public WeatherAdapter(ListItemClickListner listItemClickListner) {
        mClickHandler = listItemClickListner;
    }

    public interface ListItemClickListner {
        void onClick(int position);
    }


    @Override
    public BakingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int listLayoutId = R.layout.card_view_item;
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(listLayoutId, parent, false);
        ButterKnife.bind(parent.getContext(), view);
        return new BakingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BakingViewHolder holder, int position) {
        if (!mWeatherList.get(position).getName().equals("") ) {
            String cityCountry=(mWeatherList.get(position).getName() + ", " + mWeatherList.get(position).getSys().getCountry());
            holder.mTextViewName.setText(cityCountry);
        } else {
            holder.mTextViewName.setText(mContext.getString(R.string.no_city));
        }

       //from  Kelvin to celsius
        int celsiusMin = (int) (mWeatherList.get(position).getMain().getTempMin() - 273.15F);
        int celsiusMax = (int) (mWeatherList.get(position).getMain().getTempMax() - 273.15F);

        holder.mTextViewMin.setText(String.valueOf(celsiusMin));
        holder.mTextViewMax.setText( String.valueOf(celsiusMax));
        holder.mTextViewPre.setText(String.valueOf(mWeatherList.get(position).getMain().getPressure()));
        holder.mTextViewHum.setText(String.valueOf(mWeatherList.get(position).getMain().getHumidity()));
        if (mWeatherList.get(position).getWeather().size() > 0) {
            holder.mTextViewDes.setText(mWeatherList.get(position).getWeather().get(0).getDescription());
    Picasso.with(mContext).load("http://openweathermap.org/img/w/" + mWeatherList.get(position).getWeather().get(0).getIcon() + ".png").into(holder.weatherIcon);
        }
        holder.delete.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                showPopupMenu(view, mWeatherList.get(position).getId(), position);

            }
        });

    }

    private void showPopupMenu(View view, int id, int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_main, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(mContext, id, position));
        popup.show();


    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        Context mContext;
        int mPosition;
        int mId;

        public MyMenuItemClickListener(Context context, int id, int position) {
            mContext = context;
            mPosition = position;
            mId = id;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.delete:

                    DBHelper dbHelper = new DBHelper(mContext);
                    dbHelper.deleteCity(mId);
                    mClickHandler.onClick(mPosition);
                    return true;

                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return mWeatherList.size();
    }

    public class BakingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.name)
        TextView mTextViewName;
        @BindView(R.id.minTemp)
        TextView mTextViewMin;
        @BindView(R.id.maxTemp)
        TextView mTextViewMax;
        @BindView(R.id.pressure)
        TextView mTextViewPre;
        @BindView(R.id.humidity)
        TextView mTextViewHum;
        @BindView(R.id.desc)
        TextView mTextViewDes;
        @BindView(R.id.delete)
        ImageView delete;
        @BindView(R.id.weatherIcon)
        ImageView weatherIcon;

        public BakingViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);

        }

        @Override
        public void onClick(View view) {
            int postion = getAdapterPosition();
        }

    }

    public void loadData(ArrayList<Example> bakingList) {
        this.mWeatherList = bakingList;

    }

}
