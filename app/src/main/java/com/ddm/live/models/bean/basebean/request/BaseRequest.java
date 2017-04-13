package com.ddm.live.models.bean.basebean.request;

/**
 * Created by cxx on 16/8/8.
 */
public class BaseRequest {
    private String group;
    private Integer action;
    private String interfaceName;
    private String interfaceServiceName;
    private String methodName;
    private Boolean hasParameter;//接口是否带参

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Boolean getHasParameter() {
        return hasParameter;
    }

    public void setHasParameter(Boolean hasParameter) {
        this.hasParameter = hasParameter;
    }

    public String getInterfaceServiceName() {
        return interfaceServiceName;
    }

    public void setInterfaceServiceName(String interfaceServiceName) {
        this.interfaceServiceName = interfaceServiceName;
    }
}
