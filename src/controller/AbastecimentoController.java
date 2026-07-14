package controller;

import service.AbastecimentoService;

/**
 * Adaptador da interface gráfica para a camada de regras de negócio.
 * As regras ficam em AbastecimentoService; este tipo é mantido para compatibilidade
 * com as telas existentes.
 */
public class AbastecimentoController extends AbastecimentoService {
    public AbastecimentoController() {
        super();
    }
}
