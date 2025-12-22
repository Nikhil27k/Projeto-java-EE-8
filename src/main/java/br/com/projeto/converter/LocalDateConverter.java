package br.com.projeto.converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 * Converter para LocalDate no JSF.
 */
@FacesConverter("localDateConverter")
public class LocalDateConverter implements Converter<LocalDate> {
    
    private static final String DEFAULT_PATTERN = "dd/MM/yyyy";
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_PATTERN);
    
    @Override
    public LocalDate getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        
        try {
            // Tentar parse com o padrão padrão
            return LocalDate.parse(value, DEFAULT_FORMATTER);
        } catch (DateTimeParseException e) {
            // Tentar parse com formato ISO (yyyy-MM-dd)
            try {
                return LocalDate.parse(value);
            } catch (DateTimeParseException e2) {
                throw new ConverterException("Data inválida: " + value, e2);
            }
        }
    }
    
    @Override
    public String getAsString(FacesContext context, UIComponent component, LocalDate value) {
        if (value == null) {
            return "";
        }
        
        try {
            return value.format(DEFAULT_FORMATTER);
        } catch (Exception e) {
            throw new ConverterException("Erro ao formatar data: " + value, e);
        }
    }
}

