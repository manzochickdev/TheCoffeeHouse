package xyz.manzodev.thecoffeehouse;

import android.graphics.Typeface;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class BindingAdapter {
    @androidx.databinding.BindingAdapter("setImageUrl")
    public static void setImageUrl(ImageView view , String url){
        if (url!=null && url.trim().length()>0){
            Glide.with(view.getContext()).load(url).into(view);
        }
        else Glide.with(view.getContext()).load(R.mipmap.ic_launcher).into(view);
    }

    @androidx.databinding.BindingAdapter("setFont")
    public static void setFont(TextView view, String path) {
        Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(),path);
        view.setTypeface(typeface);
    }

    @androidx.databinding.BindingAdapter("setFont")
    public static void setFont(EditText view, String path) {
        Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(),path);
        view.setTypeface(typeface);
    }

}
