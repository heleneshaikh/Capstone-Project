package com.hfad.james;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hfad.james.contentprovider.MyTodoContentProvider;
import com.hfad.james.database.TodoTable;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by heleneshaikh on 16/09/16.
 */
public class TodoDetailActivity extends AppCompatActivity {
    @BindView(R.id.editText)
    EditText nameText;
    @BindView(R.id.submit_btn)
    Button submitButton;
    @BindView(R.id.include)
    Toolbar toolbar;
    private Uri todoUri;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.todo_edit);
        ButterKnife.bind(this);
        final Bundle extras = getIntent().getExtras();

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(R.string.resto_to_do);
        }

        todoUri = (bundle == null) ? null : (Uri) bundle.getParcelable(MyTodoContentProvider.CONTENT_ITEM_TYPE);
        nameText.setId(R.id.id_edit);

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TextUtils.isEmpty(nameText.getText().toString())) {
                    makeToast();
                } else {
                    setResult(RESULT_OK);
                    if (extras != null) {
                        todoUri = extras.getParcelable(MyTodoContentProvider.CONTENT_ITEM_TYPE);
                        fillData(todoUri);
                    }
                    finish();
                }
            }
        });
    }

    private void fillData(Uri uri) {
        String[] projection = {TodoTable.COLUMN_NAME};
        Cursor cursor = getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
            nameText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(TodoTable.COLUMN_NAME)));
            cursor.close();
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable(MyTodoContentProvider.CONTENT_ITEM_TYPE, todoUri);
        outState.putString("edit", nameText.getText().toString());
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    private void saveState() {
        String description = nameText.getText().toString();

        if (nameText.length() == 0) {
            Log.v("noEntry", "no entry provided");
            return;
        }
        ContentValues values = new ContentValues();
        values.put(TodoTable.COLUMN_NAME, description);

        SaveToDatabase saveToDatabase = new SaveToDatabase();
        saveToDatabase.execute(values);
    }

    private void makeToast() {
        Toast.makeText(TodoDetailActivity.this, R.string.add_name_toast,
                Toast.LENGTH_LONG).show();
    }

    private class SaveToDatabase extends AsyncTask<ContentValues, Void, Void> {

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            if (todoUri == null) {
                todoUri = getContentResolver().insert(MyTodoContentProvider.CONTENT_URI, contentValues[0]);
            } else {
                getContentResolver().update(todoUri, contentValues[0], null, null);
            }
            return null;
        }
    }
}
