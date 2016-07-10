package me.hao0.bcel.aop;

import org.apache.bcel.Constants;
import org.apache.bcel.generic.*;
import java.lang.reflect.Method;
import static com.sun.tools.classfile.AccessFlags.ACC_PRIVATE;
import static com.sun.tools.classfile.AccessFlags.ACC_PUBLIC;
import static com.sun.tools.classfile.AccessFlags.ACC_SUPER;

/**
 * Author: haolin
 * On: 12/28/14
 */
public abstract class ProxyClass {

    /**
     * 生成代理类
     * @param proxyed 被代理类对象
     * @param interceptor 拦截器
     * @return 代理类
     */
    public static Object getProxyClass(Object proxyed, Interceptor interceptor){
        Class proxyedClass = proxyed.getClass();
        String proxyClassName = proxyedClass.getSimpleName()+ "Proxy";
        ClassGen proxyClassGen = new ClassGen(proxyClassName, proxyedClass.getName(),
                "<generated>", ACC_PUBLIC | ACC_SUPER,
                null);
        // add interceptor field
        addField(proxyClassGen, ACC_PRIVATE, "interceptor", interceptor.getClass());
        // add target object field
        addField(proxyClassGen, ACC_PRIVATE, "proxyed", proxyed.getClass());

        Method[] methods = proxyed.getClass().getMethods();
        for (Method method : methods){
            addProxyMethod(proxyClassGen, proxyed, method, interceptor);
        }
        return null;
    }

    private static void addField(ClassGen proxyClassGen, int access_flags, String fieldName, Class<?> classType) {
        FieldGen interceptorField = new FieldGen(
                access_flags,
                Type.getType(classType),
                fieldName,
                proxyClassGen.getConstantPool());
        proxyClassGen.addField(interceptorField.getField());
    }

    private static void addProxyMethod(ClassGen proxyClass, Object targetObject, Method targetMethod, Interceptor interceptor) {

        ConstantPoolGen constantPoolGen = proxyClass.getConstantPool();

        InstructionList instructions = new InstructionList();

        Class[] targetMethodParamTypes = targetMethod.getParameterTypes();
        Type[] argTypes = new Type[targetMethodParamTypes.length];
        String[] argNames = new String[targetMethodParamTypes.length];
        for (int i=0; i<targetMethodParamTypes.length; i++){
            argTypes[i] = Type.getType(targetMethodParamTypes[i]);
            argNames[i] = "p" + i;
        }

        MethodGen proxyMethod = new MethodGen(
                targetMethod.getModifiers(),
                Type.getType(targetMethod.getReturnType()),
                argTypes,
                argNames,
                targetMethod.getName(), proxyClass.getClassName(),
                instructions, constantPoolGen);

        InstructionFactory instructionFactory = new InstructionFactory(proxyClass);

        // interceptor
        instructions.append(instructionFactory.createFieldAccess(
                proxyClass.getClassName(), "interceptor",
                new ObjectType(interceptor.getClass().getName()), Constants.GETFIELD));

        // proxyed
        instructions.append(instructionFactory.createFieldAccess(
                proxyClass.getClassName(), "proxyed",
                new ObjectType(targetObject.getClass().getName()), Constants.GETFIELD));

        instructions.append(instructionFactory.createInvoke(
                interceptor.getClass().getName(),
                proxyMethod.getName(),
                proxyMethod.getReturnType(),
                argTypes,
                Constants.INVOKEVIRTUAL
        ));

        proxyMethod.setMaxStack();
        proxyClass.addMethod(proxyMethod.getMethod());
        instructions.dispose();
    }
}
