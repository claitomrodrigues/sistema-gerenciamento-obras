package controller;

import service.ManutencaoService;

/**
 * Adaptador da interface gráfica para a camada de regras de negócio.
 * As regras ficam em ManutencaoService; este tipo é mantido para compatibilidade
 * com as telas existentes.
 */
public class ManutencaoController extends ManutencaoService {
    public ManutencaoController() {
        super();
    }
}
