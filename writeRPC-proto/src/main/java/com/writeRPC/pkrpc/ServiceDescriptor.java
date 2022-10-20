package com.writeRPC.pkrpc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Arrays;

/*
 * 表示服务，服务描述
 * */
@Data//生成数据操作
@AllArgsConstructor//生成有参构造
@NoArgsConstructor//生成无参构造
public class ServiceDescriptor {
    private String clazz; //类名称
    private String method; //类中的方法
    private String returnType; //方法的返回值
    private String[] parameterTypes; //方法所需要的参数

    /**
     * 将类和方法转换成ServiceDescriptor类型
     * @param clazz 要转换的类
     * @param method 类中要转换的方法
     * @return 返回clazz与method的服务描述
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

    /**
     * 因为再服务管理注册中会将其放到HashMap中所以要复写equal方法
     *
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if(obj==null || getClass()!=obj.getClass()){ //先比较类是否相同
            return false;
        }
        ServiceDescriptor that=(ServiceDescriptor) obj;
        return this.toString().equals(that.toString()); //再比较其String
    }

    @Override
    public String toString() {
        return "clazz="+clazz+",method="+method+",returnType="+returnType+",parameterTypes="+ Arrays.toString(parameterTypes);

    }
}
