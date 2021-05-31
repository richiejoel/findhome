package com.heavy.findhome.data.model

class RentAparment(pstNameAparment: String?, pstNameOwner: String?, pflAmount: Float?, pinNumberBedroom: Int?, pinNumberBathroom: Int?, pinNumberBell: Int?) {

    var nameAparment: String? = pstNameAparment
        get() = field
        set(value) {
            field = value
        }

    var nameOwner: String? = pstNameOwner
        get() = field
        set(value) {
            field = value
        }

    var amount: Float? = pflAmount
        get() = field
        set(value) {
            field = value
        }

    var numberBedroom: Int? = pinNumberBedroom
        get() = field
        set(value) {
            field = value
        }

    var numberBathroom: Int? = pinNumberBathroom
        get() = field
        set(value) {
            field = value
        }

    var numberBell: Int? = pinNumberBell
        get() = field
        set(value) {
            field = value
        }
}