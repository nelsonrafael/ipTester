package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class Tester {
	private String outputPath1 = "C:\\Users\\NelsonMartins\\Pictures\\gama1.txt";
	private String outputPath14 = "C:\\Users\\NelsonMartins\\Pictures\\gama14.txt";

	private String[] gama1 = new String[256];
	private String[] gama14 = new String[256];
	private String date_DMY;

	private void initializeGama() {
		try (BufferedReader br = new BufferedReader(new FileReader(outputPath1))) {
			String line, temp;
			int index;
			while ((line = br.readLine()) != null) {
				if (line.contains("\t")) {
					index = line.indexOf("\t");					
					temp = line.substring(10, index);
					index = Integer.parseInt(temp);					
					gama1[index] = line;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("FINISHED READING GAMA1.TXT");

		try (BufferedReader br = new BufferedReader(new FileReader(outputPath14))) {
			String line, temp;
			int index;
			while ((line = br.readLine()) != null) {
				if (line.contains("\t")) {
					index = line.indexOf("\t");
					temp = line.substring(11, index);
					index = Integer.parseInt(temp);
					gama14[index] = line;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("FINISHED READING GAMA14.TXT");
	}

	private void appendToFile(String ipAddress, String path) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(path, true));
			bw.write(ipAddress);
			bw.newLine();
			bw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (IOException ioe2) {
				}
		}
	}

	private void setTime() {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		this.date_DMY = "";
		if (cal.get(Calendar.DAY_OF_MONTH) < 10)
			this.date_DMY += "0";
		this.date_DMY += cal.get(Calendar.DAY_OF_MONTH) + "/";
		if (cal.get(Calendar.MONTH) < 10)
			this.date_DMY += "0";
		this.date_DMY += cal.get(Calendar.MONTH) + "/";
		this.date_DMY += cal.get(Calendar.YEAR);
	}

	private void sendPingRequest(String ipAddress, int index, String path) throws UnknownHostException, IOException {
		InetAddress ia = InetAddress.getByName(ipAddress);
		if (ia.isReachable(100)) {
			ipAddress += "\t" + date_DMY;
			appendToFile(ipAddress, path);
		}
		else {
			if (path == outputPath1) {
				if (gama1[index] != null) {
					appendToFile(gama1[index], path);
				}
			} else {
				if (gama14[index] != null) {
					appendToFile(gama14[index], path);
				}
			}
		}
	}

	public Tester() {
		Arrays.fill(gama1, null);
		Arrays.fill(gama14, null);
		File outputFile1 = new File(outputPath1), outputFile14 = new File(outputPath14);
		try {
			outputFile1.createNewFile();
			outputFile14.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("FILES EITHER EXISTED OR WERE CREATED.");
		setTime();
		initializeGama();
		if (outputFile1.delete()) {
			System.out.println("GAMA1.TXT DELETED SUCCESSFULLY.");
		} else {
			System.out.println("ERROR: GAMA1.TXT NOT DELETED!!!");
		}
		if (outputFile14.delete()) {
			System.out.println("GAMA14.TXT DELETED SUCCESSFULLY.");
		} else {
			System.out.println("ERROR: GAMA14.TXT NOT DELETED!!!");
		}
		try {
			outputFile1.createNewFile();
			outputFile14.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String ip = "192.168.1.", temp;

		for (int j = 0; j < 256; j++) {
			temp = ip + j;
			try {
				sendPingRequest(temp, j, outputPath1);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("FINISHED WRITING GAMA1.TXT");
		ip = "192.168.14.";
		for (int j = 0; j < 256; j++) {
			temp = ip + j;
			try {
				sendPingRequest(temp, j, outputPath14);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("FINISHED WRITING GAMA14.TXT");
	}

	public static void main(String[] args) {
		System.out.println("PROGRAM STARTED.");
		new Tester();
		System.out.println("PROGRAM FINISHED.");
	}
}
