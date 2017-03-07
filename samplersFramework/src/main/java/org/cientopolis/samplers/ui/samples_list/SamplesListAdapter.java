package org.cientopolis.samplers.ui.samples_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.cientopolis.samplers.R;
import org.cientopolis.samplers.model.Sample;
import java.util.List;

/**
 * Created by Xavier on 27/02/2017.
 */

public class SamplesListAdapter extends BaseAdapter{

    private List<Sample> samples;
    private Context myContext;

    public SamplesListAdapter(Context context, List<Sample> samples) {
        this.samples = samples;
        this.myContext = context;
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

            viewHolder.lb_id = (TextView) resultView.findViewById(R.id.lb_id);
            viewHolder.lb_datetime = (TextView) resultView.findViewById(R.id.lb_datetime);
            viewHolder.img_status = (ImageView) resultView.findViewById(R.id.img_status);

            resultView.setTag(viewHolder);
        }
        else {
            // Reusa una View ya creada
            resultView = convertView;
            viewHolder = (SamplesListAdapterViewHolder) resultView.getTag();
        }

        // // TODO: 27/02/2017 Add code to see prety list
        /*
        if ((position % 2) == 0) {
            resultView.setBackgroundColor(COLOR_RENGLON_0);
        }
        else {
            resultView.setBackgroundColor(COLOR_RENGLON_1);
        }
        */

        viewHolder.lb_id.setText(String.valueOf(samples.get(position).getId()));
        // // TODO: 27/02/2017 See date/time formats to display
        viewHolder.lb_datetime.setText(samples.get(position).getStartDateTime().toString());

        /*
        if (fechas.get(position).isPagadoOK()) {
            viewHolder.img_ok.setImageResource(R.drawable.ic_check_green_36dp);
        }
        else {
            viewHolder.img_ok.setImageResource(R.drawable.ic_clear_red_36dp);
        }

        if (fechas.get(position).isPagadoFueraTermino()) {
            viewHolder.lb_fecha.setTextColor(COLOR_TEXTO_PAGO_FUERA_TERMINO);
        }
        else {
            viewHolder.lb_fecha.setTextColor(COLOR_TEXTO_PAGO_OK);
        }
        */

        return resultView;
    }

    private static class SamplesListAdapterViewHolder {
        TextView lb_id;
        TextView lb_datetime;
        ImageView img_status;
    }
}
