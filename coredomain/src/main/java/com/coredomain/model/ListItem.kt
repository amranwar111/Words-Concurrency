package com.coredomain.model

import java.io.Serializable

class ListItem(val id:Int?,var name:String?,var image:String?):Serializable{
    var code:String? = null
    var type:Int? = null
    var description:String? = null
}