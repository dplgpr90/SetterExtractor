package main.java.setterextractor.executor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Executor {

	public void execute(String inputFile, String outputFile) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(inputFile));
		} catch (FileNotFoundException fnfe) {
			System.err.println("CONVERTER ERROR! Cannot find input file " + inputFile);
			System.exit(1);
		}

		Writer writer = null;
		try {
			writer = new FileWriter(outputFile);
		} catch (IOException ioe) {
			System.err.println("CONVERTER ERROR! Cannot create output file " + outputFile);
			System.exit(1);
		}

		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			line = reader.readLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		while (line != null) {
			sb.append(line);
			sb.append("\n");
			try {
				line = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			reader.close();
		} catch (IOException ioe) {
			System.err.println("CONVERTER ERROR! Cannot close file " + outputFile);
			System.exit(1);
		}

		String inputText = sb.toString();

		String stringaOutput = processString(inputText);

		try {
			writer.write(stringaOutput);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			writer.close();
		} catch (IOException ioe) {
			System.err.println("CONVERTER ERROR! Cannot close file " + outputFile);
			System.exit(1);
		}

		System.out.println("CONVERTER SUCCESS!");
	}

	private String processString(String inputText) {
		String output = "";

		String REGEX = "(?:(?:public)|(?:private)|(?:static)|(?:protected))(.)+(?:set)(.)+\n";

		Pattern p = Pattern.compile(REGEX);

		// get a matcher object
		Matcher m = p.matcher(inputText);

		while (m.find()) {
			output += inputText.substring(m.start(), m.end()) + "\n";
		}

		output = output.replaceAll("public void ", "object.").replaceAll("[{]", ";").replaceAll("\\((.)*\\)", "(parameter)").replaceAll("\n\n", "\n").replaceAll(" ;", ";");

		return output;

	}

}
