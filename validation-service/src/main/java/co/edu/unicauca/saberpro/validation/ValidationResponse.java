package co.edu.unicauca.saberpro.validation;

import java.util.List;

public record ValidationResponse(boolean valid, List<String> errors) {}
