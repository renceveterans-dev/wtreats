package com.wandertech.wandertreats;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.wandertech.wandertreats.databinding.ActivityProfileBinding;
import com.wandertech.wandertreats.general.ExecuteWebServiceApi;
import com.wandertech.wandertreats.general.GeneralFunctions;
import com.wandertech.wandertreats.general.PopUpDialog;
import com.wandertech.wandertreats.general.StartActProcess;
import com.wandertech.wandertreats.utils.Utils;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private View contentView;
    private GeneralFunctions appFunctions;
    private final Handler handler = new Handler();
    private AppCompatTextView titleTxt, forgotPasswordTxt;
    private AppCompatImageView backImgView;
    private TextInputLayout fNameTxtLayout, lNameTxtLayout, emailTxtLayout, mobileTxtLayout;
    private TextInputEditText fNameTxt, lNameTxt, emailTxt, mobileTxt;
    private AppCompatButton saveBtn;
    private boolean lNameChecked, fNameChecked, emailChecked, mobileCHecked;
    private String profileData = "";
    private AppCompatTextView profileNameTxt, profileDescTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appFunctions = MyApp.getInstance().getGeneralFun(getActContext());
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        profileData = appFunctions.retrieveValue(Utils.USER_PROFILE_JSON);

        initView();
        setLabel();

    }

    private void initView() {
        contentView = binding.contentView;
        titleTxt = binding.toolbar.titleTxt;
        backImgView = binding.toolbar.backImgView;

        profileNameTxt = binding.profileNameTxt;
        profileDescTxt = binding.profileDescTxt;

        fNameTxt = binding.fNameTxt;
        lNameTxt = binding.lNameTxt;
        emailTxt = binding.emailTxt;
        mobileTxt = binding.mobileTxt;

        fNameTxtLayout = binding.fNameTxtLayout;
        lNameTxtLayout = binding.lNameTxtLayout;
        emailTxtLayout = binding.emailTxtLayout;
        mobileTxtLayout = binding.mobileTxtLayout;

        saveBtn = binding.saveBtn;

        backImgView.setOnClickListener(new setOnClickAct());
        saveBtn.setOnClickListener(new setOnClickAct());
        fNameTxt.addTextChangedListener(new setOnTextChangeAct( fNameTxt));
        lNameTxt.addTextChangedListener(new setOnTextChangeAct(lNameTxt));
        emailTxt.addTextChangedListener(new setOnTextChangeAct(emailTxt));
        mobileTxt.addTextChangedListener(new setOnTextChangeAct(mobileTxt));
    }

    private void setLabel() {

        titleTxt.setText("Profile");

        profileNameTxt.setText(appFunctions.getJsonValue("vName", profileData)+" "+appFunctions.getJsonValue("vLastName", profileData));
        fNameTxt.setText(appFunctions.getJsonValue("vName",  profileData));
        lNameTxt.setText(appFunctions.getJsonValue("vLastName",  profileData));
        emailTxt.setText(appFunctions.getJsonValue("vEmail",  profileData));
        mobileTxt.setText(appFunctions.getJsonValue("vPhone", profileData));

    }

    public class setOnTextChangeAct implements TextWatcher {
        public TextInputEditText editText;
        public setOnTextChangeAct(TextInputEditText editText){
            this.editText = editText;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {




            switch (editText.getId()) {

                case R.id.fNameTxt:

                    fNameChecked =   Utils.checkText(fNameTxt)? true  : Utils.setErrorFields(fNameTxt, Utils.ERROR_EMPTY_TXT);


                    break;

                case R.id.lNameTxt:

                    lNameChecked =   Utils.checkText(lNameTxt)? true  : Utils.setErrorFields(lNameTxt, Utils.ERROR_EMPTY_TXT);

                    break;

                case R.id.emailTxt:

                    emailChecked =   Utils.checkText(emailTxt)? android.util.Patterns.EMAIL_ADDRESS.matcher(emailTxt.getText().toString().trim()).matches() ? true : Utils.setErrorFields(emailTxt, Utils.ERROR_INAVLID_EMAIL_FORMAT_TXT)  : Utils.setErrorFields(emailTxt, Utils.ERROR_EMPTY_TXT);

                    break;

            }
        }
    }

    public void saveProfileData() {


        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "UPDATE_PROFILE_DATA");
        parameters.put("userId", appFunctions.getMemberId());
        parameters.put("firstName", fNameTxt.getText().toString() == null ? "" : fNameTxt.getText().toString());;
        parameters.put("lastName", lNameTxt.getText().toString() == null ? "" : lNameTxt.getText().toString() );
        parameters.put("email", emailTxt.getText().toString() == null ? "" : emailTxt.getText().toString() );
        parameters.put("mobileNumber", mobileTxt.getText().toString() == null ? "" : mobileTxt.getText().toString() );
        parameters.put("displayPhoto", "");

        ExecuteWebServiceApi exeWebServer = new ExecuteWebServiceApi(getActContext(), parameters, "api_update_profile.php", true);
        exeWebServer.setLoaderConfig(getActContext(), true,appFunctions);
        exeWebServer.setDataResponseListener(new ExecuteWebServiceApi.SetDataResponse() {
            @Override
            public void setResponse(String responseString) {

                if(responseString != null){

                    if(appFunctions.checkDataAvail(Utils.action_str, responseString)){

                        appFunctions.storeData(Utils.USER_PROFILE_JSON, appFunctions.getJsonValue("data", responseString));
                        profileData = appFunctions.retrieveValue(Utils.USER_PROFILE_JSON);
                        setLabel();

                        PopUpDialog dialog = new PopUpDialog(getActContext(), "Account");
                        dialog.setMessage("Your account information has successfully updated.");
                        dialog.setPositiveBtn("Okay");
                        dialog.show();
                        dialog.getPositive_btn().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                    }else{
                        Toast.makeText(getActContext(), "Error "+ responseString, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActContext(), "Error "+ responseString, Toast.LENGTH_SHORT).show();
                }

            }
        });
        exeWebServer.execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public class setOnClickAct implements View.OnClickListener {

        private static final long MIN_CLICK_INTERVAL=600;
        private long mLastClickTime;

        @Override
        public void onClick(View view) {

            long currentClickTime= SystemClock.uptimeMillis();
            long elapsedTime=currentClickTime-mLastClickTime;

            mLastClickTime=currentClickTime;

            if(elapsedTime<=MIN_CLICK_INTERVAL)
                return;

            switch(view.getId()){

                case R.id.backImgView:

                    onBackPressed();
                    break;

                case R.id.saveBtn:

                    saveProfileData();

                    break;
                default:

                    break;



            }

        }


    }



    public Context getActContext() {
        return ProfileActivity.this;
    }
}

