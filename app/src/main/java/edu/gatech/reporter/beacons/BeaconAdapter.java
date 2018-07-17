package edu.gatech.reporter.beacons;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.gatech.reporter.R;
import edu.gatech.reporter.beacons.Database.BeaconZone;

public class BeaconAdapter extends RecyclerView.Adapter<BeaconAdapter.MyViewHolder> {

    private ArrayList<BeaconZone> institutions;

    public BeaconAdapter( ArrayList<BeaconZone> listOfInstitutions) {
        this.institutions = listOfInstitutions;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.beacon_select_viewholder, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final String zoneName = institutions.get(position).getZoneName();
        holder.checkBox.setText(zoneName);
        holder.checkBox.setChecked(institutions.get(position).isSelected());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean newStatus = holder.checkBox.isChecked();
                holder.checkBox.setChecked(newStatus);
                institutions.get(position).setSelected(newStatus);
            }
        });

    }

    @Override
    public int getItemCount() {
        return institutions.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.beaconChk) CheckBox checkBox;

        // each data item is just a string in this case
        public MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
        }
    }

}
