package com.tecsup.aopserva.aop;

import com.tecsup.aopserva.domain.entities.Auditoria;
import com.tecsup.aopserva.domain.persistence.AuditoriaDao;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

@Component
@Aspect
public class LogginAspecto {

    private Long tx;

    @Autowired
    private AuditoriaDao auditoriaDao;

    private static final Logger logger = LoggerFactory.getLogger(LogginAspecto.class);

    @Around("execution(* com.tecsup.aopserva.services.*ServiceImpl.grabar(..)) || " +
            "execution(* com.tecsup.aopserva.services.*ServiceImpl.eliminar(..)) || " +
            "execution(* com.tecsup.aopserva.services.*ServiceImpl.actualizar(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        Object result = null;
        Long currTime = System.currentTimeMillis();
        tx = currTime;

        String metodo = joinPoint.getSignature().getName();
        logger.info("→ Iniciando método: " + metodo);

        if (joinPoint.getArgs().length > 0) {
            logger.info("→ Parámetros: " + Arrays.toString(joinPoint.getArgs()));
        }

        try {
            result = joinPoint.proceed(); // Ejecuta el método real
            // Guarda en tabla auditoria
            Auditoria auditoria = new Auditoria();
            auditoria.setTabla("curso");
            auditoria.setFecha(new Date());
            auditoria.setUsuario("admin");
            auditoria.setTipo(metodo.toUpperCase());
            auditoriaDao.save(auditoria);
        } catch (Throwable e) {
            logger.error("Error en método: " + metodo + " → " + e.getMessage());
            throw e;
        }

        logger.info("← Método " + metodo + " completado en " + (System.currentTimeMillis() - currTime) + " ms.");
        return result;
    }
}
