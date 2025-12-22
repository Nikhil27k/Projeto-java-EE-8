package br.com.projeto.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.com.projeto.domain.Cliente;
import br.com.projeto.domain.Pedido;

/**
 * Repository para operações de persistência da entidade Pedido.
 */
@ApplicationScoped
public class PedidoRepository implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(PedidoRepository.class.getName());
    
    @PersistenceContext(unitName = "Projeto")
    private EntityManager entityManager;
    
    /**
     * Salva ou atualiza um pedido.
     */
    public Pedido save(Pedido pedido) {
        try {
            // Garantir que o cliente está gerenciado
            if (pedido.getCliente() != null && pedido.getCliente().getId() != null) {
                Cliente clienteGerenciado = entityManager.find(Cliente.class, pedido.getCliente().getId());
                if (clienteGerenciado != null) {
                    pedido.setCliente(clienteGerenciado);
                }
            }
            
            if (pedido.getId() == null) {
                LOGGER.log(Level.INFO, "Criando novo pedido: {0}", pedido.getNumeroPedido());
                entityManager.persist(pedido);
                entityManager.flush(); // Força o flush para garantir que o ID seja gerado
                return pedido;
            } else {
                LOGGER.log(Level.INFO, "Atualizando pedido: {0}", pedido.getNumeroPedido());
                Pedido pedidoSalvo = entityManager.merge(pedido);
                entityManager.flush(); // Força o flush
                return pedidoSalvo;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao salvar pedido", e);
            throw e;
        }
    }
    
    /**
     * Busca um pedido por ID.
     */
    public Optional<Pedido> findById(Long id) {
        try {
            TypedQuery<Pedido> query = entityManager.createQuery(
                "SELECT p FROM Pedido p JOIN FETCH p.cliente WHERE p.id = :id", Pedido.class);
            query.setParameter("id", id);
            List<Pedido> results = query.getResultList();
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar pedido por ID: " + id, e);
            return Optional.empty();
        }
    }
    
    /**
     * Busca pedidos por cliente.
     */
    public List<Pedido> findByClienteId(Long clienteId) {
        try {
            TypedQuery<Pedido> query = entityManager.createQuery(
                "SELECT p FROM Pedido p JOIN FETCH p.cliente WHERE p.cliente.id = :clienteId ORDER BY p.dataPedido DESC", 
                Pedido.class);
            query.setParameter("clienteId", clienteId);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar pedidos por cliente: " + clienteId, e);
            throw e;
        }
    }
    
    /**
     * Lista todos os pedidos.
     */
    public List<Pedido> findAll() {
        try {
            TypedQuery<Pedido> query = entityManager.createQuery(
                "SELECT p FROM Pedido p JOIN FETCH p.cliente ORDER BY p.dataPedido DESC", Pedido.class);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao listar pedidos", e);
            throw e;
        }
    }
    
    /**
     * Remove um pedido.
     */
    public void delete(Pedido pedido) {
        try {
            LOGGER.log(Level.INFO, "Removendo pedido: {0}", pedido.getNumeroPedido());
            // Buscar o pedido novamente para garantir que está gerenciado
            Pedido pedidoGerenciado = entityManager.find(Pedido.class, pedido.getId());
            if (pedidoGerenciado != null) {
                entityManager.remove(pedidoGerenciado);
            } else {
                throw new IllegalArgumentException("Pedido não encontrado para remoção");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao remover pedido", e);
            throw e;
        }
    }
    
    /**
     * Verifica se existe um pedido com o número informado (excluindo o próprio).
     */
    public boolean existsByNumeroPedidoAndIdNot(String numeroPedido, Long id) {
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(p) FROM Pedido p WHERE p.numeroPedido = :numeroPedido AND p.id != :id", 
                Long.class);
            query.setParameter("numeroPedido", numeroPedido);
            query.setParameter("id", id == null ? 0L : id);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao verificar número de pedido duplicado", e);
            return false;
        }
    }
}

