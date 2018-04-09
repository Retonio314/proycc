/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proycc.base.service;

import com.proycc.base.domain.AcumuladoCliente;
import com.proycc.base.domain.Operacion;
import com.proycc.base.domain.OperacionItem;
import com.proycc.base.domain.Parametro;
import com.proycc.base.domain.dto.OperacionDTO;
import com.proycc.base.repository.AcumuladoRepo;
import com.proycc.base.repository.OperacionRepo;
import com.proycc.base.repository.ParametroRepo;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author fafre
 */
@Service
public class OperacionService {

    private OperacionRepo opRep;
    private AcumuladoRepo acRep;
    private ParametroRepo paramRepo;

    @Autowired
    public OperacionService(OperacionRepo opRep, AcumuladoRepo acRep, ParametroRepo pr) {
        this.opRep = opRep;
        this.acRep = acRep;
        this.paramRepo = pr;
    }

    /**
     *
     * @param op
     * @param ac
     * @return Retorna el objeto acumulado cliente igual, o con acumulado mes y
     * año reseteado a cero si el mes o el año es distinto al de la operacion
     */
    public AcumuladoCliente configAcumulados(Operacion op, AcumuladoCliente ac) {
        Month opMonth = op.getFechaHora().getMonth();
        int opYear = op.getFechaHora().getYear();
        System.out.println("Comparando el año del acumulado : " + ac.getAno() + " año de la op " + opYear
                + " imprimo como int para ver diferencia " + ac.getAno());
        System.out.println("Comparando meses " + ac.getMes() + " con op  " + op.getFechaHora().getMonth());
        if (opYear != ac.getAno()) {
            //cambio año y mes
            ac.setAno(op.getFechaHora().getYear());
            ac.setMes(op.getFechaHora().getMonth());
            ac.setAcumuladoAno(new Float(0));
            ac.setAcumuladoMes(new Float(0));
            System.out.println("Distinto año " + ac.toString());
        }
        if (opMonth != ac.getMes()) {
            ac.setAcumuladoMes(new Float(0));
            System.out.println("Solo distinto mes " + ac.toString());
        }
        return ac;

    }

    /**
     *
     * @param op
     * @param monto
     * @return El objeto acumulado cliente con los acumulados actualizados con
     * los montos de la ultima operacion
     */
    public AcumuladoCliente calcAcumulado(AcumuladoCliente acuCli, float monto) {
        float acuMes = 0;
        float acuAno = 0;
        acuCli.getAcumuladoAno();
        System.out.println("Recibi estos acumulados " + acuMes + "ano " + acuAno + "monto " + monto);
        //si el mes y año es el mismo acumulo, si no cambio el mes y arranco de cero
        Month mesActual = LocalDate.now().getMonth();
        int anoActual = LocalDate.now().getYear();
        if ((acuCli.getMes().equals(mesActual)) && (acuCli.getAno() == anoActual)) {
            acuMes = acuCli.getAcumuladoMes() + monto;
        } else {
            acuCli.setMes(mesActual);
            acuMes = monto;
        }
        //si el año es el mismo acumulo, si no cambio el año y arranco en cero
        if (acuCli.getAno() == anoActual) {
            acuAno = acuCli.getAcumuladoAno() + monto;
        } else {
            acuCli.setAno(anoActual);
            acuAno = monto;
        }
        acuCli.setAcumuladoAno(acuAno);
        acuCli.setAcumuladoMes(acuMes);
        System.out.println("Retorno el ojbeto asi " + acuCli);
        return acuCli;

    }

    /**
     * Al grabar una operacion tambien se debe actualizar los acumulados del
     * cliente
     *
     * @param op
     */
    @Transactional
    public Operacion save(Operacion op) {
        AcumuladoCliente acum = actualizarAcumulados(op);
        acRep.save(acum);
        Operacion opGravada = opRep.save(op);
        return opGravada;

    }

    private AcumuladoCliente actualizarAcumulados(Operacion op) {
        //tomo de la operacion cuanto fue el monto de moneda Base para calcular tope del cliente
        AcumuladoCliente acum = op.getCliente().getAcumulado();
        for (OperacionItem t : op.getOperacionItems()) {
            if (t.getMoneda().getValor().equals("AR$")) {
                acum = calcAcumulado(acum, t.getMonto());
            }

        }
        return acum;
    }

    /*
    * Se spuede modificar, no se puede guardar y no esta procesado
     */
    public void configAltaScreen(OperacionDTO opDTO) {
        opDTO.setModificable(true);
        opDTO.setProcesado(false);
        opDTO.setReadOnly(false);

    }

    public void configProcessScreen(OperacionDTO opDTO) {
        opDTO.setModificable(false);
        opDTO.setProcesado(true);
        opDTO.setReadOnly(false);

    }

    public void configReadOnlyScreen(OperacionDTO opDTO) {
        opDTO.setModificable(false);
        opDTO.setProcesado(true);
        opDTO.setReadOnly(true);
    }

    public Operacion buildOperacionFromDTO(OperacionDTO opDTO) {
        Operacion opAGravar = opDTO.getOperacion();

        //parametros de la operacion -->TOREFACT
        Parametro caja = paramRepo.findByValor(opAGravar.getCaja().getValor());
        Parametro tipoOp = paramRepo.findByValor(opAGravar.getTipoOp().getValor());
        Parametro tipoCambio = paramRepo.findByValor(opAGravar.getTipoCambio().getValor());
        Parametro estado = paramRepo.findByValor(opAGravar.getEstado().getValor());
        opAGravar.setCaja(caja);
        opAGravar.setTipoOp(tipoOp);
        opAGravar.setTipoCambio(tipoCambio);
        opAGravar.setEstado(estado);

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

        opAGravar.setOperacionItems(new ArrayList());
        opAGravar.getOperacionItems().add(opDTO.getOpO());
        opAGravar.getOperacionItems().add(opDTO.getOpD());

        return opAGravar;

    }

}
