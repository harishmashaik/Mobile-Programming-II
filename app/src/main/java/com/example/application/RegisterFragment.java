package com.example.application;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class RegisterFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    EditText edt_email,edt_pass,edt_cpass,edt_name,edt_location;
    TextView txt_date_of_birthday;
    Button btn_register;
    RadioButton radio_male, radio_female;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private NavController navController;

    private DatePickerDialog datePickerDialog;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        datePickerDialog = new DatePickerDialog(
                getActivity(), this, 1900, 0, 1);

        edt_email = view.findViewById(R.id.edt_register_email);
        edt_pass = view.findViewById(R.id.edt_register_password);
        edt_cpass = view.findViewById(R.id.edt_register_Cpassword);
        edt_name = view.findViewById(R.id.edt_register_FullName);
        edt_location = view.findViewById(R.id.edt_register_Location);
        btn_register = view.findViewById(R.id.btn_Register);
        txt_date_of_birthday = view.findViewById(R.id.txt_date_of_birthday);

        radio_male = view.findViewById(R.id.radio_male);
        radio_female = view.findViewById(R.id.radio_female);

        navController = Navigation.findNavController(getActivity(),R.id.host_fragment);

        btn_register.setOnClickListener(view1 -> {

            if (!checkEmptyFields())
            {
                if (edt_pass.getText().length()<6)
                {
                    edt_pass.setError("Invalid Password, Password Should be at least 6 characters");
                    edt_pass.requestFocus();
                }else {
                    if (!edt_pass.getText().toString().equals(edt_cpass.getText().toString()))
                    {
                        edt_cpass.setError("Password not match!");
                        edt_cpass.requestFocus();
                    }else
                    {
                        String email = edt_email.getText().toString();
                        String pass = edt_pass.getText().toString();
                        String name = edt_name.getText().toString();
                        String location = edt_location.getText().toString();
                        String dob = txt_date_of_birthday.getText().toString();

                        if (radio_male.isChecked()) {
                            Person person = new Person(email,pass,name,location,dob,"Male");

                            createUser(person);
                        } else {
                            Person person = new Person(email,pass,name,location,dob,"Female");

                            createUser(person);
                        }

                    }
                }

            }

        });

        txt_date_of_birthday.setOnClickListener(view2 -> {
            datePickerDialog.show();
        });

    }

    public boolean checkEmptyFields()
    {
        if(TextUtils.isEmpty(edt_email.getText().toString()))
        {
            edt_email.setError("Email cannot be empty!");
            edt_email.requestFocus();
            return true;
        }else if (TextUtils.isEmpty(edt_pass.getText().toString()))
        {
            edt_pass.setError("Password cannot be empty!");
            edt_pass.requestFocus();
            return true;
        }else if (TextUtils.isEmpty(edt_cpass.getText().toString()))
        {
            edt_cpass.setError("Confirm Password cannot be empty!");
            edt_cpass.requestFocus();
            return true;
        }else if (TextUtils.isEmpty(edt_name.getText().toString()))
        {
            edt_name.setError("Name cannot be empty!");
            edt_name.requestFocus();
            return true;
        }else if (TextUtils.isEmpty(edt_location.getText().toString()))
        {
            edt_location.setError("Location cannot be empty!");
            edt_location.requestFocus();
            return true;
        }else if (txt_date_of_birthday.getText().toString().equals(getActivity().getString(R.string.date_of_birth))){
            edt_location.setError("Date of Birth cannot be empty!");
            return true;
        }

        return false;
    }

    public void createUser(Person person)
    {
        auth.createUserWithEmailAndPassword(person.getEmail(),person.getPassword())
                .addOnCompleteListener(getActivity(), task -> {

                    if (task.isSuccessful())
                    {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        writeFireStore(person, firebaseUser);
                    }else {
                        Toast.makeText(getActivity().getApplicationContext(),"Registration Error!",Toast.LENGTH_LONG).show();
                    }

                });
    }

    public void writeFireStore(Person person, FirebaseUser firebaseUser)
    {
        Map<String,Object> userMap = new HashMap<>();
        userMap.put("Name",person.getName());
        userMap.put("Email",person.getEmail());
        userMap.put("Location",person.getLocation());



        firestore.collection("User").document(firebaseUser.getUid())
                .set(userMap).addOnCompleteListener(getActivity(), task -> {
            if (task.isSuccessful())
            {
                Toast.makeText(getActivity().getApplicationContext(),"Registration Success!",Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                navController.navigate(R.id.loginFragment);
            }
            else
            {
                Toast.makeText(getActivity().getApplicationContext(),"FireStore Error!",Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        txt_date_of_birthday.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
    }
}