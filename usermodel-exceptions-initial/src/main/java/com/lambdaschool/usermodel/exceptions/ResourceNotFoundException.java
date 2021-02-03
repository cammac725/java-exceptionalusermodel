package com.lambdaschool.usermodel.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super("Error from a CamMacDev Application: " + message);
    }
}
