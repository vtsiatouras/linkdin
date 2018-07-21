package com.linkdin.app.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ImageStorageService {

    private static String m1(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "abcdefghijklmnopqrstuvwxyz"
                + "0123456789";
        String str = new Random().ints(length, 0, chars.length())
                .mapToObj(i -> "" + chars.charAt(i))
                .collect(Collectors.joining());
        return str;
    }

    private static String m2(int length) {
        String chars = "0123456789";
        String str = new Random().ints(length, 0, chars.length())
                .mapToObj(i -> "" + chars.charAt(i))
                .collect(Collectors.joining());
        return str;
    }

    public String storeImage(MultipartFile profileImage) {
        // Store image
        // Save all images at this path
        new File("user_images").mkdirs();

        // Generate random name for the image
        String imageName = m1(20);
        String imagePathName = "user_images/"+imageName;
        File userImg = new File(imagePathName);

        try {
            userImg.createNewFile();
            FileOutputStream fos = new FileOutputStream(userImg);
            fos.write(profileImage.getBytes());
            fos.close();
            return imageName;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public File retrieveImage(String imageName) {
        return null;
    }

}
