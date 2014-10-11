package io.github.nick11roberts.devfesthelloworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

import java.io.IOException;


public class PrimaryActivity extends Activity {

    // Create Dropbox account variable.
    private DbxAccountManager mDbxAcctMgr;
    private String APP_KEY = "4aputdeutps7u6g";
    private String APP_SECRET = "4kbb6jj2s0ozj7o";
    // 42 may seem arbitrary, but it's the ultimate answer (it actually is arbitrary).
    static final int REQUEST_LINK_TO_DBX = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);

        // Initialize Dropbox acct variable.
        mDbxAcctMgr = DbxAccountManager.getInstance(getApplicationContext(), APP_KEY, APP_SECRET);

        // Initialize the button.
        final Button theButton = (Button) findViewById(R.id.the_button);

        // Listen to the button, it has emotions as well.
        theButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform Dropbox-y-type thing if the user is connected.
                if (mDbxAcctMgr.hasLinkedAccount())
                    doDropboxThings();
                else
                    toDropboxNotLinked();
            }
        });
    }

    private void toDropboxNotLinked() {

        mDbxAcctMgr.startLink((Activity)this, REQUEST_LINK_TO_DBX);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LINK_TO_DBX) {
            if (resultCode == Activity.RESULT_OK) {
               doDropboxThings();
            } else {
                // ... Do something to indicate that you're disappointed that it didn't work.
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void doDropboxThings() {
        try {
            DbxFileSystem dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());
            DbxFile testFile = null;
            try {
                testFile = dbxFs.create(new DbxPath("helloFromDevFest.txt"));
            } catch (DbxException e) {
                e.printStackTrace();
            }
            try {
                try {
                    testFile.writeString("Hello from GDG Dev Fest @FCC!!!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } finally {
                testFile.close();
            }
        } catch (DbxException.Unauthorized unauthorized) {
            unauthorized.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.primary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
