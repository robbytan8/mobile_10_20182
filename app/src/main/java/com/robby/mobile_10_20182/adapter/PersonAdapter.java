package com.robby.mobile_10_20182.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.robby.mobile_10_20182.R;
import com.robby.mobile_10_20182.entity.Person;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Robby Tan
 */
public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {

    private ArrayList<Person> persons;

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row, viewGroup, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder personViewHolder, int i) {
        Person person = getPersons().get(i);
        personViewHolder.showPersonInfo(person);
    }

    @Override
    public int getItemCount() {
        return getPersons().size();
    }

    public ArrayList<Person> getPersons() {
        if (persons == null) {
            persons = new ArrayList<>();
        }
        return persons;
    }

    class PersonViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_full_name)
        TextView txtFullName;

        PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void showPersonInfo(Person person) {
            txtFullName.setText(person.getFirstName() + " " + person.getLastName());
        }
    }
}
