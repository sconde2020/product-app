package com.example.productapi.application.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationMessages {

    public final static String CATEGORY_EMPTY = "Category of a product cannot be null or empty";

    public final static String CATEGORY_TOO_LONG = "Category of a product must be at most 50 characters";

    public final static String NAME_EMPTY = "Name of a product cannot be null or empty";

    public final static String NAME_TOO_LONG = "Name of a product must be at most 100 characters";

    public final static String PRICE_EMPTY = "Price of a product cannot be null or empty";

    public final static String PRICE_BAD_VALUE = "Price of a product must be positive decimal";

    public final static String QUANTITY_EMPTY = "Quantity of a product cannot be null or empty";

    public final static String QUANTITY_BAD_VALUE = "Quantity of a product must be strictly positive integer";

    public final static String DESCRIPTION_EMPTY = "Description of a product cannot be null or empty";

    public final static String DESCRIPTION_TOO_LONG = "Description of a product must be at most 1000 characters";

    public static final String ID_BAD_VALUE = "Id of a product must be strictly positive integer";
}
