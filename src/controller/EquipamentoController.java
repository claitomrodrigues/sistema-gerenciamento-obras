package controller;

import service.EquipamentoService;

/**
 * Adaptador da interface gráfica para a camada de regras de negócio.
 * As regras ficam em EquipamentoService; este tipo é mantido para compatibilidade
 * com as telas existentes.
 */
public class EquipamentoController extends EquipamentoService {
    public EquipamentoController() {
        super();
    }
}
