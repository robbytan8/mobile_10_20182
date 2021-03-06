package com.robby.mobile_10_20182.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.robby.mobile_10_20182.R;
import com.robby.mobile_10_20182.adapter.PersonAdapter;
import com.robby.mobile_10_20182.entity.Person;
import com.robby.mobile_10_20182.util.DBRefName;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Robby Tan
 */
public class PersonFragment extends Fragment {

    @BindView(R.id.ll_root)
    LinearLayout llRoot;
    @BindView(R.id.et_first)
    EditText txtFirstName;
    @BindView(R.id.et_last)
    EditText txtLastName;
    @BindView(R.id.rv_data)
    RecyclerView rvData;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    private PersonAdapter personAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populatePersonData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.person_fragment, container, false);
        ButterKnife.bind(this, view);
        initComponents();
        return view;
    }

    @OnClick(R.id.btn_submit)
    public void submitAction() {
        if (!txtFirstName.getText().toString().trim().isEmpty() && !txtLastName.getText().toString().trim().isEmpty()) {
            //  Creating an instance of Person class to be inserted into Firebase Database
            Person person = new Person();
            person.setFirstName(txtFirstName.getText().toString().trim());
            person.setLastName(txtLastName.getText().toString().trim());

            saveFirebaseData(person);

            //  Clear text from field, show information message, refresh RecyclerView data
            txtFirstName.setText("");
            txtLastName.setText("");
            Snackbar.make(llRoot, R.string.success_msg, Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(llRoot, R.string.error_msg_person, Snackbar.LENGTH_LONG).show();
        }
    }

    private PersonAdapter getPersonAdapter() {
        if (personAdapter == null) {
            personAdapter = new PersonAdapter();
        }
        return personAdapter;
    }

    private void initComponents() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populatePersonData();
                refreshLayout.setRefreshing(false);
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), manager.getOrientation());
        rvData.setLayoutManager(manager);
        rvData.addItemDecoration(decoration);
        rvData.setAdapter(getPersonAdapter());
    }

    private void saveFirebaseData(Person person) {
        //  Create an instance of FirebaseDatabase and its reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(DBRefName.PERSON_REF);

        //  Push data to FirebaseDatabase
        myRef.push().setValue(person);
    }

    private void populatePersonData() {
        //  Create an instance of FirebaseDatabase and its reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(DBRefName.PERSON_REF);

        //  Ordering (optional) and show the result
        myRef.orderByChild(DBRefName.PERSON_ATR01).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getPersonAdapter().getPersons().clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    getPersonAdapter().getPersons().add(childSnapshot.getValue(Person.class));
                }
                getPersonAdapter().notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
