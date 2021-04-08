package com.example.application;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class HomeFragment extends Fragment {


    Button btn_signOut,btn_dashboard,btn_profile;
    FirebaseUser user;
    FirebaseFirestore fireStore;
    NavController navController;
    DrawerLayout drawer_layout;
    ImageView imgMenu;

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = getArguments().getParcelable("user");
        fireStore = FirebaseFirestore.getInstance();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_signOut = view.findViewById(R.id.btn_signOut);
        btn_dashboard = view.findViewById(R.id.btn_dashboard);
        btn_profile = view.findViewById(R.id.btn_profile);
        drawer_layout = view.findViewById(R.id.drawer_layout);
        imgMenu = view.findViewById(R.id.imgMenu);

        navController = Navigation.findNavController(getActivity(),R.id.host_fragment);

        readFireStore();

        DashboardFragment dashboardFragment = new DashboardFragment(user);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, dashboardFragment).commit();

        imgMenu.setOnClickListener(view1 -> {
            if (drawer_layout.isDrawerOpen(Gravity.LEFT)) {
                drawer_layout.closeDrawer(Gravity.LEFT);
            } else {
                drawer_layout.openDrawer(Gravity.LEFT);
            }
        });

        btn_signOut.setOnClickListener(view1 -> {
            drawer_layout.closeDrawer(Gravity.LEFT);
            FirebaseAuth.getInstance().signOut();
            navController.navigate(R.id.loginFragment);

        });

        btn_dashboard.setOnClickListener(view1 -> {
            drawer_layout.closeDrawer(Gravity.LEFT);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, dashboardFragment).commit();
        });

        btn_profile.setOnClickListener(view1 -> {
            drawer_layout.closeDrawer(Gravity.LEFT);
            ProfileFragment profileFragment = new ProfileFragment(user);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, profileFragment).commit();
        });
    }

    public void readFireStore()
    {
        DocumentReference docRef = fireStore.collection("User").document(user.getUid());

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                DocumentSnapshot doc = task.getResult();

                if (doc.exists())
                {
                    Log.d("DashboardFragment",doc.getData().toString());

                    //txt_Name.setText("Welcome "+doc.get("Name") + " !");


                }

            }
        });
    }
}