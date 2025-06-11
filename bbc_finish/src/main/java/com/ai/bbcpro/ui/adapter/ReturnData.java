package com.ai.bbcpro.ui.adapter;

import com.ai.bbcpro.ui.http.bean.SumBean;

import java.util.List;

public interface ReturnData {
    void loadSuccess(List<SumBean.DataDTO> data);
}
