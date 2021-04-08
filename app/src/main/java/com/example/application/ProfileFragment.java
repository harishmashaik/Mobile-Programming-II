package com.example.application;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class ProfileFragment extends Fragment {

    TextView edt_email,edt_name,txt_gender,edt_location,txt_date_of_birthday;

    FirebaseUser user;

    public ProfileFragment(FirebaseUser firebaseUser) {
        // Required empty public constructor
        this.user = firebaseUser;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edt_email = view.findViewById(R.id.edt_register_email);
        edt_name = view.findViewById(R.id.edt_register_FullName);
        edt_location = view.findViewById(R.id.edt_register_Location);
        txt_gender = view.findViewById(R.id.txt_gender);
        txt_date_of_birthday = view.findViewById(R.id.txt_date_of_birthday);

        edt_email.setText("Email:" + user.getEmail() );
        edt_name.setText("Name: " + user.getDisplayName());
        txt_gender.setText("Gender: ");
        edt_location.setText("Location: ");
        txt_date_of_birthday.setText("DOB: ");
    }
}