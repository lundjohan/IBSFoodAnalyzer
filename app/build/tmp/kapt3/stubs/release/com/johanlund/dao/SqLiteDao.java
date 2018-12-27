package com.johanlund.dao;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 10}, bv = {1, 0, 2}, k = 1, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0016J\u000e\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00100\u0016H\u0016J\u000e\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00180\u0016H\u0016J\u000e\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001a0\u0016H\u0016J\u0010\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001eH\u0016J\u0010\u0010\u001f\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001eH\u0016J\u0010\u0010 \u001a\u00020\u001e2\u0006\u0010!\u001a\u00020\u0014H\u0016J\u0010\u0010\"\u001a\u00020\f2\u0006\u0010#\u001a\u00020\u001eH\u0016J\u0010\u0010$\u001a\u00020\f2\u0006\u0010%\u001a\u00020\u001eH\u0016R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006&"}, d2 = {"Lcom/johanlund/dao/SqLiteDao;", "Lcom/johanlund/dao/Dao;", "c", "Landroid/content/Context;", "(Landroid/content/Context;)V", "getC", "()Landroid/content/Context;", "dbHandler", "Lcom/johanlund/database/DBHandler;", "getDbHandler", "()Lcom/johanlund/database/DBHandler;", "eventTypeAtSameTimeAlreadyExists", "", "type", "", "ldt", "Lorg/threeten/bp/LocalDateTime;", "fetchEventById", "Lcom/johanlund/base_classes/Event;", "id", "", "getAllBreaks", "", "getAllTagsWithTime", "Lcom/johanlund/base_classes/Tag;", "getCompleteTimes", "Lcom/johanlund/stat_backend/stat_util/ScoreTime;", "insertEventTemplatesFromExternalDatabase", "", "absolutePathToExternalDB", "", "insertTagTypesFromExternalDatabase", "retrieveNameOfTagTemplate", "idOfTagTemplate", "tagTemplateDoesntExist", "tagName", "tagTypeExists", "tagTypeName", "app_release"})
public final class SqLiteDao implements com.johanlund.dao.Dao {
    @org.jetbrains.annotations.NotNull()
    private final com.johanlund.database.DBHandler dbHandler = null;
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context c = null;
    
    @org.jetbrains.annotations.NotNull()
    public final com.johanlund.database.DBHandler getDbHandler() {
        return null;
    }
    
    @java.lang.Override()
    public boolean eventTypeAtSameTimeAlreadyExists(int type, @org.jetbrains.annotations.NotNull()
    org.threeten.bp.LocalDateTime ldt) {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.johanlund.base_classes.Event fetchEventById(long id) {
        return null;
    }
    
    @java.lang.Override()
    public boolean tagTemplateDoesntExist(@org.jetbrains.annotations.NotNull()
    java.lang.String tagName) {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.lang.String retrieveNameOfTagTemplate(long idOfTagTemplate) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.util.List<com.johanlund.base_classes.Tag> getAllTagsWithTime() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.util.List<com.johanlund.stat_backend.stat_util.ScoreTime> getCompleteTimes() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.util.List<org.threeten.bp.LocalDateTime> getAllBreaks() {
        return null;
    }
    
    @java.lang.Override()
    public boolean tagTypeExists(@org.jetbrains.annotations.NotNull()
    java.lang.String tagTypeName) {
        return false;
    }
    
    @java.lang.Override()
    public void insertTagTypesFromExternalDatabase(@org.jetbrains.annotations.NotNull()
    java.lang.String absolutePathToExternalDB) {
    }
    
    @java.lang.Override()
    public void insertEventTemplatesFromExternalDatabase(@org.jetbrains.annotations.NotNull()
    java.lang.String absolutePathToExternalDB) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.content.Context getC() {
        return null;
    }
    
    public SqLiteDao(@org.jetbrains.annotations.NotNull()
    android.content.Context c) {
        super();
    }
}