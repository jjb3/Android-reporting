package edu.gatech.reporter.beacons;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.gatech.reporter.R;

public class BeaconAdapter extends RecyclerView.Adapter<BeaconAdapter.MyViewHolder> {

    private ArrayList<String> institutions;
    private SparseBooleanArray checkboxStatus;

    public BeaconAdapter( ArrayList<String> listOfInstitutions, SparseBooleanArray chkboxStatus) {
        this.institutions = listOfInstitutions;
        this.checkboxStatus = chkboxStatus;

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

        holder.checkBox.setText(institutions.get(position));
        holder.checkBox.setSelected(checkboxStatus.get(position));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean newStatus = !holder.checkBox.isChecked();
                holder.checkBox.setChecked(newStatus);
                checkboxStatus.append(position, newStatus);
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
