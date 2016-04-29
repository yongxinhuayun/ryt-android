/*
 *  Copyright 2011 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.yxh.ryt.custemview.widget.adapters;

import android.content.Context;

import com.yxh.ryt.vo.DistrictModel;
import com.yxh.ryt.vo.ProvinceModel;

import java.util.List;

/**
 * The simple Array wheel adapter
 * @param <T> the element type
 */
public class DiQuWheelAdapter extends AbstractWheelTextAdapter {

    // items
    private List<DistrictModel>  items;

    /**
     * Constructor
     * @param context the current context
     * @param items the items
     */
    public DiQuWheelAdapter(Context context, List<DistrictModel> items) {
        super(context);
        
        //setEmptyItemResource(TEXT_VIEW_ITEM_RESOURCE);
        this.items = items;
    }
    
    @Override
    public CharSequence getItemText(int index) {
        if (index >= 0 && index < items.size()) {
            DistrictModel districtModel = items.get(index);
            return districtModel.getName();
        }
        return null;
    }

    @Override
    public int getItemsCount() {
        return items.size();
    }
}