package br.com.projeto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import br.com.projeto.domain.Cliente;
import br.com.projeto.domain.Pedido;
import br.com.projeto.dto.PedidoDTO;

/**
 * Mapper para conversão entre Pedido (entidade JPA) e PedidoDTO.
 */
@ApplicationScoped
public class PedidoMapper {
    
    /**
     * Converte Pedido (entidade) para PedidoDTO.
     */
    public PedidoDTO toDTO(Pedido pedido) {
        if (pedido == null) {
            return null;
        }
        
        PedidoDTO dto = new PedidoDTO();
        dto.setId(pedido.getId());
        dto.setNumeroPedido(pedido.getNumeroPedido());
        dto.setDescricao(pedido.getDescricao());
        dto.setValorTotal(pedido.getValorTotal());
        dto.setDataPedido(pedido.getDataPedido());
        
        if (pedido.getCliente() != null) {
            dto.setClienteId(pedido.getCliente().getId());
            dto.setClienteNome(pedido.getCliente().getNome());
        }
        
        return dto;
    }
    
    /**
     * Converte PedidoDTO para Pedido (entidade).
     * Nota: O cliente precisa ser carregado separadamente usando o clienteId.
     */
    public Pedido toEntity(PedidoDTO dto, Cliente cliente) {
        if (dto == null) {
            return null;
        }
        
        Pedido pedido = new Pedido();
        pedido.setId(dto.getId());
        pedido.setNumeroPedido(dto.getNumeroPedido());
        pedido.setDescricao(dto.getDescricao());
        pedido.setValorTotal(dto.getValorTotal());
        pedido.setDataPedido(dto.getDataPedido());
        pedido.setCliente(cliente);
        
        return pedido;
    }
    
    /**
     * Converte lista de Pedido para lista de PedidoDTO.
     */
    public List<PedidoDTO> toDTOList(List<Pedido> pedidos) {
        if (pedidos == null) {
            return null;
        }
        
        return pedidos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}

