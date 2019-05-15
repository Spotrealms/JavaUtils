/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spotrealms.javautils;

//Imports
import java.net.*;
import java.io.*;

/**
 *
 * @author spotrealms
 */

public class PullURL {
	public static void main(String[] args){
		String output  = getUrlContents("http://spotrealms.com");
		System.out.println(output);
	}

	private static String getUrlContents(String srcUrl){
		StringBuilder content = new StringBuilder();
		try {
			URL url = new URL(srcUrl);
			URLConnection urlConnection = url.openConnection();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			String line;
			while ((line = bufferedReader.readLine()) != null){
				content.append(line + "\n");
			}
			bufferedReader.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return content.toString();
	}
}
