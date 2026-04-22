package com.daw.forumAscasoBack.sanction.domain;

// Asumiendo que tienes un modelo de dominio Sanction. Si usas la entidad directa, habrá que adaptarlo,
// pero la interfaz debe existir.
public interface CreateSanctionRepositoryPort {
    void saveSanction(Long userId, String type, Integer days, String reason);
}