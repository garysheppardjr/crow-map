/*******************************************************************************
 * Copyright 2015 Esri
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 ******************************************************************************/
package com.esri.defensese.crowmap.android.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.defensese.crowmap.android.R;
import com.esri.defensese.crowmap.android.model.FileUtilities;
import com.esri.defensese.crowmap.common.controller.MapController;
import com.esri.defensese.crowmap.common.controller.MapLoader;
import com.esri.defensese.crowmap.common.model.MapContents;
import com.esri.defensese.crowmap.common.model.UserMapPrompt;
import com.esri.defensese.crowmap.common.model.UserMapPromptListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class CrowMapMainActivity extends AppCompatActivity {

    private static final Random random = new Random();

    private final HashMap<Integer, UserMapPromptListener> requestCodeToListener = new HashMap<>();

    private final UserMapPrompt prompt = new UserMapPrompt() {

        @Override
        public void promptUserForMapContents(UserMapPromptListener listener) {
            Intent chooseFileIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            chooseFileIntent = Intent.createChooser(chooseFileIntent, "Choose a Runtime Content Directory");
            int requestCode = random.nextInt(2 ^ 16);
            requestCodeToListener.put(requestCode, listener);
            startActivityForResult(chooseFileIntent, requestCode);
        }

        @Override
        protected Object makeLocalTiledLayer(String path) {
            return new ArcGISLocalTiledLayer(path);
        }

        @Override
        protected List<Object> readLayers(String gdbPath) throws FileNotFoundException {
            ArrayList<Object> layers = new ArrayList<>();
            try {
                Geodatabase gdb = new Geodatabase(gdbPath);
                for (GeodatabaseFeatureTable table : gdb.getGeodatabaseTables()) {
                    try {
                        FeatureLayer layer = new FeatureLayer(table);
                        layers.add(layer);
                    } catch (Throwable t) {
                        Log.e(getClass().getSimpleName(), null, t);
                    }
                }
            } catch (Throwable t) {
                Log.e(getClass().getSimpleName(), null, t);
            }
            return layers;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crow_map_main);

        MapLoader mapLoader = new MapLoader(prompt);

        mapLoader.loadMapAsync(new MapController() {

            private final MapView mapView = (MapView) findViewById(R.id.map);

            @Override
            public boolean addLayer(Object layer) {
                if (layer instanceof Layer) {
                    return 0 < mapView.addLayer((Layer) layer);
                } else {
                    return false;
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UserMapPromptListener listener = requestCodeToListener.get(requestCode);
        if (null != listener && Activity.RESULT_OK == resultCode) {
            requestCodeToListener.remove(requestCode);

            String dirUriString = Uri.decode(data.getDataString());
            int lastColonIndex = dirUriString.lastIndexOf(':');
            String dirRelativePath = dirUriString.substring(lastColonIndex + 1);
            File offlineMapDir = new File("/mnt/sdcard", dirRelativePath);
            String treeDocumentId = DocumentsContract.getTreeDocumentId(data.getData());
            String deviceId = treeDocumentId.substring(0, treeDocumentId.indexOf(':'));
            if (!"primary".equals(deviceId)) {
                /**
                 * TODO here we simply iterate through all external mounts and use the first one that
                 *      exists. It would be great to find a smarter way to identify the actual correct
                 *      one, based on deviceId.
                 */
                Iterator<String> externalMounts = FileUtilities.getExternalMounts().iterator();
                while (!(offlineMapDir.exists() && offlineMapDir.isDirectory()) && externalMounts.hasNext()) {
                    offlineMapDir = new File(externalMounts.next(), dirRelativePath);
                }
            }

            MapContents mapContents = prompt.readDirectory(offlineMapDir);
            listener.mapContentsSelected(mapContents);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((MapView) findViewById(R.id.map)).pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MapView) findViewById(R.id.map)).unpause();
    }
}
