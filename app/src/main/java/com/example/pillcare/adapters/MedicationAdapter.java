package com.example.pillcare.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.example.pillcare.R;
import com.example.pillcare.models.Medication;
import com.example.pillcare.utils.DateTimeUtils;

import java.util.List;

public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.MedicationViewHolder> {

    private List<Medication> medications;
    private OnMedicationClickListener onMedicationClickListener;
    private OnTakeButtonClickListener onTakeButtonClickListener;

    public interface OnMedicationClickListener {
        void onMedicationClick(Medication medication);
    }

    public interface OnTakeButtonClickListener {
        void onTakeButtonClick(Medication medication);
    }

    public MedicationAdapter(List<Medication> medications, 
                           OnMedicationClickListener onMedicationClickListener,
                           OnTakeButtonClickListener onTakeButtonClickListener) {
        this.medications = medications;
        this.onMedicationClickListener = onMedicationClickListener;
        this.onTakeButtonClickListener = onTakeButtonClickListener;
    }

    @NonNull
    @Override
    public MedicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medication, parent, false);
        return new MedicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationViewHolder holder, int position) {
        Medication medication = medications.get(position);
        holder.bind(medication);
    }

    @Override
    public int getItemCount() {
        return medications.size();
    }

    public void updateMedications(List<Medication> newMedications) {
        this.medications = newMedications;
        notifyDataSetChanged();
    }

    class MedicationViewHolder extends RecyclerView.ViewHolder {
        private TextView medicationName;
        private TextView medicationDosage;
        private TextView nextDoseTime;
        private View colorIndicator;
        private View statusIndicator;
        private MaterialButton takeButton;

        public MedicationViewHolder(@NonNull View itemView) {
            super(itemView);
            medicationName = itemView.findViewById(R.id.medication_name);
            medicationDosage = itemView.findViewById(R.id.medication_dosage);
            nextDoseTime = itemView.findViewById(R.id.next_dose_time);
            colorIndicator = itemView.findViewById(R.id.color_indicator);
            statusIndicator = itemView.findViewById(R.id.status_indicator);
            takeButton = itemView.findViewById(R.id.take_button);
        }

        public void bind(Medication medication) {
            medicationName.setText(medication.getName());
            medicationDosage.setText(medication.getDosage());
            
            // Set color indicator
            try {
                int color = Color.parseColor(medication.getColor());
                colorIndicator.setBackgroundColor(color);
            } catch (Exception e) {
                colorIndicator.setBackgroundColor(Color.parseColor("#2196F3"));
            }
            
            // Set next dose time
            if (medication.getTimes() != null && !medication.getTimes().isEmpty()) {
                String nextTime = getNextDoseTime(medication);
                nextDoseTime.setText(nextTime);
            } else {
                nextDoseTime.setText("No times set");
            }

            // Set click listeners
            itemView.setOnClickListener(v -> {
                if (onMedicationClickListener != null) {
                    onMedicationClickListener.onMedicationClick(medication);
                }
            });

            takeButton.setOnClickListener(v -> {
                if (onTakeButtonClickListener != null) {
                    onTakeButtonClickListener.onTakeButtonClick(medication);
                }
            });
        }

        private String getNextDoseTime(Medication medication) {
            // This is a simplified implementation
            // In a real app, you would calculate the actual next dose time
            // based on current time and medication schedule
            if (medication.getTimes() != null && !medication.getTimes().isEmpty()) {
                return medication.getTimes().get(0);
            }
            return "Not scheduled";
        }
    }
}