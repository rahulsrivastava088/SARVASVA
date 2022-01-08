package com.example.sarvasva.app.fragments;


import static android.content.ContentValues.TAG;

import android.content.Intent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.example.sarvasva.R;

import com.example.sarvasva.app.Classes.HorizontalSliderAdapter;
import com.example.sarvasva.app.Classes.ProductImagesAdaptor;
import com.example.sarvasva.app.activities.MainActivity;
import com.example.sarvasva.app.activities.ProductDetailsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
//=======
import android.widget.ImageView;

//import com.example.sarvasva.R;
//import com.example.sarvasva.app.Classes.HorizontalItemModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class ClubsProfile extends Fragment {

    private FrameLayout parentFrameLayout;
    private RecyclerView photoGalleryRv;
    private Button viewAllGallery;
    private TextView presidentTv , jointPresidentTv, vicePresidentTv, extraPresidentTv , firstHeadTv, secondHeadTv, thirdHeadTv, fourthHeadTv;
    private String president , jointPresident, vicePresident, extraPresident;
    private List<String> posterUrlList;
    private List<String> productImagesList = new ArrayList<>();
    private FirebaseFirestore firestore;
    private ViewPager pager;
    private ViewPager posterViewPager;
    private List<String> arrangeList;
    private int currentPage;
    private Timer timer ;
    final static  int DELAY_TIME =2000, PERIOD_TIME =2000;



    FragmentTransaction fragmentTransaction;
    ImageView Settings;

    public ClubsProfile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_club_profile, container, false);

        photoGalleryRv = view.findViewById(R.id.horizontalScrollRecycleView);
        jointPresidentTv = view.findViewById(R.id.club_joint_president);
        presidentTv = view.findViewById(R.id.club_president);
        vicePresidentTv = view.findViewById(R.id.club_vice_president);

        CardView card = view.findViewById(R.id.edc_vicepresident2);
        card.setVisibility(View.INVISIBLE);
        viewAllGallery = view.findViewById(R.id.viewAllPhotoBtn);
        Settings = view.findViewById(R.id.club_settings);
        Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new president_edit());
            }
        });
        viewAllGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProductDetailsActivity.class);
                intent.putExtra("gallery_reference" , "edc" );
                startActivity(intent);
            }
        });
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productImagesList = new ArrayList<>();

        firestore = FirebaseFirestore.getInstance();

        //gallary image set

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.HORIZONTAL);
        photoGalleryRv.setLayoutManager(manager);



        //end gallary imag set

        FirebaseFirestore.getInstance().collection("CLUB_PROFILE").document(
                "EDC")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {    DocumentSnapshot shot = task.getResult();
                    if (shot.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + shot.getData());


                        productImagesList = (List<String>) shot.get("edc");
                        president = (String)shot.get("PRESIDENT");
                        jointPresident = (String)shot.get("JOINT PRESIDENT");
                        vicePresident = (String)shot.get("VICE PRESIDENT");

                        //setting the data

                        presidentTv.setText(president);
                        vicePresidentTv.setText(vicePresident);
                        jointPresidentTv.setText(jointPresident);

                        //end setting data

                        //setting adapter

                        HorizontalSliderAdapter homePageAdapter = new HorizontalSliderAdapter(productImagesList);
                        photoGalleryRv.setAdapter(homePageAdapter);
                        homePageAdapter.notifyDataSetChanged();

                        //end setting adapter

                    }



                     else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }

            }
        });










    }


    private void changeFragment(Fragment fragment) {
        fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fui_slide_in_right,R.anim.fui_slide_out_left);
        fragmentTransaction.replace(R.id.main_activity_frame_layout, fragment);
        fragmentTransaction.addToBackStack(null).commit();
    }


}