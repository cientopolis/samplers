package org.cientopolis.samplers.ui.samples_list;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.cientopolis.samplers.R;
import org.cientopolis.samplers.framework.Sample;
import java.text.DateFormat;
import java.util.List;


/**
 * Created by Xavier on 27/02/2017.
 * A simple Adapter to show the sample list on a ListView
 */
public class SamplesListAdapter extends BaseAdapter{

    private List<Sample> samples;
    private Context myContext;
    private SamplesListAdapterListener listener;
    private int LIST_ITEM_BACK_COLOR_0;
    private int LIST_ITEM_BACK_COLOR_1;
    private int IMG_SAMPLE_SENT = R.drawable.ic_cloud_done_black_36dp;
    private int IMG_SAMPLE_NOT_SENT = R.drawable.ic_cloud_upload_black_36dp;
    private int IMG_SAMPLE_SENDING = R.drawable.ic_cloud_queue_black_36dp;


    public SamplesListAdapter(Context context, List<Sample> samples, SamplesListAdapterListener listener) {
        this.samples = samples;
        this.myContext = context;
        this.listener = listener;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            LIST_ITEM_BACK_COLOR_0 = context.getResources().getColor(R.color.list_item_back_color_0, context.getTheme());
            LIST_ITEM_BACK_COLOR_1 = context.getResources().getColor(R.color.list_item_back_color_1, context.getTheme());
        }
        else {
            LIST_ITEM_BACK_COLOR_0 = context.getResources().getColor(R.color.list_item_back_color_0);
            LIST_ITEM_BACK_COLOR_1 = context.getResources().getColor(R.color.list_item_back_color_1);
        }

    }

    @Override
    public int getCount() {
        return samples.size();
    }

    @Override
    public Sample getItem(int i) {
        return samples.get(i);
    }

    @Override
    public long getItemId(int i) {
        // not implemented, not needed
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View resultView;
        SamplesListAdapterViewHolder viewHolder;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            resultView =  inflater.inflate(R.layout.list_adapter_samples, parent, false);
            // inflate(int, android.view.ViewGroup, boolean)

            // ViewHolder
            viewHolder = new SamplesListAdapterViewHolder();

            viewHolder.lb_datetime = (TextView) resultView.findViewById(R.id.lb_datetime);
            viewHolder.img_status = (ImageView) resultView.findViewById(R.id.img_status);
            viewHolder.img_status.setImageAlpha(myContext.getResources().getInteger(R.integer.image_buttons_alpha));
            viewHolder.delete_sample = (ImageView) resultView.findViewById(R.id.delete_sample);
            viewHolder.delete_sample.setImageAlpha(myContext.getResources().getInteger(R.integer.image_buttons_alpha));

            resultView.setTag(viewHolder);
        }
        else {
            // Reuse a created view
            resultView = convertView;
            viewHolder = (SamplesListAdapterViewHolder) resultView.getTag();
        }


        if ((position % 2) == 0) {
            resultView.setBackgroundColor(LIST_ITEM_BACK_COLOR_0);
        }
        else {
            resultView.setBackgroundColor(LIST_ITEM_BACK_COLOR_1);
        }


        Sample sample = samples.get(position);

        if (sample.isSent()) {
            viewHolder.img_status.setImageResource(IMG_SAMPLE_SENT);
            viewHolder.img_status.setOnClickListener(null);
        }
        else {
            viewHolder.img_status.setImageResource(IMG_SAMPLE_NOT_SENT);
            viewHolder.img_status.setOnClickListener(new UploadSampleClickListener(sample,viewHolder.img_status));
        }

        viewHolder.lb_datetime.setText(DateFormat.getDateTimeInstance().format(sample.getStartDateTime()));

        viewHolder.delete_sample.setOnClickListener(new DeleteSampleClickListener(sample));

        return resultView;
    }

    private static class SamplesListAdapterViewHolder {
        TextView lb_datetime;
        ImageView img_status;
        ImageView delete_sample;
    }

    private class UploadSampleClickListener implements  View.OnClickListener{

        private Sample mySample;
        private ImageView myImg_status;

        public UploadSampleClickListener(Sample sample, ImageView img_status) {
            mySample = sample;
            myImg_status = img_status;
        }

        @Override
        public void onClick(View view) {
            myImg_status.setImageResource(IMG_SAMPLE_SENDING);
            myImg_status.setOnClickListener(null);
            myImg_status = null;
            listener.onUploadSampleClick(mySample);
        }
    }


    private class DeleteSampleClickListener implements  View.OnClickListener{

        private Sample mySample;

        public DeleteSampleClickListener(Sample sample) {
            mySample = sample;
        }

        @Override
        public void onClick(View v) {
            listener.onDeleteSampleClick(mySample);
        }
    }

    public interface SamplesListAdapterListener {

        public void onUploadSampleClick(Sample sample);

        public void onDeleteSampleClick(Sample mySample);
    }

}


