package tn.codeit.restopic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MesPhotosAdapter extends BaseAdapter {

    private Context context;
    private String[] Picture;
    private String[] Date;

    public MesPhotosAdapter(Context contxt, String[] picture , String[] date)
    {
        context = contxt;
        Picture = picture;
        Date = date;
    }
    public int getCount() {
        if(Picture != null)
            return Picture.length;
        else
            return 0;
    }
    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ImageView imageView;
        TextView textView ;

        LinearLayout layout = new LinearLayout(context);
        GridView.LayoutParams paramsLayout = new GridView.LayoutParams((int) context.getResources().getDimension(R.dimen._135sdp) , (int) context.getResources().getDimension(R.dimen._107sdp));
        layout.setLayoutParams(paramsLayout);
        layout.setBackgroundColor(Color.WHITE);
        layout.setOrientation(LinearLayout.VERTICAL);

        int pad_layout = (int) context.getResources().getDimension(R.dimen._5sdp) ;
        layout.setPadding(pad_layout, pad_layout, pad_layout, pad_layout);

        imageView = new ImageView(context);
        LinearLayout.LayoutParams params_Image = new LinearLayout.LayoutParams((int) context.getResources().getDimension(R.dimen._125sdp) , (int) context.getResources().getDimension(R.dimen._80sdp));
        imageView.setLayoutParams(params_Image);

        textView= new TextView(context);
        LinearLayout.LayoutParams params_Text = new LinearLayout.LayoutParams((int) context.getResources().getDimension(R.dimen._110sdp) , (int) context.getResources().getDimension(R.dimen._17sdp));
        textView.setLayoutParams(params_Text);
        textView.setTextColor(Color.parseColor("#FF5722"));
        textView.setTextSize((int) context.getResources().getDimension(R.dimen._5sdp));
        textView.setText(Date[position]);

        Picasso.with(this.context).load(Picture[position]).fit().into(imageView, new Callback() {
            @Override
            public void onSuccess() {
            }
            @Override
            public void onError() {
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("start.fragment.action");
                intent.putExtra("url",Picture[position] );
                context.sendBroadcast(intent);
            }
        });

        layout.addView(textView);
        layout.addView(imageView);

        return layout;
    }
}

