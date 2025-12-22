package br.com.projeto.converter;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.projeto.domain.Cliente;
import br.com.projeto.service.ClienteService;

/**
 * Conversor JSF para a entidade Cliente.
 * Permite usar objetos Cliente diretamente em componentes como selectOneMenu.
 */
@FacesConverter(value = "clienteConverter")
public class ClienteConverter implements Converter<Cliente> {
    
    private ClienteService getClienteService() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            ValueExpression ve = facesContext.getApplication()
                .getExpressionFactory()
                .createValueExpression(facesContext.getELContext(), "#{clienteService}", ClienteService.class);
            return (ClienteService) ve.getValue(facesContext.getELContext());
        }
        return null;
    }
    
    @Override
    public Cliente getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty() || "null".equals(value)) {
            return null;
        }
        
        try {
            Long id = Long.parseLong(value);
            ClienteService service = getClienteService();
            if (service != null) {
                return service.buscarEntidadePorId(id);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public String getAsString(FacesContext context, UIComponent component, Cliente cliente) {
        if (cliente == null || cliente.getId() == null) {
            return "";
        }
        return cliente.getId().toString();
    }
}
