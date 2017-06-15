package main.java.setterextractor.executor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Executor {

	public void execute(String inputFile, String outputFile, String objectName) {
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

		String stringaOutput = processString(inputText, objectName);

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

	private String processString(String inputText, String objectName) {
		String output = "";

		String REGEX = "(?:(?:public)|(?:private)|(?:static)|(?:protected))(.)+(?:set)(.)+\n";

		Pattern p = Pattern.compile(REGEX);

		// get a matcher object
		Matcher m = p.matcher(inputText);

		while (m.find()) {
			output += inputText.substring(m.start(), m.end()) + "\n";
		}

		output = output.replaceAll("public void ", objectName + ".").replaceAll("[{]", ";")
				/* .replaceAll("\\((.)*\\)", "(parameters)") */.replaceAll("\n\n", "\n").replaceAll(" ;", ";");

		String REGEX_PARAMS = "\\((.)*\\)";

		Pattern p_params = Pattern.compile(REGEX_PARAMS);

		// get a matcher object
		Matcher m_params = p_params.matcher(output);
		
		String newOutput = output;
		
		while (m_params.find()) {
			
			String params = output.substring(m_params.start()+1, m_params.end()-1);

			String[] paramsArray = params.split(",");

			if (paramsArray != null && paramsArray.length > 0) {
				List<String> names = new ArrayList<String>();
				for (String param : paramsArray) {
					String name = null;
					String[] els = param.trim().split(" ");
					if (els != null && els.length > 0) {
						name = els[els.length - 1];
						names.add(name);
					}
					
				}
				
				if (names != null && !names.isEmpty()) {
					String paramsNames = "";
					for (String string : names) {
						paramsNames += string + ", ";
					}
					paramsNames = paramsNames.substring(0, paramsNames.length() - 2);
					
					
					newOutput = newOutput.replace(params, paramsNames);
				}
			}

		}

		return newOutput;

	}

}
