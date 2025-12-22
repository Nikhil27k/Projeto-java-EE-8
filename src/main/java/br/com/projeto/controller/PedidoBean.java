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
import br.com.projeto.dto.PedidoDTO;
import br.com.projeto.service.ClienteService;
import br.com.projeto.service.PedidoService;

/**
 * ManagedBean para operações de Pedido.
 * Trabalha com DTOs para isolar a camada de apresentação das entidades JPA.
 */
@Named
@ViewScoped
public class PedidoBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(PedidoBean.class.getName());
    
    @Inject
    private PedidoService pedidoService;
    
    @Inject
    private ClienteService clienteService;
    
    private PedidoDTO pedido;
    private List<PedidoDTO> pedidos;
    private List<Cliente> clientes; // Mantido como entidade para o converter funcionar
    private Long clienteIdFiltro;
    
    @PostConstruct
    public void init() {
        pedido = new PedidoDTO();
        pedido.setDataPedido(java.time.LocalDate.now());
        carregarPedidos();
        carregarClientes();
    }
    
    /**
     * Carrega a lista de pedidos.
     */
    public void carregarPedidos() {
        try {
            if (clienteIdFiltro != null && clienteIdFiltro > 0) {
                pedidos = pedidoService.listarPorCliente(clienteIdFiltro);
            } else {
                pedidos = pedidoService.listarTodos();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar pedidos", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao carregar pedidos", e.getMessage());
        }
    }
    
    /**
     * Carrega a lista de clientes para o combo (como entidades para o converter).
     */
    public void carregarClientes() {
        try {
            clientes = clienteService.listarTodosComoEntidades();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar clientes", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao carregar clientes", e.getMessage());
        }
    }
    
    /**
     * Salva um novo pedido ou atualiza um existente.
     */
    public void salvar() {
        try {
            // Validar cliente selecionado
            if (pedido.getClienteId() == null) {
                addMessage(FacesMessage.SEVERITY_WARN, "Atenção", 
                    "Selecione um cliente para o pedido");
                return;
            }
            
            pedidoService.salvar(pedido);
            addMessage(FacesMessage.SEVERITY_INFO, "Sucesso", 
                "Pedido salvo com sucesso!");
            
            limpar();
            carregarPedidos();
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Validação falhou ao salvar pedido", e);
            addMessage(FacesMessage.SEVERITY_WARN, "Atenção", e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao salvar pedido", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro", 
                "Erro ao salvar pedido: " + e.getMessage());
        }
    }
    
    /**
     * Prepara para editar um pedido.
     */
    public void editar(PedidoDTO pedidoDTO) {
        try {
            this.pedido = pedidoService.buscarPorId(pedidoDTO.getId());
            LOGGER.log(Level.INFO, "Pedido carregado para edição: {0}", pedidoDTO.getId());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar pedido para edição", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro", 
                "Erro ao carregar pedido: " + e.getMessage());
        }
    }
    
    /**
     * Remove um pedido.
     */
    public void remover(PedidoDTO pedidoDTO) {
        try {
            pedidoService.remover(pedidoDTO.getId());
            addMessage(FacesMessage.SEVERITY_INFO, "Sucesso", 
                "Pedido removido com sucesso!");
            carregarPedidos();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao remover pedido", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro", 
                "Erro ao remover pedido: " + e.getMessage());
        }
    }
    
    /**
     * Filtra pedidos por cliente.
     */
    public void filtrarPorCliente() {
        carregarPedidos();
    }
    
    /**
     * Limpa o formulário.
     */
    public void limpar() {
        pedido = new PedidoDTO();
        pedido.setDataPedido(java.time.LocalDate.now());
    }
    
    /**
     * Adiciona mensagem ao contexto do JSF.
     */
    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(severity, summary, detail));
    }
    
    // Getters e Setters
    public PedidoDTO getPedido() {
        return pedido;
    }
    
    public void setPedido(PedidoDTO pedido) {
        this.pedido = pedido;
    }
    
    public List<PedidoDTO> getPedidos() {
        return pedidos;
    }
    
    public void setPedidos(List<PedidoDTO> pedidos) {
        this.pedidos = pedidos;
    }
    
    public List<Cliente> getClientes() {
        return clientes;
    }
    
    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }
    
    public Long getClienteIdFiltro() {
        return clienteIdFiltro;
    }
    
    public void setClienteIdFiltro(Long clienteIdFiltro) {
        this.clienteIdFiltro = clienteIdFiltro;
    }
}

