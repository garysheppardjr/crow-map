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

/**
 * Prompts the user to choose a map.
 */
public interface UserMapPrompt {
    
    /**
     * Returns a MapContents object containing the layers for the map that the user
     * selected.
     * @return a MapContents object containing the layers for the map that the user
     *         selected.
     */
    public MapContents getMapContents();
    
}
