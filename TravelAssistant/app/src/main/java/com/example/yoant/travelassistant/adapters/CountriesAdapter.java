package com.example.yoant.travelassistant.adapters;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;
import com.example.yoant.travelassistant.R;
import com.example.yoant.travelassistant.models.Country;
import com.example.yoant.travelassistant.svg_loader.SvgDecoder;
import com.example.yoant.travelassistant.svg_loader.SvgDrawableTranscoder;
import com.example.yoant.travelassistant.svg_loader.SvgSoftwareLayerSetter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class CountriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Country> mCountries;
    private Context mContext;
    private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder;

    public CountriesAdapter(ArrayList<Country> pCountries) {
        mCountries = pCountries;
    }

    public CountriesAdapter(Context pContext) {
        super();
        mContext = pContext;
        mCountries = new ArrayList<>();
        requestBuilder = Glide.with(mContext)
                .using(Glide.buildStreamModelLoader(Uri.class, mContext), InputStream.class)
                .from(Uri.class)
                .as(SVG.class)
                .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<SVG>(new SvgDecoder()))
                .decoder(new SvgDecoder())
                .placeholder(R.drawable.ic_menu_camera)
                .error(R.drawable.ic_error)
                .animate(android.R.anim.fade_in)
                .listener(new SvgSoftwareLayerSetter<Uri>());
    }

    public void addData(List<Country> country) {
        for (Country c : country)
            mCountries.add(c);
        notifyDataSetChanged();
    }

    public void clear() {
        mCountries.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_representation, parent, false);
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder pHolder, int position) {
        CountryViewHolder hholder = (CountryViewHolder) pHolder;
        Country country = mCountries.get(position);
        Log.d("Loading image : = ", country.getFlag());
        requestBuilder.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .load(Uri.parse(country.getFlag()))
                .into(hholder.imageView);
        hholder.textView.setText(country.getName());
        hholder.textViewCapital.setText(country.getCapital());
    }

    @Override
    public int getItemCount() {
        return mCountries.size();
    }

    public class CountryViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public TextView textViewCapital;
        public ImageView imageView;

        public CountryViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.country_name);
            textViewCapital = (TextView) itemView.findViewById(R.id.country_capital);
            imageView = (ImageView) itemView.findViewById(R.id.country_image);
        }
    }
}
