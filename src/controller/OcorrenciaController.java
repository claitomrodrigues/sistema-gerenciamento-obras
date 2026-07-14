package controller;

import service.OcorrenciaService;

/**
 * Adaptador da interface gráfica para a camada de regras de negócio.
 * As regras ficam em OcorrenciaService; este tipo é mantido para compatibilidade
 * com as telas existentes.
 */
public class OcorrenciaController extends OcorrenciaService {
    public OcorrenciaController() {
        super();
    }
}
