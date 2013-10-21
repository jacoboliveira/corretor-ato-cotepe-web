/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ato.cotepe.dao;

import br.com.ato.cotepe.entities.CaminhosArquivos;
import br.com.ato.cotepe.hibernate.GenericDao;

/**
 *
 * @author jacoboliveira
 */
public class CaminhosArquivosDao extends GenericDao<CaminhosArquivos, Long> implements ICaminhosArquivosDao{
    private static ICaminhosArquivosDao caminhosArquivosDao;
    public CaminhosArquivosDao() {
        super();
    }
    
    public static ICaminhosArquivosDao instance(){
        if(caminhosArquivosDao == null){
            caminhosArquivosDao = new CaminhosArquivosDao();
        }
        return caminhosArquivosDao;
    }
}
