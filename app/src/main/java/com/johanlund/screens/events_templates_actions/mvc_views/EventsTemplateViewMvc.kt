package com.johanlund.screens.events_templates_actions.mvc_views

import com.johanlund.model.EventsTemplate
import com.johanlund.screens.common.mvcviews.ViewMvc
import com.johanlund.screens.common.mvcviews.WithOptionsMenuViewMvc
import com.johanlund.screens.events_container_classes.EventsContainerUser
import com.johanlund.screens.events_container_classes.common.EventsContainer
import com.johanlund.screens.events_container_classes.common.mvcviews.EventButtonsViewMvc
import org.threeten.bp.LocalDate

interface EventsTemplateViewMvc: WithOptionsMenuViewMvc, EventsContainerUser, EventButtonsViewMvc {
    fun bindEventsTemplateToView(et: EventsTemplate)
    fun bindDateToView(date: LocalDate)
    fun setListener(listener: Listener)
    fun createEventsTemplateFromView(): EventsTemplate
    fun getEventsContainer(): EventsContainer

    interface Listener: ViewMvc.Listener, EventsContainerUser.Listener, EventButtonsViewMvc.Listener {
        fun completeSession(finalEventsTemplate: EventsTemplate)
        fun removeTagTypesThatDontExist(et: EventsTemplate): EventsTemplate
    }
}