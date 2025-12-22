package br.com.projeto.controller;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.projeto.domain.Cliente;
import br.com.projeto.dto.ClienteDTO;
import br.com.projeto.mapper.ClienteMapper;
import br.com.projeto.service.ClienteService;

/**
 * ManagedBean para operações de Cliente.
 * Trabalha com DTOs para isolar a camada de apresentação das entidades JPA.
 */
@Named
@ViewScoped
public class ClienteBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ClienteBean.class.getName());
    
    @Inject
    private ClienteService clienteService;
    
    @Inject
    private ClienteMapper clienteMapper;
    
    private ClienteDTO cliente;
    private List<ClienteDTO> clientes;
    private ClienteDTO clienteSelecionado;
    
    @PostConstruct
    public void init() {
        cliente = new ClienteDTO();
        cliente.setDataCadastro(java.time.LocalDate.now());
        carregarClientes();
    }
    
    /**
     * Carrega a lista de clientes.
     */
    public void carregarClientes() {
        try {
            clientes = clienteService.listarTodos();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar clientes", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao carregar clientes", e.getMessage());
        }
    }
    
    /**
     * Salva um novo cliente ou atualiza um existente.
     */
    public void salvar() {
        try {
            // Validar CPF
            if (!clienteService.validarCpf(cliente.getCpf())) {
                addMessage(FacesMessage.SEVERITY_WARN, "CPF inválido", 
                    "Por favor, informe um CPF válido com 11 dígitos");
                return;
            }
            
            clienteService.salvar(cliente);
            addMessage(FacesMessage.SEVERITY_INFO, "Sucesso", 
                "Cliente salvo com sucesso!");
            
            limpar();
            carregarClientes();
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Validação falhou ao salvar cliente", e);
            addMessage(FacesMessage.SEVERITY_WARN, "Atenção", e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao salvar cliente", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro", 
                "Erro ao salvar cliente: " + e.getMessage());
        }
    }
    
    /**
     * Prepara para editar um cliente.
     */
    public void editar(ClienteDTO clienteDTO) {
        try {
            this.cliente = clienteService.buscarPorId(clienteDTO.getId());
            
            // Formatar CPF para exibição (adicionar pontos e traço)
            if (this.cliente.getCpf() != null && this.cliente.getCpf().length() == 11) {
                String cpfFormatado = this.cliente.getCpf().substring(0, 3) + "." +
                                     this.cliente.getCpf().substring(3, 6) + "." +
                                     this.cliente.getCpf().substring(6, 9) + "-" +
                                     this.cliente.getCpf().substring(9, 11);
                this.cliente.setCpf(cpfFormatado);
            }
            
            LOGGER.log(Level.INFO, "Cliente carregado para edição: {0}", clienteDTO.getId());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar cliente para edição", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro", 
                "Erro ao carregar cliente: " + e.getMessage());
        }
    }
    
    /**
     * Remove um cliente.
     */
    public void remover(ClienteDTO clienteDTO) {
        try {
            clienteService.remover(clienteDTO.getId());
            addMessage(FacesMessage.SEVERITY_INFO, "Sucesso", 
                "Cliente removido com sucesso!");
            carregarClientes();
        } catch (IllegalStateException e) {
            LOGGER.log(Level.WARNING, "Não foi possível remover cliente", e);
            addMessage(FacesMessage.SEVERITY_WARN, "Atenção", e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao remover cliente", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro", 
                "Erro ao remover cliente: " + e.getMessage());
        }
    }
    
    /**
     * Limpa o formulário.
     */
    public void limpar() {
        cliente = new ClienteDTO();
        cliente.setDataCadastro(java.time.LocalDate.now());
    }
    
    /**
     * Retorna lista de clientes como entidades (para uso no converter).
     */
    public List<Cliente> getClientesComoEntidades() {
        return clienteService.listarTodosComoEntidades();
    }
    
    /**
     * Adiciona mensagem ao contexto do JSF.
     */
    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(severity, summary, detail));
    }
    
    // Getters e Setters
    public ClienteDTO getCliente() {
        return cliente;
    }
    
    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }
    
    public List<ClienteDTO> getClientes() {
        return clientes;
    }
    
    public void setClientes(List<ClienteDTO> clientes) {
        this.clientes = clientes;
    }
    
    public ClienteDTO getClienteSelecionado() {
        return clienteSelecionado;
    }
    
    public void setClienteSelecionado(ClienteDTO clienteSelecionado) {
        this.clienteSelecionado = clienteSelecionado;
    }
}

