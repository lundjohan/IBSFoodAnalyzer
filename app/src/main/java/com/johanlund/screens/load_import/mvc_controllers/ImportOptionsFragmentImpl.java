package com.johanlund.screens.load_import.mvc_controllers;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.johanlund.dao.Dao;
import com.johanlund.dao.SqLiteDao;
import com.johanlund.external_storage.ExternalStorageHandler;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.screens.load_import.mvc_views.ImportOptionViewMvc;
import com.johanlund.screens.load_import.mvc_views.ImportOptionViewMvcImpl;

import java.io.File;

import static android.app.Activity.RESULT_OK;
import static com.johanlund.constants.Constants.IMPORT_DB_CLEAN_AND_LOAD;
import static com.johanlund.constants.Constants.IMPORT_DB_TAG_TYPES;
import static com.johanlund.constants.Constants.IMPORT_EVENT_TEMPLATES;
import static com.johanlund.constants.Constants.IMPORT_MERGE;

public class ImportOptionsFragmentImpl extends Fragment implements ImportOptionViewMvc
        .ImportOptionViewMvcListener {
    ImportOptionViewMvcImpl mViewMVC;
    ImportOptionsFragment.ImportOptionsFragmentListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewMVC = new ImportOptionViewMvcImpl(inflater, container);
        getActivity().setTitle(mViewMVC.getTitleStr());
        // Return the root view of the associated MVC view
        return mViewMVC.getRootView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (ImportOptionsFragment.ImportOptionsFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewMVC.setListener(this);
    }

    //ok, read from external file? Otherwise ask for permission
    private void handleFilePermissions() {
        //getActivity doesnt feel perfect here => close coupling.
        ExternalStorageHandler.showReadablePermission(getActivity());
    }

    @Override
    public void cleanAndLoad() {
        handleFilePermissions();
        showChooser(IMPORT_DB_CLEAN_AND_LOAD);
    }

    @Override
    public void mergeDatabase() {
        handleFilePermissions();
        showChooser(IMPORT_MERGE);
    }

    @Override
    public void importTagTypes() {
        handleFilePermissions();
        showChooser(IMPORT_DB_TAG_TYPES);

    }

    @Override
    public void importEventTemplates() {
        handleFilePermissions();
        showChooser(IMPORT_EVENT_TEMPLATES);
    }

    //code reused from aFileChooser example
    private void showChooser(int requestCode) {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        ImportOptionsFragmentImpl.ImportDBAsyncTask asyncThread = null;
        if (data != null) {
            File dbFileToImport = getChosenFile(data);
            if (dbFileToImport != null) {
                switch (requestCode) {
                    case IMPORT_DB_CLEAN_AND_LOAD: {
                        asyncThread = new ImportOptionsFragmentImpl.CleanAndLoadDatabase
                                (dbFileToImport);
                        break;
                    }
                    case IMPORT_MERGE: {
                        asyncThread = new ImportOptionsFragmentImpl.MergeDatabase(dbFileToImport);
                        break;
                    }
                    case IMPORT_DB_TAG_TYPES: {
                        asyncThread = new ImportOptionsFragmentImpl.ImportTagTypes(dbFileToImport);
                        break;
                    }
                    case IMPORT_EVENT_TEMPLATES: {
                        asyncThread = new ImportOptionsFragmentImpl.ImportEventTemplates(dbFileToImport);
                        break;
                    }
                }
                if (asyncThread != null) {
                    asyncThread.execute(0);
                }
            }
        }
    }

    private File getChosenFile(Intent data) {
        // Get the URI of the selected file
        final Uri uri = data.getData();
        File file = null;
        try {
            // Get the file path from the URI
            final String path = FileUtils.getPath(getContext(), uri);
            Toast.makeText(getContext(), "File Selected: " + path, Toast.LENGTH_LONG).show();

            if (path != null && FileUtils.isLocal(path)) {
                file = new File(path);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    private abstract class ImportDBAsyncTask extends AsyncTask<Integer, Void, Void> {
        final String TAG = this.getClass().getName();
        File file = null;

        public ImportDBAsyncTask(File file) {
            this.file = file;
        }


        @Override
        protected Void doInBackground(Integer... notUsedParams) {
            doAction();
            return null;
        }

        @Override
        protected void onPostExecute(Void notUsed) {
            //after db has been replaced, make the date shown for user the last date filled in
            // new db.
            listener.startDiaryAtLastDate();
        }
        protected abstract void doAction();
    }

    private class CleanAndLoadDatabase extends ImportDBAsyncTask {
        public CleanAndLoadDatabase(File file) {
            super(file);
        }

        @Override
        protected void doAction() {
            ExternalStorageHandler.replaceDBWithExtStorageFile(file, getContext());
        }

    }
    private class ImportTagTypes extends ImportDBAsyncTask {
        public ImportTagTypes(File file) {
            super(file);
        }

        @Override
        protected void doAction() {
            Dao dao = new SqLiteDao(getContext());
            dao.insertTagTypesFromExternalDatabase(file.getAbsolutePath());
        }
    }
    private class MergeDatabase extends ImportDBAsyncTask {
        public MergeDatabase(File file) {
            super(file);
        }

        @Override
        protected void doAction() {
            //TODO
        }
    }
    private class ImportEventTemplates extends ImportDBAsyncTask {
        public ImportEventTemplates(File file) {
            super(file);
        }

        @Override
        protected void doAction() {
            Dao dao = new SqLiteDao(getContext());
            dao.insertEventTemplatesFromExternalDatabase(file.getAbsolutePath());
        }
    }
}
