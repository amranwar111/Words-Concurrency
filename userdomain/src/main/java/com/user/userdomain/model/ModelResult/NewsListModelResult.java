
package com.user.userdomain.model.ModelResult;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsListModelResult {

    @SerializedName("result")
    @Expose
    private List<ResultData> result = null;
    @SerializedName("curren_page")
    @Expose
    private Integer currenPage;
    @SerializedName("Last_page")
    @Expose
    private Integer lastPage;
    @SerializedName("total")
    @Expose
    private Integer total;

    public List<ResultData> getResult() {
        return result;
    }

    public void setResult(List<ResultData> result) {
        this.result = result;
    }

    public Integer getCurrenPage() {
        return currenPage;
    }

    public void setCurrenPage(Integer currenPage) {
        this.currenPage = currenPage;
    }

    public Integer getLastPage() {
        return lastPage;
    }

    public void setLastPage(Integer lastPage) {
        this.lastPage = lastPage;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

}
