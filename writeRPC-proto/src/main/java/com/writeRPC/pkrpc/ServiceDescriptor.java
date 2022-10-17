package com.writeRPC.pkrpc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Arrays;

/*
 * 表示服务*/
@Data//生成数据操作
@AllArgsConstructor//生成有参构造
@NoArgsConstructor//生成无参构造
public class ServiceDescriptor {
    private String clazz;
    private String method;
    private String returnType;
    private String[] parameterTypes;

    /**
     * 将类和方法转换成ServiceDescriptor类型
     * @param clazz 要转换的类
     * @param method 类中要转换的方法
     * @return
     */
    public static ServiceDescriptor from(Class clazz, Method method){

        ServiceDescriptor sdp=new ServiceDescriptor();

        sdp.setClazz(clazz.getName());
        sdp.setMethod(method.getName());
        sdp.setReturnType(method.getReturnType().getName());
        //参数
        Class[] parameterClasses = method.getParameterTypes();
        String[] parameterTypes=new String[parameterClasses.length];
        for (int i=0;i< parameterTypes.length;i++){
            parameterTypes[i]=parameterClasses[i].getName();
        }
        sdp.setParameterTypes(parameterTypes);
        return sdp;

    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if(obj==null || getClass()!=obj.getClass()){
            return false;
        }
        ServiceDescriptor that=(ServiceDescriptor) obj;
        return this.toString().equals(that.toString());
    }

    @Override
    public String toString() {
        return "clazz="+clazz+",method="+method+",returnType="+returnType+",parameterTypes="+ Arrays.toString(parameterTypes);

    }
}
