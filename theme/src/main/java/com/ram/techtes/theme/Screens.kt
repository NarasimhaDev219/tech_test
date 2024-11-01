package com.ram.techtes.theme

sealed class Screens(val route:String) {
    object SourceList: Screens("source_list")
    object SourceDetails: Screens("source_details/{sourceID}"){
        fun createRoute(sourceID: String) = "source_details/$sourceID"
    }
}