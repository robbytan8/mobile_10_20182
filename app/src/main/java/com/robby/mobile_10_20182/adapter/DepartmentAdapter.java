package com.robby.mobile_10_20182.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.robby.mobile_10_20182.R;
import com.robby.mobile_10_20182.entity.Department;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Robby Tan
 */
public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.DepartmentViewHolder> {

    private ArrayList<Department> departments;

    @NonNull
    @Override
    public DepartmentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row, viewGroup, false);
        return new DepartmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentViewHolder personViewHolder, int i) {
        Department department = getDepartments().get(i);
        personViewHolder.showPersonInfo(department);
    }

    @Override
    public int getItemCount() {
        return getDepartments().size();
    }

    public ArrayList<Department> getDepartments() {
        if (departments == null) {
            departments = new ArrayList<>();
        }
        return departments;
    }

    class DepartmentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_full_name)
        TextView txtFullName;

        DepartmentViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void showPersonInfo(Department department) {
            txtFullName.setText(department.getName());
        }
    }
}
