package com.lambdaschool.usermodel.exceptions;

import com.lambdaschool.usermodel.services.HelperFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CustomErrorDetails extends DefaultErrorAttributes {

    @Autowired
    HelperFunctions helperFunctions;

    @Override
    public Map<String, Object> getErrorAttributes(
            WebRequest webRequest,
            boolean includeStackTrace
    ) {
        Map<String, Object> errorAttributes =
                super.getErrorAttributes(webRequest, includeStackTrace);

        Map<String, Object> errorDetails = new LinkedHashMap<>();

        // title: "title"
        // status: 404 e.g.
        // details:
        // timestamp:
        // developermessage:
        // errors: list of validation errors

        errorDetails.put("title", errorAttributes.get("error"));
        errorDetails.put("status", errorAttributes.get("status"));
        errorDetails.put("details", errorAttributes.get("message"));
        errorDetails.put("timestamp", errorAttributes.get("timestamp"));
        errorDetails.put("developermessage", "path: " + errorAttributes.get("path"));

        errorDetails.put("errors", helperFunctions.getConstraintViolations(this.getError(webRequest)));
        return errorDetails;
    }
}