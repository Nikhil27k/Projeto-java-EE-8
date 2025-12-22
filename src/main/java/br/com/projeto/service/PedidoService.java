package br.com.projeto.service;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.projeto.domain.Cliente;
import br.com.projeto.domain.Pedido;
import br.com.projeto.dto.PedidoDTO;
import br.com.projeto.mapper.PedidoMapper;
import br.com.projeto.repository.PedidoRepository;
import br.com.projeto.service.ClienteService;

/**
 * Service para regras de negócio da entidade Pedido.
 * Trabalha com DTOs para isolar a camada de apresentação das entidades JPA.
 */
@ApplicationScoped
public class PedidoService implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(PedidoService.class.getName());
    
    @Inject
    private PedidoRepository pedidoRepository;
    
    @Inject
    private PedidoMapper pedidoMapper;
    
    @Inject
    private ClienteService clienteService;
    
    /**
     * Salva ou atualiza um pedido (usando DTO).
     */
    @Transactional
    public PedidoDTO salvar(PedidoDTO pedidoDTO) {
        try {
            // Validar número de pedido duplicado
            if (pedidoRepository.existsByNumeroPedidoAndIdNot(
                    pedidoDTO.getNumeroPedido(), pedidoDTO.getId())) {
                throw new IllegalArgumentException("Número de pedido já cadastrado no sistema");
            }
            
            // Validar cliente
            if (pedidoDTO.getClienteId() == null) {
                throw new IllegalArgumentException("Cliente é obrigatório");
            }
            
            // Buscar cliente como entidade
            Cliente cliente = clienteService.buscarEntidadePorId(pedidoDTO.getClienteId());
            
            // Converter DTO para entidade
            Pedido pedido = pedidoMapper.toEntity(pedidoDTO, cliente);
            
            Pedido pedidoSalvo = pedidoRepository.save(pedido);
            LOGGER.log(Level.INFO, "Pedido salvo com sucesso: {0}", pedidoSalvo.getId());
            
            // Converter entidade para DTO e retornar
            return pedidoMapper.toDTO(pedidoSalvo);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao salvar pedido", e);
            throw e;
        }
    }
    
    /**
     * Busca um pedido por ID (retorna DTO).
     */
    public PedidoDTO buscarPorId(Long id) {
        Pedido pedido = buscarEntidadePorId(id);
        return pedidoMapper.toDTO(pedido);
    }
    
    /**
     * Busca um pedido por ID como entidade (para uso interno).
     */
    private Pedido buscarEntidadePorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
    }
    
    /**
     * Lista todos os pedidos (retorna lista de DTOs).
     */
    public List<PedidoDTO> listarTodos() {
        try {
            List<Pedido> pedidos = pedidoRepository.findAll();
            return pedidoMapper.toDTOList(pedidos);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao listar pedidos", e);
            throw e;
        }
    }
    
    /**
     * Lista pedidos por cliente (retorna lista de DTOs).
     */
    public List<PedidoDTO> listarPorCliente(Long clienteId) {
        try {
            if (clienteId == null) {
                throw new IllegalArgumentException("ID do cliente é obrigatório");
            }
            List<Pedido> pedidos = pedidoRepository.findByClienteId(clienteId);
            return pedidoMapper.toDTOList(pedidos);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao listar pedidos por cliente: " + clienteId, e);
            throw e;
        }
    }
    
    /**
     * Remove um pedido.
     */
    @Transactional
    public void remover(Long id) {
        try {
            Pedido pedido = buscarEntidadePorId(id);
            pedidoRepository.delete(pedido);
            LOGGER.log(Level.INFO, "Pedido removido com sucesso: {0}", id);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao remover pedido: " + id, e);
            throw e;
        }
    }
}

