package com.johanlund.screens.load_import.mvc_views;

import com.johanlund.screens.common.mvcviews.ViewMvc;

public interface ImportOptionViewMvc extends ViewMvc {
    void setListener(ImportOptionViewMvcListener listener);

    String getTitleStr();

    interface ImportOptionViewMvcListener{
        void cleanAndLoad();
    }
}
