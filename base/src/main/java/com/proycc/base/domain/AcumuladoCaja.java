/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proycc.base.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author fafre
 */
@Entity
@Table(name = "acumulado_caja")
public class AcumuladoCaja implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "fecha")
    private LocalDate fechaActualizacion;
    
    @OneToOne
    @JoinColumn(name = "instrumento_id")
    private Parametro instrumento;
    
    @OneToOne
    @JoinColumn(name = "moneda_id")
    private Parametro moneda;
    
    @OneToOne
    @JoinColumn(name = "caja_id")
    private Parametro caja;
    
    @Column(name = "ingreso")
    private float ingreso;
    
    @Column(name = "egreso")
    private float egreso;
   
    @Column(name = "saldo")
    private float saldo;

    public AcumuladoCaja() {
    }

    
    public AcumuladoCaja(LocalDate fechaActualizacion, Parametro instrumento, Parametro moneda, 
            float ingreso, float egreso, float saldo, Parametro caja) {
        this.fechaActualizacion = fechaActualizacion;
        this.instrumento = instrumento;
        this.moneda = moneda;
        this.ingreso = ingreso;
        this.egreso = egreso;
        this.saldo = saldo;
        this.caja = caja;
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDate fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Parametro getInstrumento() {
        return instrumento;
    }

    public void setInstrumento(Parametro instrumento) {
        this.instrumento = instrumento;
    }

    public Parametro getMoneda() {
        return moneda;
    }

    public void setMoneda(Parametro moneda) {
        this.moneda = moneda;
    }

    public float getIngreso() {
        return ingreso;
    }

    public void setIngreso(float ingreso) {
        this.ingreso = ingreso;
    }

    public float getEgreso() {
        return egreso;
    }

    public void setEgreso(float egreso) {
        this.egreso = egreso;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    public Parametro getCaja() {
        return caja;
    }

    public void setCaja(Parametro caja) {
        this.caja = caja;
    }
    
    public void upadateIngreso(float monto){
        this.ingreso+=monto;
    }
    public void upadateEgreso(float monto){
        this.egreso+=monto;
    }
    public void upadateSaldo(){
        this.saldo = ingreso-egreso;
    }

    
    @Override
    public String toString() {
        return "AcumuladoCaja{" + "id=" + id + ", fechaActualizacion=" + fechaActualizacion + ", instrumento=" + instrumento + ", moneda=" + moneda + ", caja=" + caja + ", ingreso=" + ingreso + ", egreso=" + egreso + ", saldo=" + saldo + '}';
    }

    
 
    
    
    
    
}