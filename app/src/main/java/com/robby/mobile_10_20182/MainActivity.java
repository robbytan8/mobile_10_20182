package com.robby.mobile_10_20182;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.robby.mobile_10_20182.fragment.DepartmentFragment;
import com.robby.mobile_10_20182.fragment.PersonFragment;
import com.robby.mobile_10_20182.fragment.StudentFragment;

import butterknife.ButterKnife;

/**
 * @author Robby Tan
 */
public class MainActivity extends AppCompatActivity {

    private DepartmentFragment departmentFragment;
    private PersonFragment personFragment;
    private StudentFragment studentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        changeFragment(getPersonFragment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menus, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mn_person:
                changeFragment(getPersonFragment());
                return true;
            case R.id.mn_department:
                changeFragment(getDepartmentFragment());
                return true;
            case R.id.mn_student:
                changeFragment(getStudentFragment());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fl_container, fragment);
        transaction.commit();
    }

    private DepartmentFragment getDepartmentFragment() {
        if (departmentFragment == null) {
            departmentFragment = new DepartmentFragment();
        }
        return departmentFragment;
    }

    private PersonFragment getPersonFragment() {
        if (personFragment == null) {
            personFragment = new PersonFragment();
        }
        return personFragment;
    }

    private StudentFragment getStudentFragment() {
        if (studentFragment == null) {
            studentFragment = new StudentFragment();
        }
        return studentFragment;
    }
}
