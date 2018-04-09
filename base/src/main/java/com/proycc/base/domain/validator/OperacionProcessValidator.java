/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proycc.base.domain.validator;


import com.proycc.base.domain.DataMaster;
import com.proycc.base.domain.Operacion;
import com.proycc.base.domain.OperacionItem;
import com.proycc.base.domain.dto.OperacionDTO;
import com.proycc.base.domain.dto.UserCreateFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author fafre
 */
@Component
public class OperacionProcessValidator implements Validator {

    @Autowired
    DataMaster dataMaster;
    
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(OperacionDTO.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        //casteo a operacion
        OperacionDTO opDTO = (OperacionDTO) target; 
        Operacion op = opDTO.getOperacion();
        OperacionItem opO = opDTO.getOpO();
        OperacionItem opD = opDTO.getOpD();
        String monedaBase = dataMaster.getMonedaBase();
        
        if(opO.getMoneda().getValor().equals(opD.getMoneda().getValor())){
            errors.reject("operacion.igualMoneda", "La moneda de origen y destino son iguales");
        }
        
        if (!(opO.getMoneda().getValor().equals(monedaBase)||
                opD.getMoneda().getValor().equals(monedaBase))){
            errors.reject("operacion.sinMonedaBase","El origen o el destino debe estar expresado en moneda base = " + monedaBase);
        }
        
        
        if (opO.getMontoRecVlt()<opO.getMonto()) {
            errors.reject("operacion.montoMenorARecibido","El monto recibido " + opO.getMontoRecVlt() +
                    " debe ser mayor o igual al monto a operar " + opO.getMonto());
        }
        
    }       
    
}
