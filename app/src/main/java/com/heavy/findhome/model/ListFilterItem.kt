package com.heavy.findhome.model

import android.graphics.drawable.Drawable

class ListFilterItem {

    private var titleFilter:String?
        get() {
            return this?.titleFilter
        }
        set(titleFilter:String?) {
            this.titleFilter = titleFilter
        }

    private var iconFilter:Drawable?
        get() {
           return this?.iconFilter
        }
        set(iconFilter:Drawable?) {
            this?.iconFilter = iconFilter
        }

}