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

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.defensese.crowmap.android.R;
import com.esri.defensese.crowmap.common.controller.MapController;
import com.esri.defensese.crowmap.common.controller.MapLoader;
import com.esri.defensese.crowmap.common.model.MapContents;
import com.esri.defensese.crowmap.common.model.UserMapPrompt;

import java.util.ArrayList;
import java.util.List;

public class CrowMapMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crow_map_main);

        MapLoader mapLoader = new MapLoader(new UserMapPrompt() {

            @Override
            public MapContents getMapContents() {
                MapContents mapContents = new MapContents();
                return mapContents;
            }

        });

        mapLoader.loadMap(new MapController() {

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
}
