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
package com.esri.defensese.crowmap.common.model;

import java.util.ArrayList;
import java.util.List;

/**
 * MapContents stores lists of pointers to the basemap layers and operational layers
 * that make up a map.
 */
public class MapContents {
    
    private final List<Object> basemapLayers = new ArrayList<>();
    private final List<Object> operationalLayers = new ArrayList<>();

    /**
     * Returns the basemap layers in this MapContents object, each one being a layer object in the GIS
     * library you're using.
     * @return the basemap layers in this MapContents object.
     */
    public List<Object> getBasemapLayers() {
        return basemapLayers;
    }

    /**
     * Returns the operational layers in this MapContents object, each one being a layer object in the
     * GIS library you're using.
     * @return the operational layers in this MapContents object.
     */
    public List<Object> getOperationalLayers() {
        return operationalLayers;
    }
    
}
