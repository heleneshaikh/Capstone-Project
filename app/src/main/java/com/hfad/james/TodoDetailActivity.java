package com.hfad.james;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
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

        Bundle extras = getIntent().getExtras();

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(R.string.resto_to_do);
        }
        // check from the saved Instance
        todoUri = (bundle == null) ? null : (Uri) bundle
                .getParcelable(MyTodoContentProvider.CONTENT_ITEM_TYPE);

        // Or passed from the other activity
        if (extras != null) {
            todoUri = extras
                    .getParcelable(MyTodoContentProvider.CONTENT_ITEM_TYPE);

            fillData(todoUri);
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TextUtils.isEmpty(nameText.getText().toString())) {
                    makeToast();
                } else {
                    setResult(RESULT_OK);
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
        //onPreExecute
        ContentValues values = new ContentValues();
        values.put(TodoTable.COLUMN_NAME, description);

        //doInBackground
        if (todoUri == null) {
            todoUri = getContentResolver().insert(
                    MyTodoContentProvider.CONTENT_URI, values);
        } else {
            getContentResolver().update(todoUri, values, null, null);
        }
    }

    private void makeToast() {
        Toast.makeText(TodoDetailActivity.this, R.string.add_name_toast,
                Toast.LENGTH_LONG).show();
    }
}
