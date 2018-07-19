package com.johanlund.screens.load_import.mvc_views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.johanlund.ibsfoodanalyzer.R;

public class ImportOptionViewMvcImpl implements ImportOptionViewMvc {
    View mRootView;
    TextView infoBox;
    RadioGroup radioGroup;
    RadioButton wipeAndLoadBtn;
    RadioButton mergeBtn;
    RadioButton tagTypeImportBtn;
    RadioButton eventTemplatesBtn;
    Button retrieveFileBtn;
    private ImportOptionViewMvcListener listener;

    public ImportOptionViewMvcImpl(LayoutInflater inflater, ViewGroup container) {
        mRootView = inflater.inflate(R.layout.fragment_import_options, container, false);
        initializeViews();
    }

    private void initializeViews() {
        radioGroup = mRootView.findViewById(R.id.import_alternatives);
        infoBox = mRootView.findViewById(R.id.import_options_infobox);
        retrieveFileBtn = mRootView.findViewById(R.id.getDbFileBtn);
        retrieveFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int checked = radioGroup.getCheckedRadioButtonId();
                switch (checked){
                    case R.id.radio_import_override:{
                        listener.cleanAndLoad();
                        break;
                    }
                    case R.id.radio_import_merge:{

                        break;
                    }
                    case R.id.radio_import_tagtypes:{
                        listener.importTagTypes();
                        break;
                    }
                    case R.id.radio_import_event_templates:{
                        listener.importEventTemplates();
                        break;
                    }
                }
            }
        });



        wipeAndLoadBtn = mRootView.findViewById(R.id.radio_import_override);
        mergeBtn = mRootView.findViewById(R.id.radio_import_merge);
        tagTypeImportBtn = mRootView.findViewById(R.id.radio_import_tagtypes);
        eventTemplatesBtn = mRootView.findViewById(R.id.radio_import_event_templates);

        showInfoOnBtnDown(wipeAndLoadBtn, "This is \"the ordinary load\". This action will " +
                "completely REPLACE current database with the one to-be-imported. Nothing will be saved of the former database.");
        showInfoOnBtnDown(mergeBtn, "This action will ADD to database from the external database file.");
        showInfoOnBtnDown(tagTypeImportBtn, "Add Tag Types (milk, running, meditation etc) that don't already exist in database.");
        showInfoOnBtnDown(eventTemplatesBtn, "Import more Event Templates from file. If they contain unknown TagTypes these will be imported also.");
    }

    private void showInfoOnBtnDown(Button btn,final String text){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfo(text);
            }
        });
    }

    private void showInfo(String text){
        infoBox.setText(text);
    }

    @Override
    public View getRootView() {
        return mRootView;
    }

    @Override
    public void setListener(ImportOptionViewMvcListener listener) {
        this.listener = listener;
    }

    @Override
    public String getTitleStr() {
        return "Import Options";
    }
}
