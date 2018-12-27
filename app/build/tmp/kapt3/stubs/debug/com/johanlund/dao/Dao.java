package com.johanlund.dao;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 10}, bv = {1, 0, 2}, k = 1, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH&J\u000e\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00070\rH&J\u000e\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\rH&J\u000e\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\rH&J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0016J\u0010\u0010\u0016\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H&J\u0010\u0010\u0017\u001a\u00020\u00152\u0006\u0010\u0018\u001a\u00020\u000bH&J\u0010\u0010\u0019\u001a\u00020\u00032\u0006\u0010\u001a\u001a\u00020\u0015H&J\u0010\u0010\u001b\u001a\u00020\u00032\u0006\u0010\u001c\u001a\u00020\u0015H&\u00a8\u0006\u001d"}, d2 = {"Lcom/johanlund/dao/Dao;", "", "eventTypeAtSameTimeAlreadyExists", "", "type", "", "ldt", "Lorg/threeten/bp/LocalDateTime;", "fetchEventById", "Lcom/johanlund/base_classes/Event;", "id", "", "getAllBreaks", "", "getAllTagsWithTime", "Lcom/johanlund/base_classes/Tag;", "getCompleteTimes", "Lcom/johanlund/stat_backend/stat_util/ScoreTime;", "insertEventTemplatesFromExternalDatabase", "", "pathToExternal", "", "insertTagTypesFromExternalDatabase", "retrieveNameOfTagTemplate", "idOfTagTemplate", "tagTemplateDoesntExist", "tagName", "tagTypeExists", "tagTypeName", "app_debug"})
public abstract interface Dao {
    
    public abstract boolean eventTypeAtSameTimeAlreadyExists(int type, @org.jetbrains.annotations.NotNull()
    org.threeten.bp.LocalDateTime ldt);
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.johanlund.base_classes.Event fetchEventById(long id);
    
    @org.jetbrains.annotations.NotNull()
    public abstract java.lang.String retrieveNameOfTagTemplate(long idOfTagTemplate);
    
    public abstract boolean tagTemplateDoesntExist(@org.jetbrains.annotations.NotNull()
    java.lang.String tagName);
    
    @org.jetbrains.annotations.NotNull()
    public abstract java.util.List<com.johanlund.base_classes.Tag> getAllTagsWithTime();
    
    @org.jetbrains.annotations.NotNull()
    public abstract java.util.List<com.johanlund.stat_backend.stat_util.ScoreTime> getCompleteTimes();
    
    @org.jetbrains.annotations.NotNull()
    public abstract java.util.List<org.threeten.bp.LocalDateTime> getAllBreaks();
    
    public abstract boolean tagTypeExists(@org.jetbrains.annotations.NotNull()
    java.lang.String tagTypeName);
    
    public abstract void insertTagTypesFromExternalDatabase(@org.jetbrains.annotations.NotNull()
    java.lang.String pathToExternal);
    
    public abstract void insertEventTemplatesFromExternalDatabase(@org.jetbrains.annotations.NotNull()
    java.lang.String pathToExternal);
    
    @kotlin.Metadata(mv = {1, 1, 10}, bv = {1, 0, 2}, k = 3)
    public final class DefaultImpls {
        
        public static void insertEventTemplatesFromExternalDatabase(com.johanlund.dao.Dao $this, @org.jetbrains.annotations.NotNull()
        java.lang.String pathToExternal) {
        }
    }
}