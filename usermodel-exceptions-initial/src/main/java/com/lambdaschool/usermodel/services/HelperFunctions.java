package com.lambdaschool.usermodel.services;

import com.lambdaschool.usermodel.models.ValidationError;

import java.util.List;

public interface HelperFunctions {

    List<ValidationError> getConstraintViolations(Throwable cause);

}
