/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proycc.base.domain.dto.builder;

import com.proycc.base.configuration.DataMaster;
import com.proycc.base.controller.OperacionesController;
import com.proycc.base.domain.Cliente;
import com.proycc.base.domain.Cotizacion;
import com.proycc.base.domain.Operacion;
import com.proycc.base.domain.OperacionItem;
import com.proycc.base.domain.Parametro;
import com.proycc.base.domain.Role;
import com.proycc.base.domain.TopeCompra;
import com.proycc.base.domain.User;
import com.proycc.base.domain.dto.OperacionDTO;
import com.proycc.base.domain.enums.Estado;
import com.proycc.base.domain.validator.OperacionProcessValidator;
import com.proycc.base.repository.ParametroRepo;
import com.proycc.base.service.ClienteService;
import com.proycc.base.service.CotizacionService;
import com.proycc.base.service.OperacionService;
import com.proycc.base.service.UserService;
import com.proycc.base.service.strategy.operacion.OperacionHandlerFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;

/**
 *
 * @author fafre
 */
public abstract class OpDTOBuilder {

    @Autowired
    protected static final Logger LOGGER = LoggerFactory.getLogger(OpDTOBuilder.class);
    @Autowired
    protected ClienteService clienteService;
    @Autowired
    protected OperacionService operacionService;
    @Autowired
    protected UserService userService;
    @Autowired
    protected DataMaster dataMaster;
    @Autowired
    protected ParametroRepo paramRepo;
    @Autowired
    protected CotizacionService cotizacionService;

    private void buildGenericHeader(OperacionDTO opDTO, BindingResult result) {
        //Creo la operacion que es el header
        Operacion op = new Operacion();
        //seteo la fecha
        op.setFechaHora(LocalDateTime.now());
        //Recupero y seteo el usuario
        User usuario = userService.getCurrentUser();
        op.setUser(usuario);
        LOGGER.debug("El usuario recuperado para la op es: " + usuario);
        //seteo un estado para las nuevas operaciones  -->Debe salir de dataMaster
        Parametro estado = Estado.NUEVO.getEstado();
        op.setEstado(estado);
        //pongo observacion en blanco
        op.setObservaciones("");
        //guardo la operacion, cabecera en el DTO
        opDTO.setOperacion(op);
    }

    private void buildGenericBody(OperacionDTO opDTO, BindingResult result) {
        List<OperacionItem> itemsOp = new ArrayList<>();
        itemsOp.add(new OperacionItem(opDTO.getOperacion(), null, null, 0f, 0f, "ORIGEN", new Parametro("INGRESO", "M"), 1, new Cotizacion(), opDTO.getOperacion().getCaja()));
        itemsOp.add(new OperacionItem(opDTO.getOperacion(), null, null, 0f, 0f, "DESTINO", new Parametro("EGRESO", "M"), 2, new Cotizacion(), opDTO.getOperacion().getCaja()));
        opDTO.getOperacion().setOperacionItems(itemsOp);
        opDTO.setOpO(opDTO.getOperacion().getOpItemO());
        opDTO.setOpD(opDTO.getOperacion().getOpItemD());
    }

    protected abstract void buildSpecificHeader(OperacionDTO opDTO, BindingResult result);

    protected abstract void buildSpecificBody(OperacionDTO opDTO, BindingResult result);

    public void buildHeader(OperacionDTO opDTO, BindingResult result) {
        this.buildGenericHeader(opDTO, result);
        this.buildSpecificHeader(opDTO, result);
        this.setTipoMov(opDTO);
    }

    public void buildBody(OperacionDTO opDTO, BindingResult result) {
        this.buildGenericBody(opDTO, result);
        this.buildSpecificBody(opDTO, result);
    }

    public abstract void setTipoMov(OperacionDTO opDTO);

    public Operacion buildOpToSaveFromDTO(OperacionDTO opDTO) {
        Operacion opAGravar = opDTO.getOperacion();      

        //parametros de la operacion -->TOREFACT
        Parametro estado = paramRepo.findByValor(opAGravar.getEstado().getValor());
        opAGravar.setEstado(estado);
        opAGravar.setUser(userService.getCurrentUser());
        //parametrizo las suboperacions
        OperacionItem opO = opDTO.getOpO();
        Parametro monedaO = paramRepo.findByValor(opO.getMoneda().getValor());
        Parametro instO = paramRepo.findByValor(opO.getInstrumento().getValor());
        opO.setMoneda(monedaO);
        opO.setInstrumento(instO);
        opO.setOperacion(opAGravar);

        //parametrizo las suboperacion destino
        OperacionItem opD = opDTO.getOpD();
        Parametro monedaD = paramRepo.findByValor(opD.getMoneda().getValor());
        Parametro instD = paramRepo.findByValor(opD.getInstrumento().getValor());
        opD.setMoneda(monedaD);
        opD.setInstrumento(instD);
        opD.setOperacion(opAGravar);
        
        //agrego lo necesario del metodos especificos
        this.buidSpecificOpFromDTO(opDTO);
        
        opAGravar.setOperacionItems(new ArrayList());
        opAGravar.getOperacionItems().add(opDTO.getOpO());
        opAGravar.getOperacionItems().add(opDTO.getOpD());
        return opAGravar;
    }
    
    protected abstract void buidSpecificOpFromDTO(OperacionDTO opDTO);

}