package com.johanlund.screens.events_container_classes

import android.content.Intent
import com.johanlund.base_classes.Event
import com.johanlund.screens.events_container_classes.common.EventAdapter

//For view
interface EventsContainerUser: EventAdapter.EventAdapterUser {

    fun bindEventToList(event: Event)

    fun bindChangedEventToList (event: Event, posInList: Int)




    fun updateTagsInListOfEventsAfterTagTemplateChange()

    //for controller
    interface Listener{
        fun changeEventActivity(event: Event, eventType: Int, valueToReturn: Int, posInList: Int)
        fun executeChangedEvent(requestCode: Int, data: Intent)
    }
}