package com.transformer.request;

import com.transformer.validate.Validation;

import java.io.Serializable;

public interface Request extends Validation, Serializable {
    boolean validator();
}
