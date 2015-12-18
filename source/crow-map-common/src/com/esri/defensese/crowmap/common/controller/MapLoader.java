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
package com.esri.defensese.crowmap.common.controller;

import com.esri.defensese.crowmap.common.model.MapContents;
import com.esri.defensese.crowmap.common.model.UserMapPrompt;
import java.util.List;

/**
 * A class that loads an ArcGIS Runtime map with layers from an ArcGIS Runtime content
 * directory.
 */
public class MapLoader {
    
    private final UserMapPrompt prompt;
    
    /**
     * Instantiates a MapLoader.
     * @param prompt a UserMapPrompt that prompts the user for the ArcGIS Runtime
     *               content directory.
     */
    public MapLoader(UserMapPrompt prompt) {
        this.prompt = prompt;
    }
    
    /**
     * Loads the specified JMap with layers based on an ArcGIS Runtime content directory.
     * The location of the directory is determined as follows:<br/>
     * <ol>
     *     <li>Check for a previously stored user preference. (TODO not yet implemented)</li>
     *     <li>Check the working directory for an ArcGIS Runtime content directory.
     *         (TODO not yet implemented)</li>
     *     <li>Prompt the user for an ArcGIS Runtime content directory or a web
     *         map. (TODO web map chooser and download not yet implemented)</li>     * 
     * </ol>
     * @param mapController a MapController to help add the layers to the map.
     */
    public void loadMap(MapController mapController) {
        MapContents mapContents;
        
        /**
         * 1. Check for a user preference.
         */
        //TODO
        
        /**
         * 2. Check the working directory for a map.
         */
        //TODO
        
        /**
         * 3. Prompt the user.
         */
        mapContents = prompt.getMapContents();
        
        /**
         * 4. Load the map.
         */
        if (null != mapContents) {
            addAllLayers(mapController, mapContents.getBasemapLayers());
            addAllLayers(mapController, mapContents.getOperationalLayers());
        }
    }
    
    private static void addAllLayers(MapController mapController, List<Object> layers) {
        for (Object layer : layers) {
            mapController.getLayers().add(layer);
        }
    }
    
}
