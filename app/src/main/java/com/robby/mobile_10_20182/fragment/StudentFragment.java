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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.robby.mobile_10_20182.R;
import com.robby.mobile_10_20182.adapter.StudentAdapter;
import com.robby.mobile_10_20182.entity.Department;
import com.robby.mobile_10_20182.entity.Student;
import com.robby.mobile_10_20182.util.DBRefName;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Robby Tan
 */
public class StudentFragment extends Fragment {

    @BindView(R.id.ll_root)
    LinearLayout llRoot;
    @BindView(R.id.et_first)
    EditText txtFirstName;
    @BindView(R.id.et_last)
    EditText txtLastName;
    @BindView(R.id.spin_department)
    Spinner spinDepartment;
    @BindView(R.id.rv_data)
    RecyclerView rvData;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    private StudentAdapter studentAdapter;
    private ArrayList<Department> departments;
    private ArrayAdapter<Department> departmentAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateDepartmentData();
        populateStudentData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_fragment, container, false);
        ButterKnife.bind(this, view);
        initComponents();
        return view;
    }

    @OnClick(R.id.btn_submit)
    public void submitAction() {
        if (!txtFirstName.getText().toString().trim().isEmpty() && !txtLastName.getText().toString().trim().isEmpty()) {
            //  Creating an instance of Person class to be inserted into Firebase Database
            Student student = new Student();
            student.setFirstName(txtFirstName.getText().toString().trim());
            student.setLastName(txtLastName.getText().toString().trim());
            student.setDepartment(((Department) spinDepartment.getSelectedItem()).getKey());

//            System.out.println();
            saveFirebaseData(student);

            //  Clear text from field, show information message, refresh RecyclerView data
            txtFirstName.setText("");
            txtLastName.setText("");
            Snackbar.make(llRoot, R.string.success_msg, Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(llRoot, R.string.error_msg_person, Snackbar.LENGTH_LONG).show();
        }
    }

    private ArrayList<Department> getDepartments() {
        if (departments == null) {
            departments = new ArrayList<>();
        }
        return departments;
    }

    private ArrayAdapter<Department> getDepartmentAdapter() {
        if (departmentAdapter == null) {
            departmentAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, getDepartments());
            departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }
        return departmentAdapter;
    }

    private StudentAdapter getStudentAdapter() {
        if (studentAdapter == null) {
            studentAdapter = new StudentAdapter();
        }
        return studentAdapter;
    }

    private void initComponents() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateStudentData();
                refreshLayout.setRefreshing(false);
            }
        });

        spinDepartment.setAdapter(getDepartmentAdapter());

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), manager.getOrientation());
        rvData.setLayoutManager(manager);
        rvData.addItemDecoration(decoration);
        rvData.setAdapter(getStudentAdapter());
    }

    private void saveFirebaseData(Student student) {
        //  Create an instance of FirebaseDatabase and its reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(DBRefName.STUDENT_REF);

        //  Push data to FirebaseDatabase
        myRef.push().setValue(student);
    }

    private void populateStudentData() {
        //  Create an instance of FirebaseDatabase and its reference
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(DBRefName.STUDENT_REF);

        //  Ordering (optional) and show the result
        myRef.orderByChild(DBRefName.PERSON_ATR01).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getStudentAdapter().getStudents().clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    final Student student = childSnapshot.getValue(Student.class);

                    DatabaseReference myRef2 = database.getReference(DBRefName.DEPARTMENT_REF);
                    myRef2.orderByKey().equalTo(student.getDepartment()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot cd : dataSnapshot.getChildren()) {
                                student.setDepartment(cd.getValue(Department.class).getName());
                                getStudentAdapter().getStudents().add(student);
                                getStudentAdapter().notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateDepartmentData() {
        //  Create an instance of FirebaseDatabase and its reference
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(DBRefName.DEPARTMENT_REF);

        //  Ordering (optional) and show the result
        myRef.orderByChild(DBRefName.DEPARTMENT_ATR01).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getDepartments().clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Department department = childSnapshot.getValue(Department.class);
                    department.setKey(childSnapshot.getKey());
                    getDepartments().add(department);
                }
                getDepartmentAdapter().notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
