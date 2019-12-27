package com.example.rentabike.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentabike.R;
import com.example.rentabike.database.entity.RentEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Permet de gérer la liste de réservation avec les actions qui se passent sur les boutons qui sont dans chaque item réservation
 * onEditClick = éditer réservation
 * onDeleteClick = supprimer réservation
 * showBikeRented = montrer le vélo louer et permettre de le louer encore une fois car on fait que 24h de location
 */

public class RentAdapter extends RecyclerView.Adapter<RentAdapter.RentHolder> implements Filterable {

    private OnItemClickListener listener;
    private List<RentEntity> rentEntities = new ArrayList<>();
    private List<RentEntity> rentEntitiesAll;

    @NonNull
    @Override
    public RentAdapter.RentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rent_item, parent, false);

        return new RentAdapter.RentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RentAdapter.RentHolder holder, int position) {
        RentEntity currentRent = rentEntities.get(position);

        holder.textViewFirstName.setText(currentRent.getFirstNameClient());
        holder.textViewLastName.setText(currentRent.getLastNameClient());
        holder.textViewNumero.setText(currentRent.getClientNumber());
        holder.textViewDate.setText(String.valueOf(currentRent.getDateRent()));
        holder.textViewRentid.setText(String.valueOf(currentRent.getRentId()));
    }


    @Override
    public int getItemCount() {
        return rentEntities.size();
    }

    public void setrentEntities(List<RentEntity> rentEntities) {
        this.rentEntities = rentEntities;
        this.rentEntitiesAll = new ArrayList<>(rentEntities);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<RentEntity> filteredList = new ArrayList<>();
            if (constraint.toString().isEmpty()) {

                filteredList.addAll(rentEntitiesAll);
            } else {
                for (RentEntity rentEntity : rentEntitiesAll) {
                    if (rentEntity.getLastNameClient().toLowerCase().contains(constraint.toString().toLowerCase()) || rentEntity.getFirstNameClient().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredList.add(rentEntity);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            rentEntities.clear();
            rentEntities.addAll((Collection<? extends RentEntity>) results.values);
            notifyDataSetChanged();
        }
    };

    class RentHolder extends RecyclerView.ViewHolder {
        private TextView textViewFirstName;
        private TextView textViewLastName;
        private TextView textViewDate;
        private TextView textViewNumero;
        private TextView textViewRentid;
        private ImageButton buttonDeleteRent;
        private ImageButton buttonEditRent;
        private Button buttonShowBike;


        public RentHolder(View itemView) {
            super(itemView);
            textViewFirstName = itemView.findViewById(R.id.text_view_firstname);
            textViewLastName = itemView.findViewById(R.id.text_view_lastname);
            textViewDate = itemView.findViewById(R.id.text_view_date);
            textViewNumero = itemView.findViewById(R.id.text_view_number);
            textViewRentid = itemView.findViewById(R.id.text_view_rentid);


            buttonEditRent = itemView.findViewById(R.id.button_edit_rent);
            buttonEditRent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    if (listener != null & position != RecyclerView.NO_POSITION) {
                        listener.onEditClick(rentEntities.get(position));
                    }
                }
            });

            buttonDeleteRent = itemView.findViewById(R.id.button_delete_rent);
            buttonDeleteRent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null & position != RecyclerView.NO_POSITION) {
                        listener.onDeleteClick(rentEntities.get(position));
                    }
                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null & position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(rentEntities.get(position));
                    }
                }
            });
        }
    }


    public interface OnItemClickListener {
        void onItemClick(RentEntity rent);

        void onEditClick(RentEntity rent);

        void onDeleteClick(RentEntity rent);
    }

    public void setOnItemClickListener(RentAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}

