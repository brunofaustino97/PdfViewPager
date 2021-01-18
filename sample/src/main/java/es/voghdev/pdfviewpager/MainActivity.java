/*
 * Copyright (C) 2016 Olmo Gallegos Hernández.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.voghdev.pdfviewpager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.File;

import es.voghdev.pdfviewpager.library.PDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.BasePDFPagerAdapter;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;

public class MainActivity extends BaseSampleActivity {
    PDFViewPager pdfViewPager;
    BasePDFPagerAdapter adapter;
    Button btnLoad, btnDeepLink;

    final int REQUEST_CODE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("MainAppPdf");
        setContentView(R.layout.activity_main);

        btnLoad = findViewById(R.id.loadFile);
        btnDeepLink = findViewById(R.id.deepLink);
        pdfViewPager = findViewById(R.id.pdfViewPager);

        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissionsThenOpen(AssetOnSDActivity.class);
            }
        });
        btnDeepLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File pdfFolder = new File(Environment.getExternalStorageDirectory(),"teste");

                Intent i;
                PackageManager manager = getPackageManager();
                try {
                    i = manager.getLaunchIntentForPackage("printingapp.mobile");
                    if (i == null)
                        throw new PackageManager.NameNotFoundException();
                    i.addCategory(Intent.CATEGORY_LAUNCHER);
                    i.putExtra("folder",pdfFolder.getAbsolutePath());
                    startActivity(i);
                } catch (PackageManager.NameNotFoundException e) {
                }
            }
        });

        adapter = new PDFPagerAdapter(this, "sample.pdf");
        pdfViewPager.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        adapter.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sample2) {
            launchActivity(RemotePDFActivity.class);
            return false;
        } else if (id == R.id.action_sample3) {
            requestPermissionsThenOpen(AssetOnSDActivity.class);
            return false;
        } else if (id == R.id.action_sample4) {
            Toast.makeText(this, R.string.dummy_msg, Toast.LENGTH_LONG).show();
        } else if (id == R.id.action_sample5) {
            requestPermissionsThenOpen(AssetOnXMLActivity.class);
        } else if (id == R.id.action_sample8) {
            requestPermissionsThenOpen(ZoomablePDFActivityPhotoView.class);
        } else if (id == R.id.action_sample9) {
            launchActivity(PDFWithScaleActivity.class);
        } else if (id == R.id.action_sample10) {
            launchActivity(InvalidPdfActivity.class);
        }

        return super.onOptionsItemSelected(item);
    }

    protected boolean hasExternalStoragePermissions() {
        boolean hasReadPermission = ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED;

        boolean hasWritePermission = ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED;

        return hasReadPermission && hasWritePermission;
    }

    protected void requestPermissionsThenOpen(Class activityClass) {
        if (hasExternalStoragePermissions()) {
            launchActivity(activityClass);
        } else {
            requestExternalStoragePermissions();
        }
    }

    protected void requestExternalStoragePermissions() {
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
    }

    protected void launchActivity(Class activityClass) {
        Intent i = new Intent(this, activityClass);
        startActivity(i);
    }
}
