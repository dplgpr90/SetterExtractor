package test.java;

import java.util.Calendar;

import main.java.setterextractor.executor.Executor;

public class TestClass {
	/** The test resource path. */
	private static final String testResourcePath = (Executor.class.getProtectionDomain().getCodeSource().getLocation()
			.getPath()
			.substring(0, Executor.class.getProtectionDomain().getCodeSource().getLocation().getPath().length() - 4)
			+ "src/test/resources/").replace("%20", " ");

	/** The input file. */
	private static final String inputFile = testResourcePath + "input.txt";

	/** The output file. */
	private static final String outputFile = testResourcePath + "converted_" + Calendar.getInstance().getTime().toString().replace(" ", "_").replace(":", "_") + Calendar.getInstance().getTimeInMillis()
			+ ".txt";
	
	private static final String objectName = "param";

	public static void main(String[] args) {
		Executor e = new Executor();
		e.execute(inputFile, outputFile, objectName);
	}
}
