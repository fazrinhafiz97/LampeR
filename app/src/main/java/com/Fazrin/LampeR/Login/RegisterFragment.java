package com.Fazrin.LampeR.Login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.Fazrin.LampeR.R;

import java.util.HashMap;
import java.util.Map;

import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    EditText    mName,
                mEmail,
                mPassword,
                mCard,
                mPhone,
                mBrand,
                mPlate;
    Button mRegister;
    RadioRealButtonGroup mRadioGroup;

    LinearLayout mCar;

    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_registration, container, false);
        else
            container.removeView(view);


        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeObjects();
        radioController();
    }

    private void radioController() {
        mRadioGroup.setOnPositionChangedListener(new RadioRealButtonGroup.OnPositionChangedListener() {
            @Override
            public void onPositionChanged(RadioRealButton button, int currentPosition, int lastPosition) {
                if(currentPosition == 1){
                    mCar.setVisibility(View.VISIBLE);
                }else{
                    mCar.setVisibility(View.GONE);
                }
            }
        });
    }


    void register(){
        if(mEmail.getText().length()==0) {
            mEmail.setError("please fill this field");
            return;
        }
        if(mPassword.getText().length()==0) {
            mPassword.setError("please fill this field");
            return;
        }
        if(mPassword.getText().length()< 6) {
            mPassword.setError("password must have at least 6 characters");
            return;
        }
        if(mName.getText().length()==0) {
            mName.setError("please fill this field");
            return;
        }
        if(mPhone.getText().length()==0) {
            mPhone.setError("please fill this field");
            return;
        }
        if(mCard.getText().length()==0) {
            mCard.setError("please fill this field");
            return;
        }
        if(!mCard.getText().toString().matches("AI[0-9][0-9][0-9][0-9][0-9][0-9]")){
            mCard.setError("card format wrong");
            return;
        }





        final String email = mEmail.getText().toString();
        final String password = mPassword.getText().toString();
        final String phone = mPhone.getText().toString();
        final String card = mCard.getText().toString();
        final String name = mName.getText().toString();
        final String brand = mBrand.getText().toString();
        final String plate = mPlate.getText().toString();

        final String accountType;
        int selectId = mRadioGroup.getPosition();

        switch (selectId){
            case 0:
                accountType = "Customers";
                break;
            case 1:
                accountType = "Drivers";
                break;
            default:
                accountType = "Customers";
        }
        if(selectId == 1){
            if(mPlate.getText().length()==0) {
                mPlate.setError("please fill this field");
                return;
            }
            if(mBrand.getText().length()==0) {
                mBrand.setError("please fill this field");
                return;
            }
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Snackbar.make(view.findViewById(R.id.layout), "sign up error", Toast.LENGTH_SHORT).show();
                }else{
                    String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Map newUserMap = new HashMap();
                    newUserMap.put("email", email);
                    newUserMap.put("phone", phone);
                    newUserMap.put("card", card);
                    newUserMap.put("name", name);
                    if(accountType.equals("Drivers")){
                        //newUserMap.put("service", "UberX");
                        newUserMap.put("car", brand);
                        newUserMap.put("plate", plate);
                        newUserMap.put("activated", true);
                    }
                    FirebaseDatabase.getInstance().getReference().child("Users").child(accountType).child(user_id).updateChildren(newUserMap);
                }
            }
        });

    }

    void initializeObjects(){
        mEmail = view.findViewById(R.id.email);
        mPassword = view.findViewById(R.id.password);
        mRegister = view.findViewById(R.id.register);
        mRadioGroup = view.findViewById(R.id.radioRealButtonGroup);
        mName = view.findViewById(R.id.name);
        mCard = view.findViewById(R.id.card);
        mPhone = view.findViewById(R.id.phone);
        mBrand = view.findViewById(R.id.car_brand);
        mPlate = view.findViewById(R.id.car_plate);
        mCar = view.findViewById(R.id.car_details);

        mRadioGroup.setPosition(0);
        mRegister.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                register();;
        }
    }
}