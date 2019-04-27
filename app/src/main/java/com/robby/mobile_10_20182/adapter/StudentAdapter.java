package com.robby.mobile_10_20182.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.robby.mobile_10_20182.R;
import com.robby.mobile_10_20182.entity.Student;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Robby Tan
 */
public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private ArrayList<Student> students;

    @NonNull
    @Override
    public StudentAdapter.StudentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row2, viewGroup, false);
        return new StudentAdapter.StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAdapter.StudentViewHolder personViewHolder, int i) {
        Student student = getStudents().get(i);
        personViewHolder.showStudentInfo(student);
    }

    @Override
    public int getItemCount() {
        return getStudents().size();
    }

    public ArrayList<Student> getStudents() {
        if (students == null) {
            students = new ArrayList<>();
        }
        return students;
    }

    class StudentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_full_name)
        TextView txtFullName;
        @BindView(R.id.tv_department)
        TextView txtDepartment;

        StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void showStudentInfo(Student student) {
            txtFullName.setText(student.getFirstName() + " " + student.getLastName());
            txtDepartment.setText(student.getDepartment());
        }
    }
}
