package com.lambdaschool.usermodel.services;

import com.lambdaschool.usermodel.exceptions.ResourceNotFoundException;
import com.lambdaschool.usermodel.models.ValidationError;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Service(value = "helperFunctions")
public class HelperFunctionsImpl implements HelperFunctions{

    @Override
    public List<ValidationError> getConstraintViolations(Throwable cause) {

        // ConstraintViolationExceptions
        // loop through the bundled causes
        while ((cause != null) && !(cause instanceof ConstraintViolationException)) {
            cause = cause.getCause();
        }

        List<ValidationError> listVE = new ArrayList<>();

        if (cause != null) {
            ConstraintViolationException ex = (ConstraintViolationException) cause;
            for (ConstraintViolation cv : ex.getConstraintViolations()) {
                ValidationError newVe = new ValidationError();
                newVe.setCode(cv.getInvalidValue().toString());
                newVe.setMessage(cv.getMessage());
                listVE.add(newVe);
            }
        }
        return listVE;
    }

    @Override
    public boolean isAuthorizedToMakeChange(String username) {
        // true - if authenticated user is changing themselves
        // true - if authenticated user is admin

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (username.equalsIgnoreCase(authentication.getName()) ||
            authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return true;
        } else {
            throw new ResourceNotFoundException(authentication.getName() + " not authorized to make changes");
        }
    }
}
