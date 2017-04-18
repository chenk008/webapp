package org.ck.webapp.springReactor1;

import java.lang.reflect.Method;

import org.ck.webapp.springReactor.MyReactiveLibrary;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class SpringSubContext {

	public static void testSubContext(ApplicationContext rootContext) throws Exception {
		ClassLoader classLoader = new MyURLClassLoader("target/classes", SpringSubContext.class.getClassLoader());
		PathMatchingResourcePatternResolver classResolver = new PathMatchingResourcePatternResolver(classLoader);
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.setParent(rootContext);
		context.setResourceLoader(classResolver);
		context.scan("org.ck.webapp.springReactor1");
		context.refresh();
		// 必须用这个classloader
		Class<?> targetClass = classLoader.loadClass("org.ck.webapp.springReactor1.MyReactiveLibraryTest");
		Method targetMethod = targetClass.getMethod("say");
		targetMethod.invoke(context.getBean(targetClass));

		rootContext.getBean(MyReactiveLibrary.class).say();
		context.close();
	}
}
