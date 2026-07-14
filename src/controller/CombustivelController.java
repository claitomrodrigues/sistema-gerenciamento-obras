package controller;

import service.CombustivelService;

/**
 * Adaptador da interface gráfica para a camada de regras de negócio.
 * As regras ficam em CombustivelService; este tipo é mantido para compatibilidade
 * com as telas existentes.
 */
public class CombustivelController extends CombustivelService {
    public CombustivelController() {
        super();
    }
}
