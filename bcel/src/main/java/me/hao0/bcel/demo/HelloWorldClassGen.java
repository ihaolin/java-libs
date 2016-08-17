package me.hao0.bcel.demo;


import org.apache.bcel.Constants;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Example :

 public class HelloWorld {
    public void hello(String str) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String name = null;

        try {
            System.out.print("Please enter your name> ");
            name = in.readLine();
        } catch(IOException e) { return; }

        System.out.println("Hello, " + name);
    }
 }


 * Author: haolin
 * On: 12/28/14
 *
 */
public class HelloWorldClassGen {

    public static void main(String[] args) throws IOException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        ClassGen hellWorldClazz = new ClassGen("HelloWorld", "java.lang.Object",
                "<generated>", 1 | 32,
                null);
        ConstantPoolGen constantPoolGen = hellWorldClazz.getConstantPool(); // cg creates constant pool
        InstructionList instructions = new InstructionList();

        MethodGen helloMethod = new MethodGen(1, // access flags
                Type.VOID,                               // return type
                new Type[] { Type.STRING },              // arg types
                new String[] { "str" },                  // arg names
                "hello", "HelloWorld",                   // method, class
                instructions, constantPoolGen);

        InstructionFactory factory = new InstructionFactory(hellWorldClazz);

        // Create object type
        ObjectType i_stream = new ObjectType("java.io.InputStream");
        ObjectType p_stream = new ObjectType("java.io.PrintStream");

        instructions.append(factory.createNew("java.io.BufferedReader"));
        instructions.append(InstructionConstants.DUP); // Use predefined constant
        instructions.append(factory.createNew("java.io.InputStreamReader"));
        instructions.append(InstructionConstants.DUP);

        // Create static method invoke: System.in
        instructions.append(factory.createFieldAccess("java.lang.System", "in", i_stream,
                Constants.GETSTATIC));

        // Create constructor method invoke : new InputStreamReader()
        instructions.append(factory.createInvoke("java.io.InputStreamReader", "<init>",
                Type.VOID, new Type[]{i_stream},
                Constants.INVOKESPECIAL));

        // Create constructor method invoke : new BufferedReader()
        instructions.append(factory.createInvoke("java.io.BufferedReader", "<init>", Type.VOID,
                new Type[]{new ObjectType("java.io.Reader")},
                Constants.INVOKESPECIAL));

        // Create a local variable in: Ljava/io/BufferedReader;
        LocalVariableGen inVar = helloMethod.addLocalVariable("in",
                new ObjectType("java.io.BufferedReader"), null, null);
        int inVarIndex = inVar.getIndex();
        // "in" valid from here
        inVar.setStart(instructions.append(new ASTORE(inVarIndex)));

        // Create local variable name: Ljava/lang/String;
        LocalVariableGen nameVar = helloMethod.addLocalVariable("name", Type.STRING, null, null);
        int nameVarIndex = nameVar.getIndex();
        instructions.append(InstructionConstants.ACONST_NULL);
        // "name" valid from here
        nameVar.setStart(instructions.append(new ASTORE(nameVarIndex)));

        // Create try-catch block
        // create static method invoke: System.out
        InstructionHandle try_start =
                instructions.append(factory.createFieldAccess("java.lang.System", "out", p_stream,
                        Constants.GETSTATIC));
        // create constant : Please enter your name>
        instructions.append(new PUSH(constantPoolGen, "Please enter your name> "));
        // invoke object method: java.io.PrintStream.print
        instructions.append(factory.createInvoke("java.io.PrintStream", "print", Type.VOID,
                new Type[] { Type.STRING },
                Constants.INVOKEVIRTUAL));
        // "in"
        instructions.append(new ALOAD(inVarIndex));
        // java.io.BufferedReader.readLine()
        instructions.append(factory.createInvoke("java.io.BufferedReader", "readLine",
                Type.STRING, Type.NO_ARGS,
                Constants.INVOKEVIRTUAL));
        // "name"
        instructions.append(new ASTORE(nameVarIndex));

        GOTO g = new GOTO(null);
        InstructionHandle try_end = instructions.append(g);

        // catch and return;
        InstructionHandle doReturn = instructions.append(InstructionConstants.RETURN);
        helloMethod.addExceptionHandler(try_start, try_end, doReturn, new ObjectType("java.io.IOException"));

        // println invoke
        InstructionHandle println =
                instructions.append(factory.createFieldAccess("java.lang.System", "out", p_stream,
                        Constants.GETSTATIC));
        g.setTarget(println);

        // System.out.println("Hello" + name);
        instructions.append(factory.createNew(Type.STRINGBUFFER));
        instructions.append(InstructionConstants.DUP);
        instructions.append(new PUSH(constantPoolGen, "Hello, "));
        instructions.append(factory.createInvoke("java.lang.StringBuffer", "<init>",
                Type.VOID, new Type[]{Type.STRING},
                Constants.INVOKESPECIAL));
        instructions.append(new ALOAD(nameVarIndex));
        instructions.append(factory.createInvoke("java.lang.StringBuffer", "append",
                Type.STRINGBUFFER, new Type[] { Type.STRING },
                Constants.INVOKEVIRTUAL));
        instructions.append(factory.createInvoke("java.lang.StringBuffer", "toString",
                Type.STRING, Type.NO_ARGS,
                Constants.INVOKEVIRTUAL));
        instructions.append(factory.createInvoke("java.io.PrintStream", "println",
                Type.VOID, new Type[] { Type.STRING },
                Constants.INVOKEVIRTUAL));
        instructions.append(InstructionConstants.RETURN);

        helloMethod.setMaxStack();
        hellWorldClazz.addMethod(helloMethod.getMethod());
        instructions.dispose(); // Allow instruction handles to be reused
        hellWorldClazz.addEmptyConstructor(1);

        JavaClass klass = hellWorldClazz.getJavaClass();

        MyClassLoader classLoader = new MyClassLoader();
        Object helloWorld = classLoader.defineClass("HelloWorld", klass.getBytes()).newInstance();

        Method m = helloWorld.getClass().getMethod("hello", String.class);
        m.invoke(helloWorld, "linhao");
        //klass.dump("/Users/haolin/HelloWorld.class");
    }
}
