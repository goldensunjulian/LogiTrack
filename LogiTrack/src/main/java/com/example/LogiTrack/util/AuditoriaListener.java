package com.example.LogiTrack.util;

import com.example.LogiTrack.modelo.Auditoria;
import com.example.LogiTrack.modelo.Credenciales;
import com.example.LogiTrack.modelo.TipoOperacion;
import com.example.LogiTrack.modelo.Usuario;
import com.example.LogiTrack.repository.AuditoriaRepository;
import com.example.LogiTrack.repository.CredencialesRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

@Component
public class AuditoriaListener {

    // Son static porque Hibernate crea su propia instancia del listener aparte de Spring
    private static AuditoriaRepository auditoriaRepository;
    private static CredencialesRepository credencialesRepository;
    private static JsonUtil jsonUtil;
    private static PlatformTransactionManager transactionManager;
    private static EntityManagerFactory entityManagerFactory;

    // constructor vacío obligatorio para que JPA/Hibernate pueda instanciar el listener
    public AuditoriaListener() {
    }

    // spring inyecta aquí las dependencias al arrancar (@Lazy evita dependencias circulares)
    @Autowired
    public void init(@Lazy AuditoriaRepository auditoriaRepo,
                     @Lazy CredencialesRepository credencialesRepo,
                     @Lazy JsonUtil json,
                     @Lazy PlatformTransactionManager txManager,
                     @Lazy EntityManagerFactory emf) {
        AuditoriaListener.auditoriaRepository = auditoriaRepo;
        AuditoriaListener.credencialesRepository = credencialesRepo;
        AuditoriaListener.jsonUtil = json;
        AuditoriaListener.transactionManager = txManager;
        AuditoriaListener.entityManagerFactory = emf;
    }

    // sirve para buscar en MySQL el objeto usuario del que está logueado con el JWT
    private Usuario obtenerUsuarioActual(String username) {
        if (username == null || username.equals("anonymousUser")) {
            return null;
        }
        Credenciales cred = credencialesRepository.findByUsername(username);
        return cred != null ? cred.getUsuario() : null;
    }

    // usa reflexión para llamar a getId() de cualquier entidad (Usuario, Bodega, Producto, etc.)
    private Long obtenerId(Object entity) {
        try {
            return (Long) entity.getClass().getMethod("getId").invoke(entity);
        } catch (Exception e) {
            return null;
        }
    }

    //consulta en MySQL cómo estaba guardada la fila ANTES de que Hibernate aplique el UPDATE
    private String obtenerEstadoAnterior(Class<?> claseEntidad, Long id) {
        if (id == null || entityManagerFactory == null) {
            return null;
        }
        // Abrimos una consulta limpia aparte para leer los datos viejos antes de sobrescribirlos
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            Object anterior = em.find(claseEntidad, id);
            return anterior != null ? jsonUtil.toJson(anterior) : null;
        } catch (Exception e) {
            return null;
        } finally {
            em.close(); //siempre cerramos la conexión temporal
        }
    }

    //crea el registro de auditoría y lo guarda en una transacción independiente
    private void registrar(TipoOperacion operacion, String entidad, Long entidadId, String valoresAntes, String valoresDespues) {
        if (auditoriaRepository == null || transactionManager == null) {
            return;
        }
        //sacamos el username del JWT que validó JwtFilter
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (auth != null) ? auth.getName() : null;
        // REQUIRES_NEW abre una transacción aparte para no chocar con el guardado actual de Hibernate
        TransactionTemplate tx = new TransactionTemplate(transactionManager);
        tx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tx.executeWithoutResult(status -> {
            Auditoria auditoria = new Auditoria();
            auditoria.setTipoOperacion(operacion);
            auditoria.setEntidad(entidad);
            auditoria.setEntidadId(entidadId);
            auditoria.setUsuario(obtenerUsuarioActual(username));
            auditoria.setValoresAntes(valoresAntes);
            auditoria.setValoresDespues(valoresDespues);
            auditoriaRepository.save(auditoria);
        });
    }

    // triggers

    // Se dispara automáticamente DESPUÉS de crear un registro nuevo
    @PostPersist
    public void onPostPersist(Object entity) {
        if (entity instanceof Auditoria) return; // Evita bucle infinito

        String nombreEntidad = entity.getClass().getSimpleName().toLowerCase();
        Long id = obtenerId(entity);
        String despues = jsonUtil.toJson(entity);

        registrar(TipoOperacion.insert, nombreEntidad, id, null, despues);
    }

    //se dispara automáticamente ANTES de actualizar un registro existente
    @PreUpdate
    public void onPreUpdate(Object entity) {
        if (entity instanceof Auditoria) return;

        String nombreEntidad = entity.getClass().getSimpleName().toLowerCase();
        Long id = obtenerId(entity);
        String antes = obtenerEstadoAnterior(entity.getClass(), id); // Foto vieja desde MySQL
        String despues = jsonUtil.toJson(entity);                    // Foto nueva en memoria

        registrar(TipoOperacion.update, nombreEntidad, id, antes, despues);
    }

    //se dispara automáticamente ANTES de borrar un registro
    @PreRemove
    public void onPreRemove(Object entity) {
        if (entity instanceof Auditoria) return;

        String nombreEntidad = entity.getClass().getSimpleName().toLowerCase();
        Long id = obtenerId(entity);
        String antes = jsonUtil.toJson(entity);

        registrar(TipoOperacion.delete, nombreEntidad, id, antes, null);
    }
}