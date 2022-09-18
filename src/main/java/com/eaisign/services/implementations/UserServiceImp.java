package com.eaisign.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eaisign.exceptions.UserNotFoundException;
import com.eaisign.models.User;
import com.eaisign.repository.UserRepository;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;


@Service
public class UserServiceImp {
	
	private final UserRepository  userRepository;
	
	public UserServiceImp(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	public User findUser(Long id) throws UserNotFoundException {
		User user = userRepository.findById(id).orElse(null);
		if(user==null) {
			throw new UserNotFoundException();
		}else {
			return user;
		}
	}
	public  String desEncrypt(String passwordEncrypted) throws Exception {

		try
		{
			String key = "1234567812345678";
			String iv = "1234567812345678";


			byte[] encrypted1 = Base64.getDecoder().decode(passwordEncrypted);
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
			IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

			cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

			byte[] original = cipher.doFinal(encrypted1);
			String originalString = new String(original);
			originalString=originalString.trim();
			System.out.println(originalString);
			return originalString;
		}
		catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

}
