package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

public class Server {
	private static File appConfig;
	private static FileInputStream propsInput;
	private static Properties prop;
	private static String filePath = new File("src/main/java/config/server.config").getAbsolutePath();

	public static void loadInputs() {
		if (appConfig == null)
			appConfig = new File(filePath);
		if (propsInput == null) {
			try {
				propsInput = new FileInputStream(appConfig);
			} catch (FileNotFoundException e) {
				System.out.println(e);
			}
		}
		if (prop == null) {
			prop = new Properties();
			try {
				prop.load(propsInput);
			} catch (IOException e) {
				System.out.println(e);
			}
		}

	}

	public static void setProp(String key, String value, String comments) {
		try (Writer inputStream = new FileWriter(appConfig)) {
			prop.setProperty(key, value);
			prop.store(inputStream, comments == null ? "" : comments);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static String getProp(String key, String defaultValue) {
		try (Reader readStream = new FileReader(appConfig)) {
			return prop.getProperty(key, defaultValue == null ? "" : defaultValue);
		} catch (IOException e) {
			System.out.println(e);
			return "";
		}
	}

}
