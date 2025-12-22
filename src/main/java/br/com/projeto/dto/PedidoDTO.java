package br.com.projeto.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * DTO (Data Transfer Object) para a entidade Pedido.
 * Usado para transferir dados entre as camadas sem expor a entidade JPA diretamente.
 */
public class PedidoDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    @NotBlank(message = "Número do pedido é obrigatório")
    private String numeroPedido;
    
    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;
    
    @NotNull(message = "Valor total é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    private BigDecimal valorTotal;
    
    @NotNull(message = "Data do pedido é obrigatória")
    private LocalDate dataPedido;
    
    @NotNull(message = "Cliente é obrigatório")
    private Long clienteId;
    
    private String clienteNome; // Para exibição na listagem
    
    // Construtores
    public PedidoDTO() {
    }
    
    public PedidoDTO(Long id, String numeroPedido, String descricao, BigDecimal valorTotal, 
                     LocalDate dataPedido, Long clienteId, String clienteNome) {
        this.id = id;
        this.numeroPedido = numeroPedido;
        this.descricao = descricao;
        this.valorTotal = valorTotal;
        this.dataPedido = dataPedido;
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNumeroPedido() {
        return numeroPedido;
    }
    
    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public BigDecimal getValorTotal() {
        return valorTotal;
    }
    
    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
    
    public LocalDate getDataPedido() {
        return dataPedido;
    }
    
    public void setDataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
    }
    
    public Long getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
    
    public String getClienteNome() {
        return clienteNome;
    }
    
    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }
    
    @Override
    public String toString() {
        return "PedidoDTO{" +
                "id=" + id +
                ", numeroPedido='" + numeroPedido + '\'' +
                ", descricao='" + descricao + '\'' +
                ", valorTotal=" + valorTotal +
                ", dataPedido=" + dataPedido +
                ", clienteId=" + clienteId +
                ", clienteNome='" + clienteNome + '\'' +
                '}';
    }
}

