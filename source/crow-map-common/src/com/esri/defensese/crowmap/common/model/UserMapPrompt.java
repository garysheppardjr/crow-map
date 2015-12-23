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

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Prompts the user to choose a map.
 */
public abstract class UserMapPrompt {
    
    /**
     * Override this method to return a MapContents object containing the layers
     * for the map that the user selected. This is a synchronous way to get map
     * contents. This default method returns an empty MapContents object; you don't
     * need to override it if you prefer to use promptUserForMapContents.
     * @return a MapContents object containing the layers for the map that the user
     *         selected.
     */
    public MapContents getMapContents() {
        return new MapContents();
    };
    
    /**
     * Override this method to prompt the user to choose map contents; the selected
     * map contents are returned to listeners via the mapContentsSelected method.
     * This is an asynchronous way to get map contents. This default method causes
     * the listener to be called with an empty MapContents object; you don't need
     * to override it if you prefer to use getMapContents.
     * @param listener the listener to receive the MapContents object.
     */
    public void promptUserForMapContents(final UserMapPromptListener listener) {
        new Thread() {

            @Override
            public void run() {
                listener.mapContentsSelected(new MapContents());
            }
            
        }.start();
    };

    /**
     * Makes a local tiled layer from the given file path.
     * @param path a file path for a TPK or a compact cache.
     * @return a local tiled layer object.
     */
    protected abstract Object makeLocalTiledLayer(String path);

    /**
     * Reads an ArcGIS Runtime geodatabase and returns a list of the layers it contains.
     * @param gdbPath the path to an ArcGIS Runtime geodatabase (typically a .geodatabase file).
     * @return a list of the geodatabase's layers, each element being a layer object.
     * @throws FileNotFoundException if the path does not refer to an existing Runtime geodatabase
     *                               that is readable by the current user.
     */
    protected abstract List<Object> readLayers(String gdbPath) throws FileNotFoundException;

    /**
     * Reads a directory containing ArcGIS Runtime content into a MapContents object.
     * @param offlineMapDir a directory containing ArcGIS Runtime content.
     * @return a MapContents object.
     */
    public MapContents readDirectory(File offlineMapDir) {
        MapContents contents = new MapContents();
        File basemapsDir = new File(offlineMapDir, "basemap");
        File[] basemapDirs = basemapsDir.listFiles();
        if (null != basemapDirs) {
            for (File basemapDir : basemapDirs) {
                Object layer = makeLocalTiledLayer(basemapDir.getAbsolutePath());
                contents.getBasemapLayers().add(layer);
            }
        }
        File dataDir = new File(offlineMapDir, "data");
        File[] gdbFiles = dataDir.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                String name = pathname.getName();
                return null != name && name.trim().toLowerCase().endsWith(".geodatabase");
            }
        });
        if (null != gdbFiles) {
            for (File gdbFile : gdbFiles) {
                try {
                    contents.getOperationalLayers().addAll(readLayers(gdbFile.getAbsolutePath()));
                } catch (FileNotFoundException e) {

                }
            }
        }
        return contents;
    }

}
