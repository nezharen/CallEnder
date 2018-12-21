package nezharen.space.callender;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 0;
    private static final String[] permission_list = {
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.ANSWER_PHONE_CALLS
    };

    private ListView listView;
    private String numberToDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.number_list_view);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor c = (Cursor) adapterView.getItemAtPosition(i);
                numberToDelete = c.getString(c.getColumnIndex("number"));
                AlertDialog.Builder aBuilder = new AlertDialog.Builder(MainActivity.this);
                aBuilder.setIcon(R.mipmap.ic_launcher);
                aBuilder.setTitle(getString(R.string.app_name));
                aBuilder.setMessage(String.format(getString(R.string.sure_to_delete_number), numberToDelete));
                aBuilder.setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (DataBase.getInstance(getApplicationContext()).deleteNumber(numberToDelete)) {
                            refreshListView();
                        }
                    }
                });
                aBuilder.setNegativeButton(getString(R.string.cancel), null);
                aBuilder.show();
                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder aBuilder = new AlertDialog.Builder(MainActivity.this);
                aBuilder.setIcon(R.mipmap.ic_launcher);
                aBuilder.setTitle(getString(R.string.app_name));
                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                final View dialog_add_view = inflater.inflate(R.layout.dialog_add, null);
                aBuilder.setView(dialog_add_view);
                aBuilder.setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText numberEditText = (EditText) dialog_add_view.findViewById(R.id.number_edit_text);
                        String number_add = numberEditText.getText().toString().trim();
                        if (DataBase.getInstance(getApplicationContext()).insertNumber(number_add)) {
                            refreshListView();
                        }
                    }
                });
                aBuilder.setNegativeButton(getString(R.string.cancel), null);
                aBuilder.setCancelable(false);
                aBuilder.show();
            }
        });

        checkForPermission();
        refreshListView();
    }


    private void checkForPermission() {
        for (String i : permission_list) {
            if (ContextCompat.checkSelfPermission(this, i) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permission_list, PERMISSION_REQUEST_CODE);
                break;
            }
        }
    }

    private void refreshListView() {
        Cursor c = DataBase.getInstance(getApplicationContext()).getAllNumbers();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.number_list_item, c,
                new String[] {"number"}, new int[] {R.id.number_text_view});
        listView.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length < permission_list.length) {
                    Toast.makeText(this, R.string.no_permission, Toast.LENGTH_LONG).show();
                }
                for (int i : grantResults) {
                    if (i != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, R.string.no_permission, Toast.LENGTH_LONG).show();
                        break;
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help_menu_item) {
            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
