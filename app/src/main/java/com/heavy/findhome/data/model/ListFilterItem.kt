package com.heavy.findhome.data.model

import android.graphics.drawable.Drawable

class ListFilterItem constructor(pTitleFilter: String?, pIconFilter: Drawable?, pSelected:Boolean?) {

    var titleFilter: String? = pTitleFilter
        get() = field
        set(value) {
            field = value
        }

    var iconFilter: Drawable? = pIconFilter
        get() = field
        set(value) {
            field = value
        }

    var selected: Boolean? = pSelected
        get() = field
        set(value) {
            field = value
        }

}