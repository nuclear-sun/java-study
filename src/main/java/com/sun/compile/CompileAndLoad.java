package com.sun.compile;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.Arrays;

public class CompileAndLoad {

    public static void main(String[] args) throws IOException {

        System.out.println(System.getProperty("java.io.tmpdir"));
        System.out.println(System.getProperty("java.user.dir"));

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromStrings(Arrays.asList("Simple.java"));
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null,
                null, compilationUnits);
        boolean success = task.call();
        fileManager.close();
        try {
            MyClassLoader loader = new MyClassLoader();
            Class<?> clazz = loader.loadClass("Simple");
            Method method = clazz.getDeclaredMethod("main", String[].class);
            method.invoke(null, (Object)null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class MyClassLoader extends ClassLoader {
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            try {

                FileInputStream fileInputStream = new FileInputStream(name + ".class");
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int read;
                while ((read = fileInputStream.read(buffer)) > 0) {
                    byteArrayOutputStream.write(buffer, 0, read);
                }
                byte[] targetBinary = byteArrayOutputStream.toByteArray();
                return super.defineClass(name, targetBinary, 0, targetBinary.length);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}


