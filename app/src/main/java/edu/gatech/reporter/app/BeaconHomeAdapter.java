package edu.gatech.reporter.app;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.estimote.proximity_sdk.proximity.ProximityAttachment;


import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.gatech.reporter.R;

import edu.gatech.reporter.utils.Const;

public class BeaconHomeAdapter extends RecyclerView.Adapter<BeaconHomeAdapter.MyViewHolder> {

    private List<? extends ProximityAttachment> nearbyBeacons;

    public BeaconHomeAdapter( List<? extends ProximityAttachment> listOfNearbyBeacons) {
        this.nearbyBeacons = listOfNearbyBeacons;
    }

    @NonNull
    @Override
    public BeaconHomeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.beacon_info_viewholder, parent, false);

        return new BeaconHomeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final BeaconHomeAdapter.MyViewHolder holder, final int position) {
        final String deviceId = nearbyBeacons.get(position).getDeviceId();
        Map<String, String> attch  =nearbyBeacons.get(position).getPayload();
        final String institution = attch.get(Const.BEACON_INSTITUTION_KEY);
        final String busStop = attch.get(Const.BEACON_BUS_STOP_KEY);

        holder.deviceId.setText(deviceId);
        holder.institution.setText(institution);
        holder.location.setText(busStop);

    }

    @Override
    public int getItemCount() {
        return nearbyBeacons.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.beacon_id_text) TextView deviceId;
        @BindView(R.id.beacon_institution_text) TextView institution;
        @BindView(R.id.beacon_location_text) TextView location;

        // each data item is just a string in this case
        public MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
        }
    }

}
