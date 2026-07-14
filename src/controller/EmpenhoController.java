package controller;

import service.EmpenhoService;

/**
 * Adaptador da interface gráfica para a camada de regras de negócio.
 * As regras ficam em EmpenhoService; este tipo é mantido para compatibilidade
 * com as telas existentes.
 */
public class EmpenhoController extends EmpenhoService {
    public EmpenhoController() {
        super();
    }
}
