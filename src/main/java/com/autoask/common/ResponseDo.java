package com.autoask.common;

/**
 * @author hyy
 * @craete 2016/7/22 17:30
 */
public class ResponseDo {

    private int errCode;

    private String errMsg;

    private Object data;

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    public static ResponseDo buildSuccess(Object data) {
        ResponseDo responseDo = new ResponseDo();
        responseDo.setData(data);
        responseDo.setErrCode(0);
        responseDo.setErrMsg("");
        return responseDo;
    }

    public static ResponseDo buildError(String errMsg) {
        ResponseDo responseDo = new ResponseDo();
        responseDo.setData(null);
        responseDo.setErrMsg(errMsg);
        responseDo.setErrCode(1);
        return responseDo;
    }

    public static ResponseDo buildError(int errCode, String errMsg) {
        ResponseDo responseDo = new ResponseDo();
        responseDo.setData(null);
        responseDo.setErrMsg(errMsg);
        responseDo.setErrCode(errCode);
        return responseDo;
    }
}
