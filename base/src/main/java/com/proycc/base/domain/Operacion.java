/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proycc.base.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author fafre
 */

@Entity
@Table(name = "operacion")
public class Operacion implements Serializable{
  
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;
    
    @Column(name="fecha_hora")    
    private LocalDateTime fechaHora;
    
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_cli_id")
    private Cliente cliente;
    
    /*
    @OneToOne
    @JoinColumn(name = "fk_user_id")
    private User user;
    */

    @OneToOne
    @JoinColumn(name = "fk_caja_id")
    private Parametro caja;
    
    @OneToOne
    @JoinColumn(name = "fk_tipo_op")
    private Parametro tipoOp;
    
    @OneToOne
    @JoinColumn(name = "fk_tipo_cam")
    private Parametro tipoCambio;
    
    
    @OneToOne
    @JoinColumn(name = "fk_cotizacion_id")
    private Cotizacion cotizacion;
    
    @OneToOne
    @JoinColumn(name = "estado")
    private Parametro estado;
    
   
    @Column(name="observacion")    
    private String observaciones;
    
        
    @OneToMany(mappedBy = "operacion",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<OperacionItem> operacionItems;
    
    public Operacion() {
    }

    public Operacion( LocalDateTime fechaHora, Cliente cliente, Parametro caja, Parametro tipoOp, Parametro tipoCambio, Cotizacion cotizacion, Parametro estado, String observaciones, List<OperacionItem> operacionItems) {

        this.fechaHora = fechaHora;
        this.cliente = cliente;
        this.caja = caja;
        this.tipoOp = tipoOp;
        this.tipoCambio = tipoCambio;
        this.cotizacion = cotizacion;
        this.estado = estado;
        this.observaciones = observaciones;
        this.operacionItems = operacionItems;
    }

    public Operacion(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Parametro getCaja() {
        return caja;
    }

    public void setCaja(Parametro caja) {
        this.caja = caja;
    }

    public Parametro getTipoOp() {
        return tipoOp;
    }

    public void setTipoOp(Parametro tipoOp) {
        this.tipoOp = tipoOp;
    }

    public Cotizacion getCotizacion() {
        return cotizacion;
    }

    public void setCotizacion(Cotizacion cotizacion) {
        this.cotizacion = cotizacion;
    }

    public Parametro getEstado() {
        return estado;
    }

    public void setEstado(Parametro estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<OperacionItem> getOperacionItems() {
        return operacionItems;
    }

    public void setOperacionItems(List<OperacionItem> operacionItems) {
        this.operacionItems = operacionItems;
    }

    public Parametro getTipoCambio() {
        return tipoCambio;
    }

    public void setTipoCambio(Parametro tipoCambio) {
        this.tipoCambio = tipoCambio;
    }
    
    public OperacionItem getOpItemO(){
        if (!operacionItems.isEmpty()) {
            return operacionItems.get(0);
        }
        return null;
    }
    public OperacionItem getOpItemD(){
        if (!operacionItems.isEmpty()) {
            return operacionItems.get(operacionItems.size()-1);
        }
        return null;
    }


    @Override
    public String toString() {
        return "Operacion{" + "id=" + id + ", fechaHora=" + fechaHora + ", cliente=" + cliente + ", caja=" + caja + ", tipoOp=" + tipoOp + ", tipoCambio=" + tipoCambio + ", cotizacion=" + cotizacion + ", estado=" + estado + ", observaciones=" + observaciones + ", operacionItems=" + operacionItems + '}';
    }
    
    
    
    
}
