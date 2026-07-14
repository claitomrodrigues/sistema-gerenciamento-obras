package controller;

import service.LicitacaoService;

/**
 * Adaptador da interface gráfica para a camada de regras de negócio.
 * As regras ficam em LicitacaoService; este tipo é mantido para compatibilidade
 * com as telas existentes.
 */
public class LicitacaoController extends LicitacaoService {
    public LicitacaoController() {
        super();
    }
}
