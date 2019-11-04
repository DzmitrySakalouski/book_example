package com.example.crimeintent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static com.example.crimeintent.CrimeLab.*;

interface RowType {
    int ROW_TYPE_POLICE = 0;
    int ROW_TYPE_CRIME = 1;
}

public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private static final int REQUEST_CRIME = 1;
    private int positionId;

    public class CrimeHolder extends RecyclerView.ViewHolder {

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    public class PoliceCrimeHolder extends RecyclerView.ViewHolder {
        public PoliceCrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_police, parent, false));
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter {
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            RecyclerView.ViewHolder viewHolder;

            if (viewType == RowType.ROW_TYPE_CRIME) {
                viewHolder = new PoliceCrimeHolder(inflater, parent);
            } else {
                viewHolder = new CrimeHolder(inflater, parent);
            }

            return viewHolder;
        }

        @Override
        public int getItemViewType(int position) {
            if (mCrimes.get(position).isRequiresPolice()) {
                return RowType.ROW_TYPE_POLICE;
            } else {
                return  RowType.ROW_TYPE_CRIME;
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
            final Crime crime = mCrimes.get(position);

            TextView titleTextView = (TextView) holder.itemView.findViewById(R.id.crime_title_item);
            titleTextView.setText(crime.getTitle());

            TextView dateTextView = (TextView) holder.itemView.findViewById(R.id.crime_date_item);
            CharSequence date = DateFormat.format("EEEE, MMM d, yyyy", crime.getDate());
            dateTextView.setText(date);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    positionId = position;
                    Intent intent = CrimeActivity.newIntent(getActivity(), crime.getId());
                    startActivityForResult(intent, REQUEST_CRIME);
                }
            });
        }

        @Override
        public int getItemCount() {
//            Log.d("ffff", "getItemCount: INVOKED - " + mCrimes.size());
            return mCrimes.size();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return view;
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get();
        List<Crime> crimes = crimeLab.getCrimes();

        if(mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyItemChanged(positionId);
        }

    }
}
