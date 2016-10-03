/**
 * 
 */
package com.nerve24;

import java.util.Scanner;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author JOGIREDDY
 *
 */
public class PasswordEncoderUtil {
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		Scanner scanner = new Scanner(System.in);
		String rawPassword = scanner.next();
		
		System.out.println(passwordEncoder.encode(rawPassword));
		scanner.close();

	}

}
